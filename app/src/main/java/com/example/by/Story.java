package com.example.by;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Story {
    @PrimaryKey(autoGenerate = true)
    private int id = 0; //하나의 사용자에 대한 고유 ID 값

    private String name;

    public String getSubMame() {
        return subMame;
    }

    public void setSubMame(String subMame) {
        this.subMame = subMame;
    }

    private String subMame;

    private int Point;

    private int Clear;

    private int visible;

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

    public int getPoint() {
        return Point;
    }

    public void setPoint(int point) {
        Point = point;
    }

    public int getClear() {
        return Clear;
    }

    public void setClear(int clear) {
        Clear = clear;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }
}
