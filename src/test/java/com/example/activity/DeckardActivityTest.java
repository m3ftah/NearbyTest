package com.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.test.mock.MockContext;
import android.view.View;

import com.example.BuildConfig;
import com.example.MainActivity;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.PayloadCallback;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({Nearby.class,ConnectionsClient.class})
public class DeckardActivityTest{


    @Rule
    public PowerMockRule rule = new PowerMockRule();

    public MainActivity activity;

    public ConnectionsClient connectionsClient;


    @Test
    public void testSomething() throws Exception {
        assertTrue(Robolectric.buildActivity(DeckardActivity.class).create().get() != null);
    }


    @Before
    public void setUp() {
        MockContext context = new MockContext();

        connectionsClient = PowerMockito.mock(ConnectionsClient.class);

        PowerMockito.mockStatic(Nearby.class);

        Mockito.when(Nearby.getConnectionsClient(context)).thenReturn(connectionsClient);

        assertEquals(Nearby.getConnectionsClient(context),connectionsClient);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        activity = Robolectric.buildActivity(MainActivity.class, intent).create().start().resume().get();
        //exampleActivity = Robolectric.buildActivity(Activity.class).create().start().resume().get();
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

        Mockito.when(connectionsClient.startDiscovery(Mockito.anyString(),Mockito.any(EndpointDiscoveryCallback.class),Mockito.any(DiscoveryOptions.class))).thenReturn(null);

        connectionsClient.startDiscovery(packageName,edc,dop);
    }


    @Test
    public void testCallback(){

        assertNotNull(activity);
/*


        ConnectionsClient connectionsClient = Mockito.mock(ConnectionsClient.class);
*/

        View view = Mockito.mock(View.class);

        activity.findOpponent(view);

        Mockito.verify(connectionsClient.acceptConnection(Mockito.anyString(), Mockito.any(PayloadCallback.class)));

    }
}