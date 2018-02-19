package com.example.activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.tasks.Task;

import junit.framework.Assert;

import java.util.List;

/**
 * Created by guema on 2/12/2018.
 */

public class ConnectionsClient2 extends ConnectionsClient {
    private String endpointName;
    private String authenticationToken;
    private Boolean isIncomingConnection;
    private String endpointId;
    private ConnectionLifecycleCallback connectionLifecycleCallback;
    private EndpointDiscoveryCallback endpointDiscoveryCallback;
    private boolean advertising = false;

    public void sendConnectionResult(Boolean success){
        int state = success ? -1 : 1;
        ConnectionResolution connectionResolution = new ConnectionResolution(new Status(state));
        connectionLifecycleCallback.onConnectionResult(endpointId,connectionResolution);
    }

    public void disconnect(){
        this.connectionLifecycleCallback.onDisconnected(this.endpointId);
    }
    public void sendEndpoint(String endpointId){
        DiscoveredEndpointInfo discoveredEndpointInfo = new DiscoveredEndpointInfo(endpointId,"emptyString");
        this.endpointDiscoveryCallback.onEndpointFound(endpointId,discoveredEndpointInfo);
    }

    public ConnectionsClient2(){
        super(null,null,null);
    }
    public ConnectionsClient2 setOpponent(String endpointName, String authenticationToken,String endpointId){
        this.endpointName = endpointName;
        this.authenticationToken = authenticationToken;
        this.endpointId = endpointId;
        return this;
    }


    @Override
    public Task<Void> startAdvertising(@NonNull String s, @NonNull String s1, @NonNull ConnectionLifecycleCallback connectionLifecycleCallback, @NonNull AdvertisingOptions advertisingOptions) {
        this.advertising = true;
        this.connectionLifecycleCallback = connectionLifecycleCallback;
        ConnectionInfo connectionInfo = new ConnectionInfo(endpointName,authenticationToken,true);
        connectionLifecycleCallback.onConnectionInitiated(endpointId,connectionInfo);


        return null;
    }

    @Override
    public void stopAdvertising(){
        Assert.assertTrue("Must call startAdvertising() before calling stopAdvertising()",this.advertising);
        this.advertising = false;
        //else throw new Exception("Stop Advertising called while not advertising.");
    }

    @Override
    public Task<Void> startDiscovery(@NonNull String s, @NonNull EndpointDiscoveryCallback endpointDiscoveryCallback, @NonNull DiscoveryOptions discoveryOptions) {
        this.endpointDiscoveryCallback = endpointDiscoveryCallback;
        DiscoveredEndpointInfo discoveredEndpointInfo = new DiscoveredEndpointInfo(endpointName,authenticationToken);
        endpointDiscoveryCallback.onEndpointFound(endpointId,discoveredEndpointInfo);
        return null;
    }

    @Override
    public void stopDiscovery() {

    }

    @Override
    public Task<Void> requestConnection(@NonNull String s, @NonNull String s1, @NonNull ConnectionLifecycleCallback connectionLifecycleCallback) {
        return null;
    }

    @Override
    public Task<Void> acceptConnection(@NonNull String s, @NonNull PayloadCallback payloadCallback) {

        return null;
    }

    @Override
    public Task<Void> rejectConnection(@NonNull String s) {
        return null;
    }

    @Override
    public Task<Void> sendPayload(@NonNull String s, @NonNull Payload payload) {
        return null;
    }

    @Override
    public Task<Void> sendPayload(@NonNull List<String> list, @NonNull Payload payload) {
        return null;
    }

    @Override
    public Task<Void> cancelPayload(long l) {
        return null;
    }

    @Override
    public void disconnectFromEndpoint(@NonNull String s) {

    }

    @Override
    public void stopAllEndpoints() {

    }
}
