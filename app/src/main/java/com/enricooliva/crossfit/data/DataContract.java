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

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the weather database.
 */
public class DataContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.

    public static final String CONTENT_AUTHORITY = "com.enricooliva.crossfit.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ATHLETE = "athlete";
    public static final String PATH_LESSON = "classes";


    public static final class AthleteEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "athlete";
        public static final String COLUMN_id = "id";
        public static final String COLUMN_firstName = "firstName";
        public static final String COLUMN_lastName = "lastName";
        public static final String COLUMN_dateOfBirth = "dateOfBirth";
        public static final String COLUMN_sex = "sex";
        public static final String COLUMN_email = "email";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ATHLETE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_ATHLETE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_ATHLETE;


        public static Uri buildPlayerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildPlayersUri() {
            return CONTENT_URI.buildUpon().build();
        }

    }

    public static final class LessonEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LESSON).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_LESSON;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_LESSON;

        public static final String ID = "id";
        public static final String BOXID = "boxId";
        public static final String DATE = "date";
        public static final String DURATION = "duration";
        public static final String TYPE = "type";
        public static final String MAXATTENDANCE = "maxAttendance";

        public static final String TABLE_NAME = "Lesson";


        public static Uri buildLessonUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildLessonUri() {
            return CONTENT_URI.buildUpon().build();
        }

    }


}
