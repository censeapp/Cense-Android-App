package com.app.cense.data.room.testResults;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StatisticDao {

    @Query("SELECT * FROM Statistic")
    List<StatisticEntity> getAll();

    @Insert
    void insert(StatisticEntity statistic);

    @Update
    void update(StatisticEntity statistic);

    @Delete
    void delete(StatisticEntity statistic);

    @Query("DELETE FROM Statistic")
    void nukeTable();
}
