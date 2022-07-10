package com.example.assertqr;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Tag;

public class SignUp extends AppCompatActivity {
    EditText name,department,studentNum,pw,pw2;
    Button back, pwcheck, submit;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        apiService=RetrofitClient.getClient().create(ApiService.class);

        //학생 정보
        name = findViewById(R.id.signName);
        department=findViewById(R.id.signDepartment);
        studentNum=findViewById(R.id.signStudentNum);
        pw=findViewById(R.id.signPW);
        pw2=findViewById(R.id.signPW2);


        //이전 버튼
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed() );

//        //비밀번호 확인 버튼
//        pwcheck = findViewById(R.id.pwcheckbutton);
//        pwcheck.setOnClickListener(v -> {
//            if(pw.getText().toString().equals(pw2.getText().toString())&&pw.length()!=0){
//                pwcheck.setText("일치합니다");
//            }else{
//                Toast.makeText(SignUp.this, "일치하지 않습니다.", Toast.LENGTH_LONG).show();
//            }
//        });

        //등록 완료 버튼
        submit = findViewById(R.id.signupbutton);
        submit.setOnClickListener(v -> {
            tryJoin();
        });
    }

    private void tryJoin() {
        name.setError(null);
        department.setError(null);
        studentNum.setError(null);
        pw.setError(null);
        pw2.setError(null);

        String bodyName = name.getText().toString();
        String bodyDepartment = department.getText().toString();
        String bodyStudentID = studentNum.getText().toString();
        String bodyPassword = pw.getText().toString();
        String bodyPassword2 = pw2.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (bodyName.isEmpty()) {
            name.setError("이름를 입력해주세요.");
            focusView = name;
            cancel = true;
        }

        if (bodyDepartment.isEmpty()) {
            department.setError("학과를 입력해주세요.");
            focusView = department;
            cancel = true;
        }

        if (bodyStudentID.isEmpty()) {
            studentNum.setError("학번를 입력해주세요.");
            focusView = studentNum;
            cancel = true;
        }

        if (bodyPassword.isEmpty()) {
            pw.setError("비밀번호를 입력해주세요.");
            focusView = pw;
            cancel = true;
        }

        if (bodyPassword2.isEmpty()) {
            pw2.setError("비밀번호를 다시 한번 입력해주세요.");
            focusView = pw2;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else
            join(new JoinPut(bodyName, bodyDepartment, bodyStudentID, bodyPassword, bodyPassword2));

    }
    public void join(JoinPut data) {
        apiService.join(data).enqueue(new Callback<JoinGet>() {
            @Override
            public void onResponse(Call<JoinGet> call, Response<JoinGet> response) {
                if(response.isSuccessful()) {
                    JoinGet result = response.body();
                    //Log.v("please", response.body().getErrors());
                    if(result.getSuccess()) {
                        Toast.makeText(SignUp.this, "학생등록 성공!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(SignUp.this, result.getErrors(), Toast.LENGTH_SHORT).show();
                }
                else
                    Log.d("76", "실패");
            }

            @Override
            public void onFailure(Call<JoinGet> call, Throwable t) {
                Toast.makeText(SignUp.this, "학생등록 에러", Toast.LENGTH_SHORT).show();
                Log.d("학생등록 에러 발생", t.getMessage());
            }
        });
    }
}