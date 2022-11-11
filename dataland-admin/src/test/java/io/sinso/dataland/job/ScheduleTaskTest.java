package io.sinso.dataland.job;

import com.alibaba.fastjson.JSONObject;
import io.sinso.dataland.service.IRecordService;
import io.sinso.dataland.service.IScanConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * @author hengbol
 * @date 7/23/22 4:47 PM
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ScheduleTaskTest {

    @Autowired
    private ScheduleTask scheduleTask;

    @Autowired
    private IRecordService recordService;

    @Autowired
    private IScanConfigService iScanConfigService;

    @Test
    public void collection() {
        recordService.addUserRecordByUserAddress("0xa5228c7dc9835f87c9e23225f1e11f10c23d891d", 3);

    }

    @Test
    public void getRequest() {
        Map request = iScanConfigService.getRequest("https://restapi.nftscan.com/api/v2/assets/0x5499ee597543f528675dd23dcff05440dc3b1c6e/75");
        System.out.println(JSONObject.toJSONString(request));
    }
}