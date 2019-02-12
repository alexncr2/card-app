package com.alexandru.nicoara.uptcardapp2.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexandru.nicoara.uptcardapp2.R;
import com.alexandru.nicoara.uptcardapp2.model.Card;
import com.alexandru.nicoara.uptcardapp2.viewmodel.CardViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewCardsFragment extends Fragment {

    private static final String TAG = ViewCardsFragment.class.getSimpleName();

    private CardViewModel cardViewModel;
    private Card helperCard;
    private static int selectedCardId = 0;
    private CardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_cards, container, false);

        //to be moved in fragment
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new CardAdapter();
        recyclerView.setAdapter(adapter);

        cardViewModel = ViewModelProviders.of(this).get(CardViewModel.class);
        cardViewModel.getAllCards().observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(@Nullable List<Card> cards) {
                //update recyclerView
                adapter.submitList(cards);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                cardViewModel.delete(adapter.getCardAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnCardClickListener(new CardAdapter.OnCardClickListener() {
            @Override
            public void onCardClick(Card card) {
                ((CardActivity) getActivity()).setClickedCard(card);
                selectedCardId = card.getId();
                CardFormFragment f = new CardFormFragment();
                CardFormFragment.RECEIVED_REQUEST = CardFormFragment.REQUEST_UPDATE;
                f.sendCardToEdit(card);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, f)
                        .commit();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void insertNewCard(String cardName, String cardNumber) {
        helperCard = new Card(cardName, cardNumber);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                while (cardViewModel == null) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                cardViewModel.insert(helperCard);
            }
        }, 50);
    }

    public void updateCard(String cardName, String cardNumber) {
        helperCard = new Card(cardName, cardNumber);
        helperCard.setId(selectedCardId);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                while (cardViewModel == null) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "Card should update: " + helperCard.getCardNumber() + "//" + helperCard.getId());
                cardViewModel.update(helperCard);
                adapter.notifyDataSetChanged();
            }
        }, 50);
    }
}
