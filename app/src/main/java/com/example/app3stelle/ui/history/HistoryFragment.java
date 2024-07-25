package com.example.app3stelle.ui.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.app3stelle.R;
import com.example.app3stelle.databinding.FragmentHistoryBinding;
import com.google.android.material.tabs.TabLayout;


public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Log.d("cacca","cacca");

        TabLayout tabItems = root.findViewById(R.id.tabLayoutCategory2);
        final UmbrellaReservationFragment umbrellaFragment= new UmbrellaReservationFragment();
        UserBarOrderFragment userOrdersFragment = new UserBarOrderFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, userOrdersFragment).commit();

        tabItems.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:replaceCurrentFragment(userOrdersFragment);break;
                    case 1:replaceCurrentFragment(umbrellaFragment);break;
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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void replaceCurrentFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}