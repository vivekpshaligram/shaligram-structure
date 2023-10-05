package com.codestracture.test.steps;

import androidx.test.filters.MediumTest;

import com.codestracture.test.screen.LoginScreen;

import dagger.hilt.android.testing.HiltAndroidTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@MediumTest
@HiltAndroidTest
public class LoginSteps extends LoginScreen {

    @Given("^I am on splash screen$")
    public void I_am_on_splash_screen() {
        launchSplashScreen();
    }

    @Given("^I am on login screen")
    public void I_am_on_login_screen() {
        iAmOnLoginScreen();
    }

    @When("^I input email (\\S+)$")
    public void I_input_email(final String email) {
        iInputEmail(email);
    }

    @When("^I input password \"(.*?)\"$")
    public void I_input_password(final String password) {
        iInputPassword(password);
    }

    @When("^I press submit button$")
    public void I_press_submit_button() {
        iPressSubmitButton();
    }

    @When("^I tap sign up button$")
    public void I_tap_sign_up_button() {
        iTapSignUpButton();
    }

    @Then("^I should see error on the (\\S+)$")
    public void I_should_see_error_on_the_editTextView(final String viewName) {
        iShouldSeeErrorOnTheEditTextView(viewName);
    }

    @Then("^I should (true|false) auth error$")
    public void I_should_see_auth_error(boolean shouldSeeError) {
        iShouldSeeAuthError(shouldSeeError);
    }

    @Then("^I should see sign up screen$")
    public void I_should_see_sign_up_screen() {
        iShouldSeeSignUpScreen();
    }
}
