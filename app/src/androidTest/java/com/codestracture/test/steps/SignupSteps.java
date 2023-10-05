package com.codestracture.test.steps;

import androidx.test.filters.MediumTest;

import com.codestracture.test.screen.SignupScreen;

import dagger.hilt.android.testing.HiltAndroidTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@MediumTest
@HiltAndroidTest
public class SignupSteps extends SignupScreen {

    @Given("^I am on sign up screen$")
    public void I_am_on_sign_up_screen() {
        iAmOnSignupScreen();
    }

    @When("^I tap login button$")
    public void I_tap_login_button() {
        iTapLoginButton();
    }

    @Then("^I should see login screen$")
    public void I_should_see_login_screen() {
        iShouldSeeLoginScreen();
    }

}
