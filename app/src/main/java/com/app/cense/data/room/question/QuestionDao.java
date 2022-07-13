package com.app.cense.data.room.question;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuestionDao {

    @Query("SELECT * FROM Question")
    List<QuestionEntity> getAll();

    @Insert
    void insert (QuestionEntity question);
}
