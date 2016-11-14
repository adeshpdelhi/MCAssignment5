package com.creation.mcassignment5;

/**
 * Created by adesh on 11/14/16.
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class DownloadTask extends AsyncTask<URL,Void,String > {
    private static final String TAG = "DownloadTask";
    private Activity activity;
    public DownloadTask(Activity act){
        this.activity = act;
    }
    @Override
    protected String doInBackground(URL... urls){
        URL url=urls[0];
        ConnectivityManager conMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                try {
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                    try {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        Reader reader = new InputStreamReader(in, "UTF-8");
                        char[] buffer = new char[1000000];
                        reader.read(buffer);
                        String data = new String(buffer);
                        Log.v(TAG, data);
                        return data.substring(0,1000);
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    Log.v(TAG, e.toString());
                }
            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if(result==null)
        {
            Toast.makeText(activity, "Unable to connect. Check internet!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(activity, ShowActivity.class);
        i.putExtra("data",result);
        activity.startActivity(i);
    }
}
