package com.example.by;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Item.class}, version = 1)
public abstract class ItemDatabase extends RoomDatabase {

    public abstract ItemDao itemDao();

}
