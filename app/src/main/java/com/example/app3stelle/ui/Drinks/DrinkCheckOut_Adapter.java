package com.example.app3stelle.ui.Drinks;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.R;
import com.example.app3stelle.ui.MySharedData;

import java.util.ArrayList;

public class DrinkCheckOut_Adapter extends RecyclerView.Adapter<DrinkCheckOut_Adapter.ViewHolder> {

    private ArrayList<Drink> drinkList;
    private MySharedData sharedData = MySharedData.getInstance();
    private Context contex;

    public DrinkCheckOut_Adapter(ArrayList<Drink> drinks, Context contex) {
        this.drinkList = drinks;
        this.contex = contex;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drink drink = drinkList.get(position);
        String src= drink.getDrinkName().toLowerCase().replace(' ','_');
        int drawableId = contex.getResources().getIdentifier(src, "drawable", contex.getPackageName());
        holder.imageViewLettino.setImageResource(drawableId);
        holder.textViewNumeroLettino.setText(drink.getDrinkName());
        holder.textViewDataPrenotazione.setText(drink.getIngredients());
        if(drink.getDrinkSize()=="Singolo"){
            String singleDrink = "Drink: "+ drink.getDrinkPrice();
            holder.textViewPrezzo.setText(singleDrink);
        } else {
            String caraffaDrink = "Caraffa: "+ drink.getCaraffaPrice();
            holder.textViewPrezzo.setText(caraffaDrink);
        }
        //holder.textViewPrezzo.setText(drink.getDrinkPrice());

        holder.imageViewCestino.setOnClickListener(v -> {
            drinkList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, drinkList.size());
            Activity activity = (Activity) v.getContext();
            TextView textView = activity.findViewById(R.id.totalTextView);
            textView.setText(String.valueOf(sharedData.getTotalCartDrink()));
        });
    }


    @Override
    public int getItemCount() {
        return drinkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewLettino;
        TextView textViewNumeroLettino;
        TextView textViewDataPrenotazione;
        TextView textViewPrezzo;
        ImageView imageViewCestino;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewLettino = itemView.findViewById(R.id.imageViewLettino);
            textViewNumeroLettino = itemView.findViewById(R.id.textViewNumeroLettino);
            textViewDataPrenotazione = itemView.findViewById(R.id.textViewDataPrenotazione);
            textViewPrezzo = itemView.findViewById(R.id.textViewPrezzo);
            imageViewCestino = itemView.findViewById(R.id.imageViewCestino);
        }
    }
}
