package com.example.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.BuildConfig;
import com.example.CC.CController;
import com.example.MainActivity;
import com.example.R;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implements;

import static java.nio.charset.StandardCharsets.UTF_8;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by lakhdar on 2/12/2018.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18, shadows = {LogShadow.class})
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({Nearby.class})
public class MainActivityTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    public MainActivity activity;

    public CShadow cShadow;
    private CController cController;

    private ActivityController<MainActivity> activityController;




    @Before
    public void setUp() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        this.activityController = Robolectric.buildActivity(MainActivity.class, intent);
        this.activity = activityController.get();
        assertNotNull(this.activity);

        CShadow.prepare();
        this.cShadow = new CShadow().setActivity(this.activity);

        this.cController = cShadow.getCController();


    }


    @Test
    public void testConnection(){

        String endpointId = "newTestEndpointId";
        String endpointName = "Galaxy S8";
        String serviceId = activity.getPackageName().toString();
        String auth = "a;lfkdjl";
        //Go through the ActivityLifeCycle using the controller
        activityController.create().start().resume();

        //Verfiy that the activity is using our mock
        CShadow.verify();

        final Context context = RuntimeEnvironment.application;

        View view = Mockito.mock(View.class);

        //Searching oppenents
        activity.findOpponent(view);

        TextView status = (TextView) activity.findViewById(R.id.status);
        assertNotNull("TextView could not be found", status);
        assertTrue("Expected to find " + context.getString(R.string.status_searching),
                context.getString(R.string.status_searching)
                .equals(status.getText().toString()));

        cController.getcAdvertising().sendAdvertisingResult(false);

        cController.getcDiscovery().sendEndpoint(endpointId,serviceId,endpointName);

        //When connected
        cController.getcAdvertising().sendConnectionResult(true,endpointId);

        assertTrue("Expected to find " + context.getString(R.string.status_searching),
                context.getString(R.string.status_connected)
                        .equals(status.getText().toString()));

        cController.getcAdvertising().initiateConnection(endpointId,endpointName,auth);

        activity.makeMove(activity.findViewById(R.id.rock));

        MainActivity.GameChoice myChoice = MainActivity.GameChoice.ROCK;
        MainActivity.GameChoice opponentChoice = MainActivity.GameChoice.PAPER;
        cController.getPayloadController().sendPayload(endpointId,Payload.fromBytes(opponentChoice.name().getBytes(UTF_8)));

        cController.getPayloadController().sendPayloadTransferUpdate(endpointId,
                new PayloadTransferUpdate(0,
                PayloadTransferUpdate.Status.SUCCESS,0,0));

        String text = context.getString(R.string.loss_message, myChoice.name(), opponentChoice.name());
        String statusText = status.getText().toString();
        assertTrue("Expected to find " + context.getString(R.string.loss_message),
                statusText.contains(text));

        Log.i("Test",statusText);


        cController.getcDiscovery().sendEndpointLost(endpointId);

        //When disconnected
        cController.getcAdvertising().disconnect();


        assertTrue("Expected to find " + context.getString(R.string.status_disconnected),
                context.getString(R.string.status_disconnected)
                        .equals(status.getText().toString()));

    }


}