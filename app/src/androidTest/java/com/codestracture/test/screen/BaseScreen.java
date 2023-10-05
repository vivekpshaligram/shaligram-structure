package com.codestracture.test.screen;

import android.content.Context;

import org.junit.Before;
import org.junit.Rule;

import dagger.hilt.android.testing.HiltAndroidRule;

public class BaseScreen {

    @Rule(order = 0)
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    public Context context;

    @Before
    public void setup() {
        hiltRule.inject();
    }
}
