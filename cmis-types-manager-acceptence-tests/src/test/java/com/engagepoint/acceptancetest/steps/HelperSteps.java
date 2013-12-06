package com.engagepoint.acceptancetest.steps;

import com.engagepoint.acceptancetest.base.pages.UIBootstrapBasePage;
import net.thucydides.core.pages.Pages;
import net.thucydides.core.steps.ScenarioSteps;
import org.jbehave.core.annotations.When;

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
}
