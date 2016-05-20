package com.hataketsu.testmyself.question;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by hataketsu on 5/20/16.
 */
public class QuestionEntry {
    private ArrayList<AnswerEntry> answerEntries = new ArrayList<>();
    private String question;
    private View view;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void addAnswer(String text, boolean result) {
        answerEntries.add(new AnswerEntry(text, result));
    }

    public boolean checkAndColor() {
        boolean result = true;
        for (int i = 0; i < answerEntries.size(); i++) {
            AnswerEntry answerEntry = answerEntries.get(i);
            if (!answerEntry.getTrue()) result = false;
            answerEntry.color();
        }
        return result;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public ArrayList<AnswerEntry> getAnswers() {
        return answerEntries;
    }


}
