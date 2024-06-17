package org.rtsl.dhis2.cucumber.definitions;

import io.cucumber.java.en.Given;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dhis2StepDefinitionsShadow {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dhis2StepDefinitionsShadow.class);

    @Inject
    @Named("testAtomicInt")
    private AtomicInteger testCounter;

    @Given("I have {int} cukes in my underbelly")
    public void i_have_n_cukes_in_my_underbelly(int cukes) {
        testCounter.addAndGet(cukes);
        LOGGER.info("TEST: <{}> <{}> <{}>", cukes, testCounter.hashCode(), this);
    }

}
