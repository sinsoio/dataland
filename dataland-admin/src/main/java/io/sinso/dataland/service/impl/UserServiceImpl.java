package io.sinso.dataland.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.sinso.dataland.enums.AccountGetAllNftEnum;
import io.sinso.dataland.enums.ChainEnum;
import io.sinso.dataland.enums.ResCodeEnum;
import io.sinso.dataland.exception.BusinessException;
import io.sinso.dataland.mapper.UserMapper;
import io.sinso.dataland.model.FileCollection;
import io.sinso.dataland.model.Folder;
import io.sinso.dataland.model.User;
import io.sinso.dataland.service.IFileCollectionService;
import io.sinso.dataland.service.IFolderService;
import io.sinso.dataland.service.IScanConfigService;
import io.sinso.dataland.service.IUserService;
import io.sinso.dataland.util.LocalDateUtils;
import io.sinso.dataland.util.OpenSeaUtil;
import io.sinso.dataland.util.UserUtil;
import io.sinso.dataland.vo.file.AccountAllNftVo;
import io.sinso.dataland.vo.file.NftParsingUrlVo;
import io.sinso.dataland.vo.file.NftScanRpcGetResVo;
import io.sinso.dataland.vo.user.UserLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tron.trident.crypto.Hash;
import org.tron.trident.utils.Numeric;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author lee
 * @since 2022-06-13
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IFolderService folderService;

    @Autowired
    private IFileCollectionService fileCollectionService;

    @Autowired
    private IScanConfigService scanConfigService;


    @Override
    public User getToken(String token) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery()
                .ge(User::getTokenEndAt, LocalDateTime.now())
                .eq(User::getToken, token);
        User user = getOne(queryWrapper);
        if (user == null || user.getToken() == null) {
            return null;
        }
        return user;
    }

    @Override
    public String getRandom(String address) {
        String uuid = UUID.randomUUID().toString();
        LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getAddress, address);
        User one = getOne(queryWrapper);
        if (one == null) {
            User user = new User();
            user.setRegisteredAt(LocalDateTime.now());
            user.setAddress(address);
            user.setChain("ETH");
            user.setMessage(uuid);
            user.setMessageEndAt(LocalDateTime.now().plusMinutes(5));
            user.setType(1);
            save(user);
            //新建文件夹
            Folder folder = new Folder();
            folder.setCreatedAt(LocalDateTime.now());
            folder.setFolderName("DataLand");
            folder.setUserId(user.getId());
            folder.setParentId(0);
            folderService.save(folder);
        } else {
            one.setMessage(uuid);
            one.setMessageEndAt(LocalDateTime.now().plusMinutes(5));
            updateById(one);
        }
        return uuid;
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public String login(UserLoginVo userLoginVo) {
        String address = userLoginVo.getAddress();
        String message = userLoginVo.getMessage();
        LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getAddress, address);
        User one = getOne(queryWrapper);
        if (one == null) {
            throw new BusinessException(ResCodeEnum.ACCOUNT_NOT_EXIST);
        }
        if (one.getMessage() == null) {
            throw new BusinessException(ResCodeEnum.VERIFY_CODE_ERROR);
        }
        if (one.getMessageEndAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ResCodeEnum.VERIFY_CODE_ERROR);
        }
        //随机数不匹配
        if (!one.getMessage().equals(message)) {
            throw new BusinessException(ResCodeEnum.VERIFY_CODE_ERROR);
        }
        boolean signatureValid = isSignatureValid(address, userLoginVo.getSignature(), message);
        if (!signatureValid) {
            throw new BusinessException(ResCodeEnum.VERIFY_CODE_ERROR);
        }
