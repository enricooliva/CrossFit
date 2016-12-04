package com.enricooliva.crossfit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.enricooliva.crossfit.data.Lesson;

import static com.enricooliva.crossfit.data.DataContract.getFriendlyDayString;

public class LessonRegister extends AppCompatActivity {

    private TextView textDescription;
    private TextView textDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_register);

        // Fetching data from a parcelable object passed from MainActivity
        Lesson lesson = getIntent().getParcelableExtra("lesson");

        textDate = (TextView) findViewById(R.id.box_date);
        textDescription = (TextView) findViewById(R.id.box_description);

        if (lesson!=null){
            textDate.setText(String.format("Lezione "+ getFriendlyDayString(getBaseContext(), lesson.getDate())));
            textDescription.setText(lesson.getType());
        }

        Button updateButton = (Button) findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            }
        });

        Button cancelButton = (Button) findViewById(R.id.rank_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });


    }


}
