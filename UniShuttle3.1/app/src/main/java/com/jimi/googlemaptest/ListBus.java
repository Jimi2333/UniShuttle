package com.jimi.googlemaptest;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by fratelli on 10/16/2017.
 */

public class ListBus extends AppCompatActivity{
    ArrayList<String> listBus = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Spinner spBus;
    public static String busSelection;

    String serUrl = "https://unishuttle.co.nz/loadbus.php";
    String serUrl2 = "https://unishuttle.co.nz/releasebus.php";
    String serUrl3 = "https://unishuttle.co.nz/passenger.php";


    private Button btnStartTrip, btnEndTrip, btnLoadBus;
    private TextView textView;
    private BroadcastReceiver broadcastReceiver;
    RadioGroup passengerNo = null;
    RadioButton radioButton1 = null;
    RadioButton radioButton2 = null;
    RadioButton radioButton3 = null;
    RadioButton radioButton4 = null;
    int passenger;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starttrip);

        btnLoadBus = (Button) findViewById(R.id.btnConfirmBus);
        btnStartTrip = (Button) findViewById(R.id.btnStart);
        btnEndTrip = (Button) findViewById(R.id.btnEnd);
        textView = (TextView) findViewById(R.id.txtViewLocation);
        passengerNo = (RadioGroup) findViewById(R.id.radioGroupPassenger);
        radioButton1 = (RadioButton) findViewById(R.id.radio1);
        radioButton2 = (RadioButton) findViewById(R.id.radio2);
        radioButton3 = (RadioButton) findViewById(R.id.radio3);
        radioButton4 = (RadioButton) findViewById(R.id.radio4);
        passenger = 0;

        ListBusBackTask bt = new ListBusBackTask();
        bt.execute();

        spBus = (Spinner)findViewById(R.id.spinnerBus);
        adapter = new ArrayAdapter<String>(ListBus.this, android.R.layout.simple_list_item_1, listBus);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spBus.setAdapter(adapter);

        if(!runtime_permissions())
            enable_buttons();
    }

    private void enable_buttons() {

        btnStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(),GPS_service.class);
                startService(i);
            }
        });

        btnEndTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),GPS_service.class);
                stopService(i);
                releaseBus();
                btnLoadBus.setVisibility(View.VISIBLE);
                btnStartTrip.setVisibility(View.INVISIBLE);
                btnEndTrip.setVisibility(View.INVISIBLE);
                passengerNo.setVisibility(View.INVISIBLE);


            }
        });

        btnLoadBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busSelection = spBus.getSelectedItem().toString();
                LoadBus();
                btnLoadBus.setVisibility(View.INVISIBLE);
                btnStartTrip.setVisibility(View.VISIBLE);
                btnEndTrip.setVisibility(View.VISIBLE);
                passengerNo.setVisibility(View.VISIBLE);
            }
        });


        passengerNo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged (RadioGroup groupRole, int id){
                if (id == radioButton1.getId()) {
                    passenger = 5;
                }
                if (id == radioButton2.getId()) {
                    passenger = 10;
                }
                if (id == radioButton3.getId()) {
                    passenger = 15;
                }
                if (id == radioButton4.getId()) {
                    passenger = 20;
                }
                updatePassenger();
            }
        });

    }

    private void updatePassenger() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serUrl3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplication(),"Passenger Updated",Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplication(),"Server no response",Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("licenseplate", busSelection);
                params.put("passenger", String.valueOf(passenger));

                return params;
            }
        };
        MySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    private void releaseBus() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serUrl2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplication(),"Bus " + busSelection + " released",Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplication(),"Server no response",Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("licenseplate", busSelection);

                return params;
            }
        };

        MySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enable_buttons();
            }else {
                runtime_permissions();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    textView.append("\n" +intent.getExtras().get("coordinates"));

                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    private void LoadBus() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, serUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplication(),"Bus " + busSelection + " loaded",Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplication(),"Server no response",Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("licenseplate", busSelection);

                return params;
            }
        };
        MySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    private class ListBusBackTask extends AsyncTask<Void, Void, Void> {
        ArrayList<String> list;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try{
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("https://unishuttle.co.nz/vehiclejson1.php");
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();

                //get the response as a string.
                is = entity.getContent();
            }catch (IOException e){
                e.printStackTrace();
            }

            //convert response to string
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result+=line;
                }
                is.close();

            }catch(Exception e){
                e.printStackTrace();
            }

            // parse json data
            try{
                JSONArray jArray =new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                    JSONObject jsonObject=jArray.getJSONObject(i);
                    // add licensePlate to arraylist
                    list.add(jsonObject.getString("LicensePlate"));
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            listBus.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }



}