//        nft is initialized for asynchronous tasks
        if (one.getType() == 1 || one.getType() == 3) {
            CompletableFuture.runAsync(() -> addNftFile(address, one.getId()));
            one.setType(2);
        }
        String token = UUID.randomUUID().toString();
        one.setToken(token);
        one.setTokenEndAt(LocalDateTime.now().plusDays(2));
        one.setMessage(null);
        one.setMessageEndAt(null);
        updateById(one);
        return token;
    }

    @Override
    public void loginOut() {
        Integer uid = UserUtil.getUid();
        User user = getById(uid);
        user.setToken(null);
        user.setTokenEndAt(null);
        updateById(user);
    }


    @Override
    public Integer getSynType() {
        User user = getById(UserUtil.getUid());
        return user.getType();
    }

    @Override
    public User getOneByAddress(String address) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getAddress, address);
        User one = getOne(queryWrapper);
        return one;
    }

    @Override
    public Integer getNumAddress(LocalDateTime max, LocalDateTime min) {
        LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery()
                .ge(User::getRegisteredAt, min)
                .le(User::getRegisteredAt, max);
        return count(queryWrapper);
    }


    /**
     * Login random number verification
     *
     * @param address
     * @param signature
     * @param message
     * @return
     */
    private static boolean isSignatureValid(String address, String signature, String message) {
        log.info("isSignatureValid invoked for Address {} with Signature {} and Message {} ", address, signature, message);
        final String personalMessagePrefix = "\u0019Ethereum Signed Message:\n";
        boolean match = false;
        final String prefix = personalMessagePrefix + message.length();
        final byte[] msgHash = Hash.sha3((prefix + message).getBytes());
        final byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }
        final Sign.SignatureData sd = new Sign.SignatureData(v,
                Arrays.copyOfRange(signatureBytes, 0, 32),
                Arrays.copyOfRange(signatureBytes, 32, 64));

        String addressRecovered = null;

        // Iterate for each possible key to recover
        for (int i = 0; i < 4; i++) {
            final BigInteger publicKey = Sign.recoverFromSignature((byte) i, new ECDSASignature(
                    new BigInteger(1, sd.getR()),
                    new BigInteger(1, sd.getS())), msgHash);

            if (publicKey != null) {
                addressRecovered = "0x" + Keys.getAddress(publicKey);

                if (addressRecovered.equals(address)) {
                    match = true;
                    break;
                }
            }
        }
        return match;
    }


    /**
     * Asynchronous account initialization The nft is initialized upon the first login
     *
     * @param address
     */
    private void addNftFile(String address, Integer uid) {
        log.info("The system starts to synchronize the NFT account Address====>{}", address);
        for (AccountGetAllNftEnum obj : AccountGetAllNftEnum.values()) {
            String standerd = obj.getStanderd();
            String cursor = "";
            Integer limit = 100;
            try {
                do {
                    String url = obj.getUrl() + address + "?erc_type=" + standerd + "&cursor=" + cursor + "&limit=" + limit + "&show_attribute=true";
                    AccountAllNftVo accountAllNftVo = getUserAllNft(address, uid, url, obj.getChain(), standerd);
                    if (accountAllNftVo != null) {
                        List<FileCollection> list = accountAllNftVo.getList();
                        fileCollectionService.saveBatch(list);
                        cursor = accountAllNftVo.getNext();
                    }
                } while (StringUtils.isNotBlank(cursor));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("End Address of synchronizing account NFT====>{}", address);
        LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery()
                .eq(User::getAddress, address);
        User one = getOne(queryWrapper);
        one.setType(3);
        this.updateById(one);
    }

    /**
     * Log in to initialize the user's nft
     *
     * @param address
     * @return
     */
    private AccountAllNftVo getUserAllNft(String address, Integer uid, String url, String chain, String standerd) {
        User user = getById(uid);
        NftScanRpcGetResVo nftScanRpcGetResVo = scanConfigService.getRequestList(url);
        if (nftScanRpcGetResVo == null) {
            return null;
        }
        List<Map> content = nftScanRpcGetResVo.getList();
        if (content.size() == 0) {
            return null;
        }
        List<FileCollection> collect = new ArrayList<>();
        content.forEach(mapContent -> {
            try {
                String tokenId = mapContent.get("token_id").toString();
                Object nft_name = mapContent.get("name");
                String nftName = nft_name == null ? "#" + tokenId : nft_name.toString();
                if (StringUtils.isEmpty(nftName)) {
                    nftName = "#" + tokenId;
                }
                Long transactionTime = Long.valueOf(mapContent.get("mint_timestamp").toString());
                LocalDateTime mintingDate = LocalDateUtils.timestampToLocalDateTime(transactionTime);
                String contractAddress = mapContent.get("contract_address").toString();
                String creatorAddress = mapContent.get("minter").toString();
                String attributes = mapContent.get("attributes") == null ? null : mapContent.get("attributes").toString();
                String holderAddress = user.getAddress();
                Object imageType = mapContent.get("content_type");
                String nftFormat = imageType == null ? "unkown" : imageType.toString();
                NftParsingUrlVo nftParsingUrlVo = OpenSeaUtil.getUrl(mapContent);
                String imageUrl = nftParsingUrlVo.getImageUrl();
                String logo = nftParsingUrlVo.getLogo();
                FileCollection one = fileCollectionService.getOneByContractAndNftId(contractAddress, tokenId, uid, chain);
                if (one != null) {
                    return;
                }
                if (StringUtils.isEmpty(nftFormat)) {
                    nftFormat = "unknown";
                }
                String chainName = ChainEnum.getChainName(chain);
                String sourceUrl = "https://opensea.io/assets/" + chainName + "/" + contractAddress + "/" + tokenId;
                Folder folder = folderService.getByParentIdAndUserId(0, uid);
                FileCollection fileCollection = new FileCollection();
                fileCollection.setChain(chain);
                fileCollection.setCreatedAt(LocalDateTime.now());
                fileCollection.setNftName(nftName);
                fileCollection.setNftStanderd(standerd);
                fileCollection.setNftContract(contractAddress);
                fileCollection.setNftCreater(creatorAddress);
                fileCollection.setNftId(tokenId);
                fileCollection.setNftHolder(holderAddress);
                fileCollection.setMintingDate(mintingDate);
                fileCollection.setImageUrl(imageUrl);
                fileCollection.setNftFormat(nftFormat);
                fileCollection.setFavoriteAt(LocalDateTime.now());
                fileCollection.setSourceUrl(sourceUrl);
                fileCollection.setSinsoUrl(null);
                fileCollection.setCollected(address.equals(holderAddress));
                fileCollection.setCreated(address.equals(creatorAddress));
                fileCollection.setFavorited(true);
                fileCollection.setUploadSinsoState(imageUrl != null ? 1 : 4);
                fileCollection.setDel(false);
                fileCollection.setUserId(uid);
                fileCollection.setFolderId(folder.getId());
                fileCollection.setLogo(logo);
                fileCollection.setAttributes(attributes);
                fileCollection.setMintingHash(mapContent.get("mint_transaction_hash").toString());
                collect.add(fileCollection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        AccountAllNftVo accountAllNftVo = new AccountAllNftVo();
        accountAllNftVo.setList(collect);
        accountAllNftVo.setNext(nftScanRpcGetResVo.getNext());
        return accountAllNftVo;
    }


}
