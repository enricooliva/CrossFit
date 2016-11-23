package com.enricooliva.crossfit;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.enricooliva.crossfit.data.DataContract;
import com.enricooliva.crossfit.data.DataDbHelper;

import org.junit.Test;

/**
 * Created by Enrico on 06/04/2015.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    @Test
    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(DataDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new DataDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    @Test
    public void testInsertReadDb() {

        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        DataDbHelper dbHelper = new DataDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DataContract.AthleteEntry.COLUMN_firstName, "Enrico");
        values.put(DataContract.AthleteEntry.COLUMN_lastName, "oliva");
        values.put(DataContract.AthleteEntry.COLUMN_email, "oliva.enrico@gmail.com");
        values.put(DataContract.AthleteEntry.COLUMN_sex, "M");

        long athleteRowId;
        athleteRowId = db.insert(DataContract.AthleteEntry.TABLE_NAME, null, values);

        // Verify we got a row back.
        assertTrue(athleteRowId != -1);
        Log.d(LOG_TAG, "New row id: " + athleteRowId);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Specify which columns you want.
        String[] columns = {
                DataContract.AthleteEntry._ID,
                DataContract.AthleteEntry.COLUMN_firstName,
                DataContract.AthleteEntry.COLUMN_lastName,
                DataContract.AthleteEntry.COLUMN_email,
                DataContract.AthleteEntry.COLUMN_sex
        };

        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                DataContract.AthleteEntry.TABLE_NAME,  // Table to Query
                columns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // If possible, move to the first row of the query results.
        if (cursor.moveToFirst()) {
            // Get the value in each column by finding the appropriate column index.

            int nameIndex = cursor.getColumnIndex(DataContract.AthleteEntry.COLUMN_firstName);
            String name = cursor.getString(nameIndex);

            int lastnameIndex = cursor.getColumnIndex((DataContract.AthleteEntry.COLUMN_lastName));
            String lastname = cursor.getString(lastnameIndex);

            int sexIndex = cursor.getColumnIndex((DataContract.AthleteEntry.COLUMN_sex));
            String sex = cursor.getString(sexIndex);

            int emailIndex = cursor.getColumnIndex((DataContract.AthleteEntry.COLUMN_email));
            String email = cursor.getString(emailIndex);

            // Hooray, data was returned!  Assert that it's the right data, and that the database
            // creation code is working as intended.
            // Then take a break.  We both know that wasn't easy.
            assertEquals("Enrico", name);
            assertEquals("Oliva", lastname);
            assertEquals("M", sex);
            assertEquals("oliva.enrico@gmail.com", email);

            // Fantastic.  Now that we have a location, add some weather!
        } else {
            // That's weird, it works on MY machine...
            fail("No values returned :(");
        }

        dbHelper.close();
    }

}
