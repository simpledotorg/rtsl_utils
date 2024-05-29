package org.rtsl.dhis2.cucumber.definitions;

import io.cucumber.java.en.Given;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

public class Dhis2StepDefinitions {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dhis2StepDefinitions.class);

    @Inject
    @Named("testAtomicInt")
    @Autowired
    private AtomicInteger testCounter;

    @Given("I have {int} cukes in my belly")
    public void i_have_n_cukes_in_my_belly(int cukes) {
        testCounter.addAndGet(cukes);
        LOGGER.info("TEST: <{}> <{}> <{}>", cukes, testCounter.hashCode(), this);
    }

}
