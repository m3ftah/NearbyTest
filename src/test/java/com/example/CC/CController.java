package com.example.CC;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

/**
 * Created by Lakhdar on 2/19/2018.
 */

public class CController {
    public static String TAG = "CCtrl";


    private EndpointDiscoveryController endpointDiscoveryCtrl;
    private ConnectionLifecycleController cLifecycleCtrl;

    public PayloadController getPayloadController() {
        return payloadController;
    }

    private PayloadController payloadController;

    public EndpointDiscoveryController getEndpointDiscoveryCtrl() {
        return endpointDiscoveryCtrl;
    }

    public ConnectionLifecycleController getCLifecycleCtrl() {
        return cLifecycleCtrl;
    }


    public CController(){
        this.cLifecycleCtrl = new ConnectionLifecycleController();
        this.endpointDiscoveryCtrl = new EndpointDiscoveryController();
        this.payloadController = new PayloadController();
    }

    public void checkStopped() {
        this.cLifecycleCtrl.checkStopped();
        this.endpointDiscoveryCtrl.checkStopped();
    }

    public void checkResumed() {
        this.cLifecycleCtrl.checkResumed();
        this.endpointDiscoveryCtrl.checkResumed();
    }

    public void disconnect(String endpointId) {
        this.cLifecycleCtrl.disconnect(endpointId);
        this.endpointDiscoveryCtrl.disconnect(endpointId);
        this.payloadController.disconnect(endpointId);
    }
    public void disconnectAll() {
        this.cLifecycleCtrl.disconnectAll();
        this.endpointDiscoveryCtrl.disconnectAll();
        this.payloadController.disconnectAll();
    }


    public static class TaskCC extends Task<Void>{
        //private String state = "advertising";
        private OnSuccessListener<? super Void> onSuccessDiscoveryListener;
        private OnFailureListener onFailureListener;

        public OnSuccessListener<? super Void> getOnSuccessDiscoveryListener() {
            return onSuccessDiscoveryListener;
        }

        public OnFailureListener getOnFailureListener() {
            return onFailureListener;
        }

        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public boolean isSuccessful() {
            return false;
        }

        @Override
        public Void getResult() {
            return null;
        }

        @Override
        public <X extends Throwable> Void getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnSuccessListener(@NonNull OnSuccessListener<? super Void> onSuccessListener) {
            this.onSuccessDiscoveryListener = onSuccessListener;
            return this;
        }

        @NonNull
        @Override
        public Task<Void> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super Void> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super Void> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            this.onFailureListener = onFailureListener;
            return this;
        }

        @NonNull
        @Override
        public Task<Void> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<Void> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

    }


}
