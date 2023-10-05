package com.codestracture.test.utils;

import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Custom matcher to assert equal EditText.setError();
 */
class ErrorTextMatcher extends TypeSafeMatcher<View> {

    private final String mExpectedError;

    public ErrorTextMatcher(String expectedError) {
        mExpectedError = expectedError;
    }

    @Override
    public boolean matchesSafely(View view) {
        if (!(view instanceof EditText)) {
            return false;
        }

        EditText editText = (EditText) view;

        return mExpectedError.equals(editText.getError().toString());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with error: " + mExpectedError);
    }
}