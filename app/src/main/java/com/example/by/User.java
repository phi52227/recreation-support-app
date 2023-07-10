package com.example.by;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id = 0; //하나의 사용자에 대한 고유 ID 값

    private String gold;

    private String point;

    // getter & setter 가져오거나 세팅을 하기 위한 준비 단계

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
