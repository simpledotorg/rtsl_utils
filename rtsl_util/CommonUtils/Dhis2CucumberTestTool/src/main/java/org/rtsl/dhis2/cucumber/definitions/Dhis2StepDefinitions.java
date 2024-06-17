package org.rtsl.dhis2.cucumber.definitions;

import io.cucumber.java.en.Given;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.hisp.dhis.Dhis2;
import org.hisp.dhis.model.OrgUnit;
import org.hisp.dhis.model.TrackedEntityAttribute;
import org.hisp.dhis.response.object.ObjectResponse;
import org.rtsl.dhis2.cucumber.TestUniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dhis2StepDefinitions {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dhis2StepDefinitions.class);

    @Inject
    @Named("testUniqueId")
    private TestUniqueId testUniqueId;

    @Inject
    @Named("dhisClient")
    private Dhis2 dhsi2Client;

    @Inject
    @Named("testAtomicInt")
    private AtomicInteger testCounter;

    String currentFaciliyId = null;

    @Given("I have {int} cukes in my belly")
    public void i_have_n_cukes_in_my_belly(Integer cukes) {
        testCounter.addAndGet(cukes);
        LOGGER.info("TEST: <{}> <{}> <{}>", cukes, testCounter.hashCode(), this);
    }

    @Given("I create a new Facility")
    public void i_create_a_new_facility() {

        OrgUnit newFacility = new OrgUnit(null, testUniqueId.get());
        newFacility.setShortName(testUniqueId.get());
        newFacility.setOpeningDate(new Date());
        ObjectResponse response = dhsi2Client.saveOrgUnit(newFacility);
        String newFacilityId = response.getResponse().getUid();
        newFacility.setId(newFacilityId);
        this.currentFaciliyId = newFacilityId;
        LOGGER.info("created Facility: <{}> <{}>", newFacility.getId(), newFacility.getName());
        

    }

    @Given("I create a new Patient for this Facility with the following characteristics")
    public void i_create_a_new_patient_for_this_facility_with_the_following_characteristics(List<Map<String, String>> dataTable) {

        LOGGER.info("Creating patient for data: <{}> ", dataTable);
        // TODO
    }

}
