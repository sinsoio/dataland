package io.sinso.dataland.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.sinso.dataland.model.ScanConfig;
import io.sinso.dataland.vo.file.NftScanRpcGetResVo;

import java.util.Map;

/**
 * @author lee
 * @since 2022-06-23
 */
public interface IScanConfigService extends IService<ScanConfig> {

    /**
     * Fetch set
     *
     * @param url
     * @return
     */
    NftScanRpcGetResVo getRequestList(String url);

    /**
     * Get one
     *
     * @param url
     * @return
     */
    Map getRequest(String url);

}
