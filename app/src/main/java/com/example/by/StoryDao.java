package com.example.by;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StoryDao {

    @Insert
    void setInsetStory(Story story);

    @Update
    void setUpdateStory(Story story);

    @Delete
    void setDeleteStory(Story story);

    // 모든 스토리 조회
    @Query("SELECT * FROM Story")
    List<Story> getStoryAll();

    // 클리어한 스토리 조회
    @Query("SELECT * FROM Story WHERE clear = 1")
    List<Story> getStoryClear();

    // 공개된 스토리 조회
    @Query("SELECT * FROM Story WHERE visible = 1")
    List<Story> getStoryVisible();
}
