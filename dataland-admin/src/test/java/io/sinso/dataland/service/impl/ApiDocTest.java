package io.sinso.dataland.service.impl;

import cn.iinda.Docs;
import cn.iinda.DocsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;

/**
 * ApiDocTest
 *
 * @author : alibeibei
 * @date : 2020/08/11 15:40
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ApiDocTest {

    @Value("${spring.application.name}")
    private String applicationName;

    @Test
    public void testCreate() {
        DocsConfig config = new DocsConfig();
        String projectPath = System.getProperty("user.dir");
        config.setProjectPath(projectPath);
        config.setProjectName(applicationName);
        config.setApiVersion("V1.0.0");
        config.setDocsPath("/data/apiDocs/" + applicationName);
        config.setAutoGenerate(Boolean.TRUE);
        config.setLocale(Locale.CHINA);
        Docs.buildHtmlDocs(config);
    }
}
