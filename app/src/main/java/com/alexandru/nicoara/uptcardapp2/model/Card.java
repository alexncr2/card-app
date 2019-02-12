package com.alexandru.nicoara.uptcardapp2.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "card_table")
public class Card {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String cardName;

    private String cardNumber;

    public Card(String cardName, String cardNumber) {
        this.cardName = cardName;
        this.cardNumber = cardNumber;
    }

    public int getId() {
        return id;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setId(int id) {
        this.id = id;
    }
}
