package com.enricooliva.crossfit;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.enricooliva.crossfit.data.Athlete;

/**
 * Created by Enrico on 06/04/2015.
 */
public class AthleteAdapter extends CursorAdapter {

    private Context currentContext;
    private Athlete item;

    public AthleteAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.currentContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.athlete, parent, false);
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {

        item = new Athlete(cursor);

        TextView textName = (TextView) convertView.findViewById(R.id.name);
        textName.setText(item.getFirstName());

        TextView textRole = (TextView) convertView.findViewById(R.id.role);
        textRole.setText(item.getLastName());

        TextView textNumber = (TextView) convertView.findViewById(R.id.number);
        //viewHolder.image = (ImageView) convertView.findViewById(R.id.icon);

        TextView ratings = (TextView) convertView.findViewById(R.id.rate);
        RatingBar ratingbar = (RatingBar) convertView.findViewById(R.id.ratingBar1);

        textName.setText(item.getFirstName());
        textRole.setText(item.getLastName());
        textNumber.setText("");

        ratingbar.setRating((float) 2);
    }

}