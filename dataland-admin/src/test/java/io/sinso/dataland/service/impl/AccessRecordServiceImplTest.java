package io.sinso.dataland.service.impl;

import io.sinso.dataland.service.IAccessRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author hengbol
 * @date 8/3/22 5:37 PM
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class AccessRecordServiceImplTest {
    @Autowired
    private IAccessRecordService accessRecordService;

    @Test
    public void addAccessNum() {
    }

    @Test
    public void validationDayAccessNum() {

    }

    @Test
    public void validationSecondsAccessNum() {
        Integer accountId = 5;
        do {
            try {
                Boolean aBoolean = accessRecordService.validationSecondsAccessNum(accountId, "/api/account/get_nft_detail");
                if (aBoolean) {
                    accessRecordService.validationDayAccessNum(accountId, "/api/account/get_nft_detail");
//                    accessRecordService.validationDayAccessNum(accountId, "/api/account/get_all");
                }
                Thread.sleep(30);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } while (true);
    }

}