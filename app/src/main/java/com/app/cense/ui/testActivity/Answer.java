package com.app.cense.ui.testActivity;

import androidx.room.Ignore;


public class Answer {
    private String answerBody;
    private boolean isCorrect;

    public Answer(String answerBody, boolean isCorrect) {
        this.answerBody = answerBody;
        this.isCorrect = isCorrect;
    }

    public Answer(){

    }

    public String getAnswerBody() {
        return answerBody;
    }

    public void setAnswerBody(String answerBody) {
        this.answerBody = answerBody;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
