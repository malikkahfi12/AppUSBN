package com.example.asus.qunasta;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asus.qunasta.Adapter.AnswerSheetAdapter;
import com.example.asus.qunasta.Common.Common;
import com.example.asus.qunasta.DBHelper.DBHelper;
import com.example.asus.qunasta.Model.Category;
import com.example.asus.qunasta.Model.CurrentQuestion;
import com.example.asus.qunasta.Model.Question;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private int time_play = Common.TOTAL_TIME;
    private boolean isAnswerModelView = false;
    private ImageView image;
    private RecyclerView answer_sheet_view;
    private AnswerSheetAdapter answerSheetAdapter;

    private TextView txt_right_answer;
    private TextView txt_timer;
    private TextView txt_question_text;
    private Button btnA, btnB, btnC, btnD;
    private FrameLayout layout_image;
    private ProgressBar progressBar;
    private MediaPlayer soundAnswer;
    private CurrentQuestion question_state;
    private List<Question> questionList;
    private Context context;

    private Question question;
    int questionIndex = 0;


    @Override
    protected void onDestroy() {
        if (Common.countDownTimer != null)
            Common.countDownTimer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ujian " + Common.selectedCategory.getName());

        takeQuestion();

        if (Common.questionList.size() > 0) {

            //Show textview Right Answer and Count Timer
            txt_right_answer = (TextView) findViewById(R.id.txt_question_right);
            txt_timer = (TextView) findViewById(R.id.txt_timer);

            txt_timer.setVisibility(View.VISIBLE);
            txt_right_answer.setVisibility(View.VISIBLE);



            txt_right_answer.setText(new StringBuilder(String.format("%d/%d", Common.total_answer_count++, Common.questionList.size())));
            txt_right_answer.setText(new StringBuilder(String.format("%d", Common.total_answer_count))
                    .append("/")
                    .append(String.format("%d", Common.questionList.size())).toString());


            //get Question
            question = Common.questionList.get(questionIndex);

            if (question != null) {
                layout_image = (FrameLayout) findViewById(R.id.layout_image);
                progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                if (question.isImageQuestion()) {
                    ImageView img_question = (ImageView) findViewById(R.id.img_question);
                    Picasso.get().load(question.getQuestionImage()).into(img_question, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(QuizActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    layout_image.setVisibility(View.GONE);


                //View Question
                txt_question_text = (TextView) findViewById(R.id.txt_question_text);
                txt_question_text.setText(question.getQuestionText());
                btnA = (Button) findViewById(R.id.btnA);
                btnA.setText(question.getAnswerA());
                btnB = (Button) findViewById(R.id.btnB);
                btnB.setText(question.getAnswerB());
                btnC = (Button) findViewById(R.id.btnC);
                btnC.setText(question.getAnswerC());
                btnD = (Button) findViewById(R.id.btnD);
                btnD.setText(question.getAnswerD());

                btnA.setOnClickListener(this);
                btnB.setOnClickListener(this);
                btnC.setOnClickListener(this);
                btnD.setOnClickListener(this);


            }
            countTimer();
            answer_sheet_view = (RecyclerView) findViewById(R.id.grid_answer);
            answer_sheet_view.setHasFixedSize(true);
            if (Common.questionList.size() > 5)
                answer_sheet_view.setLayoutManager(new GridLayoutManager(this, Common.questionList.size() / 2));
            answerSheetAdapter = new AnswerSheetAdapter(this, Common.answerSheetList);
            answer_sheet_view.setAdapter(answerSheetAdapter);

        }
    }

    private void resetCountWrong() {
        int count = Common.wrong_answer_count;
        Common.wrong_answer_count = 0;
    }

    private void fisherYatesShuffle() {
        int n = Common.questionList.size();
        for (int i = 0; i < questionIndex; i++){
            int random = i +(int) (Math.random() * (n - 1));
            random = Common.questionList.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_finish_games) {
                new MaterialStyledDialog.Builder(this)
                        .setTitle("Ujian Berakhir")
                        .setIcon(R.drawable.ic_check_white_24dp)
                        .setDescription("Apakah anda yakin ingin mengakhiri Ujian ini ?")
                        .setPositiveText("OKE")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeText("CANCEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        }).show();
        }
        return true;
    }



    private void resetCountAnswer() {
        int count = Common.total_answer_count;
        Common.total_answer_count = 0;
    }

    private void countTimer() {
        if (Common.countDownTimer == null) {
            Common.countDownTimer = new CountDownTimer(Common.TOTAL_TIME, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    txt_timer.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    time_play -= 1000;
                }

                @Override
                public void onFinish() {
                   FinishGames();
                }
            }.start();
        } else {
            Common.countDownTimer.cancel();
            Common.countDownTimer = new CountDownTimer(Common.TOTAL_TIME, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    txt_timer.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    time_play -= 1000;
                }

                @Override
                public void onFinish() {
                   FinishGames();
                }
            }.start();

        }
    }

    private void FinishGames() {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        startActivity(intent);
        finish();
    }

    private void takeQuestion() {
        Common.questionList = DBHelper.getInstance(this).getQuestionByCategory(Common.selectedCategory.getId());
        if (Common.questionList.size() == 0) {
            new MaterialStyledDialog.Builder(this)
                    .setTitle("Oppss ! ")
                    .setIcon(R.drawable.ic_sentiment_dissatisfied_white_24dp)
                    .setDescription("Maaf soal Ujian " + Common.selectedCategory.getName() + " Belum tersedia")
                    .setPositiveText("OK")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
        } else {
            if (Common.answerSheetList.size() > 0)
                Common.answerSheetList.clear();

            for (int i = 0; i < Common.questionList.size(); i++) {
                Common.answerSheetList.add(new CurrentQuestion(i, Common.ANSWER_TYPE.NO_ANSWER));
            }
        }
    }

    @Override
    public void onClick(View v) {
        btnA.setClickable(false);
        btnB.setClickable(false);
        btnC.setClickable(false);
        btnD.setClickable(false);

        if (question.getCorrectAnswer().equals("A")) {
            btnA.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.answerColorRight)));
        } else if (question.getCorrectAnswer().equals("B")) {
            btnB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.answerColorRight)));
        } else if (question.getCorrectAnswer().equals("C")) {
            btnC.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.answerColorRight)));
        } else if (question.getCorrectAnswer().equals("D")) {
            btnD.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.answerColorRight)));
        }

        //Button A
        if (v == btnA) {
            if (question.getCorrectAnswer().equals("A")) {
                //Correct Answer
                btnA.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.answerColorRight)));
                showQuestion();
                soundCorrectAnswer();
                Common.right_answer_count++;

            } else {
                //Wrong Answer
                btnA.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.answerColorWrong)));
                showQuestion();
                soundWrongAnswer();
            }
            //Button B
        } else if (v == btnB) {
            if (question.getCorrectAnswer().equals("B")) {
                //Correct Answer
                btnB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.answerColorRight)));
                showQuestion();
                soundCorrectAnswer();
                Common.right_answer_count++;
            } else {
                //Wrong Answer
                btnB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.answerColorWrong)));
                showQuestion();
                soundWrongAnswer();
            }
            //Button C
        } else if (v == btnC) {
            if (question.getCorrectAnswer().equals("C")) {
                //Correct Answer
                btnC.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.answerColorRight)));
                showQuestion();
                soundCorrectAnswer();
                Common.right_answer_count++;
            } else {
                //Wrong Answer
                btnC.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.answerColorWrong)));
                showQuestion();
                soundWrongAnswer();
            }
        } else if (v == btnD) {
            if (question.getCorrectAnswer().equals("D")) {
                //Correct Answer
                btnD.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.answerColorRight)));
                showQuestion();
                soundCorrectAnswer();
                Common.right_answer_count++;
            } else {
                //Wrong Answer
                btnD.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.answerColorWrong)));
                showQuestion();
                soundWrongAnswer();
            }
        }
    }

    private void showQuestion() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (questionIndex < Common.questionList.size() - 1) {
                    questionIndex++;
                    fisherYatesShuffle();
                    //get Question
                    question = Common.questionList.get(questionIndex);


                    txt_right_answer.setText(new StringBuilder(String.format("%d/%d", Common.total_answer_count++, Common.questionList.size())));
                    txt_right_answer.setText(new StringBuilder(String.format("%d", Common.total_answer_count))
                            .append("/")
                            .append(String.format("%d", Common.questionList.size())).toString());


                    if (question != null) {
                        layout_image = (FrameLayout) findViewById(R.id.layout_image);
                        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                        if (question.isImageQuestion()) {
                            ImageView img_question = (ImageView) findViewById(R.id.img_question);
                            Picasso.get().load(question.getQuestionImage()).into(img_question, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Exception e) {
                                    Toast.makeText(QuizActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else
                            layout_image.setVisibility(View.GONE);
                        }
                        txt_question_text.setText(question.getQuestionText());

                        btnA.setText(question.getAnswerA());
                        btnB.setText(question.getAnswerB());
                        btnC.setText(question.getAnswerC());
                        btnD.setText(question.getAnswerD());

                        btnA.setClickable(true);
                        btnB.setClickable(true);
                        btnC.setClickable(true);
                        btnD.setClickable(true);

                        btnA.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.white)));
                        btnB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.white)));
                        btnC.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.white)));
                        btnD.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.white)));
                    } else
                    {
                        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                        startActivity(intent);
                        finish();
                    }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void soundWrongAnswer() {
        soundAnswer = MediaPlayer.create(this,R.raw.salah);
        soundAnswer.start();
    }


    private void soundCorrectAnswer() {
        soundAnswer = MediaPlayer.create(this,R.raw.true1);
        soundAnswer.start();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "Maaf selama Ujian dimulai anda tidak bisa kembali",
                    Toast.LENGTH_LONG).show();

        return false;
    }
}
