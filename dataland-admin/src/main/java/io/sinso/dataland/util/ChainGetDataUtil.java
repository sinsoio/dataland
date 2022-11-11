package io.sinso.dataland.util;

import cn.iinda.xhttputils.HttpClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.sinso.dataland.vo.file.ChainGetDataDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hengbol
 * @date 10/13/22 9:45 AM
 */
@Slf4j
public class ChainGetDataUtil {

    private Map<String, Web3j> web3jMap = new HashMap<>();

    public Web3j getWeb3j(String rpcUrl) {
        Web3j web3j = web3jMap.get("rpcUrl");
        if (web3j == null) {
            web3j = Web3j.build(new HttpService(rpcUrl));
            web3jMap.put(rpcUrl, web3j);
        }
        return web3j;
    }


    @Test
    public void tokenURI() {
        try {
            Web3j web3j = Web3j.build(new HttpService("https://rpc.ankr.com/eth"));
//            Web3j web3j = Web3j.build(new HttpService("https://polygon-rpc.com"));
            String contractAddress = "0x644a0b46bf4a4a93cfa5461583654e9c8dd5ab52";
            String tokenId = "3243";
            TransactionManager transactionManager = getRawTransactionManager(web3j);
            ContractGasProvider contractGasProvider = getContractGasProvider();
            NFTERC721 nft721 = NFTERC721.load(contractAddress, web3j, transactionManager, contractGasProvider);
            String tokenURI = nft721.tokenURI(new BigInteger(tokenId)).send();
            System.out.println("tokenURI::" + tokenURI);
            if (tokenURI.contains("ipfs://") && !tokenURI.contains("http")) {
                tokenURI = "https://ipfs.io/ipfs/" + tokenURI.split("//")[1];
            }
            String res = HttpClient.get(tokenURI).execute();
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Credentials getCredentials() {
        return Credentials.create("ff2ebc178d78c9d7a66dac29219579b195c5fd73c1b9d91c333b2fd58dd73068");
    }

    private TransactionManager getRawTransactionManager(Web3j web3j) throws IOException {
        BigInteger chainId = web3j.ethChainId().send().getChainId();
        return new RawTransactionManager(web3j, getCredentials(), chainId.longValue());
    }

    private ContractGasProvider getContractGasProvider() {
        return new StaticGasProvider(BigInteger.valueOf(22_000_000_000L), BigInteger.valueOf(6_700_000L));
    }

    /**
     * @param contractAddress
     * @param tokenId
     * @return
     */
    public ChainGetDataDto getNFTDetail(String contractAddress, String tokenId, String rpcUrl, String standerd) {
        ChainGetDataDto chainGetDataDto = new ChainGetDataDto();
        try {
            Web3j web3j = getWeb3j(rpcUrl);
            TransactionManager transactionManager = getRawTransactionManager(web3j);
            ContractGasProvider contractGasProvider = getContractGasProvider();
            String tokenURI = "";
            if (standerd.contains("721")) {
                NFTERC721 nft721 = NFTERC721.load(contractAddress, web3j, transactionManager, contractGasProvider);
                tokenURI = nft721.tokenURI(new BigInteger(tokenId)).send();
            }
            if (standerd.contains("1155")) {
                NFTERC1155 nfterc1155 = NFTERC1155.load(contractAddress, web3j, transactionManager, contractGasProvider);
                tokenURI = nfterc1155.uri(new BigInteger(tokenId)).send();
            }
            chainGetDataDto.setTokenUri(tokenURI);
            if (tokenURI.contains("ipfs://"))    {
                tokenURI = "https://ipfs.io/ipfs/" + tokenURI.split("//")[1];
            }
            if (tokenURI.contains("{id}")) {
                String[] split = tokenURI.split("\\{id}");
                if (split.length == 1) {
                    tokenURI = split[0] + tokenId;
                }
                if (split.length == 2) {
                    tokenURI = split[0] + tokenId + split[1];
                }
            }
            String res = HttpClient.get(tokenURI).execute();
            JSONObject jsonObject = JSON.parseObject(res);
            String image = jsonObject.get("image") == null ? null : jsonObject.get("image").toString();
//            String attributes = jsonObject.get("attributes") == null ? null : jsonObject.get("attributes").toString();
//            chainGetDataDto.setAttributes(attributes);
            chainGetDataDto.setImageUrl(image);
            chainGetDataDto.setState(true);
        } catch (Exception e) {
            chainGetDataDto.setChainGetStateFailAt(LocalDateTime.now());
            chainGetDataDto.setChainGetStateFailMsg(e.getMessage());
            chainGetDataDto.setState(false);
            e.printStackTrace();
        } finally {
            return chainGetDataDto;
        }
    }

}
