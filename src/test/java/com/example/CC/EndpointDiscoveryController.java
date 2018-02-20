package com.example.CC;

import android.util.Log;

import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.tasks.Task;

import junit.framework.Assert;

/**
 * Created by guema on 2/19/2018.
 */

public class EndpointDiscoveryController {
    public static String TAG = "EndpointDiscoveryCtrl";

    private EndpointDiscoveryCallback endpointDiscoveryCallback;

    private CController.TaskCC discoveryTask;

    private boolean discovering = false;

    private String serviceId;

    private DiscoveryOptions discoveryOptions;
    private String otherEndpointId;


    public Task<Void> update(String serviceId,
                             EndpointDiscoveryCallback endpointDiscoveryCallback,
                             DiscoveryOptions discoveryOptions) {
        this.discoveryTask = new CController.TaskCC();
        if (!this.discovering){
            this.serviceId = serviceId;
            this.endpointDiscoveryCallback = endpointDiscoveryCallback;
            this.discoveryOptions = discoveryOptions;
            this.discovering = true;
        }else{
            this.discoveryTask.getOnFailureListener().onFailure(new Exception("Already discovering"));
        }
        return discoveryTask;
    }
    public void sendEndpoint(String endpointId, String serviceId, String endpointName){
        Assert.assertTrue("The App must start discovery before receiving any EndpointFound",this.discovering);
        DiscoveredEndpointInfo discoveredEndpointInfo = new DiscoveredEndpointInfo(serviceId,endpointName);
        this.endpointDiscoveryCallback.onEndpointFound(endpointId,discoveredEndpointInfo);
    }
    public void sendEndpointLost(String endpointId) {
        Assert.assertTrue("The App must start discovering before receiving any EndpointLost",this.discovering);
        this.endpointDiscoveryCallback.onEndpointLost(endpointId);
    }

    public void sendDiscoveryResult(boolean success){
        Assert.assertTrue("The App must start discovering before receiving any DiscoveryResult",this.discovering);
        if (success) this.discoveryTask.getOnSuccessDiscoveryListener().onSuccess(null);
        else this.discoveryTask.getOnFailureListener().onFailure(new Exception("Failed to discover"));
    }

    public void stopDiscovery() {
        Assert.assertTrue("Must call startDiscovery() before calling stopDiscovery()",this.discovering);
        this.discovering = false;
    }

    public void checkResumed() {
        //Assert.assertTrue("The App didn't start discovering in the activity onResume() lifeCycle",this.discovering);
        if (!this.discovering) Log.w(TAG,"The App didn't start discovering in the activity onResume() lifeCycle");
    }

    public void checkStopped() {
        Assert.assertFalse("Must stop discovering in the activity onStop() lifeCycle",this.discovering);
    }

    public void disconnect(String endpointId) {
        Assert.assertFalse("Must stop discovering before disconnecting",this.discovering);
    }

    public void disconnectAll(){
        //disconnect(otherEndpointId);
    }
}
