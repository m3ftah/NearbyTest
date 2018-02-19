package com.example.CC;

import android.util.Log;

import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.tasks.Task;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by guema on 2/19/2018.
 */

public class PayloadController {

    private PayloadCallback payloadCallback;
    private String endpointId;
    private CController.TaskCC payloadTask;
    private CController.TaskCC sendpayloadTask;
    private boolean accepting = false;

    public Task<Void> update(String endpointId, PayloadCallback payloadCallback) {
        if (!accepting){
            this.endpointId  = endpointId;
            this.payloadCallback = payloadCallback;
            this.payloadTask = new CController.TaskCC();
            this.accepting = true;
        }else{
            payloadTask.getOnFailureListener().onFailure(new Exception("Already accepting Connection"));
        }
        return payloadTask;
    }

    public void sendPayload(String endpointId, Payload payload) {
        if (accepting){
            payloadCallback.onPayloadReceived(endpointId,payload);
        }else{
            payloadTask.getOnFailureListener().onFailure(new Exception("can't send payload when not connected"));
        }
    }


    public void sendPayloadTransferUpdate(String endpointId, PayloadTransferUpdate payloadTransferUpdate) {
        payloadCallback.onPayloadTransferUpdate(endpointId,payloadTransferUpdate);
    }

    public Task<Void> receivePayload(String endpointId, Payload payload) {
        String str = new String(payload.asBytes(), UTF_8);
        Log.i("Test",endpointId + " : " + str);
        this.sendpayloadTask = new CController.TaskCC();
        return this.sendpayloadTask;
    }
}
