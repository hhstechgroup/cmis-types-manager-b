package com.engagepoint.acceptancetest.pages;

import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.WebDriver;

/**
 * User: victor.klymenko
 * Date: 11/26/13
 * Time: 11:58 AM
 */
@DefaultUrl("http://lab16:8080/web/")
public class HomePage extends PageObject {
    private WebDriver driver;

    public HomePage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }
}
