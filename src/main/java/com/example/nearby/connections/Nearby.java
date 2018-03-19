package com.example.nearby.connections;

import android.app.Activity;

/**
 * Created by Lakhdar on 2/23/2018.
 */

public class Nearby {
    public static ConnectionsClient connectionsClient;
    public static ConnectionsClient getConnectionsClient(Activity activity){
        return new ConnectionsClient(activity);
    }
}
