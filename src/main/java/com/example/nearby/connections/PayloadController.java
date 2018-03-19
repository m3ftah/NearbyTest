package com.example.nearby.connections;

import android.util.Log;

import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.tasks.Task;

import junit.framework.Assert;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by guema on 2/19/2018.
 */

public class PayloadController {
    public static String TAG = "PayloadCtrl";

    private PayloadCallback payloadCallback;
    private String endpointId;
    private CController.TaskCC payloadTask;
    private CController.TaskCC sendpayloadTask;
    private boolean accepting = false;

    private SocketController socketController;


    public PayloadController(SocketController socketController) {
        this.socketController = socketController;
    }

    public Task<Void> update(String endpointId, PayloadCallback payloadCallback) {
        if (!accepting){
            this.endpointId  = endpointId;
            this.payloadCallback = payloadCallback;
            this.payloadTask = new CController.TaskCC();
            this.accepting = true;
        }else{
            Log.e(TAG,"already accepting connection");
            //payloadTask.getOnFailureListener().onFailure(new Exception("Already accepting Connection"));
        }
        return payloadTask;
    }

    public void onPayloadReceived(String endpointId, Payload payload) {
        Assert.assertTrue("Must call accept connection before sending payload",this.accepting);
        payloadCallback.onPayloadReceived(endpointId,payload);
        String str = new String(payload.asBytes(), UTF_8);
        Log.i(TAG,"Sending, endpointId : " + endpointId + ", payload : " + str);

        //payloadTask.getOnFailureListener().onFailure(new Exception("can't send payload"));

    }


    public void sendPayloadTransferUpdate(String endpointId, PayloadTransferUpdate payloadTransferUpdate) {
        if (!this.accepting) Log.e(TAG,"Must call accept connection before sending payload");
        Log.i(TAG,"PayloadTransferUpdate, endpointId : " + endpointId);
        payloadCallback.onPayloadTransferUpdate(endpointId,payloadTransferUpdate);
    }

    public Task<Void> sendPayload(String endpointId, Payload payload) {
        String str = new String(payload.asBytes(), UTF_8);
        Log.i(TAG,"Receiving, endpointId : " + endpointId + ", payload : " + str);
        this.sendpayloadTask = new CController.TaskCC();
        return this.sendpayloadTask;
    }
    public void disconnect(String endpointId){
        this.accepting = false;
    }

    public void disconnectAll() {
        disconnect(endpointId);//TODO Disconnect ALl opponents
    }
}
