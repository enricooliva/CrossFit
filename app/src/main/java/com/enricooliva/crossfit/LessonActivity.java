package com.enricooliva.crossfit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LessonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.listLesson, new LessonFragment())
                    .commit();
        }
    }
}
