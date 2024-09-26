package org.rtsl.dhis2.cucumber.utils;

public final class TestUniqueId {

    private final String testUniqueId = "test_" + System.currentTimeMillis() + "_" + Thread.currentThread().threadId();

    public String get() {
        return testUniqueId;
    }
}
