package com.example.nearby.connections;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * Created by Lakhdar on 2/23/2018.
 */

public class SocketController {
    public static String TAG = "SocketCtrl";

    private CController cController;
    private Socket mSocket;

    private Activity activity;


    List<String> listPeerSocket;

    public SocketController(CController cController, Activity activity) {
        this.cController = cController;
        try {
            this.activity = activity;
            mSocket = IO.socket(Constants.SERVER_ADDRESS + ":" + Constants.SERVER_PORT);
            //mSocket = IO.socket("https://socket-io-chat.now.sh/");

            mSocket.connect();
            Log.d(TAG, "mSocket.connect()");


            mSocket.on(Constants.ON_ENDPOINT_FOUND, onEndpointFound);
            mSocket.on(Constants.ON_ENDPOINT_LOST, onEndpointLost);

            mSocket.on(Constants.ON_CONNECTION_RESULT, onConnectionResult);
            mSocket.on(Constants.ON_CONNECTION_INITIATED, onConnectionInitiated);
            mSocket.on(Constants.ON_DISCONNECTED, onDisconnected);

            mSocket.on(Constants.ON_PAYLOAD_RECEIVED, onPayloadReceived);
            mSocket.on(Constants.ON_PAYLOAD_TRANSFER_UPDATE, onPayloadTransferUpdate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Emitter.Listener onEndpointFound = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.d(TAG,"onEndpointFound : " + data.toString());
            try {
                String endpointId = data.getString(Constants.ENDPOINT_ID);
                String serviceId = data.getString(Constants.SERVICE_ID);
                String endpointName = data.getString(Constants.ENDPOINT_NAME);
                cController.getEndpointDiscoveryCtrl().onEndpointFound(endpointId,serviceId,endpointName);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    };
    private Emitter.Listener onEndpointLost = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.d(TAG,"onEndpointLost : " + data.toString());
            try {
                String endpointId = data.getString(Constants.ENDPOINT_ID);
                cController.getEndpointDiscoveryCtrl().onEndpointLost(endpointId);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    };
    private Emitter.Listener onConnectionResult = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.d(TAG,"onConnectionResult : " + data.toString());
            try {
                final String endpointId = data.getString(Constants.ENDPOINT_ID);
                final Boolean state = data.getBoolean(Constants.STATE);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cController.getCLifecycleCtrl().onConnectionResult(state,endpointId);
                    }
                });
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    };
    private Emitter.Listener onConnectionInitiated = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.d(TAG,"onConnectionInitiated : " + data.toString());
            try {
                final String endpointId = data.getString(Constants.ENDPOINT_ID);
                final String endpointName = data.getString(Constants.ENDPOINT_NAME);
                final String auth = data.getString(Constants.AUTH);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cController.getCLifecycleCtrl().onConnectionInitiated(endpointId,endpointName,auth);
                    }
                });

            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    };
    private Emitter.Listener onPayloadReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.d(TAG,"onPayloadReceived : " + data.toString());
            try {
                String endpointId = data.getString(Constants.ENDPOINT_ID);
                String payloadStr = data.getString(Constants.PAYLOAD);
                cController.getPayloadController().onPayloadReceived(endpointId, Payload.fromBytes(payloadStr.getBytes(UTF_8)));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Constants.ENDPOINT_ID,endpointId);
                mSocket.emit(Constants.PAYLOAD_RECEIVED,jsonObject);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    };

    private Emitter.Listener onPayloadTransferUpdate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.d(TAG,"onPayloadReceived : " + data.toString());
            try {
                String endpointId = data.getString(Constants.ENDPOINT_ID);
                cController.getPayloadController().sendPayloadTransferUpdate(endpointId,
                        new PayloadTransferUpdate(0,
                                PayloadTransferUpdate.Status.SUCCESS,0,0));
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    };

    private Emitter.Listener onDisconnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.d(TAG,"onDisconnected : " + data.toString());
            try {
                String endpointId = data.getString(Constants.ENDPOINT_ID);
                cController.getCLifecycleCtrl().onDisconnected(endpointId);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());

            }
        }
    };

    public void advertise(String name, String serviceId, String strategy){
        Log.d(TAG,"Advertise");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ENDPOINT_NAME,name);
            jsonObject.put(Constants.SERVICE_ID,serviceId);
            jsonObject.put(Constants.STRATEGY,strategy);
            mSocket.emit(Constants.ADVERTISE,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void discover(String serviceId, String strategy) {
        Log.d(TAG,"Discover");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.SERVICE_ID,serviceId);
            jsonObject.put(Constants.STRATEGY,strategy);
            mSocket.emit(Constants.DISCOVER,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void stopDiscovery() {
        Log.d(TAG,"stopDiscovery");
        mSocket.emit(Constants.STOP_DISCOVERY);
    }

    public void requestConnection(String endpointName, String endpointId) {
        Log.d(TAG,"requestConnection");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ENDPOINT_NAME,endpointName);
            jsonObject.put(Constants.ENDPOINT_ID,endpointId);
            mSocket.emit(Constants.REQUEST_CONNECTION,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void acceptConnection(String endpointId) {
        Log.d(TAG,"acceptConnection");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ENDPOINT_ID,endpointId);
            mSocket.emit(Constants.ACCEPT_CONNECTION,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendPayload(String endpointId, Payload payload) {
        JSONObject jsonObject = new JSONObject();
        try {
            String str = new String(payload.asBytes(), UTF_8);
            jsonObject.put(Constants.ENDPOINT_ID,endpointId);
            jsonObject.put(Constants.PAYLOAD,str);
            mSocket.emit(Constants.SEND_PAYLOAD,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void disconnectAll() {
        mSocket.emit(Constants.DISCONNECT);
        //mSocket.disconnect();
    }

    public void stopAdvertising() {
        Log.d(TAG,"stopAdvertising");
        mSocket.emit(Constants.STOP_ADVERTISING);
    }
}
