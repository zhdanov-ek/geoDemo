package com.example.gek.geodemo;


import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/** Получаем инфу о местонахождении по широте и долготе
 * Geocoder - класс, которые преобразовывает широту и долготу в адреса(дома) и обратно.
 * Этот класс использует дополнения к ядру андроида и этих дополнений в системе может не быть. */
public class CityAsyncTask extends AsyncTask<String, String, String> {
    Activity act;
    double latitude;
    double longitude;

    public CityAsyncTask(Activity act, double latitude, double longitude) {
        // TODO Auto-generated constructor stub
        this.act = act;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        Geocoder geocoder = new Geocoder(act, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Log.e("Addresses", "-->" + addresses);
            result = addresses.get(0).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

    }
}