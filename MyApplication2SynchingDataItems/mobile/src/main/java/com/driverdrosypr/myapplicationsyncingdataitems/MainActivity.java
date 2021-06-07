package com.driverdrosypr.myapplicationsyncingdataitems;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
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
        //send message();
        sendDataMapToDataLayer();
    }

    private DataMap createDataMap(){
        DataMap dataMap = new DataMap();
        dataMap.putLong("time",System.currentTimeMillis());
        dataMap.putString("one","Soup");
        dataMap.putString("two","Pasta");
        dataMap.putString("three","Salad");
        return dataMap;
    }

    public void sendDataMapToDataLayer(){
        if (googleApiClient.isConnected()){
            DataMap dataMap = createDataMap();
            new SendDataMapToDataLayer(WEARABLE_DATA_PATH,dataMap).start();
        }else {
            Log.v(TAG,"Connection is closed");
        }
    }

//    public void sendMessage(){
//        if (googleApiClient.isConnected()){
//            String message = ((TextView)findViewById(R.id.text)).getText().toString();
//            if (message == null || message.equalsIgnoreCase("")){
//                message = "Hello world !!!";
//            }
//            new SendMessageToDataLayer(WEARABLE_DATA_PATH,message).start();
//        }else {
//
//        }
//    }
//
//    public void sendMessageOnClick(View view) {
//        sendMessage();
//    }

    public void sendDataMapOnClick(){
        sendDataMapToDataLayer();
    }

    public class SendDataMapToDataLayer extends Thread{
        String path;
        DataMap dataMap;

        public SendDataMapToDataLayer(String path, DataMap dataMap) {
            this.path = path;
            this.dataMap = dataMap;
        }

        @Override
        public void run() {
//            NodeApi.GetConnectedNodesResult nodeList = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();

//            for (Node node : nodeList.getNodes()){
            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(WEARABLE_DATA_PATH);
            putDataMapRequest.getDataMap().putAll(dataMap);

            PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
            DataApi.DataItemResult dataItemResult = Wearable.DataApi.putDataItem(googleApiClient, putDataRequest).await();
//                MessageApi.SendMessageResult messageResult =  Wearable.MessageApi.sendMessage(googleApiClient,node.getId(),path,message.getBytes()).await();
                if (dataItemResult.getStatus().isSuccess()){
//                    Print success log
                    Log.v(TAG,"######### Data Item: successfully sent ##########");
//                    Log.v(TAG,"Message: successfully sent to " + node.getDisplayName());
//                    Log.v(TAG,"Message: Node Id is " + node.getId());
//                    Log.v(TAG,"Message: Node Size is " + nodeList.getNodes());

                }else {
                    //Print failure log
                    Log.v(TAG,"Error while sending message");
                }
//            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}