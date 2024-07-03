package com.example.app3stelle.ui.Beverage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.MainActivity;
import com.example.app3stelle.R;
import com.example.app3stelle.ui.MySharedData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DrinkCheckout extends AppCompatActivity {
    private MySharedData sharedData = MySharedData.getInstance();
    private EditText editTextNumberDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drink_checkout_layout);

        editTextNumberDelivery = findViewById(R.id.editTextNumberDelivery);
        Button buttonCheckout = findViewById(R.id.buttonCheckout);
        TextView totalTextView = findViewById(R.id.totalTextView);
        ImageButton btnPreviousPage = findViewById(R.id.btnPreviousPage);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String totaleEuro = sharedData.getTotalCartDrink() +" €";
        totalTextView.setText(totaleEuro);

        DrinkCheckOut_Adapter adapter = new DrinkCheckOut_Adapter(sharedData.getSharedOrderDrinkList(),getApplicationContext());
        recyclerView.setAdapter(adapter);


        btnPreviousPage.setOnClickListener(v -> {
            Intent intent = new Intent( DrinkCheckout.this, DrinkSelectionWindow.class);
            startActivity(intent);
        });

        buttonCheckout.setOnClickListener(v -> showConfirmationDialog());

        editTextNumberDelivery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    hideKeyboard(v);
                    return true;
                }
                return false;
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Vuoi inviare l'ordine al bar? \n"+"Potrai ritirarlo con il nome: "+editTextNumberDelivery.getText())
                .setPositiveButton("Conferma", (dialog, id) -> {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference refOrdini = database.getReference("Ordini Drink").push();
                    String orderDescription = sharedData.getFullOrderDescription();
                    String clientName = editTextNumberDelivery.getText().toString();

                    OrderItem itm = new OrderItem(
                            sharedData.getTotalCartDrink(),
                            orderDescription,
                            clientName,
                            sharedData.getUserId());
                    refOrdini.setValue(itm);

                    refOrdini.setValue(itm, (databaseError, databaseReference) -> {
                        sharedData.getSharedOrderDrinkList().clear();
                        if (databaseError != null) {
                            showConfirmationOrderDialog("Errore nell'invio");
                        } else {
                            showConfirmationOrderDialog("Ordine Inviato");
                        }
                    });
                })
                .setNegativeButton("Annulla", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showConfirmationOrderDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, id) -> {
                    Intent intent3 = new Intent(DrinkCheckout.this, MainActivity.class);
                    startActivity(intent3);
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}