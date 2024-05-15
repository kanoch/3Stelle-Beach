package com.example.app3stelle.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.databinding.FragmentCouponsBinding;
import com.example.app3stelle.ui.Menu.MenuAdapter;
import com.example.app3stelle.ui.Menu.Piatto;
import com.example.app3stelle.ui.MySharedData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private FragmentCouponsBinding binding;
    private MySharedData sharedData = MySharedData.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCouponsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final RecyclerView recyclerViewCoupon = binding.recycleViewCoupon;

        ArrayList<Piatto> couponsList= new ArrayList<>();
        String idUser = sharedData.getUserId();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Coupons").child(idUser);
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot couponSnapshot : dataSnapshot.getChildren()) {
                        String couponId = couponSnapshot.getKey();
                        String couponPrice= couponSnapshot.getValue(String.class);
                        Piatto temp = new Piatto(couponId,couponPrice);
                        couponsList.add(temp);
                    }
                    MenuAdapter adapter = new MenuAdapter(couponsList);
                    recyclerViewCoupon.setAdapter(adapter);
                    recyclerViewCoupon.setLayoutManager(new LinearLayoutManager(requireContext()));
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