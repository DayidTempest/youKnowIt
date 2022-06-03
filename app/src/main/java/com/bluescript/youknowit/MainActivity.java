package com.bluescript.youknowit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.bluescript.youknowit.Question;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);
        String fileName = "TEST.txt";
        Question test = new Question("How are you?", "I am fine");
        writeToJSON(fileName, test);
        TextView text = findViewById(R.id.TESTOWEID);
        text.setText(test.getQuestion());
    }
}