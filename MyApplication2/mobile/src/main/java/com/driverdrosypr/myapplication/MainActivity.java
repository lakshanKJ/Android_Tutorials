package com.driverdrosypr.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.NodeClient;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient googleApiClient = null;
    public static final String TAG = null;
    public static final String WEARABLE_DATA_PATH ="/wearable/data/path";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
         builder.addApi(Wearable.API);
         builder.addConnectionCallbacks(this);
         builder.addOnConnectionFailedListener(this);
         googleApiClient = builder.build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (googleApiClient != null && googleApiClient.isConnected())
            googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    public void sendMessage(){
        if (googleApiClient.isConnected()){
            String message = ((TextView)findViewById(R.id.text)).getText().toString();
            if (message == null || message.equalsIgnoreCase("")){
                message = "Hello world !!!";
            }
            new SendMessageToDataLayer(WEARABLE_DATA_PATH,message).start();
        }else {

        }
    }

    public void sendMessageOnClick(View view) {
        sendMessage();
    }

    public class SendMessageToDataLayer extends Thread{
        String path;
        String message;

        public SendMessageToDataLayer(String path, String message) {
            this.path = path;
            this.message = message;
        }

        @Override
        public void run() {
            NodeApi.GetConnectedNodesResult nodeList = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
//            NodeClient nodeClient = Wearable.getNodeClient().getConnectedNodes(googleApiClient).await();
            for (Node node : nodeList.getNodes()){
                MessageApi.SendMessageResult messageResult =  Wearable.MessageApi.sendMessage(googleApiClient,node.getId(),path,message.getBytes()).await();
                if (messageResult.getStatus().isSuccess()){
                    //Print success log
                    Log.v(TAG,"Message: successfully sent to " + node.getDisplayName());
                    Log.v(TAG,"Message: Node Id is " + node.getId());
                    Log.v(TAG,"Message: Node Size is " + nodeList.getNodes());

                }else {
                    //Print failure log
                    Log.v(TAG,"Error while sending message");
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}