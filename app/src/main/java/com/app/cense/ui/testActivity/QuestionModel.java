package com.app.cense.ui.testActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionModel {
    public String subjectName;
    public String question;
    public int difficult;
    public int className;
    public Answer correctAnswer;
    public Answer answer1;
    public Answer answer2;
    public Answer answer3;
    public String tip;
    public String tag1;
    public String tag2;
    public String tag3;
    public String tag4;
    public String tag5;

    public QuestionModel(String subjectName,
                         String question,
                         int difficult,
                         int className,
                         String correctAnswer,
                         String answer1,
                         String answer2,
                         String answer3) {
        tag1 = "Вычитание";
        this.subjectName = subjectName;
        this.question = question;
        this.difficult = difficult;
        this.className = className;
        this.correctAnswer = new Answer(correctAnswer, true);
        this.answer1 = new Answer(answer1, false);
        this.answer2 = new Answer(answer2, false);
        this.answer3 = new Answer(answer3, false);
    }

    public QuestionModel(String subjectName,
                         String question,
                         int difficult,
                         int className,
                         Answer correctAnswer,
                         Answer answer1,
                         Answer answer2,
                         Answer answer3,
                         String tip,
                         String tag1,
                         String tag2,
                         String tag3,
                         String tag4,
                         String tag5) {
        this.subjectName = subjectName;
        this.question = question;
        this.difficult = difficult;
        this.className = className;
        this.correctAnswer = correctAnswer;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.tip = tip;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.tag4 = tag4;
        this.tag5 = tag5;
    }

    public QuestionModel() {}

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getDifficult() {
        return difficult;
    }

    public void setDifficult(int difficult) {
        this.difficult = difficult;
    }

    public int getClassName() {
        return className;
    }

    public void setClassName(int className) {
        this.className = className;
    }

    public Answer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Answer getAnswer1() {
        return answer1;
    }

    public void setAnswer1(Answer answer1) {
        this.answer1 = answer1;
    }

    public Answer getAnswer2() {
        return answer2;
    }

    public void setAnswer2(Answer answer2) {
        this.answer2 = answer2;
    }

    public Answer getAnswer3() {
        return answer3;
    }

    public void setAnswer3(Answer answer3) {
        this.answer3 = answer3;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public List<Answer> getRandomAnswers() {
        List<Answer> answers = new ArrayList<>();
        answers.add(correctAnswer);
        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        Collections.shuffle(answers);
        return answers;
    }

    @Override
    public String toString() {
        return "QuestionModel{" +
                "subjectName='" + subjectName + '\'' +
                ", question='" + question + '\'' +
                ", difficult=" + difficult +
                ", className=" + className +
                ", correctAnswer=" + correctAnswer +
                ", answer1=" + answer1 +
                ", answer2=" + answer2 +
                ", answer3=" + answer3 +
                ", tip='" + tip + '\'' +
                ", tag1='" + tag1 + '\'' +
                ", tag2='" + tag2 + '\'' +
                ", tag3='" + tag3 + '\'' +
                ", tag4='" + tag4 + '\'' +
                ", tag5='" + tag5 + '\'' +
                '}';
    }
}
