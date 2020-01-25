package com.lightcyclesoftware.photoscodeexample;

import android.content.Context;
<<<<<<< HEAD
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
=======
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
>>>>>>> 0829545893dfa9db91b1bcdc4a842ef506a9d179

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.lightcyclesoftware.waldophotoscodeexample", appContext.getPackageName());
    }
}
