package com.hataketsu.testmyself.question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hataketsu.testmyself.R;

/**
 * Created by hataketsu on 5/20/16.
 */
public class QuestionAdapter extends ArrayAdapter<QuestionEntry> {
    public QuestionAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionEntry questionEntry = getItem(position);
        View row = questionEntry.getView();
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.question_row, parent, false);
            TextView questionTV = (TextView) row.findViewById(R.id.question_tv);
            questionTV.setText(questionEntry.getQuestion());
            LinearLayout layout = (LinearLayout) row.findViewById(R.id.answer_ll);
            for (AnswerEntry answerEntry : questionEntry.getAnswers()) {
                CheckBox checkBox = (CheckBox) layoutInflater.inflate(R.layout.answer_row, null);
                checkBox.setText(answerEntry.getText());
                answerEntry.setCheckBox(checkBox);
                layout.addView(checkBox);
            }
            questionEntry.setView(row);
        }
        return row;
    }
}
