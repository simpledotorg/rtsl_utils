package org.rtsl.dhis2.cucumber.definitions;

import io.cucumber.java.en.Given;
import io.cucumber.spring.CucumberContextConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@ContextConfiguration("classpath:spring.main.xml")
//@ImportResource("classpath:org.rtsl.common.properties.default.context.xml")
public class Dhis2StepDefinitions {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dhis2StepDefinitions.class);

    static {
        LOGGER.info("TEST");
    }

    @Given("I have {int} cukes in my belly")
    public void i_have_n_cukes_in_my_belly(int cukes) {
        LOGGER.info("TEST");
        LOGGER.info(LOGGER.getClass().getName());
    }

}
