package com.hataketsu.testmyself;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hataketsu.testmyself.question.QuestionAdapter;
import com.hataketsu.testmyself.question.QuestionEntry;

import java.util.HashMap;
import java.util.Map;

public class TestScrollingActivity extends AppCompatActivity {
    private int numberQuestion;
    private TextView testTextView;
    private ListView listView;
    private QuestionAdapter adapter;
    private String tenchuong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scrolling);

        Intent intent = getIntent();
        numberQuestion = intent.getIntExtra("number", -1);
        String sttchuong = intent.getStringExtra("sttchuong");
        tenchuong = intent.getStringExtra("tenchuong");
        testTextView = ((TextView) findViewById(R.id.test_message));
        testTextView.setText(String.format("You got %d question(s) at %s", numberQuestion, tenchuong));
        listView = ((ListView) findViewById(R.id.test_lv));
        adapter = new QuestionAdapter(this, -1);
        listView.setAdapter(adapter);
        String query = String.format("select cauhoiid,noidung,loaicauhoiid from cauhoi where chuongid=%s order by random() limit %d", sttchuong, numberQuestion);
        new QuestionAsyncTask().execute(query);
    }


    public void checkAnswers(View v) {
        int wrongAnswerNumber = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            QuestionEntry entry = adapter.getItem(i);
            boolean isRight = entry.checkAndColor();
            if (!isRight) {
                wrongAnswerNumber++;
            }
        }
        testTextView.setText(String.format("%d question(s) at %s with %d wrong on total %d", numberQuestion, tenchuong, wrongAnswerNumber, adapter.getCount()));
    }


    private class QuestionAsyncTask extends AsyncTask<String, QuestionEntry, String> {
        @Override
        protected String doInBackground(String... strings) {
            int count = 1;
            Cursor questionCursor = DatabaseManager.getDatabase().rawQuery(strings[0], null);
            while (questionCursor.moveToNext()) {
                QuestionEntry entry = new QuestionEntry();
                String question = count++ + " : [" + questionCursor.getString(2) + "] : " + questionCursor.getString(1);
                entry.setQuestion(question);
                Cursor answerCursor = DatabaseManager.getDatabase().rawQuery("select noidung,ladapan from dapan where cauhoiid==" + questionCursor.getString(0) + " order by random()", null);
                while (answerCursor.moveToNext()) {
                    String text = answerCursor.getString(0);
                    boolean result = true;
                    if (answerCursor.getString(1).equals("0")) {
                        result = false;
                    }
                    entry.addAnswer(text, result);
                }
                publishProgress(entry);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(QuestionEntry... values) {
            for (QuestionEntry i : values) {
                adapter.add(i);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            listView.addFooterView(getLayoutInflater().inflate(R.layout.check_button, null));
        }
    }
}
