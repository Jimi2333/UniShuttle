package com.jimi.googlemaptest;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Jimi on 9/29/2017.
 */

public class UpdateLocation extends AsyncTask <String, Void, String > {

    Context context;

    Dialog alertDialog;
    UpdateLocation(Context ctx) {context = ctx;}


    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String result) {

    }
    @Override
    protected void onProgressUpdate(Void... values) {


        super.onProgressUpdate(values);
    }
}


