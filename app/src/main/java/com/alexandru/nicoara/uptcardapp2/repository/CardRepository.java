package com.alexandru.nicoara.uptcardapp2.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.alexandru.nicoara.uptcardapp2.model.Card;

import java.util.List;

import androidx.lifecycle.LiveData;

public class CardRepository {

    public static final String TAG = CardRepository.class.getSimpleName();

    private CardDao cardDao;
    private LiveData<List<Card>> allCards;

    public CardRepository(Application application){
        CardDatabase database = CardDatabase.getInstance(application);
        cardDao = database.cardDao();

        allCards = cardDao.getAllCards();
    }

    public void insert(Card card){
        new InsertCardAsyncTask(cardDao).execute(card);
    }

    public void update(Card card){
        Log.d(TAG, "Inside CardRepository with: " + card.getCardNumber());
        new UpdateCardAsyncTask(cardDao).execute(card);
    }

    public void delete(Card card){
        new DeleteCardAsyncTask(cardDao).execute(card);
    }

    public void deleteAllCards(){
        new DeleteAllCardsAsyncTask(cardDao).execute();
    }

    public LiveData<List<Card>> getAllCards() {
        return allCards;
    }

    private static class InsertCardAsyncTask extends AsyncTask<Card, Void, Void>{

        private CardDao cardDao;

        private InsertCardAsyncTask(CardDao cardDao){
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
            cardDao.insert(cards[0]);
            return null;
        }
    }

    private static class UpdateCardAsyncTask extends AsyncTask<Card, Void, Void>{

        private CardDao cardDao;

        private UpdateCardAsyncTask(CardDao cardDao){
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
            Log.d(TAG, "Inside UpdateCardAsyncTask with: " + cards[0].getCardNumber());
            cardDao.update(cards[0]);
            return null;
        }
    }

    private static class DeleteCardAsyncTask extends AsyncTask<Card, Void, Void>{

        private CardDao cardDao;

        private DeleteCardAsyncTask(CardDao cardDao){
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
            cardDao.delete(cards[0]);
            return null;
        }
    }

    private static class DeleteAllCardsAsyncTask extends AsyncTask<Void, Void, Void>{

        private CardDao cardDao;

        private DeleteAllCardsAsyncTask(CardDao cardDao){
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cardDao.deteleAllCards();
            return null;
        }
    }
}
