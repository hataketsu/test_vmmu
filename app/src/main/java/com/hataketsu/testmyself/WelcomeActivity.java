package com.hataketsu.testmyself;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class WelcomeActivity extends AppCompatActivity {
    ArrayList<String> chapterIDs, chapterNames;
    private Spinner spinner;
    private EditText numberED;
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        chapterIDs = new ArrayList<>();
        chapterNames = new ArrayList<>();
        Cursor chapterCursor = DatabaseManager.query("select chuongid,tenchuong,monhocid from chuong order by monhocid,tenchuong");
        while (chapterCursor.moveToNext()) {
            chapterIDs.add(chapterCursor.getString(0));
            Cursor tmp = DatabaseManager.query("select tenmonhoc from tenmonhoc where monhocid==" + chapterCursor.getString(2));
            String subjectName;
            if (tmp.moveToFirst()) {
                subjectName = tmp.getString(0);
            } else subjectName = chapterCursor.getString(2);
            chapterNames.add(subjectName + " : " + chapterCursor.getString(1));
        }

        initViews();
    }

    private void initViews() {
        setContentView(R.layout.activity_welcome);
        spinner = (Spinner) findViewById(R.id.subjectSpinner);
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, chapterNames));
        numberED = (EditText) findViewById(R.id.number_ed);
    }


    public void onClick(View view) {
        if (view.getId() == R.id.start) {
            int no = (spinner).getSelectedItemPosition();
            int numberQuestion =Integer.valueOf(numberED.getText().toString());
            Intent intent = new Intent(this, TestScrollingActivity.class);
            intent.putExtra("number", numberQuestion);
            intent.putExtra("tenchuong", chapterNames.get(no));
            intent.putExtra("sttchuong", chapterIDs.get(no));
            startActivity(intent);
        }
    }

}
