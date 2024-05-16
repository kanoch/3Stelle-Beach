package com.example.app3stelle.ui.Drinks;

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

    private ArrayList<Drink> drinkList;
    private Context context;
    private MySharedData sharedData = MySharedData.getInstance();
    private ArrayList<Drink> sharedDrinkList = sharedData.getSharedDrinkMap();
    private FragmentManager fragmentManager;

    public DrinkAdapter(Context context, ArrayList<Drink> drinksList,FragmentManager fragmentManager) {
        this.context = context;
        this.drinkList = drinksList;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drinks_layout3, parent, false);
        return new ViewHolder(view,fragmentManager);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drink drink = drinkList.get(position);
        String drinkPrice = drink.getDrinkPrice()+"€";
        holder.drinkNameTextView.setText(drink.getDrinkName());
        String src= drink.getDrinkName().toLowerCase().replace(' ','_');
        int drawableId = context.getResources().getIdentifier(src, "drawable", context.getPackageName());
        if(drawableId!=0){
            holder.drinkImage.setImageResource(drawableId);
        }
        holder.textViewIncredient.setText(drink.getIngredients());
        holder.textViewPrice.setText(drinkPrice);
        holder.btnAddDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDrinks.newInstance(drink).show(
                        fragmentManager, "dialog");
            }
        });
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

        public ViewHolder(@NonNull View itemView,FragmentManager fragmentManager) {
            super(itemView);
            drinkNameTextView = itemView.findViewById(R.id.drinkNameTextView);
            textViewIncredient = itemView.findViewById(R.id.textViewIncredient);
            textViewPrice = itemView.findViewById(R.id.textViewPriceDrink);
            btnAddDrink = itemView.findViewById(R.id.buttonAddDrink);
            drinkImage = itemView.findViewById(R.id.drinkView);

        }

    }
}