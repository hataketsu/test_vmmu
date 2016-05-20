package com.hataketsu.testmyself.question;

import android.os.Build;
import android.widget.CheckBox;

import com.hataketsu.testmyself.R;
import com.hataketsu.testmyself.WelcomeActivity;

/**
 * Created by hataketsu on 5/20/16.
 */
public class AnswerEntry {
    private String text;
    private boolean isTrue;
    private CheckBox checkBox;

    public AnswerEntry(String text, boolean isTrue) {
        this.text = text;
        this.isTrue = isTrue;
    }

    public String getText() {
        return text;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public boolean getTrue() {
        return (checkBox.isChecked() == isTrue);
    }

    public void color() {
        checkBox.setBackgroundColor(WelcomeActivity.getContext().getResources().getColor(android.R.color.white));
        if (isTrue)
            checkBox.setBackgroundColor(WelcomeActivity.getContext().getResources().getColor(R.color.colorRight));
        else if (checkBox.isChecked() != isTrue) {
            checkBox.setBackgroundColor(WelcomeActivity.getContext().getResources().getColor(R.color.colorWrong));
        }
    }
}
