package com.app.cense.data.room.testResults;

import com.app.cense.App;
import com.app.cense.data.room.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class StatisticHelper {
    AppDatabase db = App.getInstance().getDatabase();
    StatisticDao dao = db.statisticDao();

    public void insertStatistic(TestData data){
        StatisticEntity statisticEntity = new StatisticEntity();
        Runnable task = () -> {
            statisticEntity.date = data.date;
            statisticEntity.averageTime = data.averageTime;
            statisticEntity.rightAnswers = data.rightAnswers;
            dao.insert(statisticEntity);
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

    public List<TestData> getAllStatistic(){
        List<TestData> testDataList = new ArrayList<>();

        Runnable task = () -> {
            List<StatisticEntity> statisticEntityList = dao.getAll();
            for (StatisticEntity statisticEntity: statisticEntityList){
                TestData testData = new TestData();
                testData.date = statisticEntity.date;
                testData.averageTime = statisticEntity.averageTime;
                testData.rightAnswers = statisticEntity.rightAnswers;
                testDataList.add(testData);
            }
        };

        Thread thread1 = new Thread(task);
        thread1.start();

        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return testDataList;
    }
}
