package org.rtsl.dhis2.cucumber.definitions;

import io.cucumber.java.en.Given;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//@ImportResource("classpath:org.rtsl.common.properties.default.context.xml")
public class Dhis2StepDefinitionsShadow {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dhis2StepDefinitionsShadow.class);

    static {
        LOGGER.info("TEST");
    }

    @Given("I have {int} cukes in my underbelly")
    public void i_have_n_cukes_in_my_belly(int cukes) {
        LOGGER.info("TEST");
        LOGGER.info(LOGGER.getClass().getName());
    }

}
