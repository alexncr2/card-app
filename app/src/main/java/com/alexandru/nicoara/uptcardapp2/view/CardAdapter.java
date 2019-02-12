package com.alexandru.nicoara.uptcardapp2.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexandru.nicoara.uptcardapp2.R;
import com.alexandru.nicoara.uptcardapp2.model.Card;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends ListAdapter<Card, CardAdapter.CardHolder> {

    private OnCardClickListener listener;

    public CardAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Card> DIFF_CALLBACK = new DiffUtil.ItemCallback<Card>() {
        @Override
        public boolean areItemsTheSame(@NonNull Card card, @NonNull Card newCard) {
            return card.getId() == newCard.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Card card, @NonNull Card newCard) {
            return card.getCardNumber().equals(newCard.getCardNumber())
                    && card.getCardName().equals(newCard.getCardName());
        }
    };

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);

        return new CardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder cardHolder, int position) {
        Card currentCard = getItem(position);
        cardHolder.textViewCardName.setText(currentCard.getCardName());
        cardHolder.textViewCardNumber.setText(currentCard.getCardNumber());
        //cardHolder.textViewId.setText(String.valueOf(currentCard.getId()));
    }

    public Card getCardAt(int position){
        return getItem(position);
    }

    class CardHolder extends RecyclerView.ViewHolder{

        private TextView textViewCardName;
        private TextView textViewCardNumber;
        private TextView textViewId;

        public CardHolder(@NonNull View itemView) {
            super(itemView);
            textViewCardName = itemView.findViewById(R.id.tv_card_name);
            textViewCardNumber = itemView.findViewById(R.id.tv_card_number);
            // textViewId = itemView.findViewById(R.id.tv_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onCardClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnCardClickListener {
        void onCardClick(Card card);
    }

    public void setOnCardClickListener(OnCardClickListener listener){
        this.listener = listener;
    }
}
