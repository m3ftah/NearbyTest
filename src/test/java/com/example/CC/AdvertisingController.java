package com.example.CC;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;

import junit.framework.Assert;

/**
 * Created by guema on 2/19/2018.
 */

public class AdvertisingController {
    private ConnectionLifecycleCallback connectionLifecycleCallback;
    private CController.TaskCC advertisingTask;
    private AdvertisingOptions advertisingOptions;
    private String name;
    private String serviceId;

    private String otherEndpintName;
    private String otherEndpointId;

    private boolean advertising = false;

    public CController.TaskCC update(String name,
                                     String serviceId,
                                     ConnectionLifecycleCallback connectionLifecycleCallback,
                                     AdvertisingOptions advertisingOptions) {
        this.advertisingTask = new CController.TaskCC();
        if (!this.advertising){
            this.name = name;
            this.serviceId = serviceId;
            this.connectionLifecycleCallback = connectionLifecycleCallback;
            this.advertisingOptions = advertisingOptions;
            this.advertising = true;
        }else{
            advertisingTask.getOnFailureListener().onFailure(new Exception("Already advertising"));
        }
        return advertisingTask;

    }

    public void stopAdvertising() {
        Assert.assertTrue("Must call startAdvertising() before calling stopAdvertising()",this.advertising);
        this.advertising = false;
    }
    public void initiateConnection(String endpointId, String endpointName, String authenticationToken){
        this.otherEndpointId = endpointId;
        this.otherEndpintName = endpointName;
        ConnectionInfo connectionInfo = new ConnectionInfo(endpointName,authenticationToken,true);
        connectionLifecycleCallback.onConnectionInitiated(endpointId,connectionInfo);
    }
    public void sendAdvertisingResult(boolean success){
        if (success) this.advertisingTask.getOnSuccessDiscoveryListener().onSuccess(null);
        else this.advertisingTask.getOnFailureListener().onFailure(new Exception("Failed to advertise"));
    }
    public void disconnect(){
        this.connectionLifecycleCallback.onDisconnected(this.otherEndpointId);
    }

    public void sendConnectionResult(Boolean success, String endpointId){
        int state = success ? -1 : 1;
        ConnectionResolution connectionResolution = new ConnectionResolution(new Status(state));
        connectionLifecycleCallback.onConnectionResult(endpointId,connectionResolution);
    }

    public void disconnect(String endpointId){
        this.connectionLifecycleCallback.onDisconnected(endpointId);
    }
}
