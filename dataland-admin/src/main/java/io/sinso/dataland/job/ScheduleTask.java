package io.sinso.dataland.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.sinso.dataland.enums.ChainEnum;
import io.sinso.dataland.model.FileCollection;
import io.sinso.dataland.model.User;
import io.sinso.dataland.service.IFileCollectionService;
import io.sinso.dataland.service.IRecordService;
import io.sinso.dataland.service.IUserService;
import io.sinso.dataland.util.ChainGetDataUtil;
import io.sinso.dataland.util.GetWayUploadUtils;
import io.sinso.dataland.vo.file.ChainGetDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author hengbol
 * @date 6/20/22 11:07 AM
 */

@Configuration
@EnableScheduling
@Slf4j
@Component
public class ScheduleTask {

    @Autowired
    private IFileCollectionService fileCollectionService;

    @Autowired
    private IRecordService iRecordService;

    @Autowired
    private IUserService userService;

    @Value("${sinso-getway.url}")
    private String url;
    @Value("${sinso-getway.token}")
    private String token;

    /**
     * upload getway
     */
    @PostConstruct
    private void uploadGetWay() {
        CompletableFuture.runAsync(() -> {
            do {
                FileCollection fileCollection = fileCollectionService.findOneUpload();
                if (fileCollection != null) {
                    uploadFile(fileCollection);
                } else {
                    try {
                        log.info("There is no data to be uploaded for thread sleep");
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            } while (true);
        });
    }

    /**
     * Failed to process the upload
     */
    @PostConstruct
    private void uploadFailDealWith() {
        CompletableFuture.runAsync(() -> {
            do {
                FileCollection fileCollection = fileCollectionService.findOneUploadFail();
                if (fileCollection != null && fileCollection.getImageUrl() != null) {
                    uploadFile(fileCollection);
                } else {
                    try {
                        log.info("No upload failed data for thread sleep");
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while (true);
        });

    }

    /**
     * upload File
     *
     * @param fileCollection
     * @return
     */
    private void uploadFile(FileCollection fileCollection) {
        if (fileCollectionService.synSucUploadSinso(fileCollection.getImageUrl())) {
            return;
        }
        try {
            String nftFormat = fileCollection.getNftFormat();
            if (nftFormat.equals("unkown")) {
                nftFormat = "image/png";
            }
            log.info("Upload data ====>id={}, start", fileCollection.getId());
            InputStream inputStream = GetWayUploadUtils.downLoadFromUrl(fileCollection.getImageUrl());
            JSONObject jsonObject = JSON.parseObject(GetWayUploadUtils.upload(inputStream, UUID.randomUUID().toString(), url, token, nftFormat));
            if (jsonObject.get("code").toString().equals("200")) {
                Map mapData = (Map) jsonObject.get("data");
                if (mapData == null) {
                    fileCollection.setUploadSinsoState(3);
                    fileCollection.setUploadSinsoFailMsg(jsonObject.toJSONString());
                } else {
                    String sinsoUrl = mapData.get("url").toString();
                    fileCollection.setSinsoUrl(sinsoUrl);
                    fileCollection.setUploadSinsoState(2);
                    fileCollection.setUploadSinsoFailAt(null);
                }
            } else {
                fileCollection.setUploadSinsoState(3);
                fileCollection.setUploadSinsoFailMsg(jsonObject.toJSONString());
            }
        } catch (Exception e) {
            fileCollection.setUploadSinsoState(3);
            fileCollection.setUploadSinsoFailMsg(e.getMessage());
            e.printStackTrace();
        } finally {
            if (fileCollection.getUploadSinsoState() == 3) {
                log.info("Failed to upload data ====>id={}. Procedure", fileCollection.getId());
                fileCollectionService.synFailUploadSinso(fileCollection.getImageUrl(), fileCollection.getUploadSinsoFailMsg());
                fileCollection.setUploadSinsoFailAt(LocalDateTime.now().plusHours(24));
            }
            if (fileCollection.getUploadSinsoState() == 2) {
                log.info("Upload data ====>id={}, succeeded", fileCollection.getId());
                fileCollection.setUploadSinsoAt(LocalDateTime.now());
                fileCollection.setUploadSinsoFailMsg(null);
            }
            fileCollectionService.updateById(fileCollection);
        }
    }

    /**
     * Add user nft transaction records
     */
    @Scheduled(cron = "0 15 00 ? * *")
    public void updateNftRecord() {
        log.info("updateNftRecord==========>>>>");
        List<User> userList = userService.list();
        userList.forEach(user -> {
            String address = user.getAddress();
            Integer uid = user.getId();
            log.info("updateNftRecord,userId===>{}", uid);
            try {
                iRecordService.addUserRecordByUserAddress(address, uid);
                log.info("updateNftRecord,userId===>{},abnormal", uid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * updateUserNft
     */
    @Scheduled(cron = "0 15 01 ? * *")
    private void updateUserNft() {
        log.info("updateUserNft==========>>>>");
        List<User> userList = userService.list();
        userList.forEach(user -> {
            String address = user.getAddress();
            Integer uid = user.getId();
            log.info("updateUserNft,userId===>{}", uid);
            try {
                iRecordService.updateUserNft(address, uid);
            } catch (Exception e) {
                log.info("updateUserNft,userId===>{},abnormal", uid);
                e.printStackTrace();
            }
        });
    }


    /**
     * chainGetData
     */
    @PostConstruct
    private void chainGetData() {
        ChainGetDataUtil chainGetDataUtil = new ChainGetDataUtil();
        CompletableFuture.runAsync(() -> {
            do {
                FileCollection fileCollection = fileCollectionService.getOneByChainGetData(1);
                if (fileCollection != null) {
                    String chain = fileCollection.getChain();
                    String rpcUrl = ChainEnum.getRpcUrl(chain);
                    ChainGetDataDto chainGetDataDto = chainGetDataUtil.getNFTDetail(fileCollection.getNftContract(), fileCollection.getNftId(), rpcUrl, fileCollection.getNftStanderd());
                    if (chainGetDataDto.getState()) {
                        fileCollection.setImageUrl(chainGetDataDto.getImageUrl());
                        fileCollection.setAttributes(chainGetDataDto.getAttributes());
                        fileCollection.setTokenUri(chainGetDataDto.getTokenUri());
                        fileCollectionService.updateChainGetData(fileCollection);
                    } else {
                        fileCollection.setChainGetState(3);
                        fileCollection.setChainGetStateFailAt(chainGetDataDto.getChainGetStateFailAt());
                        fileCollection.setChainGetStateFailMsg(chainGetDataDto.getChainGetStateFailMsg());
                        fileCollectionService.updateById(fileCollection);
                    }
                } else {
                    try {
                        log.info("No on-chain data sleeps to be retrieved====>>");
                        Thread.sleep(60 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            } while (true);
        });
    }
}
