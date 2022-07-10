package com.example.assertqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    TextView join;
    Button login;
    EditText studentID, password;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        apiService=RetrofitClient.getClient().create(ApiService.class);

        studentID = findViewById(R.id.studentNum);
        password=findViewById(R.id.password);
        join = findViewById(R.id.signin);   //학생등록
        login= findViewById(R.id.loginbutton);  //메인화면


        join.setOnClickListener(v -> {
            Intent intent = new Intent(this,SignUp.class);
            startActivity(intent);
        });

        login.setOnClickListener(v -> {
            tryLogin();
        });
    }

    private void tryLogin() {
        studentID.setError(null);
        password.setError(null);

        String bodyStudentID = studentID.getText().toString();
        String bodyPassword = password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 패스워드의 유효성 검사
        if (bodyStudentID.isEmpty()) {
            studentID.setError("학번을 입력해주세요.");
            focusView = studentID;
            cancel = true;
        }

        if (bodyPassword.isEmpty()) {
            password.setError("이름를 입력해주세요.");
            focusView = password;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            login(new LoginPut(bodyStudentID,bodyPassword));
        }
    }
    public void login(LoginPut data) {
        apiService.login(data).enqueue(new Callback<LoginGet>() {
            @Override
            public void onResponse(Call<LoginGet> call, Response<LoginGet> response) {
                if (response.isSuccessful()) {
                    LoginGet result = response.body();
                    SharedPreferences pref= getSharedPreferences("token", MODE_PRIVATE);
                    SharedPreferences.Editor editor= pref.edit();
                    editor.putString("inputToken",result.getToken());
                    editor.commit();

                    if (result.getSuccess()) {
                        Intent intent = new Intent(getApplicationContext(), Main.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(Login.this, result.getErrors(), Toast.LENGTH_SHORT).show();

                }
                else
                    Log.d("76", "실패");
            }
            @Override
            public void onFailure(Call<LoginGet> call, Throwable t) {
                Toast.makeText(Login.this, "로그인 에러 발생", Toast.LENGTH_SHORT).show();
                Log.d("로그인 에러 발생", t.getMessage());
            }
        });
    }

}
