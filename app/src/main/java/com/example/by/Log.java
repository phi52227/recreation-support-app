package com.example.by;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Log {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    private String name;

    private String time;

    private String cur_value;

    private int result;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCur_value() {
        return cur_value;
    }

    public void setCur_value(String cur_value) {
        this.cur_value = cur_value;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
