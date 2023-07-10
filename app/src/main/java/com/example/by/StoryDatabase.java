package com.example.by;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Story.class}, version = 1)
public abstract class StoryDatabase extends RoomDatabase {
    public abstract StoryDao storyDao();
}
