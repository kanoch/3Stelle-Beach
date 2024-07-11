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
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.R;
import com.example.app3stelle.ui.MySharedData;

import java.util.ArrayList;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.ViewHolder> {

    protected ArrayList<Beverage> drinkList;
    protected Context context;
    private final MySharedData sharedData = MySharedData.getInstance();
    protected ArrayList<Beverage> sharedDrinkList = sharedData.getSharedOrderDrinkList();
    protected FragmentManager fragmentManager;
    protected int layoutSelected;

    public DrinkAdapter(Context context, ArrayList<Beverage> drinksList,FragmentManager fragmentManager, int layoutSelected) {
        this.context = context;
        this.drinkList = drinksList;
        this.fragmentManager = fragmentManager;
        this.layoutSelected = layoutSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutSelected, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drink drink = (Drink)drinkList.get(position);
        String drinkPrice = drink.getBeveragePrice()+"€";
        holder.drinkNameTextView.setText(drink.getBeverageName());
        String src= drink.getBeverageName().toLowerCase().replace(' ','_');
        int drawableId = context.getResources().getIdentifier(src, "drawable", context.getPackageName());
        if(drawableId!=0){
            holder.drinkImage.setImageResource(drawableId);
        }
        holder.textViewIncredient.setText(drink.getIngredients());
        holder.textViewPrice.setText(drinkPrice);
        holder.btnAddDrink.setOnClickListener(v -> BottomSheetDrinks.newInstance(drink).show(
                fragmentManager, "dialog"));
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView drinkNameTextView;
        TextView textViewIncredient;
        TextView textViewPrice;
        Button btnAddDrink;
        ImageView drinkImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            drinkNameTextView = itemView.findViewById(R.id.drinkNameTextView);
            textViewIncredient = itemView.findViewById(R.id.textViewIncredient);
            textViewPrice = itemView.findViewById(R.id.textViewPriceDrink);
            btnAddDrink = itemView.findViewById(R.id.buttonAddDrink);
            drinkImage = itemView.findViewById(R.id.drinkImageView);

        }

    }
}