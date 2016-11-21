package com.enricooliva.crossfit.data;

import android.database.Cursor;


import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;

public class Athlete {

    private String id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String sex;
    private String email;

    public Athlete(Cursor cursor) {

        id = cursor.getString(cursor.getColumnIndex(DataContract.AthleteEntry.COLUMN_id));
        firstName = cursor.getString(cursor.getColumnIndex(DataContract.AthleteEntry.COLUMN_firstName));
        lastName = cursor.getString(cursor.getColumnIndex(DataContract.AthleteEntry.COLUMN_lastName));
        //dateOfBirth = cursor.getDate(cursor.getColumnIndex(DataContract.AthleteEntry.COLUMN_dateOfBirth));
        sex = cursor.getString(cursor.getColumnIndex(DataContract.AthleteEntry.COLUMN_sex));

    }


    public Athlete(JSONObject json_data) {

        try {
            id = json_data.getString("Id");
            firstName = json_data.getString("FirstName");
            lastName = json_data.getString("LastName");
            dateOfBirth=  Date.valueOf(json_data.getString("DateOfBirth"));
            sex = json_data.getString("Sex");
            email = json_data.getString("Email");

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

        public Date getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(Date dateOfBirth) {
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
