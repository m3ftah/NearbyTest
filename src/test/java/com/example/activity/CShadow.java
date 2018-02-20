package com.example.activity;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.CC.CAssertions;
import com.example.CC.CController;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.tasks.Task;

import org.junit.Rule;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.modules.junit4.rule.PowerMockRule;

import java.util.List;

/**
 * Created by Lakhdar on 2/12/2018.
 */

public class CShadow extends ConnectionsClient {

    public static String TAG = "CShadow";

    @Rule
    public PowerMockRule rule = new PowerMockRule();


    private CController cController;
    private static Activity activity;
    private CAssertions cAssertion;


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
        this.cAssertion = new CAssertions(cController);
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
        return cController.getcAdvertising().update(name,serviceId,connectionLifecycleCallback,advertisingOptions);
    }

    @Override
    public void stopAdvertising(){
        cController.getcAdvertising().stopAdvertising();
    }

    @Override
    public Task<Void> startDiscovery(@NonNull String serviceId,
                                     @NonNull EndpointDiscoveryCallback endpointDiscoveryCallback,
                                     @NonNull DiscoveryOptions discoveryOptions) {
        return cController.getcDiscovery().update(serviceId, endpointDiscoveryCallback,discoveryOptions);
    }

    @Override
    public void stopDiscovery() {
        cController.getcDiscovery().stopDiscovery();
    }

    @Override
    public Task<Void> requestConnection(@NonNull String s, @NonNull String s1, @NonNull ConnectionLifecycleCallback connectionLifecycleCallback) {
        return null;
    }

    @Override
    public Task<Void> acceptConnection(@NonNull String endpointId, @NonNull PayloadCallback payloadCallback) {
        return cController.getPayloadController().update(endpointId,payloadCallback);
    }

    @Override
    public Task<Void> rejectConnection(@NonNull String s) {
        return null;
    }

    @Override
    public Task<Void> sendPayload(@NonNull String endpointId, @NonNull Payload payload) {
        return cController.getPayloadController().receivePayload(endpointId,payload);
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
        cController.disconnectAll();
    }
}
