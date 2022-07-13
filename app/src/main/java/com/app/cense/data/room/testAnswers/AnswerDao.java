package com.app.cense.data.room.testAnswers;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.ArrayList;
import java.util.List;

@Dao
public interface AnswerDao {
    @Query("SELECT * FROM AnswerEntity")
    List<AnswerEntity> getAll();

    @Insert
    void insert(AnswerEntity answer);

    @Update
    void update(AnswerEntity answer);

    @Delete
    void delete(AnswerEntity answer);

    @Query("DELETE FROM AnswerEntity")
    void nukeTable();
}
