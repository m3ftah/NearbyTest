package com.example.activity;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.BuildConfig;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
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

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implements;

import java.util.List;

/**
 * Created by lakhdar on 2/12/2018.
 */

public class ConnectionsClientShadow extends ConnectionsClient {
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private String endpointName;
    private String authenticationToken;
    private Boolean isIncomingConnection;
    private String endpointId;
    private ConnectionLifecycleCallback connectionLifecycleCallback;
    private EndpointDiscoveryCallback endpointDiscoveryCallback;
    private boolean advertising = false;
    private static Activity activity;


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
    public static void prepare(){
        MemberModifier.suppress(MemberMatcher.constructorsDeclaredIn(ConnectionsClientShadow.class));

    }

    public static void verify(){
        /// Verify that our Mock has been called inside onCreate method in MainActivity.
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        Nearby.getConnectionsClient(activity);
    }

    public ConnectionsClientShadow setActivity(Activity activity){
        ConnectionsClientShadow.activity = activity;
        PowerMockito.mockStatic(Nearby.class);
        Mockito.when(Nearby.getConnectionsClient(activity)).thenReturn(this);

        return this;
    }
    public ConnectionsClientShadow setOpponent(String endpointName, String authenticationToken, String endpointId){

        this.endpointName = endpointName;
        this.authenticationToken = authenticationToken;
        this.endpointId = endpointId;
        return this;
    }

    public ConnectionsClientShadow(){
        super(null,null,null);
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
