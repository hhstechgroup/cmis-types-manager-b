package com.engagepoint.acceptancetest.steps;

import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * User: victor.klymenko
 * Date: 11/26/13
 * Time: 11:40 AM
 */
public class LoginScenarioSteps extends ScenarioSteps {

    public LoginScenarioSteps(Pages pages) {
        super(pages);
    }

    @Given("username '$username' , password '$password' and url '$url'")
    public void given(String username, String password, String url) {
        WebElement usernameField = getDriver().findElement(By.id("loginForm:login"));
        usernameField.sendKeys(username);
        System.out.println("usernameField="+usernameField.toString());

        WebElement passwordField = getDriver().findElement(By.id("loginForm:password"));
        passwordField.sendKeys(password);
        System.out.println("passwordField="+passwordField.toString());

        WebElement urlField = getDriver().findElement(By.id("loginForm:URL"));
        urlField.sendKeys(url);
        System.out.println("passwordField="+passwordField.toString());
    }

    @Then("press login button")
    public void then() {
        WebElement submitButton = getDriver().findElement(By.id("loginForm:j_idt23"));
        submitButton.click();
    }
}
