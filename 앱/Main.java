package com.example.assertqr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Main extends AppCompatActivity {
    Button createQR;

    //Button requestBuilding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createQR = (Button) findViewById(R.id.createQR);
        //requestBuilding = (Button) findViewById(R.id.requestButton);


        createQR.setOnClickListener(v -> {
            Intent intent = new Intent(this,CreateQR.class);
            startActivity(intent);
        });

//        requestBuilding.setOnClickListener(v -> {
//            Intent intent = new Intent(this,RequestBuilding.class);
//            startActivity(intent);
//        });

    }


}
