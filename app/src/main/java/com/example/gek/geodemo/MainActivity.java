package com.example.gek.geodemo;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvInfo;
    private Button btnGetAddress, btnShowMap;
    private LatLng position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = (TextView)findViewById(R.id.tvInfo);
        btnGetAddress = (Button) findViewById(R.id.btnGetAddress);
        btnGetAddress.setOnClickListener(this);
        btnShowMap = (Button) findViewById(R.id.btnShowMap);
        btnShowMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGetAddress:
                tvInfo.setText(locationInfo());
//                CityAsyncTask cst = new CityAsyncTask(this, 49.4294499, 32.0075313);
//                cst.execute();
//                String lo = null;
//                try {
//                    lo = cst.get().toString();
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
                break;
            case R.id.btnShowMap:
                Intent showMap = new Intent(getBaseContext(), MapsActivity.class);
                showMap.putExtra("position", position);
                startActivity(showMap);
            default:
                break;
        }

    }

    /** Получаем инфу с системного сервиса о гео-локации */
    public String locationInfo(){
        String s = "";
        double latitude;
        double longitude;
        position = null;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        // тут надо проверить разрешение на получение координат прежде чем пробовать получать их
        // или не ставить таржет СДК 23
        Location location = locationManager.getLastKnownLocation(bestProvider);
        // Определяем широту и долготу
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        s += "getLatitude() = " + latitude;
        s += "\ngetLongitude() = " + longitude;
        // Высота над уровнем моря
        s += "\ngetAltitude() = " + location.getAltitude();

        if (latitude != 0) {
            position = new LatLng(latitude, longitude);
            btnShowMap.setEnabled(true);
        } else {
            btnShowMap.setEnabled(false);
        }

        return s;
    }
}