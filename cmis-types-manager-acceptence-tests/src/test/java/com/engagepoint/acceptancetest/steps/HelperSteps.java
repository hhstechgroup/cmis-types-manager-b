package com.engagepoint.acceptancetest.steps;

import com.engagepoint.acceptancetest.base.pages.UIBootstrapBasePage;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * User: victor.klymenko
 * Date: 12/6/13
 * Time: 11:13 AM
 */
public class HelperSteps extends ScenarioSteps {

    private static final String XPATH_SELECTOR_SUFFIX = "')]";
    private UIBootstrapBasePage uIBootstrapBasePage;

    public HelperSteps(Pages pages) {
        super(pages);
        uIBootstrapBasePage = pages().get(UIBootstrapBasePage.class);
    }

    @When("opens all tree with className '$className'")
    @Alias("the user opens all tree with className '$className'")
    public void openAllTree(String className) {
        for (WebElement element : uIBootstrapBasePage.getDriver().findElements(By.className(className))) {
            element.click();
        }
    }

    @When("clicks on element with className '$className' with text '$text'")
    @Alias("the user clicks on element with className '$className' with text '$text'")
    public void clickByText(String className, String text) {
        for (WebElement webElement : uIBootstrapBasePage.getDriver().findElements(By.className(className))) {
            if (webElement.getText().equalsIgnoreCase(text)) {
                webElement.click();
            }
        }
    }

    @When("clicks on first element with className '$className'")
    @Alias("the user clicks on first element with className '$className'")
    public void clickOnFirstTreeElement(String className) {
        WebElement webElement = uIBootstrapBasePage.getDriver().findElement(By.className(className));
        webElement.click();
    }

    @When("clicks on first element with className '$className' with text '$text'")
    @Alias("the user clicks on first element with className '$className' with text '$text'")
    public void clickOnFirstTreeElementByText(String className, String text) {
        for (WebElement webElement : uIBootstrapBasePage.getDriver().findElements(By.className(className))) {
            if (webElement.getText().equalsIgnoreCase(text)) {
                webElement.click();
                break;
            }
        }
    }

    @When("the user fills '$id' field with '$contextPath' using baseUrl")
    @Alias("'$id' field with '$contextPath' using baseUrl")
    public void fillField(String id, String contextPath) {
        String baseUrl = pages().getConfiguration().getBaseUrl();
        int lastSlash = baseUrl.lastIndexOf('/');
        String url = baseUrl.substring(0, lastSlash - 3);
        uIBootstrapBasePage.enter(url + contextPath).intoField(findVisibleElementAndGetSelector(id));
    }
    
    @Then("wait for element '$id' is not visible")
    public void waitForElementWithIdIsNotPresent(String id) {
        uIBootstrapBasePage.waitForRenderedElementsToDisappear(findVisibleElementAndGetSelector(id));
    }

    @Then("wait for element '$id' is visible")
    public void waitForElementWithIdIsPresent(String id) {
        uIBootstrapBasePage.waitForRenderedElementsToBePresent(findVisibleElementAndGetSelector(id));
    }
    
    public By findVisibleElementAndGetSelector(String id) {
        By[] selectors = {By.id(id), By.xpath("//*[contains(@id, '" + id + XPATH_SELECTOR_SUFFIX), By.name(id), By.className(id)};
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

    @When("clicks on element with '$xpathOrCss'")
    @Alias("the user clicks on element with '$xpathOrCss'")
    public void clickBySelector(String xpathOrCss) {
        uIBootstrapBasePage.element(xpathOrCss).click();
    }

}


