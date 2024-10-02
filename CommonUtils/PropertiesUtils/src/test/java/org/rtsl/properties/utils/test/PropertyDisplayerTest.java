package org.rtsl.properties.utils.test;

import jakarta.annotation.Resource;
import java.util.Properties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/test.context.xml")
public class PropertyDisplayerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyDisplayerTest.class);

    @Resource(name = "org.rtsl.common.properties.standard.rawProperties")
    Properties testProperties;

    @Test
    public void test0() throws Exception {
        LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        LOGGER.info("" + testProperties);
        LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }
}
