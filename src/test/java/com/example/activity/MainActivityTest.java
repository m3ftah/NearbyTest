package com.example.activity;

import android.content.Context;
import android.content.Intent;
import android.test.mock.MockContext;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.BuildConfig;
import com.example.MainActivity;
import com.example.R;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implements;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;

/**
 * Created by lakhdar on 2/12/2018.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18, shadows = {MainActivityTest.ShadowLog2.class})
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({Nearby.class})
public class MainActivityTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    public MainActivity activity;

    public ConnectionsClientShadow cShadow;


    @Implements(Log.class)
    public static class ShadowLog2 extends org.robolectric.shadows.ShadowLog{
        public static void i(String tag, String msg) {
            System.out.println("[" + tag + "] " + msg);
        }
    }


    @Before
    public void setUp() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        ActivityController<MainActivity> activityController = Robolectric.buildActivity(MainActivity.class, intent);
        activity = activityController.get();
        assertNotNull(activity);

        ConnectionsClientShadow.prepare();
        this.cShadow = new ConnectionsClientShadow()
                .setActivity(activity)
                .setOpponent("GALAXY_A5_2017",
                        ";lkdasfj;asjfdo[",
                        "ENDPOINT_1"
                );


        //Go through the ActivityLifeCycle using the controller
        activityController.create().start().resume().get();

        ConnectionsClientShadow.verify();

    }


    @Test
    public void testConnection(){
        final Context context = RuntimeEnvironment.application;

        View view = Mockito.mock(View.class);

        //Searching oppenents
        activity.findOpponent(view);

        TextView status = (TextView) activity.findViewById(R.id.status);
        assertNotNull("TextView could not be found", status);
        assertTrue("Expected to find " + context.getString(R.string.status_searching),
                context.getString(R.string.status_searching)
                .equals(status.getText().toString()));

        cShadow.sendEndpoint("NewEndpoint");
        //When connected
        cShadow.sendConnectionResult(true);

        assertTrue("Expected to find " + context.getString(R.string.status_searching),
                context.getString(R.string.status_connected)
                        .equals(status.getText().toString()));

        //When disconnected
        cShadow.disconnect();


        assertTrue("Expected to find " + context.getString(R.string.status_disconnected),
                context.getString(R.string.status_disconnected)
                        .equals(status.getText().toString()));

    }

    @Test
    public void testStaticMocking() {
        //Context context = mock(Context.class);
        MockContext context = new MockContext();

        ConnectionsClient connectionsClient = PowerMockito.mock(ConnectionsClient.class);

        PowerMockito.mockStatic(Nearby.class);

        Mockito.when(Nearby.getConnectionsClient(context)).thenReturn(connectionsClient);

        assertEquals(Nearby.getConnectionsClient(context),connectionsClient);
    }

    @Test
    public void testDiscovery(){
        String packageName = "com.example";
        EndpointDiscoveryCallback edc = new EndpointDiscoveryCallback() {
            @Override
            public void onEndpointFound(String s, DiscoveredEndpointInfo discoveredEndpointInfo) {

            }

            @Override
            public void onEndpointLost(String s) {

            }
        };
        DiscoveryOptions dop = Mockito.mock(DiscoveryOptions.class);
        PowerMockito.mockStatic(ConnectionsClient.class);

        ConnectionsClient connectionsClient = PowerMockito.mock(ConnectionsClient.class);

        Mockito.when(connectionsClient.startDiscovery(anyString(),Mockito.any(EndpointDiscoveryCallback.class),Mockito.any(DiscoveryOptions.class))).thenReturn(null);

        connectionsClient.startDiscovery(packageName,edc,dop);
    }


}