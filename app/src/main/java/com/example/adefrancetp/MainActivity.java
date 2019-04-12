package com.example.adefrancetp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.example.adefrancetp.databinding.ActivityMainBinding;
import com.example.adefrancetp.models.Global;
import com.example.adefrancetp.models.Weather;
import com.example.adefrancetp.services.OpenWeatherService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{

    ActivityMainBinding binding;
    String latitude;
    String longitude;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());

            getCityWeather(latitude,longitude);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,10,locationListener);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch(requestCode){
            case 1 :{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,10,locationListener);
                    }
                }
            }
        }
    }


    //On récupère la température de la ville passée en paramètre
    private void getCityWeather(String lat,String lng){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Log.d("LAT", lat);
        Log.d("LNG",lng);

        OpenWeatherService service = retrofit.create(OpenWeatherService.class);
        //On appel le WebService
        Call<Global> call = service.getWeather(lat,lng,"e7635207e7a83be8fa9925c5fc57402a","metric","fr");
        call.enqueue(new Callback<Global>() {
            @Override
            public void onResponse(Call<Global> call, Response<Global> response) {
                Global global = response.body();

                if(global.getName().length() > 0) {
                    binding.city.setText(global.getName());
                }else{
                    binding.city.setText("Unknown");
                }
                binding.degree.setText(global.getMain().getTemp());
                binding.temp.setText(global.getWeather().get(0).getDescription());

                String c = removeLastCaracter(global.getWeather().get(0).getIcon());
                switch(c){
                    case "01":
                        binding.weatherImageView.setImageResource(R.drawable.i01d);
                        break;
                    case "02":
                        binding.weatherImageView.setImageResource(R.drawable.i02d);
                        break;
                    case "03":
                        binding.weatherImageView.setImageResource(R.drawable.i03d);
                        break;
                    case "04":
                        binding.weatherImageView.setImageResource(R.drawable.i04d);
                        break;
                    case "09":
                        binding.weatherImageView.setImageResource(R.drawable.i09d);
                        break;
                    case "10":
                        binding.weatherImageView.setImageResource(R.drawable.i10d);
                        break;
                    case "11":
                        binding.weatherImageView.setImageResource(R.drawable.i11d);
                        break;
                    case "13":
                        binding.weatherImageView.setImageResource(R.drawable.i13d);
                        break;
                    case "50":
                        binding.weatherImageView.setImageResource(R.drawable.i50d);
                        break;
                    default:

                }
                Log.d("Weather",c);
            }

            @Override
            public void onFailure(Call<Global> call, Throwable t) {
                Log.d("Error",String.valueOf(t));
            }
        });
    }

    public void searchCityWeather(View view) {
        String city = String.valueOf(binding.cityEditText.getText());
        if(city.length() > 0) {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("city", city);
            this.startActivity(intent);
        }
    }

    public String removeLastCaracter(String str) {
        str = str.substring(0, str.length() - 1);
        return str;
    }

}
