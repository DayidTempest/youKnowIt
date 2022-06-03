package com.bluescript.youknowit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateAndEditSetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_and_edit_set);

        FloatingActionButton fab = findViewById(R.id.AddQuestionFAB);
        final LayoutInflater inflater = LayoutInflater.from(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup parent = findViewById(R.id.AddRepeatsLinear);
                inflater.inflate(R.layout.single_question_and_answer_tile, parent);
            }
        });
    }
}