package com.example.asus.qunasta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class EssayActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btn_indonesia, btn_ipa, btn_matematika;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essay);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Prediksi Essay");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_indonesia = (Button)findViewById(R.id.btn_Indonesia);
        btn_ipa = (Button)findViewById(R.id.btn_IPA);
        btn_matematika = (Button)findViewById(R.id.btn_Matematika);
        btn_indonesia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EssayActivity.this, IndonesiaActivity.class);
                startActivity(intent);
            }
        });
        btn_ipa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EssayActivity.this, IpaActivity.class);
                startActivity(intent);
            }
        });
        btn_matematika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EssayActivity.this, MatematikaActivity.class);
                startActivity(intent);
            }
        });
    }
}
