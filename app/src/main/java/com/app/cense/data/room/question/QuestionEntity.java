package com.app.cense.data.room.question;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.app.cense.ui.testActivity.Answer;

@Entity(tableName = "Question")
public class QuestionEntity {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    public String subjectName;
    public String question;
    public int difficult;
    public int className;
    @Embedded(prefix = "correct")
    public Answer correctAnswer;
    @Embedded(prefix = "1")
    public Answer answer1;
    @Embedded(prefix = "2")
    public Answer answer2;
    @Embedded(prefix = "3")
    public Answer answer3;
    public String tip;
    public String tag1;
    public String tag2;
    public String tag3;
    public String tag4;
    public String tag5;
}
