package com.example.gek.geodemo;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tvInfo;
    Button btnGetAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = (TextView)findViewById(R.id.tvInfo);
        btnGetAddress = (Button) findViewById(R.id.btnGetAddress);
        btnGetAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGetAddress:
                tvInfo.setText(locationInfo());
                CityAsyncTask cst = new CityAsyncTask(this, 49.4294499, 32.0075313);
                cst.execute();
                String lo = null;
                try {
                    lo = cst.get().toString();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            default:
                break;
        }

    }

    /** Получаем инфу с системного сервиса о гео-локации */
    public String locationInfo(){
        String s = "";
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        // Определяем широту и долготу
        s += "getLatitude() = " + location.getLatitude();
        s += "getLongitude() = " + location.getLongitude();
        // Высота над уровнем моря
        s += "getAltitude() = " + location.getAltitude();

        return s;
    }
}