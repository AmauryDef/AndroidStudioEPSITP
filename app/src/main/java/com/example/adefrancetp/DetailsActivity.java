package com.example.adefrancetp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.adefrancetp.databinding.ActivityDetailsBinding;
import com.example.adefrancetp.models.Global;
import com.example.adefrancetp.services.OpenWeatherService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {
    ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_details);

        String city = getIntent().getStringExtra("city");
        getCityWeather(city);
    }

    private void getCityWeather(String city){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherService service = retrofit.create(OpenWeatherService.class);
        //On appel le WebService
        Call<Global> call = service.getWeatherByCity(city,"e7635207e7a83be8fa9925c5fc57402a","metric","fr");
        call.enqueue(new Callback<Global>() {
            @Override
            public void onResponse(Call<Global> call, Response<Global> response) {
                Global global = response.body();
                binding.city.setText(global.getName());
                binding.degree.setText(global.getMain().getTemp());
                binding.temp.setText(global.getWeather().get(0).getDescription());
                binding.windSpeed.setText(String.valueOf(Math.round(global.getWind().getSpeed())));
                binding.windDirection.setText(String.valueOf(Math.round(global.getWind().getDeg())));
                binding.country.setText(global.getSys().getCountry());
                binding.pressure.setText(global.getMain().getPressure());
                if(String.valueOf(global.getSys().getSunrise()).length() > 0) {
                    Date date = new Date(global.getSys().getSunrise()*1000L);
                    String sunrise = String.valueOf(date.getHours()+2+":"+date.getMinutes()+1);
                    binding.sunrise.setText(sunrise);
                }

                if(String.valueOf(global.getSys().getSunset()).length() > 0) {
                    Date date = new Date(global.getSys().getSunset()*1000L);
                    String sunset = String.valueOf(date.getHours()+2+":"+date.getMinutes());
                    binding.sunset.setText(sunset);
                }

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
            getCityWeather(city);
        }
    }


    public String removeLastCaracter(String str) {
        str = str.substring(0, str.length() - 1);
        return str;
    }

}
