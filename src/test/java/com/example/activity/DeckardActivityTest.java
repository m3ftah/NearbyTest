package com.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.test.mock.MockContext;
import android.view.View;

import com.example.BuildConfig;
import com.example.MainActivity;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.support.membermodification.MemberMatcher.constructor;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({Nearby.class, ConnectionsClient.class})
public class DeckardActivityTest{


    @Rule
    public PowerMockRule rule = new PowerMockRule();

    public MainActivity activity;

    public ConnectionsClient2 connectionsClient;


    @Test
    public void testSomething() throws Exception {
        assertTrue(Robolectric.buildActivity(DeckardActivity.class).create().get() != null);
    }


    @Before
    public void setUp() {

        PowerMockito.mockStatic(Nearby.class);


        Intent intent = new Intent(Intent.ACTION_VIEW);
        ActivityController<MainActivity> activityController = Robolectric.buildActivity(MainActivity.class, intent);
        activity = activityController.get();
        assertNotNull(activity);



        PowerMockito.mock(ConnectionsClient.class);


        MemberModifier.suppress(MemberMatcher.constructorsDeclaredIn(ConnectionsClient2.class));

        //suppress(constructor(ConnectionsClient.class,Activity.class,Mockito.any(Object.class), Mockito.any(Object.class)));
        connectionsClient = new ConnectionsClient2(null,null,null);




        Mockito.when(Nearby.getConnectionsClient(activity)).thenReturn(connectionsClient);


        //Go through the ActivityLifeCycle using the controller
        activityController.create().start().resume().get();

        /// Verify that our Mock has been called inside onCreate method in MainActivity.
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        Nearby.getConnectionsClient(activity);

    }


    @Test
    public void testCallback(){
        String s = "example";
        String s2 = "example2";
        String s3 = "example3";
        Boolean b  = true;

        ConnectionInfo connectionInfo = new ConnectionInfo(s2,s3,b);
        ConnectionLifecycleCallback connectionLifecycleCallback = Mockito.mock(ConnectionLifecycleCallback.class);

        View view = Mockito.mock(View.class);
        activity.findOpponent(view);

        connectionLifecycleCallback.onConnectionInitiated(s,connectionInfo);


        //Mockito.verify(connectionsClient.acceptConnection(Mockito.anyString(), Mockito.any(PayloadCallback.class)));

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