package org.rtsl.properties.utils.test;

import org.rtsl.properties.utils.PropertiesFilterValuesMappedToBean;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PropertiesFilterValuesMappedToBeanTest {

    private BeanTest beanTest;
    private String prefix = "jdbc";
    private Properties properties;
    private String host = "jdbc:oracle:thin:@localhost:1521:XE";
    private int port = 4444;

    @Before
    public void init() throws Exception {
        beanTest = new BeanTest();
        properties = new Properties();
        String filename = "default.properties";
        InputStream input = PropertiesFilterValuesMappedToBeanTest.class.getClassLoader().getResourceAsStream(filename);
        properties.load(input);
    }

    @Test
    public void testfilterMappedValueToBean() throws Exception {

        PropertiesFilterValuesMappedToBean propertiesMappingBean = new PropertiesFilterValuesMappedToBean(properties,
                prefix);
        propertiesMappingBean.filterMappedValueToBean(beanTest);
        Assert.assertEquals(host, beanTest.getHost());
        Assert.assertEquals(port, beanTest.getPort());

    }

    @Test
    public void testgetMapObjectBaddingPropertiesPrefix() {
        PropertiesFilterValuesMappedToBean propertiesMappingBean = new PropertiesFilterValuesMappedToBean(properties,
                prefix);
        Map<String, String> map = propertiesMappingBean.getMapObjectBaddingPropertiesPrefix();
        Assert.assertNotNull(map);
        Assert.assertEquals(host, map.get("host"));
    }

}
