package com.bluescript.youknowit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class CreateAndEditSetActivity extends AppCompatActivity {
    private ViewGroup parent;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_and_edit_set);
        parent = findViewById(R.id.AddRepeatsLinear);

        Intent intent = getIntent();
        String editUuid = intent.getStringExtra("uuid");

        this.context = getApplicationContext();
        TextInputLayout name = findViewById(R.id.projectname);
        MaterialToolbar materialToolbar = findViewById(R.id.topAppBar);
        materialToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemID = item.getItemId();
                if(itemID == R.id.action_save_set){

                    LinearLayout scroll = findViewById(R.id.AddRepeatsLinear);
                    int childCount = scroll.getChildCount();

                    ArrayList list = new ArrayList<Question>();

                    for(int i = 0; i < childCount; i++){
                        TextInputLayout questionPlace = scroll.getChildAt(i).findViewById(R.id.question_place);
                        String question = questionPlace.getEditText().getText().toString();
                        TextInputLayout answerPlace = scroll.getChildAt(i).findViewById(R.id.answer_place);
                        String answer = answerPlace.getEditText().getText().toString();
                        if(question == null || answer == null){

                        }else{
                            Question q1 = new Question(question, answer);
                            list.add(q1);
                        }


                    }
                    UUID uuid;
                    if(editUuid.equals("")){
                         uuid = UUID.randomUUID();
                    }else{
                         uuid = UUID.fromString(editUuid);
                    }

                    QuestionSet set =  new QuestionSet(uuid, name.getEditText().getText().toString(), list);

                    File folder = new File(context.getFilesDir().getAbsolutePath() + "/questionSets");
//                    if(!folder.exists()){
//                        folder.mkdir();
//                    }
                    Log.e("tag", folder.getAbsolutePath().toString());
                    MainActivity.writeToJSON("questionSets/" + uuid.toString() + ".json", set, getApplicationContext());
                    set = MainActivity.readFromJSON("questionSets/" + uuid.toString() + ".json", getApplicationContext());
                    Log.e("tag", set.getId().toString());

                    Switch notifiSwitch = findViewById(R.id.notificationSetSwitch);
                    EditText intervalText = findViewById(R.id.notificationSetInterval);
                    int interval = Integer.parseInt(intervalText.getText().toString());
                    if(notifiSwitch.isEnabled()) {
                        ManageNotifications.startNotificationTask(getApplicationContext(), "questionSets/" + uuid.toString() + ".json", interval);
                    }
                }
                return false;
            }
        });
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