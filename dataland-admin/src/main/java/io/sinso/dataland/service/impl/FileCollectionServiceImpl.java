package io.sinso.dataland.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.sinso.dataland.config.DynamicConfig;
import io.sinso.dataland.enums.AccountCollectionNftEnum;
import io.sinso.dataland.enums.ChainEnum;
import io.sinso.dataland.enums.ResCodeEnum;
import io.sinso.dataland.exception.BusinessException;
import io.sinso.dataland.mapper.FileCollectionMapper;
import io.sinso.dataland.model.FileCollection;
import io.sinso.dataland.model.Folder;
import io.sinso.dataland.model.NftJson;
import io.sinso.dataland.model.User;
import io.sinso.dataland.service.*;
import io.sinso.dataland.util.*;
import io.sinso.dataland.vo.IdVo;
import io.sinso.dataland.vo.PageResultVo;
import io.sinso.dataland.vo.account.ContentTypeEnum;
import io.sinso.dataland.vo.account.NftDetailApiPageVo;
import io.sinso.dataland.vo.file.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author lee
 * @since 2022-06-13
 */
@Service
public class FileCollectionServiceImpl extends ServiceImpl<FileCollectionMapper, FileCollection> implements IFileCollectionService {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IFolderService folderService;

    @Autowired
    private IScanConfigService scanConfigService;

    @Autowired
    private INftJsonService nftJsonService;

    @Value("${sinso-getway.url}")
    private String url;
    @Value("${sinso-getway.token}")
    private String token;

    @Autowired
    private DynamicConfig dynamicConfig;

    @Autowired
    private FileCollectionMapper fileCollectionMapper;


