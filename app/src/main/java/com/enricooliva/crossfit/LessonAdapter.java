package com.enricooliva.crossfit;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enricooliva.crossfit.data.Lesson;

/**
 * Created by Enrico on 06/04/2015.
 */
public class LessonAdapter extends CursorAdapter {

    private Context currentContext;
    private Lesson item;

    public LessonAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.currentContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.lesson, parent, false);
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {

        item = new Lesson(cursor);

        TextView textName = (TextView) convertView.findViewById(R.id.data);
        textName.setText(item.getDate());

        TextView textRole = (TextView) convertView.findViewById(R.id.type);
        textRole.setText(new StringBuilder()
                .append(String.valueOf(item.getMaxAttendance()))
                .append(" ").append(item.getType())
                .append(" ").append(item.getDuration()).toString());

        //TextView textNumber = (TextView) convertView.findViewById(R.id.number);
        //viewHolder.image = (ImageView) convertView.findViewById(R.id.icon);

    }

}