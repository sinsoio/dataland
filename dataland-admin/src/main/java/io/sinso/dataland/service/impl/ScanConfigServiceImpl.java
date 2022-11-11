package io.sinso.dataland.service.impl;

import cn.iinda.xhttputils.HttpClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.sinso.dataland.mapper.ScanConfigMapper;
import io.sinso.dataland.model.ScanConfig;
import io.sinso.dataland.service.IScanConfigService;
import io.sinso.dataland.util.LocalDateUtils;
import io.sinso.dataland.vo.file.NftScanRpcGetResVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lee
 * @since 2022-06-23
 */
@Service
@Slf4j
public class ScanConfigServiceImpl extends ServiceImpl<ScanConfigMapper, ScanConfig> implements IScanConfigService {


    @Autowired
    private ScanConfigMapper scanConfigMapper;

    /**
     * 获取token
     *
     * @return
     */
    private String getApiKey() {
        String randKey = scanConfigMapper.getRandKey(LocalDateTime.now());
        if (randKey == null) {
            log.error("The number of api availability times is insufficient");
            return null;
        }
        return randKey;
    }


    void modifyKeyLockAt(String apiKey) {
        LocalDateTime min = LocalDateUtils.todayMinAndMax(LocalDateTime.now().plusDays(1)).getMin();
        LambdaUpdateWrapper<ScanConfig> lambdaUpdateWrapper = Wrappers.<ScanConfig>lambdaUpdate()
                .eq(ScanConfig::getApiKey, apiKey)
                .set(ScanConfig::getKeyLockAt, min);
        update(lambdaUpdateWrapper);
    }

    @Override
    public Map getRequest(String url) {
        String apiKey = getApiKey();
        String res = HttpClient.get(url).addHeader("X-API-KEY", apiKey).execute();
        JSONObject jsonObject = JSON.parseObject(res);
        if (jsonObject.get("code").toString().equals("403")
                && jsonObject.get("msg").toString().contains("Up To 10000 API Calls Per Day")) {
            modifyKeyLockAt(apiKey);
            return getRequest(url);
        }
        if (!jsonObject.get("code").toString().equals("200")) {
            return null;
        }
        if (jsonObject.get("data") == null) {
            return null;
        }
        return (Map) jsonObject.get("data");
    }


    /**
     * nft Scan get
     *
     * @param url
     * @return
     */
    @Override
    public NftScanRpcGetResVo getRequestList(String url) {
        NftScanRpcGetResVo nftScanRpcGetResVo = new NftScanRpcGetResVo();
        try {
            String apiKey = getApiKey();
            String res = HttpClient.get(url).addHeader("X-API-KEY", apiKey).execute();
            JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject.get("code").toString().equals("403")
                    && jsonObject.get("msg").toString().contains("Up To 10000 API Calls Per Day")) {
                modifyKeyLockAt(apiKey);
                return getRequestList(url);
            }
            if (!jsonObject.get("code").toString().equals("200")) {
                return null;
            }
            if (jsonObject.get("data") == null) {
                return null;
            }
            Map mapData = (Map) jsonObject.get("data");
            Object next = jsonObject.get("next");
            if (mapData.get("content") == null) {
                return null;
            }
            List<Map> content = (List) mapData.get("content");
            if (content == null) {
                return null;
            }
            nftScanRpcGetResVo.setList(content);
            nftScanRpcGetResVo.setNext(next == null ? null : next.toString());
            return nftScanRpcGetResVo;
        } catch (Exception e) {
            log.error("nftscan The data failed to be obtained. Procedure>>>>>>>>>>>>>>>>>>,{},erc==={}", url);
            e.printStackTrace();
        }
        return null;
    }
}
