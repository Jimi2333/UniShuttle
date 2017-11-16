package com.jimi.googlemaptest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fratelli on 10/4/2017.
 */

public class Maintenance extends Activity {

    Button submit;
    EditText Cost, LicensePlate, Description;
    TextView dateView;
    Date date;
    String serUrl = "https://unishuttle.co.nz/maintenancereport.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        submit = (Button)findViewById(R.id.btnSubmit);
        Cost = (EditText)findViewById(R.id.edtCost);
        dateView = (TextView)findViewById(R.id.txtDateView);
        Description = (EditText)findViewById(R.id.edtDescription);
        LicensePlate = (EditText)findViewById(R.id.edtLicense);

        date = Calendar.getInstance().getTime();
        final DateFormat dateWihtOutTime = new SimpleDateFormat("MM/dd/yyyy");
        final String outputDate = dateWihtOutTime.format(date);
        dateView.setText(outputDate);

        LicensePlate.setText(ListBus.busSelection);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertReport();
            }
        });
    }

    private void InsertReport() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, serUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplication(),response,Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Maintenance.this, "Submission failed", Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String description = Description.getText().toString();
                String licenseplate = LicensePlate.getText().toString();
                String cost = Cost.getText().toString();

                params.put("description", description);
                params.put("licenseplate", licenseplate);
                params.put("cost", cost);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}
