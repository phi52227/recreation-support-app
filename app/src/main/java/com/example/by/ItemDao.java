package com.example.by;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object
 */

@Dao
public interface ItemDao {

    @Insert // 삽입
    void setInsertUser(Item item);

    @Update // 수정
    void setUpdateUser(Item item);

    @Delete // 삭제
    void setDeleteUser(Item item);

    // 조회 쿼리
    @Query("SELECT * FROM Item WHERE visible = 1") // 쿼리 : 데이터베이스에 요청하는 명령문
    List<Item> getItemStore();

    // 모든 아이템 조회
    @Query("SELECT * FROM Item")
    List<Item> getItemAll();

    // 인벤토리 쿼리
    @Query("SELECT * FROM ITEM WHERE qty > 0")
    List<Item> getItemInventory();

    // 인벤토리 쿼리 정렬
    @Query("SELECT * FROM ITEM WHERE qty > 0 ORDER BY name asc")
    List<Item> getItemInventoryAsc();

    // Invisible 쿼리
    @Query("SELECT * FROM ITEM WHERE visible = 0")
    List<Item> getItemInvisible();

    // 모든 아이템 조회 정렬
    @Query("SELECT * FROM Item WHERE visible = 1 ORDER BY name asc")
    List<Item> getItemAsc();
}
