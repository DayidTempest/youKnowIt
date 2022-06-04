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
import android.widget.Button;
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


    int testCount = 1;
    int testCountDelete = 1;

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
    }

    private void createNotificationChannel() {
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