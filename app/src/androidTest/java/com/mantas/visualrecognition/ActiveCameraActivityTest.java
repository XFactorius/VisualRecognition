package com.mantas.visualrecognition;

import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class ActiveCameraActivityTest {

    private ActiveCameraActivity activeCameraActivity;

    @Rule
    public ActivityTestRule<ActiveCameraActivity> activeCameraActivityActivityTestRule =
            new ActivityTestRule<>(ActiveCameraActivity.class);


    @Before
    public void setUp() {
        activeCameraActivity = activeCameraActivityActivityTestRule.getActivity();
    }

    @Test
    public void testTurnOnAndOffScanning() {
        activeCameraActivity.turnScanningOn();
        assertEquals(true, activeCameraActivity.scanning);
        activeCameraActivity.turnScanningOff();
        assertEquals(false, activeCameraActivity.scanning);
    }

}