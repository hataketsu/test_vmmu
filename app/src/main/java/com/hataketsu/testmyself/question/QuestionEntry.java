package com.hataketsu.testmyself.question;

import android.database.Cursor;
import android.util.Log;
import android.view.View;

import com.hataketsu.testmyself.DatabaseManager;

import java.util.ArrayList;

/**
 * Created by hataketsu on 5/20/16.
 */
public class QuestionEntry {
    private ArrayList<AnswerEntry> answerEntries = new ArrayList<>();
    private String question;
    private View view;
    private int id;
    private boolean isChecked = false;
    private int passedTime;

    public static QuestionEntry fromCursor(Cursor questionCursor, int number) {
        QuestionEntry entry = new QuestionEntry();
        String passedTime = questionCursor.getString(2);
        String question = number + " : [" + passedTime + "] : " + questionCursor.getString(1);
        entry.setQuestion(question);
        entry.setId(Integer.parseInt(questionCursor.getString(0)));
        entry.setPassedTime(Integer.parseInt(passedTime));

        Cursor answerCursor = DatabaseManager.query("select noidung,ladapan from dapan where cauhoiid==" + questionCursor.getString(0) + " order by random()");
        while (answerCursor.moveToNext()) {
            String text = answerCursor.getString(0);
            boolean result = true;
            if (answerCursor.getString(1).equals("0")) {
                result = false;
            }
            entry.addAnswer(text, result);
        }
        return entry;
    }

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
        if (!isChecked) {
            savePassedTime(result);
            isChecked = true;
        }
        ;
        return result;
    }

    private void savePassedTime(boolean result) {
        if (result) passedTime++;
        else passedTime--;
        String sql = String.format("update cauhoi set passedTime=%d where cauhoiid=%d", passedTime, id);
        DatabaseManager.getDatabase().execSQL(sql);
        Log.d("TEST", "set passedTime = " + passedTime + " in id= " + id + "content= " + question);
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

    public void setId(int id) {
        this.id = id;
    }

    public void setPassedTime(int passedTime) {
        this.passedTime = passedTime;
    }
}
