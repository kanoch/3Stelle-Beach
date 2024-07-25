package com.example.app3stelle.ui.Beverage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.app3stelle.R;

import java.util.ArrayList;

public class CoffeeAdapter extends DrinkAdapter{
    public CoffeeAdapter(Context context, ArrayList<Beverage> drinksList, FragmentManager fragmentManager, int layoutSelected) {
        super(context, drinksList, fragmentManager, layoutSelected);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutSelected, parent, false);
        return new ViewHolderCoffee(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Beverage selectedCoffee = super.drinkList.get(position);
        String drinkPrice = selectedCoffee.getBeveragePrice()+"€";
        if (holder instanceof ViewHolderCoffee) {
            ViewHolderCoffee customHolder = (ViewHolderCoffee) holder;

            customHolder.coffeeNameTextView.setText(selectedCoffee.getBeverageName());
            String src = selectedCoffee.getBeverageName().toLowerCase().replace(' ', '_');
            int drawableId = context.getResources().getIdentifier(src, "drawable", context.getPackageName());

            if (drawableId != 0) {
                customHolder.coffeeImage.setImageResource(drawableId);
            } else {
                customHolder.coffeeImage.setImageResource(R.drawable.caffe);
            }
            customHolder.textViewPrice.setText(drinkPrice);
            customHolder.btnAddCoffee.setOnClickListener(v -> {
                Beverage tempCoffee = new Beverage(selectedCoffee.getBeverageName(),
                        selectedCoffee.getBeveragePrice(),
                        "Tazza");
                CoffeeAdapter.super.sharedDrinkList.add(tempCoffee);
            });
        }
    }

    public class ViewHolderCoffee extends ViewHolder {
        TextView coffeeNameTextView;
        TextView textViewPrice;
        Button btnAddCoffee;
        ImageView coffeeImage;

        public ViewHolderCoffee(@NonNull View itemView) {
            super(itemView);
            coffeeNameTextView = itemView.findViewById(R.id.coffeeNameTextView);
            textViewPrice = itemView.findViewById(R.id.textViewPriceCoffee);
            btnAddCoffee = itemView.findViewById(R.id.buttonAddCoffee);
            coffeeImage = itemView.findViewById(R.id.coffeeImageView);

        }

    }
}
