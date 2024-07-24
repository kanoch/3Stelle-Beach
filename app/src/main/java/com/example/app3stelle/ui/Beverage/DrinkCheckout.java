package com.example.app3stelle.ui.Beverage;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.MainActivity;
import com.example.app3stelle.R;
import com.example.app3stelle.ui.MySharedData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DrinkCheckout extends AppCompatActivity {
    private final MySharedData sharedData = MySharedData.getInstance();
    private EditText editTextNumberDelivery;
    private  Button buttonCheckout;
    private ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drink_checkout_layout);

        mainLayout = findViewById(R.id.mainLayout);

        editTextNumberDelivery = findViewById(R.id.editTextNumberDelivery);
        buttonCheckout = findViewById(R.id.buttonCheckout);
        TextView totalTextView = findViewById(R.id.totalTextView);
        ImageButton btnPreviousPage = findViewById(R.id.btnPreviousPage);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        String totaleEuro = sharedData.getTotalCartDrink() +" €";
        totalTextView.setText(totaleEuro);

        DrinkCheckOut_Adapter adapter = new DrinkCheckOut_Adapter(sharedData.getSharedOrderDrinkList(),getApplicationContext());
        recyclerView.setAdapter(adapter);


        btnPreviousPage.setOnClickListener(v -> {
            Intent intent = new Intent( DrinkCheckout.this, DrinkSelectionWindow.class);
            startActivity(intent);
        });

        buttonCheckout.setOnClickListener(v -> showConfirmationDialog());

        editTextNumberDelivery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                hideKeyboard(v);
                return true;
            }
            return false;
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

                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = now.format(formatter);


                    OrderItem itm = new OrderItem(
                            sharedData.getTotalCartDrink(),
                            orderDescription,
                            clientName,
                            sharedData.getUserId(),
                            formattedDateTime
                            );
                    refOrdini.setValue(itm);

                    refOrdini.setValue(itm, (databaseError, databaseReference) -> {
                        sharedData.getSharedOrderDrinkList().clear();
                        if (databaseError != null) {
                            showConfirmationOrderDialog();
                        } else {
                            animateOrder();
                        }
                    });
                })
                .setNegativeButton("Annulla", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showConfirmationOrderDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Errore nell'invio")
                .setPositiveButton("OK", (dialog, id) -> {
                    Intent intent3 = new Intent(DrinkCheckout.this, MainActivity.class);
                    startActivity(intent3);
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void animateOrder() {
        int colorFrom = Color.WHITE;
        int colorTo = Color.GREEN;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(400);
        colorAnimation.addUpdateListener(animator -> mainLayout.setBackgroundColor((int) animator.getAnimatedValue()));
        colorAnimation.start();

        buttonCheckout.postDelayed(() -> {
            for(int i=0;i< mainLayout.getChildCount();i++){
                mainLayout.getChildAt(i).setVisibility(View.GONE);
            }
            TextView finalMessage = new TextView(getApplicationContext());
            finalMessage.setId(View.generateViewId());
            finalMessage.setText(R.string.message_sendedOrder);
            finalMessage.setTextSize(40);
            finalMessage.setTextColor(Color.WHITE);
            finalMessage.setTypeface(Typeface.DEFAULT_BOLD);
            finalMessage.setGravity(View.TEXT_ALIGNMENT_CENTER);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            mainLayout.addView(finalMessage, params);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(mainLayout);

            constraintSet.connect(finalMessage.getId(), ConstraintSet.TOP, mainLayout.getId(), ConstraintSet.TOP, 0);
            constraintSet.connect(finalMessage.getId(), ConstraintSet.BOTTOM, mainLayout.getId(), ConstraintSet.BOTTOM, 0);
            constraintSet.connect(finalMessage.getId(), ConstraintSet.LEFT, mainLayout.getId(), ConstraintSet.LEFT, 0);
            constraintSet.connect(finalMessage.getId(), ConstraintSet.RIGHT, mainLayout.getId(), ConstraintSet.RIGHT, 0);

            constraintSet.applyTo(mainLayout);

            finalMessage.setOnClickListener(v -> {
                Intent intent3 = new Intent(DrinkCheckout.this, MainActivity.class);
                startActivity(intent3);
            });
        }, 200);
    }

}