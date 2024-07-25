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
import com.example.app3stelle.ui.MySharedData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UmbrellaReservationFragment extends Fragment {
    private MySharedData sharedData = MySharedData.getInstance();
    String idUser = sharedData.getUserId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View umbrellaFragment = inflater.inflate(R.layout.fragment_cocktails_layout, container, false);
        ArrayList<RowElement> umbrellaList = new ArrayList<>();
        RecyclerView umbrellaView = umbrellaFragment.findViewById(R.id.beerRecycleView);
        umbrellaView.setLayoutManager(new LinearLayoutManager(getContext()));
        DatabaseReference umbrellaRef = FirebaseDatabase.getInstance().getReference().child("Prenotazioni Lettini");
        umbrellaRef.orderByChild("mail").equalTo(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String dataOrder = snapshot.child("dataPrenotazione").getValue(String.class);
                    String umbrellaNumber = snapshot.child("numeroLettino").getValue(String.class);
                    String umbrellaPeriod = snapshot.child("period").getValue(String.class);
                    String umbrellaPrice= String.valueOf(snapshot.child("prezzo").getValue(Double.class));
                    RowElement temp = new RowElement(dataOrder+"\t"+umbrellaPeriod,umbrellaPrice,umbrellaNumber,0,"");
                    umbrellaList.add(temp);
                }
                RowAdapter adapter = new RowAdapter(umbrellaList,requireContext());
                umbrellaView.setAdapter(adapter);
                umbrellaView.setLayoutManager(new LinearLayoutManager(requireContext()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        DatabaseReference seasonalRef = FirebaseDatabase.getInstance().getReference().child("Lettini");
        seasonalRef.orderByChild("seasonal").equalTo(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String umbrellaNumber = snapshot.getKey();
                    RowElement temp2 = new RowElement("Tutta la Stagione","0",umbrellaNumber,0,"");
                    umbrellaList.add(temp2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }


    });
        return umbrellaFragment;
}}
