package com.example.app3stelle.ui.history;

import android.util.Log;

import androidx.annotation.NonNull;
import com.example.app3stelle.ui.MySharedData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Random;

public class CouponManager {
    private static CouponManager instance;
    private CouponManager() {
        // Costruttore privato per impedire l'istanziazione diretta
    }

    public static CouponManager getInstance() {
        if (instance == null) {
            instance = new CouponManager();
        }
        return instance;
    }

    public void insertNewCoupon(String idUser,String days) {
        DatabaseReference couponRef = FirebaseDatabase.getInstance().getReference().child("Coupons");
        MySharedData sharedData = MySharedData.getInstance();
        Random random = new Random();

            int nuovaChiaveInt = random.nextInt(90000) + 10000;
            String nuovaChiave = String.valueOf(nuovaChiaveInt);
            couponRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot sottoCategoriaSnapshot : dataSnapshot.getChildren()) {
                        //String sottoCategoriaKey = sottoCategoriaSnapshot.getKey();
                        if (sottoCategoriaSnapshot.hasChild(nuovaChiave)) {
                            sharedData.setFindId(true);
                            insertNewCoupon(idUser,days);
                            break;
                        }
                    }
                    if(!sharedData.getFindId()){
                        DatabaseReference userCouponRef = couponRef.child(idUser);
                        userCouponRef.child(nuovaChiave).setValue(days+" drink");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("TAG", "Errore durante la lettura dei dati: " + databaseError.getMessage());
                }
            });




    }
}
