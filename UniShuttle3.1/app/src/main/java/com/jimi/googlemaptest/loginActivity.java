package com.jimi.googlemaptest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import static com.jimi.googlemaptest.R.id.etSID;

public class loginActivity extends Activity {

    EditText sIDet;
    EditText passwordet;
    RadioGroup groupRole = null;
    RadioButton radioButtonStudent = null;
    RadioButton radioButtonDriver = null;
    boolean isStudent;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sIDet = findViewById(etSID);
        passwordet = findViewById(R.id.etPassword);
        groupRole = findViewById(R.id.groupRole);
        radioButtonStudent = findViewById(R.id.radioStudent);
        radioButtonDriver = findViewById(R.id.radioDriver);
        isStudent = true;
        groupRole.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged (RadioGroup groupRole, int id){
                if (id == radioButtonDriver.getId()) {
                    isStudent = false;
                }
            }
        });

    }


        public void onLogin(View aView) {

            String username = sIDet.getText().toString();
            String password = passwordet.getText().toString();
            String type = null;
            if (isStudent == true) {
                type = "student";
            }
            else{
                type = "driver";
            }

            LoginBackgound backgroundWorker = new LoginBackgound(this);
            backgroundWorker.execute(type, username, password);

        }


    /*
    EditText sIDet;
    EditText passwordet;
    Button login;
    String serUrl = "https://unishuttle.co.nz/login2.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sIDet = findViewById(etSID);
        passwordet = findViewById(R.id.etPassword);
        login = findViewById(R.id.btnSignIn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, serUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplication(),response,Toast.LENGTH_SHORT).show();
                        if (response.contains("1")){
                            toDriver();
                        }else{
                            toMap();
                        };

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(loginActivity.this, "Submission failed", Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String username = sIDet.getText().toString();
                String password = passwordet.getText().toString();

                params.put("username", username);
                params.put("password", password);


                return params;
            }
        };

        MySingleton.getInstance().addToRequestQueue(stringRequest);

    }

    private void toDriver() {
        Intent intent = new Intent(this, HomeDriverActivity.class);
        startActivity(intent);
    }

    private void toMap(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    */
}
