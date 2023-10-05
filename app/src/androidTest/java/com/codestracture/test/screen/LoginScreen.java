package com.codestracture.test.screen;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.not;

import android.content.Context;
import android.os.SystemClock;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;

import com.codestracture.R;
import com.codestracture.test.utils.CommonUtils;
import com.codestracture.ui.test.LoginActivity;
import com.codestracture.ui.test.SplashActivity;

public class LoginScreen extends BaseScreen {

    private ActivityScenario<SplashActivity> activityActivityScenario;

    public void launchSplashScreen() {
        activityActivityScenario = ActivityScenario.launch(SplashActivity.class);
        activityActivityScenario.onActivity(activity -> context = activity.getBaseContext());
        SystemClock.sleep(1000);
    }

    public void iAmOnLoginScreen() {
        assertNotNull(context);
        SystemClock.sleep(1000);
    }

    public void iInputEmail(final String email) {
        onView(withId(R.id.email)).perform(typeText(email));
    }

    public void iInputPassword(final String password) {
        onView(withId(R.id.password)).perform(typeText(password), closeSoftKeyboard());
    }

    public void iPressSubmitButton() {
        SystemClock.sleep(500);
        onView(withId(R.id.submit)).perform(click());
    }

    public void iTapSignUpButton() {
        SystemClock.sleep(1000);
        onView(withId(R.id.signup)).perform(click());
    }

    public void iShouldSeeErrorOnTheEditTextView(final String viewName) {
        SystemClock.sleep(500);
        int viewId = (viewName.equals("email")) ? R.id.email : R.id.password;
        int messageId = (viewName.equals("email")) ? R.string.msg_email_error : R.string.msg_password_error;

        onView(withId(viewId)).check(matches(CommonUtils.hasErrorText(context.getString(messageId))));
    }

    public void iShouldSeeAuthError(boolean shouldSeeError) {
        if (shouldSeeError) {
            onView(withId(R.id.error)).check(matches(isDisplayed()));
        } else {
            onView(withId(R.id.error)).check(matches(not(isDisplayed())));
        }
    }

    public void iShouldSeeSignUpScreen() {
        onView(withId(R.id.page_title)).check(matches(withText(R.string.signup)));
    }
}
