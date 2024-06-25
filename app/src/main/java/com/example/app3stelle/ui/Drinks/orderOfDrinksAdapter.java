package com.example.app3stelle.ui.Drinks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.MainActivity;
import com.example.app3stelle.R;
import com.example.app3stelle.ui.MySharedData;
import com.example.app3stelle.ui.history.CouponManager;
import com.example.app3stelle.ui.history.RowAdapter;
import com.example.app3stelle.ui.history.RowElement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class orderOfDrinksAdapter extends RecyclerView.Adapter<orderOfDrinksAdapter.ViewHolder> {

    private ArrayList<RowElement> rowElements;
    private Context contex;

    public orderOfDrinksAdapter(ArrayList<RowElement> rowElements, Context context) {
        this.rowElements = rowElements;
        this.contex = context;

    }

    @NonNull
    @Override
    public orderOfDrinksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drink_history_row, parent, false);
        return new orderOfDrinksAdapter.ViewHolder(view,contex,this);
    }

    @Override
    public void onBindViewHolder(@NonNull orderOfDrinksAdapter.ViewHolder holder, int position) {
        RowElement element = rowElements.get(position);
        String drinkDestination = element.getDestination();
        String drinkList = element.getDrinkList();
        String orderPrice = element.getDrinkPrice()+"€";
        holder.textViewDrinkList.setText(drinkList);
        holder.destinationTextView.setText(drinkDestination);
        holder.textViewPrice.setText(orderPrice);
    }

    public void removeItem(int position) {
        rowElements.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, rowElements.size());
    }

    @Override
    public int getItemCount() {
        return rowElements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView destinationTextView;
        TextView textViewDrinkList;
        TextView textViewPrice;
        CardView cardViewBox;
        Context cont;
        orderOfDrinksAdapter adapter;
        public ViewHolder(@NonNull View itemView,Context cont,orderOfDrinksAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
            textViewDrinkList = itemView.findViewById(R.id.textViewDrinkList);
            textViewPrice = itemView.findViewById(R.id.textViewPriceDrink);
            cardViewBox = itemView.findViewById(R.id.cardViewBox);
            this.cont= cont;
            this.adapter = adapter;
        }

        private void showDialog(Context context,String color){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.message_freeUmbrella)
                    .setPositiveButton("Prosegui", (dialog, id) -> {
                        cardViewBox.setCardBackgroundColor(Color.parseColor(color));
                    })
                    .setNegativeButton("Annulla", (dialog, id) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            int cardBackgroundColor = cardViewBox.getCardBackgroundColor().getDefaultColor();
            if (position != RecyclerView.NO_POSITION) {
                if(cardBackgroundColor == Color.WHITE){
                    showDialog(cont,"#FFC400");
                }else if(cardBackgroundColor == Color.parseColor("#FFC400")){
                    showDialog(cont,"#76FF03");
                }else if(cardBackgroundColor == Color.parseColor("#76FF03")){
                    adapter.removeItem(position);
                }
            }
        }
        }
    }


