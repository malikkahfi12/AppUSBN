package com.example.asus.qunasta.Common;

import android.os.CountDownTimer;

import com.example.asus.qunasta.Model.Category;
import com.example.asus.qunasta.Model.CurrentQuestion;
import com.example.asus.qunasta.Model.Question;

import java.util.ArrayList;
import java.util.List;

public class Common {

    public static final int TOTAL_TIME = 120*60*1000; // 10 Minutes
    public static Category selectedCategory = new Category();
    public static List<Question>questionList = new ArrayList<>();
    public static List<CurrentQuestion> answerSheetList = new ArrayList<>();
    public static CountDownTimer countDownTimer;
    public static int total_answer_count = 0;
    public static int wrong_answer_count = 0;
    public static int right_answer_count = 0;


    public enum ANSWER_TYPE{
        NO_ANSWER,
        WRONG_ANSWER,
        RIGHT_ANSWER
    }

}
