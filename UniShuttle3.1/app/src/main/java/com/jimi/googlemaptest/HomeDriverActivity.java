package com.jimi.googlemaptest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Jimi on 9/29/2017.
 */

public class HomeDriverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstantceState){
        super.onCreate(savedInstantceState);
        setContentView(R.layout.activity_homedriver);

    }

    public void onStartTrip(View aView) {
        Intent intent = new Intent(this, ListBus.class);
        startActivity(intent);
    }

    public void onFuelUp(View aView){
        Intent intent = new Intent(this, FuelUp.class);
        startActivity(intent);
    }

    public void onMaintenance(View aView){
        Intent intent = new Intent(this, Maintenance.class);
        startActivity(intent);
    }

    public void onBreakDown(View aView){
        Intent intent = new Intent(this, BreakDown.class);
        startActivity(intent);
    }
}
