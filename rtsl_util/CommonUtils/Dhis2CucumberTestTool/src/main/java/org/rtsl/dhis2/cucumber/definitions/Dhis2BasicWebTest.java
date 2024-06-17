package org.rtsl.dhis2.cucumber.definitions;

import io.cucumber.java.en.Given;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dhis2BasicWebTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dhis2BasicWebTest.class);

    @Inject
    @Named("webDriver")
    private WebDriver webDriver;

 //   @Given("I navigate to the DHIS2 homepage")
    public void dhis2HomePage() {
        webDriver.get("https://dhis2-sandbox1.simple.org/");
        WebElement usernameField = webDriver.findElement(By.id("j_username"));
        usernameField.sendKeys("admin");
        WebElement passwordField = webDriver.findElement(By.id("j_password"));
        passwordField.sendKeys("district");
        WebElement submitButton = webDriver.findElement(By.id("submit"));
        submitButton.click();
        LOGGER.info("j_username tag name is <{}>", webDriver.getTitle());
    }

}
