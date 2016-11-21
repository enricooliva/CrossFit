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
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.enricooliva.crossfit.data.Athlete;
import com.enricooliva.crossfit.data.DataContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String LOG_TAG = "SyncAdapter";

    /**
     * URL to fetch content from during a sync.
     */
    private static final String ATHLETE_URL = "http://www.enricooliva.com/getAllPlayers.php";

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
            updateLocalPlayerData(result);
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
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            result=sb.toString();
        } catch (UnsupportedEncodingException e) {
            Log.e("log_tag", "Error converting result " + e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("log_tag", "Error converting result " + e.toString());
            e.printStackTrace();
        } finally {
            if (is!=null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }


        return result;
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

        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        }catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
        }

        return this.getResult(inputStream);

    }

    public static ArrayList<Athlete> getPlayerList(String athletes)
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

    public void updateLocalPlayerData(final String result)
            throws IOException, XmlPullParserException, RemoteException,
            OperationApplicationException, ParseException {

        final ContentResolver contentResolver = getContext().getContentResolver();

        List<Athlete> athleteList = getPlayerList(result);

        // Get and insert the new weather information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(athleteList.size());

        for (Athlete p : athleteList){

            ContentValues athleteValues = new ContentValues();
            athleteValues.put(DataContract.AthleteEntry.COLUMN_id, p.getId());
            athleteValues.put(DataContract.AthleteEntry.COLUMN_firstName, p.getFirstName());
            athleteValues.put(DataContract.AthleteEntry.COLUMN_lastName, p.getLastName());
            athleteValues.put(DataContract.AthleteEntry.COLUMN_dateOfBirth, String.valueOf(p.getDateOfBirth()));
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

}
