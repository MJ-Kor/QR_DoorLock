package com.example.assertqr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateQR extends AppCompatActivity {
    private ImageView image;
    private Bitmap bitmap;
    private ApiService apiService;
    private String qrcode;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);
        apiService=RetrofitClient.getClient().create(ApiService.class);
        image = (ImageView)findViewById(R.id.qrcode);

        getQR();

        image.setOnClickListener(v -> {
            bitmap = StringToBitmap(qrcode);
            image.setImageBitmap(bitmap);

        });
    };

    public void getQR() {
        SharedPreferences pref= getSharedPreferences("token", MODE_PRIVATE);
        String token=pref.getString("inputToken","");
        apiService.getQr(token).enqueue(new Callback<QrGet>() {
            @Override
            public void onResponse(Call<QrGet> call, Response<QrGet> response) {
                if (response.isSuccessful()) {
                    QrGet result = response.body();
                    if (result.getSuccess()) {
                        Log.v("성공", "result = " + response.body().toString());
                        qrcode = result.getQRcode();
                    } else {
                        Log.v("실패", "result = " + response.body().toString());
                    }
                }

            }
            @Override
            public void onFailure(Call<QrGet> call, Throwable t) {
                Toast.makeText(CreateQR.this, "QR코드 생성 에러", Toast.LENGTH_SHORT).show();
                Log.e("QR 생성 에러 발생", t.getMessage());
            }
        });
    }

    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}