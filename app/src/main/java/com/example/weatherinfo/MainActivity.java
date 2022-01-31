package com.example.weatherinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.IllformedLocaleException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText edname;
    TextView tv1, tv2, tv3, tv4;
    ImageView img, imv1;
    RecyclerView rv;
    adapter adapter1;
    modal modal2;
    List<modal> l2;
    LocationManager locationManager;
    String cityname1;
    RelativeLayout  relativeLayout;
    ProgressBar progressBar;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.
                FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        edname = findViewById(R.id.edname);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        img = findViewById(R.id.img);
        imv1 = findViewById(R.id.imv1);
        rv = findViewById(R.id.rv);
        relativeLayout = findViewById(R.id.rv2);
        progressBar = findViewById(R.id.progressBar);
        l2 = new ArrayList<>();
        adapter1 = new adapter(l2, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adapter1);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
          if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                ==PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location!=null){
                        cityname1 = getcity(location.getLongitude(),location.getLatitude());
                        getinfo(cityname1);
                    }else{
                        Toast toast = Toast.makeText(MainActivity.this, "Please! On Your Device Location....", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }
            });
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
          }
        edname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edname.getText().toString();
                if(city.isEmpty()){
                    Toast toast = Toast.makeText(MainActivity.this, "Please! Enter city name....", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();}
                else{getinfo(city);
                l2.clear();}
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Toast toast = Toast.makeText(MainActivity.this, "Permission Granted....", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(MainActivity.this, "Please! provide the permissions....", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void getinfo(String city){
        // String apiKey = "b608220564fd7f5b3735d1232576c9e8";
        String apiKey = "a98a5ab93cc6404683194337220501";
        //String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
        String url = "https://api.weatherapi.com/v1/forecast.json?key="+apiKey+"&q="+city+"&days=1&aqi=no&alerts=no";
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest obj = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(JSONObject response) {
                relativeLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                try{
                    JSONObject obj1 = response.getJSONObject("current");
                    String temp = obj1.getString("temp_c");
                    String condition = obj1.getJSONObject("condition").getString("text");
                    String icon = "https:"+ obj1.getJSONObject("condition").getString("icon");
                    Picasso.with(MainActivity.this).load(icon).into(img);
                    tv2.setText(condition);
                    tv3.setText(temp+"°C");
                    tv1.setText(city.toUpperCase());
                    JSONObject forecast = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hour = forecast.getJSONArray("hour");
                    int day = obj1.getInt("is_day");
                    for (int i=0;i<hour.length();i++){
                        int drawable;
                        String tempere = hour.getJSONObject(i).getString("temp_c")+"°C";
                        String time = hour.getJSONObject(i).getString("time");
                        String wind = hour.getJSONObject(i).getString("wind_kph")+" Km/h";
                        String icon1 = hour.getJSONObject(i).getJSONObject("condition").getString("icon");
                        int is_day = hour.getJSONObject(i).getInt("is_day");
                        if(is_day==1){
                             drawable = R.drawable.blue;
                        }
                        else{
                            drawable = R.drawable.black;
                        }
                        l2.add(new modal(time,tempere,wind,icon1,drawable));
                    }
                    adapter1.notifyDataSetChanged();
                    if(day==1){
                        Picasso.with(MainActivity.this).load("https://images.unsplash.com/photo-1566228015668-4c45dbc4e2f5?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=334&q=80").into(imv1);
                        imv1.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                    else{
                        Picasso.with(MainActivity.this).load("https://wallpaperaccess.com/full/922698.jpg").into(imv1);
                        imv1.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(MainActivity.this, "Please! Enter valid city name....", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
        rq.add(obj);
    }
    public String getcity(double longitude,double latitude){
        String cityname = "Not Found";
        Geocoder gcd = new Geocoder(MainActivity.this,Locale.getDefault());
        try{
            List<Address> addresses = gcd.getFromLocation(latitude,longitude,10);
            for(Address adr : addresses){
                if(adr!=null){
                    String city1 = adr.getLocality();
                    if(city1!=null && !city1.equals("")){
                        cityname = city1;
                    }else{
                        Log.d("TAG","CITY NOT FOUND");
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return cityname;
    }
}