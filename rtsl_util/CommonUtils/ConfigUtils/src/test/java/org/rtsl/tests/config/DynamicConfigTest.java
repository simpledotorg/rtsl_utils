package org.rtsl.tests.config;

import jakarta.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.rtsl.config.dynamic.DynamicConfigRegistry;
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

    @Test
    public void test0() throws Exception {
        LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        String content = readFile("./src/test/resources/config/key1.json", Charset.defaultCharset());
        LOGGER.info("Json Text is : {}", content);

        Object testObject = testRegistry.apply(content);
        LOGGER.info("Obtained Object is :{}", testObject);

    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
