package com.alexandru.nicoara.uptcardapp2.viewmodel;

import android.app.Application;
import android.util.Log;

import com.alexandru.nicoara.uptcardapp2.model.Card;
import com.alexandru.nicoara.uptcardapp2.repository.CardRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CardViewModel extends AndroidViewModel {

    public static final String TAG = CardViewModel.class.getSimpleName();

    private CardRepository repository;
    private LiveData<List<Card>> allCards;

    public CardViewModel(@NonNull Application application) {
        super(application);

        repository = new CardRepository(application);
        allCards = repository.getAllCards();
    }

    public void insert(Card card){
        repository.insert(card);
    }

    public void update(Card card){
        Log.d(TAG, "Inside update method with: " + card.getCardNumber());
        repository.update(card);
    }

    public void delete(Card card){
        repository.delete(card);
    }

    public void deleteAllCards(){
        repository.deleteAllCards();
    }

    public LiveData<List<Card>> getAllCards(){
        return allCards;
    }
}
