package com.enricooliva.crossfit;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.enricooliva.crossfit.data.Athlete;
import com.enricooliva.crossfit.data.DataContract;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Enrico on 06/04/2015.
 */
public class FetchAthleteTask extends AsyncTask<String, Void, Void> {

    private final Context mContext;
    String result = "";
    private final static String LOG_TAG = "FetchPlayerTask";

    public FetchAthleteTask(Context context) {
        mContext = context;
    }

    private String getResult(InputStream is) {
        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            result = sb.toString();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }

        return result;
    }

    public String MadeGetQuery(String builtUri) {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        //http post
        try {
            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();

        } catch (IOException e) {
            Log.d("HTTPCLIENT", e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        return this.getResult(inputStream);

    }


    public static ArrayList<Athlete> getAthleteList(String athletes) {
        ArrayList<Athlete> athleteList = new ArrayList<Athlete>();
        //parse json data
        try {
            JSONArray jArray = new JSONArray(athletes);
            for (int i = 0; i < jArray.length(); i++) {
                //new Gson().fromJson(jArray.getJSONObject(i), Athlete.class);
                athleteList.add(new Athlete(jArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());

        }

        return athleteList;
    }

    @Override
    protected Void doInBackground(String... params) {
        String result = MadeGetQuery("http://95.85.62.67:9876/athletes");
        List<Athlete> athleteList = getAthleteList(result);

        // Get and insert the new weather information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(athleteList.size());

        for (Athlete p : athleteList) {

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

            int rowsDeleted = mContext.getContentResolver().delete(DataContract.AthleteEntry.CONTENT_URI, "", null);
            int rowsInserted = mContext.getContentResolver().bulkInsert(DataContract.AthleteEntry.CONTENT_URI, cvArray);

            Log.v(LOG_TAG, "inserted " + rowsInserted + " rows of weather data");
            Log.v(LOG_TAG, "deleted " + rowsDeleted + " rows of weather data");
        }


        return null;
    }


}


