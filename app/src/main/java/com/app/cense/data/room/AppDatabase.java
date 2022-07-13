package com.app.cense.data.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.app.cense.data.room.question.QuestionDao;
import com.app.cense.data.room.question.QuestionEntity;

import com.app.cense.data.room.testAnswers.AnswerDao;
import com.app.cense.data.room.testAnswers.AnswerEntity;
import com.app.cense.data.room.testResults.StatisticDao;
import com.app.cense.data.room.testResults.StatisticEntity;

@Database(entities = {QuestionEntity.class, StatisticEntity.class, AnswerEntity.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public abstract QuestionDao questionDao();
    public abstract StatisticDao statisticDao();
    public abstract AnswerDao answerDao();
}
