package com.app.cense.data.room.question;

import com.app.cense.App;
import com.app.cense.data.room.AppDatabase;
import com.app.cense.ui.testActivity.QuestionModel;

import java.util.ArrayList;
import java.util.List;

public class QuestionHelper {
    AppDatabase db = App.getInstance().getDatabase();
    QuestionDao dao = db.questionDao();

    public void rewriteTable(List<QuestionModel> questionModels){
        System.out.println("rewriteTable");
        QuestionEntity questionEntity = new QuestionEntity();
        Runnable task = () -> {
            for (QuestionModel question: questionModels) {
                questionEntity.subjectName = question.subjectName;
                questionEntity.question = question.question;
                questionEntity.difficult = question.difficult;
                questionEntity.className = question.className;
                questionEntity.correctAnswer = question.correctAnswer;
                questionEntity.answer1 = question.answer1;
                questionEntity.answer2 = question.answer2;
                questionEntity.answer3 = question.answer3;
                questionEntity.tip = question.tip;
                questionEntity.tag1 = question.tag1;
                questionEntity.tag2 = question.tag2;
                questionEntity.tag3 = question.tag3;
                questionEntity.tag4 = question.tag4;
                questionEntity.tag5 = question.tag5;
                dao.insert(questionEntity);
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public List<QuestionModel> getAllQuestions(){
        System.out.println("getAllQuestions");

        List<QuestionModel> questionModels = new ArrayList<>();

        Runnable task = () -> {
            List<QuestionEntity> questionEntityList = dao.getAll();
            for (QuestionEntity questionEntity: questionEntityList) {
                QuestionModel question = new QuestionModel();
                question.subjectName = questionEntity.subjectName;
                question.question = questionEntity.question;
                question.difficult = questionEntity.difficult;
                question.className = questionEntity.className;
                question.correctAnswer = questionEntity.correctAnswer;
                question.answer1 = questionEntity.answer1;
                question.answer2 = questionEntity.answer2;
                question.answer3 = questionEntity.answer3;
                question.tip = questionEntity.tip;
                question.tag1 = questionEntity.tag1;
                question.tag2 = questionEntity.tag2;
                question.tag3 = questionEntity.tag3;
                question.tag4 = questionEntity.tag4;
                question.tag5 = questionEntity.tag5;
                //System.out.println("test getAll "+question.toString());
                questionModels.add(question);
            }
        };
        Thread thread1 = new Thread(task);
        thread1.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return questionModels;
    }
}
