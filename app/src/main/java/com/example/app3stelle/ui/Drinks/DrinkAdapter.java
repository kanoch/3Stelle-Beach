package com.example.app3stelle.ui.Drinks;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.R;
import com.example.app3stelle.ui.MySharedData;

import java.util.ArrayList;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.ViewHolder> {

    private ArrayList<Drink> drinkList;
    private Context context;
    private MySharedData sharedData = MySharedData.getInstance();
    private ArrayList<Drink> sharedDrinkList = sharedData.getSharedDrinkMap();

    public DrinkAdapter(Context context, ArrayList<Drink> drinksList) {
        this.context = context;
        this.drinkList = drinksList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drinks_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drink drink = drinkList.get(position);
        String drinkPrice = "Drink: "+ drink.getDrinkPrice()+"€";
        String caraffaPrice = "Caraffa: "+ drink.getCaraffaPrice()+"€";
        holder.drinkNameTextView.setText(drink.getDrinkName());
        holder.textViewIncredient.setText(drink.getIngredients());
        holder.textViewPrice.setText(drinkPrice);
        holder.textViewCaraffaPrice.setText(caraffaPrice);
        holder.btnAddDrink.setOnClickListener(v -> showSizeDialog(drink));
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView drinkNameTextView;
        TextView textViewIncredient;
        TextView textViewPrice;
        TextView textViewCaraffaPrice;
        Button btnAddDrink;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            drinkNameTextView = itemView.findViewById(R.id.drinkNameTextView);
            textViewIncredient = itemView.findViewById(R.id.textViewIncredient);
            textViewPrice = itemView.findViewById(R.id.textViewPriceDrink);
            btnAddDrink = itemView.findViewById(R.id.buttonAddDrink);
            textViewCaraffaPrice = itemView.findViewById(R.id.textViewPriceCaraffa);
        }
    }

    private void showSizeDialog(Drink drink) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Preferisci un Drink singolo oppure una Caraffa?")
                .setPositiveButton("Drink Singolo", (dialog, id) -> {
                    drink.setDrinkSize("Singolo");
                    sharedDrinkList.add(drink);
                    Toast.makeText(context, "Drink Aggiunto all' ordine", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Caraffa", (dialog, id) ->{
                    drink.setDrinkSize("Caraffa");
                    sharedDrinkList.add(drink);
                    Toast.makeText(context, "Drink Aggiunto all' ordine", Toast.LENGTH_SHORT).show();
                    //dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}