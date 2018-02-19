package com.example.CC;

import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.tasks.Task;

/**
 * Created by guema on 2/19/2018.
 */

public class DiscoveryController {
    private EndpointDiscoveryCallback endpointDiscoveryCallback;

    private CController.TaskCC discoveryTask;

    private boolean discovering = false;

    private String serviceId;

    private DiscoveryOptions discoveryOptions;


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
        DiscoveredEndpointInfo discoveredEndpointInfo = new DiscoveredEndpointInfo(serviceId,endpointName);
        this.endpointDiscoveryCallback.onEndpointFound(endpointId,discoveredEndpointInfo);
    }
    public void sendEndpointLost(String endpointId) {
        this.endpointDiscoveryCallback.onEndpointLost(endpointId);
    }

    public void sendDiscoveryResult(boolean success){
        if (success) this.discoveryTask.getOnSuccessDiscoveryListener().onSuccess(null);
        else this.discoveryTask.getOnFailureListener().onFailure(new Exception("Failed to discover"));
    }
}
