package com.app.cense.data.room.testResults;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Statistic")
public class StatisticEntity {
    @NonNull
    @PrimaryKey
    public String date;

    public int averageTime;
    public int rightAnswers;
}
