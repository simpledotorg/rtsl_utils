package org.rtsl.dhis2.cucumber.definitions;

import io.cucumber.java.AfterAll;
import org.rtsl.dhis2.cucumber.factories.OrganisationUnit;

public class Hooks {
    @AfterAll
    public static void afterAll() {
        System.out.println("Delete all the test org units along with it's dependencies after all scenarios");
        try {
            new OrganisationUnit().deleteAll();
        } catch (Exception e) {
//            TODO handle exception
        }
    }
}
