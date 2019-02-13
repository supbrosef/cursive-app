package com.nvarelas.nick.cursivemadeeasy;

import android.content.Context;
import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowContextWrapper;

import static org.junit.Assert.assertEquals;
import androidx.test.core.app.ApplicationProvider;

@RunWith(RobolectricTestRunner.class)

public class MainActivityTest {
    private final Context context = ApplicationProvider.getApplicationContext();
    private final ShadowContextWrapper shadowContextWrapper = Shadow.extract(context);
    @Test
    public void clickingLogin() {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
        activity.findViewById(R.id.button_tutorial).performClick();

        Intent expectedIntent = new Intent(activity, TraceActivity.class);
        Intent actualIntent = shadowContextWrapper.getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }
}
