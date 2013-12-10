package com.engagepoint.acceptancetest.steps;

import com.engagepoint.acceptancetest.base.pages.UIBootstrapBasePage;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * User: victor.klymenko
 * Date: 12/6/13
 * Time: 11:13 AM
 */
public class HelperSteps extends ScenarioSteps {
    private static final String XPATH_SELCTOR_SUFIX = "')]";
    private UIBootstrapBasePage uIBootstrapBasePage;

    public HelperSteps(Pages pages) {
        super(pages);
        uIBootstrapBasePage = pages().get(UIBootstrapBasePage.class);
    }

    @When("opens all tree with className '$className'")
    @Alias("the user opens all tree with className '$className'")
    public void openAllTree(String className){
        for (WebElement element : uIBootstrapBasePage.getDriver().findElements(By.className(className))){
            element.click();
        }
    }

    @When("clicks on element with class '$className' with text '$text'")
    @Alias("the user clicks on element with class '$className' with text '$text'")
    public void clickByText(String className, String text){
        for (WebElement webElement : uIBootstrapBasePage.getDriver().findElements(By.className(className))) {
            if (webElement.getText().equalsIgnoreCase(text)) {
                webElement.click();
            }
        }
    }

    @When("clicks on first element with class '$className' with text '$text'")
    @Alias("the user clicks on first element with class '$className' with text '$text'")
    public void clickOnFirstElementByText(String className, String text){
        for (WebElement webElement : uIBootstrapBasePage.getDriver().findElements(By.className(className))) {
            if (webElement.getText().equalsIgnoreCase(text)) {
                webElement.click();
                break;
            }
        }
    }

    public By findVisibleElementAndGetSelector(String id) {
        By[] selectors = { By.id(id), By.xpath("//*[contains(@id, '" + id + XPATH_SELCTOR_SUFIX), By.name(id), By.className(id) };
        for (By selector : selectors) {
            if (isElementDisplayed(selector)) {
                return selector;
            }
        }
        return selectors[0];
    }

    private boolean isElementDisplayed(By selector) {
        try {
            return uIBootstrapBasePage.element(selector).isCurrentlyVisible();
        } catch (Exception e) {
        }
        return false;
    }
}
