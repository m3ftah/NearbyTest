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
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowLog;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.support.membermodification.MemberMatcher.constructor;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18, shadows = {DeckardActivityTest.ShadowLog2.class})
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({Nearby.class})
public class DeckardActivityTest{


    @Rule
    public PowerMockRule rule = new PowerMockRule();

    public MainActivity activity;

    public ConnectionsClient2 connectionsClient;


    @Implements(Log.class)
    public static class ShadowLog2 extends org.robolectric.shadows.ShadowLog{
        public static void i(String tag, String msg) {
            System.out.println("[" + tag + "] " + msg);
        }
    }
/*    @Implements(ConnectionsClient.class)
    public static class ConnectionsClientShadow{
        public static void i(String tag, String msg) {
            System.out.println("[" + tag + "] " + msg);
        }
    }*/


    @Test
    public void testSomething() throws Exception {
        assertTrue(Robolectric.buildActivity(DeckardActivity.class).create().get() != null);
    }


    @Before
    public void setUp() {

        Log.i("LogTest", "log message");

        PowerMockito.mockStatic(Nearby.class);


        Intent intent = new Intent(Intent.ACTION_VIEW);
        ActivityController<MainActivity> activityController = Robolectric.buildActivity(MainActivity.class, intent);
        activity = activityController.get();
        assertNotNull(activity);



        MemberModifier.suppress(MemberMatcher.constructorsDeclaredIn(ConnectionsClient2.class));

        connectionsClient = new ConnectionsClient2()
                .setOpponent("GALAXY_A5_2017",
                        ";lkdasfj;asjfdo[",
                        "ENDPOINT_1"
                );


        Mockito.when(Nearby.getConnectionsClient(activity)).thenReturn(connectionsClient);


        //Go through the ActivityLifeCycle using the controller
        activityController.create().start().resume().get();

        /// Verify that our Mock has been called inside onCreate method in MainActivity.
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        Nearby.getConnectionsClient(activity);

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

        connectionsClient.sendEndpoint("NewEndpoint");
        //When connected
        connectionsClient.sendConnectionResult(true);

        assertTrue("Expected to find " + context.getString(R.string.status_searching),
                context.getString(R.string.status_connected)
                        .equals(status.getText().toString()));

        //When disconnected
        connectionsClient.disconnect();


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