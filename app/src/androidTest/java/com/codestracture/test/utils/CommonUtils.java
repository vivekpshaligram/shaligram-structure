package com.codestracture.test.utils;

import android.view.View;
import org.hamcrest.Matcher;

public class CommonUtils {

    public static Matcher<? super View> hasErrorText(String expectedError) {
        return new ErrorTextMatcher(expectedError);
    }
}
