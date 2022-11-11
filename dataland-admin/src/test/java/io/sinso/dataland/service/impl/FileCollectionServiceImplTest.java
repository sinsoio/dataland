package io.sinso.dataland.service.impl;

import io.sinso.dataland.service.IFileCollectionService;
import io.sinso.dataland.service.IScanConfigService;
import io.sinso.dataland.vo.file.CollectionVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author hengbol
 * @date 7/6/22 9:42 AM
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class FileCollectionServiceImplTest {

    @Autowired
    private IFileCollectionService iFileCollectionService;

    @Autowired
    private IScanConfigService iScanConfigService;

    @Test
    public void collection() {
        CollectionVo collectionVo = new CollectionVo();
        collectionVo.setSourceUrl("https://opensea.io/assets/ethereum/0x8f6a4d8ad2493adfd7d1540ccdba11bde5c7eb9e/4597");
        collectionVo.setFolderId(51);
        iFileCollectionService.collection(collectionVo);

    }

    @Test
    public void getRequest() {
    }
}