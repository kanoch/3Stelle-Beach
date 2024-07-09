package com.example.app3stelle.ui.Beverage;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
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
                            animateOrder();
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

    private void animateOrder() {
        int colorFrom = Color.WHITE;
        int colorTo = Color.GREEN;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(400);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mainLayout.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();

        buttonCheckout.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i< mainLayout.getChildCount();i++){
                    mainLayout.getChildAt(i).setVisibility(View.GONE);
                }
                TextView finalMessage = new TextView(getApplicationContext());
                finalMessage.setId(View.generateViewId());
                finalMessage.setText("Ordine Inviato");
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

                finalMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent3 = new Intent(DrinkCheckout.this, MainActivity.class);
                        startActivity(intent3);
                    }
                });
            }
        }, 200);
    }

}