/*
 * Copyright 2013 The Android Open Source Project
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

package com.enricooliva.crossfit.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.enricooliva.crossfit.R;
import com.enricooliva.crossfit.data.Athlete;
import com.enricooliva.crossfit.data.DataContract;
import com.enricooliva.crossfit.data.Lesson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Define a sync adapter for the app.
 *
 * <p>This class is instantiated in {@link SyncService}, which also binds SyncAdapter to the system.
 * SyncAdapter should only be initialized in SyncService, never anywhere else.
 *
 * <p>The system calls onPerformSync() via an RPC call through the IBinder object supplied by
 * SyncService.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String LOG_TAG = "SyncAdapter";

    /**
     * URL to fetch content from during a sync.
     */
    private static final String ATHLETE_URL = "http://95.85.62.67:9876/athletes";
    private static final String LESSON_URL = "http://95.85.62.67:9876/classes";

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    /**
     * Network connection timeout, in milliseconds.
     */
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds

    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;

        /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the network, parse it, and store it in the content provider is
     * done here. Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     .
     *
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link android.content.AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to peform blocking I/O here.
     *
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.i(LOG_TAG, "Beginning network synchronization");
        try {

            Log.i(LOG_TAG, "Streaming data from network: ");
            String result = MadeGetQuery(ATHLETE_URL);
            updateLocalAthleteData(result);
            result = MadePostQuery(LESSON_URL);
            updateLocalLessonData(result);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Feed URL is malformed", e);
            syncResult.stats.numParseExceptions++;
            return;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error reading from network: " + e.toString());
            syncResult.stats.numIoExceptions++;
            return;
        } catch (XmlPullParserException e) {
            Log.e(LOG_TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        } catch (OperationApplicationException e) {
            Log.e(LOG_TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        }
        Log.i(LOG_TAG, "Network synchronization complete");
    }

    private String getResult(InputStream is)
    {
        //convert response to string
        String result = "";
        if (is!=null) {

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while (reader.ready() && (line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                reader.close();
            } catch (UnsupportedEncodingException e) {
                Log.e("log_tag", "Error converting result " + e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("log_tag", "Error converting result " + e.toString());
                e.printStackTrace();
            } finally {
                if (is != null)
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

        }
        return result;
    }

    public String MadePostQuery(String builtUri)
    {
        HttpURLConnection urlConnection = null;
        InputStream inputStream=null;

        //http post
        try{
            URL url = new URL(builtUri.toString());


            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");//some header you want to add
            //urlConnection.setRequestProperty("Authorization", "key=" + AppConfig.API_KEY);//some header you want to add
            urlConnection.setDoOutput(true);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("boxId", "58340522c9e77c000130e2d4");

            OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
            outputPost.write(jsonParam.toString().getBytes());
            outputPost.flush();
            outputPost.close();

            int responseCode = urlConnection.getResponseCode();

            inputStream = urlConnection.getInputStream();

        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
        }

        return this.getResult(inputStream);

    }





    public String MadeGetQuery(String builtUri)
    {
        HttpURLConnection urlConnection = null;
        InputStream inputStream=null;

        //http post
        try{
            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();

            urlConnection.disconnect();

        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
        }

        return this.getResult(inputStream);

    }

    public static ArrayList<Athlete> getAthleteList(String athletes)
    {
        ArrayList<Athlete> athleteList = new ArrayList<Athlete>();
        //parse json data
        try{
            JSONArray jArray = new JSONArray(athletes);
            for(int i=0;i<jArray.length();i++){
                athleteList.add(new Athlete( jArray.getJSONObject(i)));
            }
        }catch(JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());

        }

        return athleteList;
    }

    public static ArrayList<Lesson> getLessonList(String lesson)
    {
        ArrayList<Lesson> list = new ArrayList<Lesson>();
        //parse json data
        try{
            JSONArray jArray = new JSONArray(lesson);
            for(int i=0;i<jArray.length();i++){
                list.add(new Lesson( jArray.getJSONObject(i)));
            }
        }catch(JSONException e){
            Log.e("log_tag", "Error parsing data " + e.toString());

        }

        return list;
    }



    public void updateLocalAthleteData(final String result)
            throws IOException, XmlPullParserException, RemoteException,
            OperationApplicationException, ParseException {


        List<Athlete> athleteList = getAthleteList(result);

        // Get and insert the new weather information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(athleteList.size());

        for (Athlete p : athleteList){

            ContentValues athleteValues = new ContentValues();
            athleteValues.put(DataContract.AthleteEntry.COLUMN_id, p.getId());
            athleteValues.put(DataContract.AthleteEntry.COLUMN_firstName, p.getFirstName());
            athleteValues.put(DataContract.AthleteEntry.COLUMN_lastName, p.getLastName());
            athleteValues.put(DataContract.AthleteEntry.COLUMN_dateOfBirth, p.getDateOfBirth());
            athleteValues.put(DataContract.AthleteEntry.COLUMN_sex, p.getSex());
            athleteValues.put(DataContract.AthleteEntry.COLUMN_email, p.getEmail());
            cVVector.add(athleteValues);
        }

        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            int rowsDeleted = mContentResolver.delete(DataContract.AthleteEntry.CONTENT_URI, "", null);
            int rowsInserted = mContentResolver.bulkInsert(DataContract.AthleteEntry.CONTENT_URI, cvArray);
            Log.v(LOG_TAG, "deleted " + rowsDeleted + " rows of athlete data");
            Log.v(LOG_TAG, "inserted " + rowsInserted + " rows of athlete data");


            mContentResolver.notifyChange(
                    DataContract.AthleteEntry.CONTENT_URI, // URI where data was modified
                    null,                           // No local observer
                    false);                         // IMPORTANT: Do not sync to network

        }

    }


    public void updateLocalLessonData(final String result)
            throws IOException, XmlPullParserException, RemoteException,
            OperationApplicationException, ParseException {

        List<Lesson> lessonList = getLessonList(result);

        // Get and insert the new weather information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(lessonList.size());

        for (Lesson p : lessonList){

            ContentValues values = new ContentValues();
            values.put(DataContract.LessonEntry.ID, p.getId());
            values.put(DataContract.LessonEntry.BOXID, p.getBoxId());
            values.put(DataContract.LessonEntry.DATE, p.getDate());
            values.put(DataContract.LessonEntry.DURATION, p.getDuration());
            values.put(DataContract.LessonEntry.MAXATTENDANCE, p.getMaxAttendance());
            values.put(DataContract.LessonEntry.TYPE, p.getType());
            cVVector.add(values);
        }

        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            int rowsDeleted = mContentResolver.delete(DataContract.LessonEntry.CONTENT_URI, "", null);
            int rowsInserted = mContentResolver.bulkInsert(DataContract.LessonEntry.CONTENT_URI, cvArray);
            Log.v(LOG_TAG, "deleted " + rowsDeleted + " rows of lesson data");
            Log.v(LOG_TAG, "inserted " + rowsInserted + " rows of lesson data");

            mContentResolver.notifyChange(
                    DataContract.LessonEntry.CONTENT_URI, // URI where data was modified
                    null,                           // No local observer
                    false);                         // IMPORTANT: Do not sync to network

        }

    }



    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }


    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        SyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

}
