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

public class Coffee_Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View coffeeFragment = inflater.inflate(R.layout.fragment_coffee_layout, container, false);
        ArrayList<Beverage> coffeeList = new ArrayList<>();
        RecyclerView coffeeView = coffeeFragment.findViewById(R.id.coffeeRecycleView);
        coffeeView.setLayoutManager(new LinearLayoutManager(getContext()));
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Caffetteria");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot coffeeSnapshot : dataSnapshot.getChildren()) {
                    String coffeeName = coffeeSnapshot.getKey();
                    String coffeePrice = String.valueOf(coffeeSnapshot.getValue(Double.class));
                    Beverage temp = new Beverage(coffeeName,coffeePrice,"Tazza");
                    coffeeList.add(temp);
                }
                CoffeeAdapter adapter = new CoffeeAdapter(coffeeFragment.getContext(),coffeeList,getParentFragmentManager(),R.layout.coffee_layout);
                coffeeView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return coffeeFragment;
    }
}