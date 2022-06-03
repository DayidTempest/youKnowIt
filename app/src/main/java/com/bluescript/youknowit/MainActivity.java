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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.bluescript.youknowit.Question;
import android.view.View;
import android.view.Window;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    public static Intent serviceIntent = null;
    NotificationsService notificationsService;

    //Helper function for writeToJSON
    private void writeToFile(String text, String fileName){
        Context context = getApplicationContext();
        try {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), fileName));
            out.write(text);
            out.close();
        } catch(IOException e ){
            System.out.println(e);
        }

    }

    //Function to read from JSON file
    private Question readFromJSON(String fileName) {
        String JSONtext = readFile(fileName);
        try{
            Question question = new Question(JSONtext);
            return question;
        } catch (JSONException e){
            System.out.println(e);
        }

        return new Question("none", "none");
    }

    protected void writeToJSON(String fileName, Question data) {
        try{
            JSONObject JSONData = data.toJSON();
            writeToFile(JSONData.toString(), fileName);
        } catch (JSONException e){
            System.out.println(e);
        }
        Question lostQuestion = new Question("none", "none");
        writeToFile(lostQuestion.toString(), fileName);
    }

    //Helper function for readFromJson
    private String readFile(String filename){
        Context context = getApplicationContext();
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(new File(context.getFilesDir(), filename)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);
        } catch (IOException e ){
            System.out.println(e);
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("FOREGROUND_SERVICE_CHANNEL", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}