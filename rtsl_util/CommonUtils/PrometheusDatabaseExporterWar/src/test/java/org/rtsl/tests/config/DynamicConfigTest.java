package org.rtsl.tests.config;

import com.google.gson.Gson;
import jakarta.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtsl.config.dynamic.DynamicConfigRegistry;
import org.rtsl.config.dynamic.folder.FolderCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/test.context.xml")
public final class DynamicConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicConfigTest.class);

    @Test
    public void test0() throws Exception {
        LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

    }

}