    @Override
    public void collection(CollectionVo collectionVo) {
        Integer uid = UserUtil.getUid();
        User user = iUserService.getById(uid);
        String address = user.getAddress();
        String sourceUrl = collectionVo.getSourceUrl();
        if (!sourceUrl.contains("http")) {
            sourceUrl = "https://" + sourceUrl;
        }
        String chain = "ETH";
        if (sourceUrl.contains("polygon.nftscan") || sourceUrl.contains("matic")) {
            chain = ChainEnum.POLYGON.getChain();
        }
        if (sourceUrl.contains("bnb.nftscan")) {
            chain = ChainEnum.BNB.getChain();
        }
        String nftContract;
        String nftId;
        try {
            String[] split = sourceUrl.split("/");
            nftContract = split[split.length - 2];
            nftId = split[split.length - 1];
        } catch (Exception e) {
            throw new BusinessException(ResCodeEnum.INCORRECT_URL);
        }
        if (!checkAddress(nftContract)) {
            throw new BusinessException(ResCodeEnum.INCORRECT_URL);
        }
        FileCollection one = getOneByContractAndNftId(nftContract, nftId, uid, chain);
        if (one != null) {
            throw new BusinessException(ResCodeEnum.ALREADY_FAVORITED);
        }
        String url = AccountCollectionNftEnum.getUrl(chain) + "/" + nftContract + "/" + nftId + "?show_attribute=true";
        Map data = scanConfigService.getRequest(url);
        if (data == null) {
            throw new BusinessException(ResCodeEnum.INCORRECT_URL);
        }
        try {
            String creatorAddress = data.get("minter").toString();
            String attributes = data.get("attributes") == null ? null : data.get("attributes").toString();
            String holderAddress = data.get("owner") == null ? creatorAddress : data.get("owner").toString();
            Long transactionTime = Long.valueOf(data.get("mint_timestamp").toString());
            LocalDateTime mintingDate = LocalDateUtils.timestampToLocalDateTime(transactionTime);
            String nftFormat = data.get("content_type") == null ? null : data.get("content_type").toString();
            String nftName = data.get("name") == null ? "#" + nftId : data.get("name").toString();
            if (StringUtils.isEmpty(nftName)) {
                nftName = "#" + nftId;
            }
            NftParsingUrlVo nftParsingUrlVo = OpenSeaUtil.getUrl(data);
            if (StringUtils.isEmpty(nftFormat)) {
                nftFormat = "unknown";
            }
            String imageUrl = nftParsingUrlVo.getImageUrl();
            String logo = nftParsingUrlVo.getLogo();
            String nftStanderd = data.get("erc_type").toString();
            FileCollection fileCollection = new FileCollection();
            fileCollection.setCreatedAt(LocalDateTime.now());
            fileCollection.setNftName(nftName);
            fileCollection.setNftStanderd(nftStanderd);
            fileCollection.setNftContract(nftContract);
            fileCollection.setNftCreater(creatorAddress);
            fileCollection.setNftId(nftId);
            fileCollection.setNftHolder(holderAddress);
            fileCollection.setMintingDate(mintingDate);
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
            fileCollection.setChain(chain);
            fileCollection.setLogo(logo);
            fileCollection.setAttributes(attributes);
            fileCollection.setMintingHash(data.get("mint_transaction_hash").toString());
            Integer folderId = collectionVo.getFolderId();
            if (folderId == 0) {
                Folder folder = folderService.getByParentIdAndUserId(0, UserUtil.getUid());
                folderId = folder.getId();
            }
            fileCollection.setFolderId(folderId);
            fileCollection.setImageUrl(imageUrl);
            if (getOneByContractAndNftId(nftContract, nftId, uid, chain) != null) {
                throw new BusinessException(ResCodeEnum.ALREADY_FAVORITED);
            }
            save(fileCollection);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResCodeEnum.NFT_FAVORITED_UNSUCCESSFUL);
        }

    }


    @Override
    public void removeFile(Integer id) {
        FileCollection fileCollection = getById(id);
        fileCollection.setDel(true);
        fileCollection.setDelAt(LocalDateTime.now());
        updateById(fileCollection);
    }

    @Override
    public void moveFile(MoveFileVo moveFileVo) {
        if (moveFileVo.getFolderId() == 0) {
            return;
        }
        FileCollection fileCollection = getById(moveFileVo.getId());
        fileCollection.setFolderId(moveFileVo.getFolderId());
        updateById(fileCollection);
    }

    @Override
    public FileCollection getOneByContractAndNftId(String contract, String nftId, Integer uid, String chain) {
        LambdaQueryWrapper<FileCollection> queryWrapper = Wrappers.<FileCollection>lambdaQuery()
                .eq(FileCollection::getNftContract, contract)
                .eq(FileCollection::getUserId, uid)
                .eq(FileCollection::getDel, false)
                .eq(FileCollection::getChain, chain)
                .eq(FileCollection::getNftId, nftId);
        return getOne(queryWrapper);
    }

    @Override
    public void manuallyClick(IdVo idVo) {
        FileCollection fileCollection = getById(idVo.getId());
        fileCollection.setManuallyClick(true);
        updateById(fileCollection);
    }

    @Override
    public NftStatisticalVo getNftStatistical() {
        QueryWrapper<FileCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("IFNULL(sum(is_collected), 0) AS ownedNum,"
                + "IFNULL(sum(is_created), 0) AS createdNum,"
                + "IFNULL(sum(is_favorited), 0) AS favoriteNum")
                .eq(FileCollection.USER_ID, UserUtil.getUid())
                .eq(FileCollection.IS_DEL, false);
        Map<String, Object> map = getMap(queryWrapper);
        NftStatisticalVo nftStatisticalVo = new NftStatisticalVo();
        nftStatisticalVo.setCreatedNum(Integer.valueOf(map.get("createdNum").toString()));
        nftStatisticalVo.setFavoriteNum(Integer.valueOf(map.get("favoriteNum").toString()));
        nftStatisticalVo.setOwnedNum(Integer.valueOf(map.get("ownedNum").toString()));
        return nftStatisticalVo;
    }

    @Override
    public Integer getFileStatistical(Integer parentId, String searchMsg, Integer type,
                                      Integer nftFormat) {
        QueryWrapper<FileCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("IFNULL(count(1), 0) AS fileNum")
                .eq(FileCollection.USER_ID, UserUtil.getUid())
                .eq(FileCollection.IS_DEL, false)
                .eq(type == 4, FileCollection.IS_COLLECTED, true)
                .eq(type == 2, FileCollection.IS_CREATED, true)
                .eq(type == 3, FileCollection.IS_FAVORITED, true)
                .like(nftFormat == 2, FileCollection.NFT_FORMAT, "image")
                .like(nftFormat == 3, FileCollection.NFT_FORMAT, "video")
                .like(nftFormat == 4, FileCollection.NFT_FORMAT, "audio")
                .like(nftFormat == 5, FileCollection.NFT_FORMAT, "model")
                .like(nftFormat == 6, FileCollection.NFT_FORMAT, "unknown")
                .like(searchMsg != null, FileCollection.NFT_NAME, searchMsg)
                .eq(parentId != null, FileCollection.FOLDER_ID, parentId);
        Map<String, Object> map = getMap(queryWrapper);
        return Integer.valueOf(map.get("fileNum").toString());
    }

    @Override
    public FileCollection findOneUpload() {
        LambdaQueryWrapper<FileCollection> queryWrapper = Wrappers.<FileCollection>lambdaQuery()
                .eq(FileCollection::getUploadSinsoState, 1)
                .eq(FileCollection::getDel, false)
                .orderByAsc(FileCollection::getId);
        return getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void delFolderMoveFile(Integer delFolderId, Integer moveFolderId) {
        LambdaUpdateWrapper<FileCollection> updateWrapper = Wrappers.<FileCollection>lambdaUpdate()
                .eq(FileCollection::getFolderId, delFolderId)
                .eq(FileCollection::getUserId, UserUtil.getUid())
                .set(FileCollection::getFolderId, moveFolderId);
        update(updateWrapper);

        //del Favorited
        LambdaUpdateWrapper<FileCollection> updateWrapper1 = Wrappers.<FileCollection>lambdaUpdate()
                .eq(FileCollection::getFolderId, delFolderId)
                .eq(FileCollection::getDel, false)
                .eq(FileCollection::getFavorited, true)
                .eq(FileCollection::getCollected, false)
                .eq(FileCollection::getUserId, UserUtil.getUid())
                .set(FileCollection::getDel, true)
                .set(FileCollection::getDelAt, LocalDateTime.now());
        update(updateWrapper1);
    }

    @Override
    public FileCollection findOneUploadFail() {
        LambdaQueryWrapper<FileCollection> queryWrapper = Wrappers.<FileCollection>lambdaQuery()
                .eq(FileCollection::getUploadSinsoState, 3)
                .eq(FileCollection::getDel, false)
                .le(FileCollection::getUploadSinsoFailAt, LocalDateTime.now())
                .orderByAsc(FileCollection::getUploadSinsoFailAt);
        return getOne(queryWrapper);
    }


    @Override
    public FileDetailVo getDetail(Integer id) {
        FileCollection fileCollection = getById(id);
        FileDetailVo fileDetailVo = new FileDetailVo();
        BeanUtils.copyProperties(fileCollection, fileDetailVo);
        return fileDetailVo;
    }

    @Override
    public void removeFileByTxTokenId(String txTokenId) {
        LambdaQueryWrapper<FileCollection> queryWrapper = Wrappers.<FileCollection>lambdaQuery()
                .eq(FileCollection::getDel, false)
                .eq(FileCollection::getNftId, txTokenId);
        FileCollection fileCollection = getOne(queryWrapper);
        if (fileCollection == null) {
            return;
        }
        fileCollection.setDel(true);
        fileCollection.setDelAt(LocalDateTime.now());
        updateById(fileCollection);
    }

    @Override
    public UploadFileVo fileUpload(MultipartFile file) {
        UploadFileVo uploadFileVo = new UploadFileVo();
        String fileUrl;
        String contentType = file.getContentType();
        String name = file.getOriginalFilename();
        try {
            byte[] byteArr = file.getBytes();
            InputStream inputStream = new ByteArrayInputStream(byteArr);
            if (!dynamicConfig.getUploadSwitch()) {
                String res = GetWayUploadUtils.upload(inputStream, name, url, token, contentType);
                JSONObject jsonObject = JSON.parseObject(res);
                if (jsonObject.get("code").toString().equals("200")) {
                    Map mapData = (Map) jsonObject.get("data");
                    if (mapData == null) {
                        throw new BusinessException(ResCodeEnum.UPLOAD_ERROR);
                    }
                    fileUrl = mapData.get("url").toString();
                } else {
                    throw new BusinessException(ResCodeEnum.UPLOAD_ERROR);
                }
            } else {
                String[] split = name.split("\\.");

                File temp = File.createTempFile(UUID.randomUUID().toString(), "." + split[split.length - 1]);
                FileUtils.inputStreamToFile(inputStream, temp);
                fileUrl = OciUploadUtil.uploadOci(temp, temp.getName(), contentType);
            }
            uploadFileVo.setContentType(contentType);
            uploadFileVo.setUrl(fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResCodeEnum.UPLOAD_ERROR);
        }
        return uploadFileVo;
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void mintNft(MintNftVo mintNftVo) {
        Integer uid = UserUtil.getUid();
        Integer folderId = mintNftVo.getFolderId();
        if (folderId.equals(0)) {
            Folder folder = folderService.getOneByParentId(0);
            folderId = folder.getId();
        }
        String nftContract = mintNftVo.getNftContract();
        String nftId = mintNftVo.getNftId();
        String sourceUrl = "https://opensea.io/assets/" + mintNftVo.getChain().getChainName() + "/" + nftContract + "/" + nftId;
//        String sourceUrl = "https://www.nftscan.com/" + nftContract + "/" + nftId;
        String chain = mintNftVo.getChain().getChain();
        FileCollection one = getOneByContractAndNftId(nftContract, nftId, uid, chain);
        if (one != null) {
            throw new BusinessException(ResCodeEnum.ALREADY_FAVORITED);
        }
        Long data = mintNftVo.getMintingDate();
        LocalDateTime mintingDate = LocalDateUtils.timestampToLocalDateTime(data);
        FileCollection fileCollection = new FileCollection();
        fileCollection.setCreatedAt(LocalDateTime.now());
        fileCollection.setNftName(mintNftVo.getNftName());
        fileCollection.setNftStanderd(mintNftVo.getNftStanderd());
        fileCollection.setNftContract(nftContract);
        fileCollection.setNftCreater(mintNftVo.getNftCreater());
        fileCollection.setNftId(mintNftVo.getNftId());
        fileCollection.setNftHolder(mintNftVo.getNftCreater());
        fileCollection.setMintingDate(mintingDate);
        fileCollection.setNftFormat(mintNftVo.getNftFormat());
        fileCollection.setFavoriteAt(LocalDateTime.now());
        fileCollection.setSourceUrl(sourceUrl);
        fileCollection.setImageUrl(mintNftVo.getImageUrl());
        if (mintNftVo.getImageUrl().contains("getway")) {
            fileCollection.setSinsoUrl(mintNftVo.getImageUrl());
            fileCollection.setUploadSinsoState(2);
            fileCollection.setUploadSinsoAt(LocalDateTime.now());
        }
        fileCollection.setCollected(true);
        fileCollection.setManuallyClick(false);
        fileCollection.setCreated(true);
        fileCollection.setFavorited(true);
        fileCollection.setDel(false);
        fileCollection.setUserId(UserUtil.getUid());
        fileCollection.setFolderId(folderId);
        fileCollection.setLogo(mintNftVo.getLogo());
        fileCollection.setChain(chain);
        fileCollection.setUploadSinsoState(1);
        fileCollection.setMint(true);
        fileCollection.setMintingHash(mintNftVo.getMintingHash());
        save(fileCollection);
        String fileUrl = mintNftVo.getJsonFileUrl();
        NftJson nftJson = nftJsonService.findOneByFileUrl(fileUrl);
        nftJson.setNftContract(nftContract);
        nftJson.setNftId(nftId);
        nftJsonService.updateById(nftJson);
    }

    @Override
    public String jsonUpload(UploadJsonStrVo uploadJsonStrVo) {
        Object sampleString = JSONObject.toJSON(uploadJsonStrVo.getJsonStr());
        NftJson nftJson = new NftJson();
        nftJson.setCreatedAt(LocalDateTime.now());
        nftJson.setJsonStr(uploadJsonStrVo.getJsonStr());
        //Here converting string to input stream
        InputStream stream = new ByteArrayInputStream(sampleString.toString().getBytes());
        String fileUrl;
        if (!dynamicConfig.getUploadSwitch()) {
            String res = GetWayUploadUtils.upload(stream, UUID.randomUUID().toString() + ".json", url, token, "text/json");
            JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.get("code").toString().equals("200")) {
                Map mapData = (Map) jsonObject.get("data");
                if (mapData == null) {
                    throw new BusinessException(ResCodeEnum.UPLOAD_ERROR);
                }
                fileUrl = mapData.get("url").toString();
                nftJson.setCid(mapData.get("reference").toString());
                nftJson.setUploadAddress(2);
            } else {
                throw new BusinessException(ResCodeEnum.UPLOAD_ERROR);
            }
        } else {
            try {
                File temp = File.createTempFile(UUID.randomUUID().toString(), ".json");
                FileUtils.inputStreamToFile(stream, temp);
                fileUrl = OciUploadUtil.uploadOci(temp, temp.getName(), "text/json");
                nftJson.setUploadAddress(1);
            } catch (Exception e) {
                throw new BusinessException(ResCodeEnum.UPLOAD_ERROR);
            }
        }
        nftJson.setFileUrl(fileUrl);
        nftJsonService.save(nftJson);
        return fileUrl;
    }

    @Override
    public PageResultVo<NftDetailApiPageVo> getNftFavoriteList(Integer pageNum, Integer pageSize,
                                                               ContentTypeEnum contentType, ChainEnum chain, String address) {

        User user = iUserService.getOneByAddress(address);
        if (user == null) {
            return null;
        }
        LambdaQueryWrapper<FileCollection> queryWrapper = Wrappers.<FileCollection>lambdaQuery()
                .eq(FileCollection::getUserId, user.getId())
                .like(FileCollection::getNftFormat, contentType.getType())
                .eq(FileCollection::getChain, chain.getChain())
                .eq(FileCollection::getFavorited, true)
                .eq(FileCollection::getDel, false)
                .orderByDesc(FileCollection::getId);
        Page<FileCollection> pageFileDto = new Page<>(pageNum, pageSize);
        Page<FileCollection> fileCollectionPage = page(pageFileDto, queryWrapper);
        List<NftDetailApiPageVo> collect = fileCollectionPage.getRecords().stream().map(fileCollection -> {
            NftDetailApiPageVo nftDetailApiPageVo = new NftDetailApiPageVo();
            nftDetailApiPageVo.setFileUrl(fileCollection.getSinsoUrl());
            nftDetailApiPageVo.setName(fileCollection.getNftName());
            nftDetailApiPageVo.setContentType(fileCollection.getNftFormat());
            nftDetailApiPageVo.setMintDate(fileCollection.getMintingDate());
            nftDetailApiPageVo.setMintHash(null);
            nftDetailApiPageVo.setHolder(fileCollection.getNftHolder());
            return nftDetailApiPageVo;
        }).collect(Collectors.toList());
        return PageResultVo.page(fileCollectionPage, collect);
    }

    @Override
    public Map getStatistical(LocalDate time) {
        Map map = new HashMap();
        LocalDateUtils.CustomLocalDateTime customLocalDateTime = LocalDateUtils.todayMinAndMax(time);
        LocalDateTime max = customLocalDateTime.getMax();
        LocalDateTime min = customLocalDateTime.getMin();
        Integer numAddress = iUserService.getNumAddress(max, min);
        map.put("Adding an Account", numAddress);
        for (ChainEnum chainEnum : ChainEnum.values()) {
            String chain = chainEnum.getChain();
            if (!chain.equals("BNB")) {
                LambdaQueryWrapper<FileCollection> queryWrapper = Wrappers.<FileCollection>lambdaQuery()
                        .ge(FileCollection::getCreatedAt, min)
                        .le(FileCollection::getCreatedAt, max)
                        .eq(FileCollection::getMint, true)
                        .eq(FileCollection::getChain, chain);
                int count = count(queryWrapper);
                map.put(chainEnum.getChainName() + "Addition of casting", count);
            }
        }
        LambdaQueryWrapper<FileCollection> queryWrapper1 = Wrappers.<FileCollection>lambdaQuery()
                .ge(FileCollection::getCreatedAt, min)
                .le(FileCollection::getCreatedAt, max)
                .eq(FileCollection::getFavorited, true);
        int count = count(queryWrapper1);
        map.put("New collection search", count);

        QueryWrapper<FileCollection> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.select("count(1) as id,user_id")
                .ge(FileCollection.CREATED_AT, min)
                .le(FileCollection.CREATED_AT, max)
                .eq(FileCollection.IS_FAVORITED, true)
                .groupBy(FileCollection.USER_ID)
                .orderByDesc(FileCollection.ID).last("limit " + 1);

        FileCollection fileCollection2 = getOne(queryWrapper2);
        if (fileCollection2 != null) {
            Integer id2 = fileCollection2.getId();
            Integer userId2 = fileCollection2.getUserId();
            User user2 = iUserService.getById(userId2);
            map.put("The largest collection of addresses", user2.getAddress());
            map.put("The largest collection", id2);
        }


        QueryWrapper<FileCollection> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.select("count(1) as id,user_id")
                .ge(FileCollection.CREATED_AT, min)
                .le(FileCollection.CREATED_AT, max)
                .eq(FileCollection.IS_MINT, true)
                .groupBy(FileCollection.USER_ID)
                .orderByDesc(FileCollection.ID).last("limit " + 1);
        FileCollection fileCollection3 = getOne(queryWrapper3);

        if (fileCollection3 != null) {
            Integer id3 = fileCollection3.getId();
            Integer userId3 = fileCollection3.getUserId();
            User user3 = iUserService.getById(userId3);
            map.put("Foundry the most addresses", user3.getAddress());
            map.put("Cast the largest amount", id3);
        }

        return map;
    }

    @Override
    public List<DownloadStatisticalVo> getMintStatistical(LocalDateTime startAt, LocalDateTime endAt, String address) {
        List<FileCollection> fileCollections = fileCollectionMapper.getMintStatistical(startAt, endAt, address);
        return fileCollections.stream().map(fileCollection -> {
            DownloadStatisticalVo downloadStatisticalVo = new DownloadStatisticalVo();
            downloadStatisticalVo.setMintNum(fileCollection.getId());
            downloadStatisticalVo.setAddress(fileCollection.getNftName());
            return downloadStatisticalVo;
        }).collect(Collectors.toList());
    }

    @Override
    public Object getCollectionStatistical(LocalDate time, Integer collectionNum) {
        LocalDateUtils.CustomLocalDateTime customLocalDateTime = LocalDateUtils.todayMinAndMax(time);
        LocalDateTime max = customLocalDateTime.getMax();
        LocalDateTime min = customLocalDateTime.getMin();
        List<FileCollection> fileCollections = fileCollectionMapper.getCollectionStatistical(min, max, collectionNum);
        return fileCollections.stream().
                filter(fileCollection -> fileCollection.getId() >= collectionNum)
                .map(fileCollection -> {
                    CollectionStatisticalVo vo = new CollectionStatisticalVo();
                    vo.setCollectionNum(fileCollection.getId());
                    vo.setAddress(fileCollection.getNftName());
                    return vo;
                }).collect(Collectors.toList());
    }

    @Override
    public void transferNft(TransferNftVo transferNftVo) {
        FileCollection fileCollection = getById(transferNftVo.getId());
        fileCollection.setCollected(transferNftVo.getToAddress().equals(fileCollection.getNftHolder()));
        fileCollection.setNftHolder(transferNftVo.getToAddress());
        updateById(fileCollection);
    }

    @Override
    public FileCollection getOneByChainGetData(Integer chainGetState) {
        LambdaQueryWrapper<FileCollection> queryWrapper = Wrappers.<FileCollection>lambdaQuery()
                .eq(FileCollection::getDel, false)
                .eq(FileCollection::getMint, false)
                .eq(FileCollection::getChainGetState, chainGetState);
        return getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void updateChainGetData(FileCollection fileCollection) {
        LambdaUpdateWrapper<FileCollection> updateWrapper = Wrappers.<FileCollection>lambdaUpdate()
                .eq(FileCollection::getDel, false)
                .eq(FileCollection::getChainGetState, 1)
                .eq(FileCollection::getNftId, fileCollection.getNftId())
                .eq(FileCollection::getNftContract, fileCollection.getNftContract())
                .set(StringUtils.isNotEmpty(fileCollection.getImageUrl()), FileCollection::getImageUrl, fileCollection.getImageUrl())
                .set(FileCollection::getChainGetState, 2)
                .set(StringUtils.isNotEmpty(fileCollection.getTokenUri()), FileCollection::getTokenUri, fileCollection.getTokenUri())
                .set(StringUtils.isNotEmpty(fileCollection.getAttributes()), FileCollection::getAttributes, fileCollection.getAttributes());
        update(updateWrapper);
    }

    @Override
    public Boolean synSucUploadSinso(String imageUrl) {
        LambdaQueryWrapper<FileCollection> queryWrapper = Wrappers.<FileCollection>lambdaQuery()
                .eq(FileCollection::getImageUrl, imageUrl)
                .eq(FileCollection::getUploadSinsoState, 2);
        FileCollection fileCollection = getOne(queryWrapper);
        if (fileCollection == null) {
            return false;
        }
        LambdaUpdateWrapper<FileCollection> updateWrapper = Wrappers.<FileCollection>lambdaUpdate()
                .eq(FileCollection::getDel, false)
                .eq(FileCollection::getImageUrl, imageUrl)
                .set(FileCollection::getSinsoUrl, fileCollection.getSinsoUrl())
                .set(FileCollection::getUploadSinsoState, 2)
                .set(FileCollection::getUploadSinsoAt, fileCollection.getUploadSinsoAt());
        update(updateWrapper);
        return true;
    }

    @Override
    public Boolean synFailUploadSinso(String imageUrl, String uploadSinsoFailMsg) {
        LambdaUpdateWrapper<FileCollection> updateWrapper = Wrappers.<FileCollection>lambdaUpdate()
                .eq(FileCollection::getDel, false)
                .ne(FileCollection::getUploadSinsoState, 2)
                .eq(FileCollection::getImageUrl, imageUrl)
                .set(FileCollection::getUploadSinsoState, 3)
                .set(FileCollection::getUploadSinsoFailAt, LocalDateTime.now().plusHours(24))
                .set(FileCollection::getUploadSinsoFailMsg, uploadSinsoFailMsg);
        update(updateWrapper);
        return true;
    }


    /**
     * check eth Address
     *
     * @param address
     * @return
     */
    public Boolean checkAddress(String address) {
        String pattern = "^0x[0-9a-fA-F]{40}$";
        return Pattern.matches(pattern, address);
    }

}
