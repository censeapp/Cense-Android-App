package com.app.cense.data.room.testAnswers;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AnswerEntity {
    public int resolutionTime;
    public String date;
    public Boolean isRight;
    public String option;
    public Integer attempt;
    public String tag1;
    public String tag2;
    public String tag3;
    public String tag4;
    public String tag5;
    public String subject;
    public Integer difficult;
    @PrimaryKey(autoGenerate = true)
    int id;
    public AnswerEntity(Integer resolutionTime,
                        String date, Boolean isRight,
                        String option, Integer attempt, String tag1,
                        String tag2, String tag3,
                        String tag4, String tag5,
                        String subject,
                        Integer difficult) {
        this.resolutionTime = resolutionTime;
        this.date = date;
        this.isRight = isRight;
        this.option = option;
        this.attempt = attempt;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.tag4 = tag4;
        this.tag5 = tag5;
        this.subject = subject;
        this.difficult = difficult;
    }
}
