package com.example.assertqr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class RequestBuilding extends AppCompatActivity {
    EditText name,studentNum,department;
    Button back, submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_building);

        //기입 정보
        name = findViewById(R.id.name);
        studentNum=findViewById(R.id.studentNum);
        department=findViewById(R.id.department);



        //이전 버튼
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed() );

        //신청 건물 콤보박스
        Spinner buildingSpinner = (Spinner)findViewById(R.id.buildingBox);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.buildingList, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingSpinner.setAdapter(adapter);



        //신청 완료 버튼
        submit = findViewById(R.id.requestButton);
        submit.setOnClickListener(v -> {
            Intent intent = new Intent(this, Main.class);
            Toast.makeText(getApplicationContext(), "신청이 완료되었습니다!", Toast.LENGTH_LONG).show();
            startActivity(intent);
        });



    }
}