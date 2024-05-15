package com.example.app3stelle.ui.Menu;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.MainActivity;
import com.example.app3stelle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MenuWindow extends AppCompatActivity {

    private RecyclerView recyclerViewAppetizers;
    private RecyclerView recyclerViewMains;
    private RecyclerView recyclerViewSeconds;
    private RecyclerView recyclerViewDesserts;
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_menu_layout);
        buttonBack = findViewById(R.id.buttonBack);
        recyclerViewAppetizers = findViewById(R.id.StarterList);
        recyclerViewMains = findViewById(R.id.MainsList);
        recyclerViewSeconds = findViewById(R.id.SecondsList);
        recyclerViewDesserts = findViewById(R.id.DeesertsList);

        recyclerViewAppetizers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMains.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSeconds.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDesserts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAppetizers.setNestedScrollingEnabled(false);
        recyclerViewMains.setNestedScrollingEnabled(false);
        recyclerViewSeconds.setNestedScrollingEnabled(false);
        recyclerViewDesserts.setNestedScrollingEnabled(false);

        TextView textViewSecondi = findViewById(R.id.textViewSecondi);
        textViewSecondi.setPaintFlags(textViewSecondi.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        TextView textViewPrimi = findViewById(R.id.textViewPrimi);
        textViewPrimi.setPaintFlags(textViewPrimi.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        TextView textViewAntipasti = findViewById(R.id.textViewAntipasti);
        textViewAntipasti.setPaintFlags(textViewAntipasti.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        TextView textViewDessert = findViewById(R.id.textViewDessert);
        textViewDessert.setPaintFlags(textViewDessert.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ArrayList<Piatto> startersList= new ArrayList<>();
        ArrayList<Piatto> mainPlateList= new ArrayList<>();
        ArrayList<Piatto> secondPlateList= new ArrayList<>();
        ArrayList<Piatto> dessertPlateList= new ArrayList<>();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Menu");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot categoriaSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot piattoSnapshot : categoriaSnapshot.getChildren()) {
                        String plateName = piattoSnapshot.getKey();;
                        double platePrice = piattoSnapshot.getValue(Double.class);
                        Piatto temp = new Piatto(plateName,String.valueOf(platePrice));
                        if(categoriaSnapshot.getKey().equals("Antipasti")){
                            startersList.add(temp);
                        }if(categoriaSnapshot.getKey().equals("Primi")){
                            mainPlateList.add(temp);
                        }if(categoriaSnapshot.getKey().equals("Secondi")){
                            secondPlateList.add(temp);
                        }if(categoriaSnapshot.getKey().equals("Dessert")){
                            dessertPlateList.add(temp);
                        }
                    }
                }
                MenuAdapter adapter = new MenuAdapter(mainPlateList);
                recyclerViewMains.setAdapter(adapter);

                adapter = new MenuAdapter(startersList);
                recyclerViewAppetizers.setAdapter(adapter);

                adapter = new MenuAdapter(secondPlateList);
                recyclerViewSeconds.setAdapter(adapter);

                adapter = new MenuAdapter(dessertPlateList);
                recyclerViewDesserts.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuWindow.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
