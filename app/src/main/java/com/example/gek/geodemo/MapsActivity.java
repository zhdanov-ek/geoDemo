package com.example.gek.geodemo;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


// https://developers.google.com/maps/documentation/android-api/map?hl=ru

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private LatLng courierPosition, yourPosition;
    private Button btnOneMarker, btnTwoMarkers;
    private static final int OFFSET_FROM_EDGES_OF_THE_MAP = 50;
    private static final int MAP_ZOOM = 12;
    private ArrayList<LatLng> targets;

    private BitmapDescriptor bdCyan, bdPizza, bdCar;
    private ArrayList<BitmapDescriptor> targetsIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        btnOneMarker = (Button) findViewById(R.id.btnClient);
        btnTwoMarkers = (Button) findViewById(R.id.btnCourier);

        btnOneMarker.setOnClickListener(this);
        btnTwoMarkers.setOnClickListener(this);

        courierPosition = new LatLng(49.423354, 32.041006);

        targets = new ArrayList<>();
        targets.add(new LatLng(49.430945, 32.010220));
        targets.add(new LatLng(49.480835, 31.988634));
        targets.add(new LatLng(49.448891, 32.053838));
        targets.add(new LatLng(49.4169368,32.031447));

        // Устанавливаем колбек для найденного фрагмента, который сработает после загрузки карты
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // получаем с интента координаты нашего местоположения
        Intent intent = getIntent();
        if ((intent != null) && (intent.hasExtra("position"))){
            yourPosition = intent.getParcelableExtra("position");
        }
    }



    /**
     * Колбек срабатывает как только карта готова к роботе
     * В нем мы получаем ссылку на саму карту, через которую будем управлять ею
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (yourPosition != null) {
            btnOneMarker.setEnabled(true);
            btnTwoMarkers.setEnabled(true);
        }

        // тип карты
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Устанавливаем в качестве обработчика нажатия на информационное окно маркера нашу активити
        mMap.setOnInfoWindowClickListener(this);

        // Формируем различные значки маркеров
        bdCyan = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
        bdPizza = BitmapDescriptorFactory.fromResource(R.drawable.ic_pizza_map);
        bdCar =  BitmapDescriptorFactory.fromResource(R.drawable.ic_car);

        // Наполняем список иконками разных цветов
        fillTargetIcons();
    }

    /** Создаем список иконок разных цветов по кол-ву меток */
    private void fillTargetIcons(){
        float color = 0.0F;         // цвето должен быть от 0.0F до 330.0F
        targetsIcons = new ArrayList<>();
        for (int i = 0; i < targets.size() ; i++) {
            targetsIcons.add(BitmapDescriptorFactory.defaultMarker(color));
            color += 30;
            if (color == 360) {
                color = 0.0F;
            }
        }
    }

    @Override
    public void onClick(View view) {
        // убираем маркеры если они были
        mMap.clear();
        switch (view.getId()){
            case R.id.btnClient:
                showForClient();
                break;
            case R.id.btnCourier:
                showForCourier();
                break;
            default:
                break;
        }
    }

    /** Показываем на карте маркер с нашими координами. Масштаб вручную устанавливаем */
    private void showForClient(){
        mMap.addMarker(new MarkerOptions()
                .position(courierPosition)
                .icon(bdPizza)
                .title("Your pizza"));

        mMap.addMarker(new MarkerOptions()
                .position(yourPosition)
                .icon(bdCyan)
                .title("Your position"));

        // Формируем границы выводимой карты по всем нашим маркерам, что бы они были видны
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(courierPosition);
        boundsBuilder.include(yourPosition);
        LatLngBounds bounds = boundsBuilder.build();

        // Позиционируем камеру по указанным границам со смещением от краев экрана относительно крайних маркеров
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, OFFSET_FROM_EDGES_OF_THE_MAP);
        mMap.moveCamera(cu);

    }

    /** Показываем список маркеров, которые вмещаются на экране и масштаб задается автоматом */
    private void showForCourier(){
        mMap.addMarker(new MarkerOptions()
                .position(courierPosition)
                .icon(bdCar)
                .title("Courier"));

        for (int i = 0; i < targets.size(); i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(targets.get(i))
                    .icon(targetsIcons.get(i))
                    .title("Name " + i)
                    .snippet("Phone " + i));
        }

        // Формируем границы выводимой карты по всем нашим маркерам, что бы они были видны
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(courierPosition);
        for (LatLng target: targets) {
            boundsBuilder.include(target);
        }
        LatLngBounds bounds = boundsBuilder.build();

        // Позиционируем камеру по указанным границам со смещением от краев экрана относительно крайних маркеров
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, OFFSET_FROM_EDGES_OF_THE_MAP);
        mMap.moveCamera(cu);
    }


    // Реализация интерфейса нажатия на информационное окно маркера
    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "You click on marker " + marker.getTitle(), Toast.LENGTH_SHORT).show();
    }
}