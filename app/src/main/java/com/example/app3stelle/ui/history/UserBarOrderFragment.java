package com.example.app3stelle.ui.history;

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
import com.example.app3stelle.ui.Beverage.OrderItem;
import com.example.app3stelle.ui.MySharedData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserBarOrderFragment extends Fragment {
    private MySharedData sharedData = MySharedData.getInstance();
    String idUser = sharedData.getUserId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View UserBarOrderFragment = inflater.inflate(R.layout.fragment_cocktails_layout, container, false);
        ArrayList<RowElement> drinksList = new ArrayList<>();
        RecyclerView drinksRecycleView = UserBarOrderFragment.findViewById(R.id.beerRecycleView);
        DatabaseReference drinkRef = FirebaseDatabase.getInstance().getReference().child("Ordini Drink");
        drinkRef.orderByChild("userId").equalTo(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String description = snapshot.child("descrizione").getValue(String.class);
                    String clientName = snapshot.child("clientName").getValue(String.class);
                    String orderTime = snapshot.child("date").getValue(String.class);
                    Double drinkPrice= snapshot.child("prezzo").getValue(Double.class);
                    int state = snapshot.child("state").getValue(int.class);
                    RowElement temp = new RowElement(clientName,String.valueOf(drinkPrice),description,state,orderTime);
                    drinksList.add(temp);
                }
                RowAdapter adapter = new RowAdapter(drinksList, requireContext());
                drinksRecycleView.setAdapter(adapter);
                drinksRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e("Firebase", "Errore durante il recupero dei dati: " + databaseError.getMessage());
            }
        });

        drinkRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                drinksList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String description = snapshot.child("descrizione").getValue(String.class);
                    String clientName = snapshot.child("clientName").getValue(String.class);
                    int state = snapshot.child("state").getValue(Integer.class);
                    String orderTime = snapshot.child("date").getValue(String.class);
                    Double drinkPrice= snapshot.child("prezzo").getValue(Double.class);
                    RowElement orderTmp = new RowElement(clientName,String.valueOf(drinkPrice),description,state,orderTime);
                    drinksList.add(orderTmp);
                }
                drinksRecycleView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return UserBarOrderFragment;
    }
}
