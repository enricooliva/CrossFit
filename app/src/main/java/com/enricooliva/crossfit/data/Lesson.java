package com.enricooliva.crossfit.data;

import android.database.Cursor;

import com.enricooliva.crossfit.data.DataContract.LessonEntry;

import org.json.JSONException;
import org.json.JSONObject;


public class Lesson {

    /*{
        "id": "581fb1f7fbe45a3145e1e331",
            "boxId": "58340522c9e77c000130e2d4",
            "date": "2016-12-27 13:30",
            "duration": 1.0,
            "type": "metcon",
            "maxAttendance": 14
    }*/

    // POST /classes (get all classes for the specified box)

    private String id;
    private String boxId;
    private String date;
    private String duration;
    private String type;
    private String maxAttendance;

    public Lesson(Cursor cursor) {

        id = cursor.getString(cursor.getColumnIndex(LessonEntry.ID));
        boxId = cursor.getString(cursor.getColumnIndex(LessonEntry.BOXID));
        date = cursor.getString(cursor.getColumnIndex(LessonEntry.DATE));
        duration = cursor.getString(cursor.getColumnIndex(LessonEntry.DURATION));
        type = cursor.getString(cursor.getColumnIndex(LessonEntry.TYPE));
        maxAttendance = cursor.getString(cursor.getColumnIndex(LessonEntry.MAXATTENDANCE));

    }

    //{"id":"5835a2a2c9e77c000130e2d6","firstName":"Mario1","lastName":"Rossi1","dateOfBirth":325425600,"sex":"M","email":"mario.rossi1@demo.com"}

    public Lesson(JSONObject json_data) {

        try {

            id = json_data.isNull(LessonEntry.ID) ? null : json_data.getString(LessonEntry.ID);
            boxId = json_data.isNull(LessonEntry.BOXID) ? null : json_data.getString(LessonEntry.BOXID);
            date = json_data.isNull(LessonEntry.DATE) ? null : json_data.getString(LessonEntry.DATE);
            duration = json_data.isNull(LessonEntry.DURATION) ? null : json_data.getString(LessonEntry.DURATION);
            type = json_data.isNull(LessonEntry.TYPE) ? null : json_data.getString(LessonEntry.TYPE);
            maxAttendance = json_data.isNull(LessonEntry.MAXATTENDANCE) ? null : json_data.getString(LessonEntry.MAXATTENDANCE);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

        public String getId() {
            return id;
        }

        public String getBoxId() {
            return boxId;
        }

        public String getDate() {
            return date;
        }

        public String getDuration() {
            return duration;
        }

        public String getType() {
            return type;
        }

        public String getMaxAttendance() {
            return maxAttendance;
        }

        @Override
        public String toString() {
            return "Athlete{" +
                    "id='" + id + '\'' +
                    ", boxId='" + boxId + '\'' +
                    ", date='" + date + '\'' +
                    ", duration=" + duration +
                    ", type='" + type + '\'' +
                    ", maxAttendance='" + maxAttendance + '\'' +
                    '}';
        }
    }
