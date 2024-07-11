package com.example.app3stelle.ui.Beverage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Beer_Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View beerFragment = inflater.inflate(R.layout.fragment_beer_layout, container, false);
        ArrayList<Drink> drinkList = new ArrayList<>();
        RecyclerView beerView = beerFragment.findViewById(R.id.beerRecycleView);
        beerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                DrinkAdapter adapter = new DrinkAdapter(beerFragment.getContext(),drinkList,getParentFragmentManager(),R.id.drawer_layout);
                beerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return beerFragment;
    }
}