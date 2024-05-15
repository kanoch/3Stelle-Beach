package com.example.app3stelle.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.MainActivity;
import com.example.app3stelle.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UmbrellaCartSummary extends AppCompatActivity {
    private MySharedData sharedData = MySharedData.getInstance();
    private TextView totalTextView;
    private ArrayList<Prenotazione> prenotazioni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.umbrella_summary);

        totalTextView = findViewById(R.id.totalTextView);
        Button btnCheckOut = findViewById(R.id.buttonCheckout);
        ImageButton btnPreviousPage = findViewById(R.id.btnPreviousPage);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalTextView.setText(sharedData.getTotalUmbrellaCart()+"€");

        Intent intent = getIntent();
        if (intent != null) {
            prenotazioni = (ArrayList<Prenotazione>) intent.getSerializableExtra("umbrella_list");
            CustomAdapter adapter = new CustomAdapter(prenotazioni);
            recyclerView.setAdapter(adapter);
        }

        btnPreviousPage.setOnClickListener(v -> {
            Intent intent1 = new Intent( UmbrellaCartSummary.this,UmbrellaBooking.class);
            startActivity(intent1);
        });

        btnCheckOut.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference refOrdini = database.getReference("Prenotazioni Lettini");

            for (Prenotazione prenotazione : prenotazioni) {
                DatabaseReference newRef = refOrdini.push();
                newRef.setValue(prenotazione);
            }

            Intent intent1 = new Intent(UmbrellaCartSummary.this, MainActivity.class);
            startActivity(intent1);
        });
    }
}
