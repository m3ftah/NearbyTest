package com.example.CC;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.tasks.Task;

import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.api.support.membermodification.MemberModifier;

import java.util.List;

/**
 * Created by Lakhdar on 2/12/2018.
 */

public class CShadow extends ConnectionsClient {

    public static String TAG = "ConnectionsClient";


    private CController cController;
    private static Activity activity;


    public static void prepare(){
        MemberModifier.suppress(MemberMatcher.constructorsDeclaredIn(CShadow.class));

    }

    public static void verify(){
        /// Verify that our Mock has been called inside onCreate method in MainActivity.
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        Nearby.getConnectionsClient(activity);
    }

    public CShadow setActivity(Activity activity){
        CShadow.activity = activity;
        this.cController = new CController();
        //Because Nearby.class is final, we force its instantiation
        PowerMockito.mockStatic(Nearby.class);
        Mockito.when(Nearby.getConnectionsClient(activity)).thenReturn(this);
        return this;
    }
    public CController getCController(){
        return this.cController;
    }


    public CShadow(){
        super(null,null,null);
    }
    @Override
    public Task<Void> startAdvertising(@NonNull String name,
                                       @NonNull String serviceId,
                                       @NonNull ConnectionLifecycleCallback connectionLifecycleCallback,
                                       @NonNull AdvertisingOptions advertisingOptions) {
        //cAssertion.assertStartAdvertising();
        return cController.getCLifecycleCtrl().update(name,serviceId,connectionLifecycleCallback,advertisingOptions);
    }

    @Override
    public void stopAdvertising(){
        cController.getCLifecycleCtrl().stopAdvertising();
    }

    @Override
    public Task<Void> startDiscovery(@NonNull String serviceId,
                                     @NonNull EndpointDiscoveryCallback endpointDiscoveryCallback,
                                     @NonNull DiscoveryOptions discoveryOptions) {
        return cController.getEndpointDiscoveryCtrl().update(serviceId, endpointDiscoveryCallback,discoveryOptions);
    }

    @Override
    public void stopDiscovery() {
        cController.getEndpointDiscoveryCtrl().stopDiscovery();
    }

    @Override
    public Task<Void> requestConnection(@NonNull String endpointName,
                                        @NonNull String endpointId,
                                        @NonNull ConnectionLifecycleCallback connectionLifecycleCallback) {//TODO requestConnection
        return cController.getCLifecycleCtrl().requestConnection(endpointName,endpointId,connectionLifecycleCallback);
    }

    @Override
    public Task<Void> acceptConnection(@NonNull String endpointId, @NonNull PayloadCallback payloadCallback) {
        return cController.getPayloadController().update(endpointId,payloadCallback);
    }

    @Override
    public Task<Void> rejectConnection(@NonNull String s) {//TODO reject Connection
        return null;
    }

    @Override
    public Task<Void> sendPayload(@NonNull String endpointId, @NonNull Payload payload) {
        return cController.getPayloadController().receivePayload(endpointId,payload);
    }

    @Override
    public Task<Void> sendPayload(@NonNull List<String> list, @NonNull Payload payload) {//TODO Broadcast payload
        return null;
    }

    @Override
    public Task<Void> cancelPayload(long l) {//todo Cancel payload
        return null;
    }

    @Override
    public void disconnectFromEndpoint(@NonNull String s) {//Todo Disconnect from specific endpoint

    }

    @Override
    public void stopAllEndpoints() {
        cController.disconnectAll();
    }
}
