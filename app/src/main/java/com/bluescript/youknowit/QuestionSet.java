package com.bluescript.youknowit;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class QuestionSet {
    private String setName;
    private UUID id;
    private ArrayList<Question> questions;


    public QuestionSet(UUID id, String setName, ArrayList<Question> questions){
        this.id = id;
        this.questions = questions;
        this.setName = setName;
    }

    public QuestionSet(String setName, ArrayList<Question> questions) {
        this.id = UUID.randomUUID();
        this.questions = questions;
        this.setName = setName;
    }

    public QuestionSet(String JSONString){
        try{
            JSONObject json = new JSONObject(JSONString);
            this.setName = json.getString("setName");
            this.id = UUID.fromString(json.getString("id"));
            JSONArray questions = json.getJSONArray("questions");
            ArrayList<Question> newQuestions = new ArrayList<Question>();
            for (int i = 0; i < questions.length(); i++) {
                JSONObject singleQuestion = (JSONObject) questions.get(i);
                String forCreation = singleQuestion.toString();
                newQuestions.add(new Question(forCreation));
            }
            this.questions = newQuestions;

        } catch (JSONException e){
            e.printStackTrace();
            this.setName = "none";
            this.id = null;
            this.questions = new ArrayList<Question>(0);

        }

    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public UUID getId() {
        return id;
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        try {
            json.put("id", this.id);
            json.put("setName", this.setName);
            JSONArray questions = new JSONArray();
            for (Question question: this.questions) {
                questions.put(question.toJSON());
            }
            json.put("questions",questions);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}
