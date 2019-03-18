package com.example.asus.qunasta.Model;

public class Feedback {
    private String feedback1, feedback2, feedback3;

    public Feedback() {
    }

    public Feedback(String feedback1, String feedback2, String feedback3) {
        this.feedback1 = feedback1;
        this.feedback2 = feedback2;
        this.feedback3 = feedback3;
    }

    public String getFeedback1() {
        return feedback1;
    }

    public void setFeedback1(String feedback1) {
        this.feedback1 = feedback1;
    }

    public String getFeedback2() {
        return feedback2;
    }

    public void setFeedback2(String feedback2) {
        this.feedback2 = feedback2;
    }

    public String getFeedback3() {
        return feedback3;
    }

    public void setFeedback3(String feedback3) {
        this.feedback3 = feedback3;
    }
}
