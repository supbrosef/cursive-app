package com.example.nick.cursiveapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(AndroidJUnit4.class)
//public class ExampleInstrumentedTest {
//
//    Intent intent;
//    SharedPreferences.Editor preferencesEditor;
//
//    @Rule
//    public ActivityTestRule<AccountInfoActivity> mActivityRule = new ActivityTestRule<>(
//            AccountInfoActivity.class,
//            true,
//            false); // Activity is not launched immediately
//
//    @Before
//    public void setUp() {
//        Context targetContext = getInstrumentation().getTargetContext();
//        preferencesEditor = PreferenceManager.getDefaultSharedPreferences(targetContext).edit();
//    }
//
//    @Test
//    public void populateUsernameFromSharedPrefsTest() {
//        preferencesEditor.putString("username", "testUsername");
//        preferencesEditor.commit();
//
//        // Launch activity
//        mActivityRule.launchActivity(new Intent());
//
//        onView(withId(R.id.textview_account_username))
//                .check(matches(isDisplayed()))
//                .check(matches(withText(testUsername)));
//    }
//}
