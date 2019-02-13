package com.nvarelas.nick.cursivemadeeasy;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import 	org.robolectric.shadow.api.Shadow;

import androidx.test.core.app.ApplicationProvider;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

    @Test
    public void clickingLogin() {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
        activity.findViewById(R.id.button_tutorial).performClick();

        Intent expectedIntent = new Intent(activity, TraceActivity.class);
        Intent actualIntent = Shadows.shadowOf(ApplicationProvider.getApplicationContext()).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }
}
