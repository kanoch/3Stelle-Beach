package com.example.app3stelle.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.databinding.FragmentHistoryBinding;
import com.example.app3stelle.ui.MySharedData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private MySharedData sharedData = MySharedData.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final RecyclerView recyclerViewBeverage = binding.recyclerViewBeverage;
        final RecyclerView recyclerViewUmbrella = binding.recyclerViewUmbrella;

        ArrayList<RowElement> drinksList= new ArrayList<>();
        String idUser = sharedData.getUserId();
        DatabaseReference drinkRef = FirebaseDatabase.getInstance().getReference().child("Ordini Drink");
        drinkRef.orderByChild("mail").equalTo(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String description = snapshot.child("descrizione").getValue(String.class);
                    String destination = snapshot.child("destinazione").getValue(String.class);
                    Double drinkPrice= snapshot.child("prezzo").getValue(Double.class);
                    RowElement temp = new RowElement(destination,String.valueOf(drinkPrice),description);
                    drinksList.add(temp);
                }
                RowAdapter adapter = new RowAdapter(drinksList, requireContext());
                recyclerViewBeverage.setAdapter(adapter);
                recyclerViewBeverage.setLayoutManager(new LinearLayoutManager(requireContext()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e("Firebase", "Errore durante il recupero dei dati: " + databaseError.getMessage());
            }
        });

        ArrayList<RowElement> umbrellaList= new ArrayList<>();
        DatabaseReference umbrellaRef = FirebaseDatabase.getInstance().getReference().child("Prenotazioni Lettini");
        umbrellaRef.orderByChild("mail").equalTo(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String dataOrder = snapshot.child("dataPrenotazione").getValue(String.class);
                    String umbrellaNumber = snapshot.child("numeroLettino").getValue(String.class);
                    String umbrellaPeriod = snapshot.child("period").getValue(String.class);
                    String umbrellaPrice= String.valueOf(snapshot.child("prezzo").getValue(Double.class));
                    RowElement temp = new RowElement(dataOrder+"\t"+umbrellaPeriod,umbrellaPrice,umbrellaNumber);
                    umbrellaList.add(temp);
                }
                RowAdapter adapter = new RowAdapter(umbrellaList,requireContext());
                recyclerViewUmbrella.setAdapter(adapter);
                recyclerViewUmbrella.setLayoutManager(new LinearLayoutManager(requireContext()));
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
                    RowElement temp2 = new RowElement("Tutta la Stagione","0",umbrellaNumber);
                    umbrellaList.add(temp2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}