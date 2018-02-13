package com.example.activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.tasks.Task;

import java.util.List;

/**
 * Created by guema on 2/12/2018.
 */

public class ConnectionsClient2 extends ConnectionsClient {
    private static final String endpointName = "GALAXY_A5_2017";
    private static final String authenticationToken = ";lkdasfj;asjfdo[";
    private static final Boolean isIncomingConnection = true;
    private static final String endpointId = "ENDPOINT_1";
    private static ConnectionLifecycleCallback connectionLifecycleCallback;
    private static EndpointDiscoveryCallback endpointDiscoveryCallback;

    protected ConnectionsClient2(Activity activity, Api<Api.ApiOptions.NoOptions> api, zza zza) {
        super(activity, api, zza);
    }

    @Override
    public Task<Void> startAdvertising(@NonNull String s, @NonNull String s1, @NonNull ConnectionLifecycleCallback connectionLifecycleCallback, @NonNull AdvertisingOptions advertisingOptions) {
        this.connectionLifecycleCallback = connectionLifecycleCallback;
        ConnectionInfo connectionInfo = new ConnectionInfo(endpointName,authenticationToken,isIncomingConnection);
        connectionLifecycleCallback.onConnectionInitiated(endpointId,connectionInfo);
        return null;
    }

    @Override
    public void stopAdvertising() {

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
