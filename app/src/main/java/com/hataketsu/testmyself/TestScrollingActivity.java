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
        numberQuestion = intent.getIntExtra("number", -1) + 1;
        String sttchuong = intent.getStringExtra("sttchuong");
        tenchuong = intent.getStringExtra("tenchuong");
        testTextView = ((TextView) findViewById(R.id.test_message));
        testTextView.setText(String.format("You got %d question(s) at %s", numberQuestion, tenchuong));
        listView = ((ListView) findViewById(R.id.test_lv));
        adapter = new QuestionAdapter(this, -1);
        listView.setAdapter(adapter);
        new QuestionAsyncTask().execute(sttchuong);
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
            String id = strings[0];
            Cursor minCursor = DatabaseManager.query("select min(passedTime) from cauhoi where chuongid=" + id);
            minCursor.moveToNext();
            int min = Integer.parseInt(minCursor.getString(0));
            while (count < numberQuestion) {
                String query = String.format("select cauhoiid,noidung,passedTime from cauhoi where chuongid=%s and passedTime=%d order by random() limit %d", id, min, numberQuestion - count);
                Cursor questionCursor = DatabaseManager.query(query);
                while (questionCursor.moveToNext()) {
                    QuestionEntry entry = QuestionEntry.fromCursor(questionCursor, count++);
                    publishProgress(entry);
                }
                query = String.format("select min(passedTime) from cauhoi where chuongid=%s and passedTime>%d", id, min);
                minCursor = DatabaseManager.query(query + id);
                minCursor.moveToNext();
                String minString = minCursor.getString(0);
                if (minString != null)
                    min = Integer.parseInt(minString);

                else return null;
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
