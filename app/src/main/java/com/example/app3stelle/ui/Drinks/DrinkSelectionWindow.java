package com.example.app3stelle.ui.Drinks;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.MainActivity;
import com.example.app3stelle.R;
import java.util.ArrayList;

import com.example.app3stelle.ui.MySharedData;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DrinkSelectionWindow extends AppCompatActivity {
    private Button btnNextPage;
    private ImageButton imgbuttonBack;
    private ArrayList<Drink> drinkList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.drinks_selection_layout);

        btnNextPage = findViewById(R.id.buttonNextDrink);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewDrinks);
        imgbuttonBack= findViewById(R.id.imageButtonBackDrink);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Drinks");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot drinkSnapshot : dataSnapshot.getChildren()) {
                    String drinkName = drinkSnapshot.getKey();
                    String drinkPrice = String.valueOf(drinkSnapshot.child("prezzo").getValue(Double.class));
                    String drinkIngredients = drinkSnapshot.child("ingredienti").getValue(String.class);
                    String drinkPriceCaraffa = String.valueOf(drinkSnapshot.child("caraffa").getValue(Double.class));
                    Drink temp = new Drink(drinkName,drinkPrice,drinkIngredients,drinkPriceCaraffa);
                    drinkList.add(temp);
                }
                DrinkAdapter adapter = new DrinkAdapter(DrinkSelectionWindow.this,drinkList,getSupportFragmentManager());
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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


}