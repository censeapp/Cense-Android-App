package com.app.cense.data.room.testAnswers;

import com.app.cense.App;
import com.app.cense.data.room.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class AnswerHelper {
    AppDatabase db = App.getInstance().getDatabase();
    AnswerDao dao = db.answerDao();
    public void insertAll(ArrayList<AnswerEntity> answers){
        Runnable task = () -> {
            for (AnswerEntity i : answers){
                dao.insert(i);
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
    public void deleteAll(){
        Runnable task = () -> {
            dao.nukeTable();
        };
        Thread thread1 = new Thread(task);
        thread1.start();
    }
    public List<AnswerEntity> getAll(){
        List<AnswerEntity> list = new ArrayList<>();
        Runnable task = () -> {
            list.addAll(dao.getAll());
        };
        Thread thread1 = new Thread(task);
        thread1.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }
}
