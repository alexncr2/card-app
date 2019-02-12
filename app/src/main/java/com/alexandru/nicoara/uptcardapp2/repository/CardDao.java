package com.alexandru.nicoara.uptcardapp2.repository;

import com.alexandru.nicoara.uptcardapp2.model.Card;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CardDao {

    @Insert
    void insert(Card card);

    @Update
    void update(Card card);

    @Delete
    void delete(Card card);

    @Query("DELETE FROM card_table")
    void deteleAllCards();

    @Query("SELECT * FROM card_table ORDER BY id ASC")
    LiveData<List<Card>> getAllCards();
}
