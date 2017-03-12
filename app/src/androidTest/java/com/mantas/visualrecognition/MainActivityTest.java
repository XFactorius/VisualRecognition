package com.mantas.visualrecognition;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MainActivityTest {

    private MainActivity mainActivity;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        mainActivity = mainActivityActivityTestRule.getActivity();
    }

    @Test
    public void testTurnOnSettings() {
        Instrumentation.ActivityMonitor am = new Instrumentation.ActivityMonitor(SettingsActivity.class.getName(), null, false);
        Instrumentation inst = InstrumentationRegistry.getInstrumentation();
        inst.addMonitor(am);

        mainActivity.doActionBasedOnMatch(Arrays.asList("settings"));
        am.waitForActivity();

        assertEquals(1, am.getHits());
    }

    @Test
    public void testIfTitleIsSet() {
        final TextView textView = (TextView) mainActivity.findViewById(R.id.welcome_text);

        assertEquals("Visual recognition by Mantas Tekorius", textView.getText());

    }

    @Test
    public void testTurnOnCamera() {
        Instrumentation.ActivityMonitor am = new Instrumentation.ActivityMonitor(ActiveCameraActivity.class.getName(), null, false);
        Instrumentation inst = InstrumentationRegistry.getInstrumentation();
        inst.addMonitor(am);

        mainActivity.doActionBasedOnMatch(Arrays.asList("camera"));
        am.waitForActivity();

        assertEquals(1, am.getHits());
    }

}