package com.example.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;
import android.test.mock.MockContext;

import com.example.BuildConfig;
import com.google.android.gms.nearby.Nearby;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18, manifest = "src/main/AndroidManifest.xml")
//@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest(Nearby.class)
public class DeckardActivityTest{


    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Test
    public void testSomething() throws Exception {
        assertTrue(Robolectric.buildActivity(DeckardActivity.class).create().get() != null);
    }

    @Test
    public void testStaticMocking() {
        //Context context = mock(Context.class);
        MockContext context = new MockContext();
        PowerMockito.mockStatic(Nearby.class);
        Mockito.when(Nearby.getConnectionsClient(context)).thenReturn(null);

        assertEquals(Nearby.getConnectionsClient(context),null);
    }
}