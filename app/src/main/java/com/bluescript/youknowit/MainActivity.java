package com.bluescript.youknowit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import org.chromium.net.CronetEngine;
import org.chromium.net.UrlRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.bluescript.youknowit.Question;
import android.view.View;
import android.view.Window;

import com.bluescript.youknowit.api.MyUrlRequestCallback;
import com.bluescript.youknowit.utils.PathInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    public Context context;

    public static Intent serviceIntent = null;
    NotificationsService notificationsService;

    //Helper function for writeToJSON
    static public void writeToFile(String text, String fileName, Context context){

        try {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), fileName));
            out.write(text);
            out.close();
        } catch(IOException e ){
            Log.e("ERROR", e.toString());
        }

    }

    //Function to read from JSON file
    static public QuestionSet readFromJSON(String fileName, Context context) {
        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

        String JSONtext = readFile(fileName, context);
        QuestionSet questionSet = new QuestionSet(JSONtext);
        return questionSet;
    }

    static public void writeToJSON(String fileName, QuestionSet data, Context context) {
        JSONObject JSONData = data.toJSON();
        MainActivity.writeToFile(JSONData.toString(), fileName, context);
    }

    //Helper function for readFromJson
    static public String readFile(String filename, Context context){

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(new File(context.getFilesDir() + PathInfo.PATH_TO_SETS, filename)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);
        } catch (IOException e ){
            Log.e("ERROR", e.toString());
        }
        String output = stringBuilder.toString();
        return output;
    }

    private void getSetsFromServer(Executor executor, CronetEngine cronetEngine){
        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder("http://192.168.0.100:3000/healthCheck", new MyUrlRequestCallback(), executor);
        UrlRequest request = requestBuilder.build();
        request.start();
    }

    private void postSetToServer(String uuid){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getApplicationContext();

        CronetEngine.Builder myBuilder = new CronetEngine.Builder(context);
        CronetEngine cronetEngine = myBuilder.build();

        Executor executor = Executors.newSingleThreadExecutor();

        getSetsFromServer(executor, cronetEngine);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        FloatingActionButton fab = findViewById(R.id.addingActivities);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), CreateAndEditSetActivity.class));
            }
        });

        createAndBindService();
    }


    protected void onResume(){
        super.onResume();

        File folder = new File(context.getFilesDir().getAbsolutePath() + "/questionSets");
        if(!folder.exists()){
            folder.mkdir();
        }
        String path = context.getFilesDir().toString();
        File dic = new File(path + PathInfo.PATH_TO_SETS);
        File[] listOfFiles = dic.listFiles();
        final LayoutInflater inflater = LayoutInflater.from(this);
        ViewGroup parent = findViewById(R.id.scroll_in_main);


        if(listOfFiles.length > 0) {
            for (int i = 0; i < listOfFiles.length; i++) {
                QuestionSet questionSet = MainActivity.readFromJSON(listOfFiles[i].toString(), context);


                View singleTileSet = inflater.inflate(R.layout.tile_set, parent, false);
                TextView setName = singleTileSet.findViewById(R.id.projectname);
                setName.setText(questionSet.getSetName());
                parent.addView(singleTileSet);
            }
        }

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            NotificationsService.NotificationsServiceBinder binder = (NotificationsService.NotificationsServiceBinder) service;
            notificationsService = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    private void createAndBindService() {
        serviceIntent = new Intent(this, NotificationsService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.foreground_channel_name);
            String description = getString(R.string.foreground_channel_description);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("FOREGROUND_SERVICE_CHANNEL", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            CharSequence name2 = getString(R.string.foreground_channel_name);
            String description2 = getString(R.string.foreground_channel_description);
            int importance2 = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel2 = new NotificationChannel("QUESTION_NOTIFICATION_CHANNEL", name2, importance2);
            channel2.setDescription(description2);

            NotificationManager notificationManager2 = getSystemService(NotificationManager.class);
            notificationManager2.createNotificationChannel(channel2);

            CharSequence name3 = getString(R.string.foreground_channel_name);
            String description3 = getString(R.string.foreground_channel_description);
            int importance3 = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel channel3 = new NotificationChannel("ANSWER_NOTIFICATION_CHANNEL", name3, importance3);
            channel3.setDescription(description3);

            NotificationManager notificationManager3 = getSystemService(NotificationManager.class);
            notificationManager3.createNotificationChannel(channel3);
        }
    }


}