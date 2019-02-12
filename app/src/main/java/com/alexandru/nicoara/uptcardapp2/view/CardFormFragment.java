package com.alexandru.nicoara.uptcardapp2.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandru.nicoara.uptcardapp2.R;
import com.alexandru.nicoara.uptcardapp2.model.Card;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CardFormFragment extends Fragment {

    public static final String TAG = CardFormFragment.class.getSimpleName();
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_UPDATE = 2;
    public static int RECEIVED_REQUEST;


    private EditText editTextCardName;
    private EditText editTextCardNumber;

    private TextView textViewHelperTitle;
    private TextView textViewHelperDescription;

    private Card cardToEdit;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card_form, container, false);
        setHasOptionsMenu(true);

        editTextCardName = view.findViewById(R.id.et_card_name);
        editTextCardNumber = view.findViewById(R.id.et_card_number);

        textViewHelperTitle = view.findViewById(R.id.helper_title);
        textViewHelperDescription = view.findViewById(R.id.helper_description);

        textViewHelperTitle.setText("");
        textViewHelperDescription.setText("");

        Card clickedCard = ((CardActivity) getActivity()).getClickedCard();

        if (clickedCard != null) {
            if (clickedCard.getCardNumber().matches("[a-zA-Z0-9]+")) {
                textViewHelperTitle.setText("Barcode");
                textViewHelperDescription.setText("Select process code from the drawer to generate a barcode for this number");
            } else {
                if (clickedCard.getCardNumber().matches
                        ("((http|https):\\/\\/)?(www\\.)?([a-zA-Z0-9]+\\.[a-zA-Z]{2,3})(\\/[a-zA-Z0-9=?_+/]*)?")) {
                    textViewHelperTitle.setText("Link");
                    textViewHelperDescription.setText("Select process code from the drawer to open this URL in a browser");
                }
            }
        }


        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_card_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CardActivity.hideKeyboard(getActivity());

        switch (item.getItemId()) {
            case R.id.menu_save_card:
                saveCard();
                return true;
            case R.id.menu_close:

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ViewCardsFragment())
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveCard(){
        String cardName = editTextCardName.getText().toString();
        String cardNumber = editTextCardNumber.getText().toString();

        if (cardNumber.trim().isEmpty()){
            Toast.makeText(getContext(), "Please insert card details", Toast.LENGTH_SHORT).show();
            return;
        }

        if (RECEIVED_REQUEST == REQUEST_ADD) {
            Log.d(TAG, "REQUEST_ADD");

            ViewCardsFragment f = new ViewCardsFragment();
            f.insertNewCard(cardName, cardNumber);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, f)
                    .commit();

            Toast.makeText(getContext(), "Card Added", Toast.LENGTH_SHORT).show();
        }
        if (RECEIVED_REQUEST == REQUEST_UPDATE){
            Log.d(TAG, "REQUEST_UPDATE" + cardNumber);

            ViewCardsFragment f = new ViewCardsFragment();
            f.updateCard(cardName, cardNumber); //also pass old card
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, f)
                    .commit();
        }

        editTextCardName.getText().clear();
        editTextCardNumber.getText().clear();
    }

    protected void sendCardToEdit(Card card)
    {
        RECEIVED_REQUEST = REQUEST_UPDATE;

        cardToEdit = new Card(card.getCardName(), card.getCardNumber());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                while (editTextCardName == null || editTextCardNumber == null){
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                editTextCardName.setText(cardToEdit.getCardName());
                editTextCardNumber.setText(cardToEdit.getCardNumber());
            }
        }, 50);

    }
}
