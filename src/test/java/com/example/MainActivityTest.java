package com.example;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.CC.CController;
import com.example.CC.CShadow;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

import org.junit.After;
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
    public static String TAG = "MainActivityTest";

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    public MainActivity activity;

    public CShadow cShadow;
    private CController cController;

    private ActivityController<MainActivity> activityController;

    String endpointId = "Galaxy_S8_2";
    String endpointName = "Galaxy S8";
    String serviceId;
    String auth = "a;lfkdjl";
    TextView status;
    private Context context;
    private View view;
    private String myEndpointId;


    @Before
    public void setUp() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        this.activityController = Robolectric.buildActivity(MainActivity.class, intent);
        this.activity = activityController.get();
        assertNotNull(this.activity);

        CShadow.prepare();
        this.cShadow = new CShadow().setActivity(this.activity);

        this.cController = cShadow.getCController();

        //Go through the ActivityLifeCycle using the controller
        activityController.create().start().resume();


        //Verfiy that the activity is using our mock
        CShadow.verify();


        cController.checkResumed();

        this.context = RuntimeEnvironment.application;

        this.view = Mockito.mock(View.class);


        this.status = (TextView) activity.findViewById(R.id.status);

        this.serviceId = activity.getPackageName().toString();

    }

    public void connect(){
        //Searching oppenents
        activity.findOpponent(view);

        assertNotNull("TextView could not be found", status);
        assertTrue("Expected to find " + context.getString(R.string.status_searching),
                context.getString(R.string.status_searching)
                        .equals(status.getText().toString()));

        cController.getCLifecycleCtrl().sendAdvertisingResult(true);

        this.myEndpointId = cController.getCLifecycleCtrl().getMyEdpointId();

        cController.getEndpointDiscoveryCtrl().sendEndpoint(endpointId,serviceId,endpointName);

        //Initiate the connection
        cController.getCLifecycleCtrl().initiateConnection(endpointId,endpointName,auth);

        //When connected
        cController.getCLifecycleCtrl().sendConnectionResult(true,endpointId);

        assertTrue("Expected to find " + context.getString(R.string.status_searching),
                context.getString(R.string.status_connected)
                        .equals(status.getText().toString()));
    }

    @Test
    public void play(){
        connect();
        activityController.pause().stop();
        cController.checkStopped();
        activityController.start().resume();
        connect();
        for (int i = 0; i<3; i++){
            for (int j = 0; j<3; j++){
                playRound(i,j);
            }
        }

    }

    public View tappedView(int move){
        View tappedView = null;
        switch (move){
            case 0 : tappedView = activity.findViewById(R.id.rock);
                break;
            case 1 : tappedView = activity.findViewById(R.id.scissors);
                break;
            case 2 : tappedView = activity.findViewById(R.id.paper);
                break;
        }
        return tappedView;
    }
    public MainActivity.GameChoice choosed(int move){
        MainActivity.GameChoice choice = null;
        switch (move){
            case 0 : choice = MainActivity.GameChoice.ROCK;
                break;
            case 1 : choice = MainActivity.GameChoice.SCISSORS;
                break;
            case 2 : choice = MainActivity.GameChoice.PAPER;
                break;
        }
        return choice;
    }
    public void playRound(int move1, int move2){

        //Start exchanging messages
        activity.makeMove(tappedView(move1));

        MainActivity.GameChoice myChoice = choosed(move1);
        MainActivity.GameChoice opponentChoice = choosed(move2);

        cController.getPayloadController().sendPayload(myEndpointId,Payload.fromBytes(opponentChoice.name().getBytes(UTF_8)));

        cController.getPayloadController().sendPayloadTransferUpdate(endpointId,
                new PayloadTransferUpdate(0,
                        PayloadTransferUpdate.Status.SUCCESS,0,0));

        String statusText = status.getText().toString();

        //String text = context.getString(R.string.loss_message, myChoice.name(), opponentChoice.name());
        /*assertTrue("Expected to find " + context.getString(R.string.loss_message),
                statusText.contains(text));*/

        Log.i(TAG,statusText);

    }

    public void disconnect(){
        cController.disconnect(endpointId);
        assertTrue("Expected to find " + context.getString(R.string.status_disconnected),
                context.getString(R.string.status_disconnected)
                        .equals(status.getText().toString()));
    }

    @After
    public void tearDown(){
        //cController.getEndpointDiscoveryCtrl().onEndpointLost(endpointId);
        activityController.pause().stop();
        cController.checkStopped();
    }


}