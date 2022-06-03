package com.bluescript.youknowit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateAndEditSetActivity extends AppCompatActivity {
    private ViewGroup parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_and_edit_set);
        parent = findViewById(R.id.AddRepeatsLinear);

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

    public void deleteButton(View v){
        if(parent.getChildCount() > 1){
            View view = (View)v.getParent();
            int indexOfChildOfParent = parent.indexOfChild(view);
            parent.removeViewAt(indexOfChildOfParent);
        }
    }
}