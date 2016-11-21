package com.enricooliva.crossfit.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class DataProvider extends ContentProvider {

     // The URI Matcher used by this content provider.
     private static final UriMatcher sUriMatcher = buildUriMatcher();
     private DataDbHelper mOpenHelper;

     private static final int ATHLETE = 100;
     private static final int ATHLETE_ID = 101;

     private static final int GAME = 200;
     private static final int GAME_ID = 201;

     private static UriMatcher buildUriMatcher() {

         final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
         final String authority = DataContract.CONTENT_AUTHORITY;

         // For each type of URI you want to add, create a corresponding code.
         matcher.addURI(authority, DataContract.PATH_ATHLETE, ATHLETE);
         matcher.addURI(authority, DataContract.PATH_ATHLETE + "/#", ATHLETE_ID);
         //matcher.addURI(authority, DataContract.PATH_PLAYER + "/*/*", WEATHER_WITH_LOCATION_AND_DATE);

         matcher.addURI(authority, DataContract.PATH_GAME, GAME);
         matcher.addURI(authority, DataContract.PATH_GAME + "/#", GAME_ID);

         return matcher;
     }



    @Override
     public boolean onCreate() {
         mOpenHelper = new DataDbHelper(getContext());
         return true;
     }

     @Override
     public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                         String sortOrder) {
         // Here's the switch statement that, given a URI, will determine what kind of request it is,
         // and query the database accordingly.
         Cursor retCursor;
         switch (sUriMatcher.match(uri)) {
             // "weather/*/*"
             case ATHLETE:
             {
                 retCursor = mOpenHelper.getReadableDatabase().query(
                         DataContract.AthleteEntry.TABLE_NAME,
                         projection,
                         selection,
                         selectionArgs,
                         null,
                         null,
                         sortOrder
                 );
                 break;
             }
             // "weather/*"
             case ATHLETE_ID: {
                 retCursor = mOpenHelper.getReadableDatabase().query(
                         DataContract.AthleteEntry.TABLE_NAME,
                         projection,
                         DataContract.AthleteEntry._ID + "=" + ContentUris.parseId(uri),
                         selectionArgs,
                         null,
                         null,
                         sortOrder
                 );

                 break;
             }



             default:
                 throw new UnsupportedOperationException("Unknown uri: " + uri);
         }
         retCursor.setNotificationUri(getContext().getContentResolver(), uri);
         return retCursor;
     }

     @Override
     public String getType(Uri uri) {

         // Use the Uri Matcher to determine what kind of URI this is.
         final int match = sUriMatcher.match(uri);

         switch (match) {
             case ATHLETE_ID:
                 return DataContract.AthleteEntry.CONTENT_ITEM_TYPE;
             case ATHLETE:
                 return DataContract.AthleteEntry.CONTENT_TYPE;

             default:
                 throw new UnsupportedOperationException("Unknown uri: " + uri);
         }
     }

     @Override
     public Uri insert(Uri uri, ContentValues values) {
         final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
         final int match = sUriMatcher.match(uri);
         Uri returnUri;

         switch (match) {
             case ATHLETE: {
                 long _id = db.insert(DataContract.AthleteEntry.TABLE_NAME, null, values);
                 if ( _id > 0 )
                     returnUri = DataContract.AthleteEntry.buildPlayerUri(_id);
                 else
                     throw new android.database.SQLException("Failed to insert row into " + uri);
                 break;
             }
             default:
                 throw new UnsupportedOperationException("Unknown uri: " + uri);
         }
         getContext().getContentResolver().notifyChange(uri, null);
         return returnUri;
     }

     @Override
     public int delete(Uri uri, String selection, String[] selectionArgs) {
         final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
         final int match = sUriMatcher.match(uri);
         int rowsDeleted;
         switch (match) {
             case ATHLETE:
                 rowsDeleted = db.delete(
                         DataContract.AthleteEntry.TABLE_NAME, selection, selectionArgs);
                 break;
             default:
                 throw new UnsupportedOperationException("Unknown uri: " + uri);
         }
         // Because a null deletes all rows
         if (selection == null || rowsDeleted != 0) {
             getContext().getContentResolver().notifyChange(uri, null);
         }
         return rowsDeleted;
     }

     @Override
     public int update(
             Uri uri, ContentValues values, String selection, String[] selectionArgs) {

         final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
         final int match = sUriMatcher.match(uri);
         int rowsUpdated;
         switch (match) {
             case ATHLETE:
                 rowsUpdated = db.update(
                         DataContract.AthleteEntry.TABLE_NAME, values, selection, selectionArgs);
                 break;

             default:
                 throw new UnsupportedOperationException("Unknown uri: " + uri);
         }
         // Because a null deletes all rows
         if (selection == null || rowsUpdated != 0) {
             getContext().getContentResolver().notifyChange(uri, null);
         }
         return rowsUpdated;

     }

     @Override
     public int bulkInsert(Uri uri, ContentValues[] values) {
         final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
         final int match = sUriMatcher.match(uri);
         switch (match) {
             case ATHLETE:
                 db.beginTransaction();
                 int returnCount = 0;
                 try {
                     for (ContentValues value : values) {
                         long _id = db.insert(DataContract.AthleteEntry.TABLE_NAME, null, value);
                         if (_id != -1) {
                             returnCount++;
                         }
                     }
                     db.setTransactionSuccessful();
                 } finally {
                     db.endTransaction();
                 }
                 getContext().getContentResolver().notifyChange(uri, null);

                 return returnCount;


             default:
                 return super.bulkInsert(uri, values);
         }
     }

}

