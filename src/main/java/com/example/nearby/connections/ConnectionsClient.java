package com.example.nearby.connections;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.tasks.Task;


import java.util.List;

/**
 * Created by Lakhdar on 2/12/2018.
 */

public class ConnectionsClient {

    public static String TAG = "ConnectionsClient";


    private CController cController;
    private static Activity activity;



    public ConnectionsClient(Activity activity){
        ConnectionsClient.activity = activity;
        this.cController = new CController(activity);
    }
    public CController getCController(){
        return this.cController;
    }



    public Task<Void> startAdvertising(@NonNull String name,
                                       @NonNull String serviceId,
                                       @NonNull ConnectionLifecycleCallback connectionLifecycleCallback,
                                       @NonNull AdvertisingOptions advertisingOptions) {
        cController.getSocketController().advertise(name,serviceId,advertisingOptions.getStrategy().toString());
        return cController.getCLifecycleCtrl().update(name,serviceId,connectionLifecycleCallback,advertisingOptions);
    }

    public void stopAdvertising(){
        cController.getCLifecycleCtrl().stopAdvertising();
    }

    public Task<Void> startDiscovery(@NonNull String serviceId,
                                     @NonNull EndpointDiscoveryCallback endpointDiscoveryCallback,
                                     @NonNull DiscoveryOptions discoveryOptions) {
        cController.getSocketController().discover(serviceId,discoveryOptions.getStrategy().toString());
        return cController.getEndpointDiscoveryCtrl().update(serviceId, endpointDiscoveryCallback,discoveryOptions);
    }

    public void stopDiscovery() {
        cController.getEndpointDiscoveryCtrl().stopDiscovery();
    }

    public Task<Void> requestConnection(@NonNull String endpointName,
                                        @NonNull String endpointId,
                                        @NonNull ConnectionLifecycleCallback connectionLifecycleCallback) {//TODO requestConnection
        cController.getSocketController().requestConnection(endpointName,endpointId);
        return cController.getCLifecycleCtrl().requestConnection(endpointName,endpointId,connectionLifecycleCallback);
    }

    public Task<Void> acceptConnection(@NonNull String endpointId, @NonNull PayloadCallback payloadCallback) {
        cController.getSocketController().acceptConnection(endpointId);
        return cController.getPayloadController().update(endpointId,payloadCallback);
    }

    public Task<Void> rejectConnection(@NonNull String s) {//TODO reject Connection
        return null;
    }

    public Task<Void> sendPayload(@NonNull String endpointId, @NonNull Payload payload) {
        cController.getSocketController().sendPayload(endpointId,payload);
        return cController.getPayloadController().sendPayload(endpointId,payload);
    }

    public Task<Void> sendPayload(@NonNull List<String> list, @NonNull Payload payload) {//TODO Broadcast payload
        return null;
    }

    public Task<Void> cancelPayload(long l) {//todo Cancel payload
        return null;
    }

    public void disconnectFromEndpoint(@NonNull String s) {//Todo Disconnect from specific endpoint
        stopAllEndpoints();

    }

    public void stopAllEndpoints() {
        cController.disconnectAll();
    }
}
