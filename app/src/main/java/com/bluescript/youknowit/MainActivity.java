package com.bluescript.youknowit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.view.View;
import android.view.Window;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    //Helper function for writeToJSON
    private void writeToFile(String text, String fileName){
        Context context = getApplicationContext();
        try {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), fileName));
            out.write(text);
            out.close();
        } catch(IOException e ){
            Log.e("ERROR", e.toString());
        }
    }

    //Function to read from JSON file
    private QuestionSet readFromJSON(String fileName) {
        String JSONtext = readFile(fileName);
        QuestionSet questionSet = new QuestionSet(JSONtext);
        return questionSet;
    }

    protected void writeToJSON(String fileName, QuestionSet data) {
        JSONObject JSONData = data.toJSON();
        writeToFile(JSONData.toString(), fileName);
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
            Log.e("ERROR", e.toString());
        }
        String output = stringBuilder.toString();
        return output;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.addingActivities);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), CreateAndEditSetActivity.class));
            }
        });
    }

}