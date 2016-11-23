/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.enricooliva.crossfit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "footbal.db";

    public DataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_PLAYER_TABLE = "CREATE TABLE " + DataContract.AthleteEntry.TABLE_NAME + " (" +
                DataContract.AthleteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DataContract.AthleteEntry.COLUMN_id  + " TEXT NOT NULL, " +
                DataContract.AthleteEntry.COLUMN_firstName  + " TEXT NOT NULL, " +
                DataContract.AthleteEntry.COLUMN_lastName  + " TEXT NOT NULL, " +
                DataContract.AthleteEntry.COLUMN_dateOfBirth + " TEXT NOT NULL, " +
                DataContract.AthleteEntry.COLUMN_sex  + " TEXT NOT NULL, " +
                DataContract.AthleteEntry.COLUMN_email  + " TEXT NOT NULL );";


        sqLiteDatabase.execSQL(SQL_CREATE_PLAYER_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataContract.AthleteEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);

    }
}
