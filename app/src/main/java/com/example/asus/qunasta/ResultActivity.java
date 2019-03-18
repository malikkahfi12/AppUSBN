package com.example.asus.qunasta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.qunasta.Common.Common;

public class ResultActivity extends AppCompatActivity {
    private TextView txt_right_answer;
    private TextView txt_result;
    private Button btn_PlayAgain;
    private Button btn_BackCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        txt_right_answer = (TextView)findViewById(R.id.txt_right_answer);
        txt_right_answer.setText(new StringBuilder("").append(Common.total_answer_count).append("/")
        .append(Common.questionList.size()));

        txt_result = (TextView)findViewById(R.id.txt_result);


        float percent = (Common.right_answer_count*100/ Common.questionList.size());
        txt_result.setText(String.valueOf(percent));

        btn_BackCategory = (Button)findViewById(R.id.btn_backCategory);
        btn_BackCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResultActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
