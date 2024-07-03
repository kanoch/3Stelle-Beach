package com.example.app3stelle.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.app3stelle.databinding.FragmentHomeBinding;
import com.example.app3stelle.ui.Beverage.DrinkSelectionWindow;
import com.example.app3stelle.ui.Beverage.provaResoconto;
import com.example.app3stelle.ui.Menu.MenuWindow;
import com.example.app3stelle.ui.UmbrellaBooking;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button provaaa = binding.buttonProvona;
        CardView cardUmbrella = binding.CardLettini;
        CardView cardMenu = binding.CardMenu;
        CardView cardDrink=binding.CardDrink;

        provaaa.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), provaResoconto.class);
            startActivity(intent);
        });

        cardUmbrella.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), UmbrellaBooking.class);
            startActivity(intent);
        });

        cardMenu.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MenuWindow.class);
            startActivity(intent);
        });

        cardDrink.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), DrinkSelectionWindow.class);
            startActivity(intent);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}