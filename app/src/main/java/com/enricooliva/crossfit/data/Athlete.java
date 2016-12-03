package com.enricooliva.crossfit.data;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;


public class Athlete {

    private String id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String sex;
    private String email;

    public Athlete(Cursor cursor) {

        id = cursor.getString(cursor.getColumnIndex(DataContract.AthleteEntry.COLUMN_id));
        firstName = cursor.getString(cursor.getColumnIndex(DataContract.AthleteEntry.COLUMN_firstName));
        lastName = cursor.getString(cursor.getColumnIndex(DataContract.AthleteEntry.COLUMN_lastName));
        dateOfBirth = cursor.getString(cursor.getColumnIndex(DataContract.AthleteEntry.COLUMN_dateOfBirth));
        sex = cursor.getString(cursor.getColumnIndex(DataContract.AthleteEntry.COLUMN_sex));
        email = cursor.getString(cursor.getColumnIndex(DataContract.AthleteEntry.COLUMN_email));


    }

    //{"id":"5835a2a2c9e77c000130e2d6","firstName":"Mario1","lastName":"Rossi1","dateOfBirth":325425600,"sex":"M","email":"mario.rossi1@demo.com"}

    public Athlete(JSONObject json_data) {

        try {

            id = json_data.isNull("id") ? null : json_data.getString("id");
            firstName = json_data.isNull("firstName") ? null : json_data.getString("firstName");
            lastName = json_data.isNull("lastName") ? null : json_data.getString("lastName");
            dateOfBirth = json_data.isNull("dateOfBirth") ? null : json_data.getString("dateOfBirth");
            sex = json_data.isNull("sex") ? null : json_data.getString("sex");
            email = json_data.isNull("email") ? null : json_data.getString("email");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


        public String getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String toString() {
            return "Athlete{" +
                    "id='" + id + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", dateOfBirth=" + dateOfBirth +
                    ", sex='" + sex + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }
