package com.example.CC;

import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;

import junit.framework.Assert;

/**
 * Created by guema on 2/19/2018.
 */

public class ConnectionLifecycleController {
    public static String TAG = "ConnectionLifecycleCtrl";

    private ConnectionLifecycleCallback connectionLifecycleCallback;
    private CController.TaskCC advertisingTask;
    private AdvertisingOptions advertisingOptions;
    private String myName;
    private String myEdpointId;
    private String serviceId;

    private String otherEndpintName;
    private String otherEndpointId;

    private boolean connected = false;

    private boolean advertising = false;

    private boolean connectionInitiated = false;

    private boolean sentAdvertise = false;

    private String opponentId;//TODO table of opponents

    public CController.TaskCC update(String name,
                                     String serviceId,
                                     ConnectionLifecycleCallback connectionLifecycleCallback,
                                     AdvertisingOptions advertisingOptions) {
        if (!this.advertising){
            this.advertisingTask = new CController.TaskCC();
            this.myName = name;
            this.myEdpointId = name.replace(" ","_") + "_1";
            this.serviceId = serviceId;
            this.connectionLifecycleCallback = connectionLifecycleCallback;
            this.advertisingOptions = advertisingOptions;
            this.advertising = true;
        }else{
            advertisingTask.getOnFailureListener().onFailure(new Exception("Already advertising"));
        }
        return advertisingTask;

    }

    public void sendAdvertisingResult(boolean success){
        Assert.assertTrue("The App must start advertising before receiving advertising result",this.advertising);
        if (success) this.advertisingTask.getOnSuccessDiscoveryListener().onSuccess(null);
        else this.advertisingTask.getOnFailureListener().onFailure(new Exception("Failed to advertise results"));
        this.sentAdvertise = success;
    }
    public void initiateConnection(String endpointId, String endpointName, String authenticationToken){
        Assert.assertTrue("The App must receive advertising result first",this.sentAdvertise);
        this.otherEndpointId = endpointId;
        this.otherEndpintName = endpointName;
        ConnectionInfo connectionInfo = new ConnectionInfo(endpointName,authenticationToken,true);
        connectionLifecycleCallback.onConnectionInitiated(endpointId,connectionInfo);
        this.connectionInitiated = true;
    }
    public void sendConnectionResult(Boolean success, String endpointId){
        Assert.assertTrue("The App must received advertise result first",this.sentAdvertise);
        Assert.assertTrue("The App must initiate/accept connection first",this.connectionInitiated);
        Assert.assertFalse("The App is already connected",this.connected);
        int status = success ? ConnectionsStatusCodes.STATUS_OK : ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED;
        ConnectionResolution connectionResolution = new ConnectionResolution(new Status(status));
        connectionLifecycleCallback.onConnectionResult(endpointId,connectionResolution);
        //TODO check if discovery and advertising are stopped
        this.connected = success;
        this.connectionInitiated = !success;
    }
    public void stopAdvertising() {
        Assert.assertTrue("Must call startAdvertising() before calling stopAdvertising()",this.advertising);
        this.advertising = false;
    }
    public void disconnect(String endpointId){
        Assert.assertTrue("Can't disconnect if the app is not connected",this.connected);
        this.connectionLifecycleCallback.onDisconnected(endpointId);
        this.connected = false;
    }
    public void checkResumed(){
        //Assert.assertTrue("The App didn't start advertising in the activity onResume() lifeCycle",this.advertising);
        if (!this.advertising) Log.w(TAG,"The App didn't start advertising in the activity onResume() lifeCycle");

    }
    public void checkStopped(){
        Assert.assertFalse("Must stop advertising in the activity onStop() lifeCycle",this.advertising);
        Assert.assertFalse("Must Disconnect in the activity onStop() lifeCycle",this.connected);
    }
    public String getMyName(){
        return this.myName;
    }
    public String getMyEdpointId(){
        return this.myEdpointId;
    }

    public void disconnectAll() {
        disconnect(this.otherEndpointId);//TODO disconnect all the opponent from a table
    }
}
