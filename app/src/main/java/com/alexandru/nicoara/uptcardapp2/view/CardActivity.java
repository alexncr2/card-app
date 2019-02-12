package com.alexandru.nicoara.uptcardapp2.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.alexandru.nicoara.uptcardapp2.R;
import com.alexandru.nicoara.uptcardapp2.model.Card;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class CardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = CardActivity.class.getSimpleName();
    private DrawerLayout drawer;
    private Card clickedCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        clickedCard = null;

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ViewCardsFragment())
                    .commit();
            // navigationView.setCheckedItem(R.id.menu_view_cards);
        }
    }

    @Override
    public void onBackPressed() {
        hideKeyboard(this);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        hideKeyboard(this);
        switch (menuItem.getItemId()) {
            case R.id.menu_card_form:
                CardFormFragment.RECEIVED_REQUEST = CardFormFragment.REQUEST_ADD;
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new CardFormFragment())
                        .commit();
                break;
            case R.id.menu_card_camera:
                startActivity(new Intent(this, CameraActivity.class));
                break;
            case R.id.menu_view_cards:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ViewCardsFragment())
                        .commit();
                break;
            case R.id.menu_comm_share:
                if (clickedCard != null) {
                    if (clickedCard.getCardNumber().isEmpty()) {
                        Toast.makeText(this, "Empty card value", Toast.LENGTH_SHORT).show();
                    } else {
                        if (clickedCard.getCardNumber().matches("[a-zA-Z0-9]+")) {
                            Intent barcodeIntent = new Intent(this, BarcodeActivity.class);
                            barcodeIntent.putExtra("code_name", clickedCard.getCardName());
                            barcodeIntent.putExtra("code_value", clickedCard.getCardNumber());
                            barcodeIntent.putExtra("code_id", clickedCard.getId());
                            startActivity(barcodeIntent);
                        } else {
                            if (clickedCard.getCardNumber().matches
                                    ("((http|https):\\/\\/)?(www\\.)?([a-zA-Z0-9]+\\.[a-zA-Z]{2,3})(\\/[a-zA-Z0-9=?_+/]*)?")) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickedCard.getCardNumber()));
                                startActivity(browserIntent);
                            }
                        }
                    }
                }
                else{
                    Toast.makeText(this, "Select a card", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_comm_send:
                if (clickedCard != null) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, clickedCard.getCardNumber());
                    sendIntent.setType("text/plain");
                    //sendIntent.setPackage("com.facebook.orca");
                    try {
                        startActivity(sendIntent);
                    }
                    catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(this,"Please Install Facebook Messenger", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        clickedCard = null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setClickedCard(Card card) {
        this.clickedCard = card;
    }

    public Card getClickedCard() {
        return this.clickedCard;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
