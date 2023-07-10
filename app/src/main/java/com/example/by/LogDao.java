package com.example.by;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LogDao {

    @Insert // 삽입
    void setInsertLog(Log log);

    @Update
        // 수정
    void setUpdateLog(Log log);

    @Delete
        // 삭제
    void setDeleteLog(Log log);

    // 조회 쿼리
    @Query("SELECT * FROM Log") // 쿼리 : 데이터베이스에 요청하는 명령문
    List<Log> getLogAll();

}
