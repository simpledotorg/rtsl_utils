package org.rtsl.tests.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Resource(name = "testMetaFactory")
    DynamicConfigRegistry testRegistry;

    @Resource(name = "testCrawler")
    FolderCrawler testCrawler;

    @Test
    public void test0() throws Exception {
        LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        String content = readFile("./src/test/resources/config/key1.json", Charset.defaultCharset());
        LOGGER.info("Json Text is : {}", content);

        Object testObject = testRegistry.apply(content);
        LOGGER.info("Obtained Object is :{}", testObject);
        ObjectMapper objectMapper = new ObjectMapper();
        LOGGER.info("Obtained Object is :{}", objectMapper.writeValueAsString(testObject));

    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    @Test
    public void testCrawler() throws Exception {
        LOGGER.info("testing");
        Map<String, Object> parsedObjects = testCrawler.getAll();
        LOGGER.info("___" + parsedObjects.toString());

    }
}
