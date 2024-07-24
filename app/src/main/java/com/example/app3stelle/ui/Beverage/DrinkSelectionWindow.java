package com.example.app3stelle.ui.Beverage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.app3stelle.MainActivity;
import com.example.app3stelle.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;

public class DrinkSelectionWindow extends AppCompatActivity {
    private Button btnNextPage;
    private ImageButton imgbuttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.drinks_selection_layout);

        btnNextPage = findViewById(R.id.buttonNextDrink);
        imgbuttonBack= findViewById(R.id.imageButtonBackDrink);
        TabLayout tabItems = findViewById(R.id.tabLayoutCategory);
        Cocktails_Fragment cocktailFragment = new Cocktails_Fragment();
        Beer_Fragment exampleFragment = new Beer_Fragment();
        Coffee_Fragment coffeeFragment = new Coffee_Fragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, cocktailFragment).commit();

        tabItems.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:replaceCurrentFragment(cocktailFragment);break;
                    case 1:replaceCurrentFragment(exampleFragment);break;
                    case 2:replaceCurrentFragment(coffeeFragment);break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Evento quando un tab è deselezionato
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Evento quando un tab già selezionato è nuovamente cliccato
            }
        });

        btnNextPage.setOnClickListener(v -> {
            Intent intent = new Intent( DrinkSelectionWindow.this, DrinkCheckout.class);
            startActivity(intent);
        });
        imgbuttonBack.setOnClickListener(v -> {
            Intent intent = new Intent( DrinkSelectionWindow.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void replaceCurrentFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}