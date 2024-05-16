package com.example.app3stelle.ui.Drinks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.app3stelle.R;
import com.example.app3stelle.ui.MySharedData;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BottomSheetDrinks extends BottomSheetDialogFragment {
    static Drink currentDrink;
    private MySharedData sharedData = MySharedData.getInstance();
    private ArrayList<Drink> sharedDrinkList = sharedData.getSharedDrinkMap();

    public static  BottomSheetDrinks newInstance(Drink currentDrinks) {
        currentDrink = currentDrinks;
        return new  BottomSheetDrinks();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        Button btnGlass = view.findViewById(R.id.btnGlass);
        Button btnCarafe = view.findViewById(R.id.btnCarafe);
        btnGlass.setText("Bicchiere: €"+currentDrink.getDrinkPrice());
        btnCarafe.setText("Caraffa: €"+currentDrink.getCaraffaPrice());

        btnGlass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDrink.setDrinkSize("Singolo");
                sharedDrinkList.add(currentDrink);
                Toast.makeText(view.getContext(), "Drink Aggiunto all' ordine", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });


        btnCarafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDrink.setDrinkSize("Caraffa");
                sharedDrinkList.add(currentDrink);
                Toast.makeText(view.getContext(), "Caraffa Aggiunta all'ordine", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        return view;
    }
}