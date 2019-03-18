package com.example.asus.qunasta.Model;

import com.example.asus.qunasta.Common.Common;

public class CurrentQuestion {
    private int questionIndex;
    private Common.ANSWER_TYPE type;

    public CurrentQuestion(int questionIndex, Common.ANSWER_TYPE type) {
        this.questionIndex = questionIndex;
        this.type = type;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }

    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
    }

    public Common.ANSWER_TYPE getType() {
        return type;
    }

    public void setType(Common.ANSWER_TYPE type) {
        this.type = type;
    }
}