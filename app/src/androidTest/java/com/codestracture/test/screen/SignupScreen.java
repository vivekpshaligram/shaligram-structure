package com.codestracture.test.screen;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertNotNull;

import android.os.SystemClock;

import androidx.test.core.app.ActivityScenario;

import com.codestracture.R;
import com.codestracture.ui.test.SignupActivity;
import com.codestracture.ui.test.SplashActivity;

import org.junit.Before;
import org.junit.Rule;

import dagger.hilt.android.testing.HiltAndroidRule;

public class SignupScreen extends BaseScreen {

    private ActivityScenario<SplashActivity> splashActivityActivityScenario;
    private SplashActivity splashActivity;

    public ActivityScenario<SignupActivity> activityTestRule;

    private SignupActivity signupActivity;

    public void launchSplashScreen() {
        splashActivityActivityScenario = ActivityScenario.launch(SplashActivity.class);
        splashActivityActivityScenario.onActivity(activity -> splashActivity = activity);
    }
    public void finishSplashScreen() {
        splashActivityActivityScenario.close();
    }

    public void launchSignupScreen() {
        activityTestRule = ActivityScenario.launch(SignupActivity.class);
        activityTestRule.onActivity(activity -> signupActivity = activity);
    }

    public void iAmOnSignupScreen() {
        assertNotNull(signupActivity);
    }

    public void iTapLoginButton() {
        // Close the keyboard else the login button is not available for click on the screen
        onView(withId(R.id.login)).perform(closeSoftKeyboard(), click());
    }

    public void iShouldSeeLoginScreen() {
        SystemClock.sleep(2000);
        onView(withId(R.id.page_title)).check(matches(withText(R.string.login)));
    }
}
