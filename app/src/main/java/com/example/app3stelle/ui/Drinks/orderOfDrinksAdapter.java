package com.example.app3stelle.ui.Drinks;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.R;
import com.example.app3stelle.ui.history.RowElement;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class orderOfDrinksAdapter extends RecyclerView.Adapter<orderOfDrinksAdapter.ViewHolder> {

    private ArrayList<RowElement> rowElements;
    private Context contex;
    private printInterface printInterface;

    public orderOfDrinksAdapter(ArrayList<RowElement> rowElements, Context context, printInterface printInterface) {
        this.rowElements = rowElements;
        this.contex = context;
        this.printInterface = printInterface;

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
        String clientName = element.getDestination();
        String drinkList = element.getDrinkList();
        String orderPrice = element.getDrinkPrice()+"€";
        holder.textViewDrinkList.setText(drinkList);
        holder.clientNameTextView.setText(clientName);
        holder.textViewPrice.setText(orderPrice);
        switch (element.getOrderState()){
            case 0:holder.cardViewBox.setCardBackgroundColor(Color.WHITE);break;
            case 1:holder.cardViewBox.setCardBackgroundColor(ContextCompat.getColor(contex,R.color.yellow));break;
            case 2:holder.cardViewBox.setCardBackgroundColor(ContextCompat.getColor(contex,R.color.readyGreen));break;
            default:break;
        }
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
        TextView clientNameTextView;
        TextView textViewDrinkList;
        TextView textViewPrice;
        CardView cardViewBox;
        Context cont;
        orderOfDrinksAdapter adapter;
        public ViewHolder(@NonNull View itemView,Context cont,orderOfDrinksAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            clientNameTextView = itemView.findViewById(R.id.clientNameTextView);
            textViewDrinkList = itemView.findViewById(R.id.textViewDrinkList);
            textViewPrice = itemView.findViewById(R.id.textViewPriceDrink);
            cardViewBox = itemView.findViewById(R.id.cardViewBox);
            this.cont= cont;
            this.adapter = adapter;
        }

        private void showDialog(Context context,String color,String message, int position,int state){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message)
                    .setPositiveButton("Prosegui", (dialog, id) -> {
                        cardViewBox.setCardBackgroundColor(Color.parseColor(color));
                        if(cardViewBox.getCardBackgroundColor().getDefaultColor() == ContextCompat.getColor(cont, R.color.readyGreen))
                            adapter.printInterface.printOrder(position);
                        updateOrder(clientNameTextView.getText().toString(),
                                (textViewDrinkList.getText().toString()),
                                state
                                );
                    })
                    .setNegativeButton("Annulla", (dialog, id) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            //Toast.makeText(cont, clientNameTextView.getText(), Toast.LENGTH_SHORT).show();
            int cardBackgroundColor = cardViewBox.getCardBackgroundColor().getDefaultColor();
            if (position != RecyclerView.NO_POSITION) {
                if(cardBackgroundColor == Color.WHITE){
                    showDialog(cont,"#FFC400", cont.getString(R.string.message_updateOrder),0,1);
                }else if(cardBackgroundColor == ContextCompat.getColor(cont, R.color.yellow)){
                    showDialog(cont,"#76FF03",cont.getString(R.string.message_completeOrder),position,2);
                }else if(cardBackgroundColor == ContextCompat.getColor(cont, R.color.readyGreen)){
                    updateOrder(clientNameTextView.getText().toString(), (textViewDrinkList.getText().toString()), 3);
                    adapter.removeItem(position);
                }
            }
        }

        private void updateOrder(String name,String itemsList,int newState){
            DatabaseReference refOrdini = FirebaseDatabase.getInstance().getReference("Ordini Drink");
            Query query = refOrdini.orderByChild("clientName").equalTo(name);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot data:snapshot.getChildren()){
                        if(data.child("descrizione").getValue(String.class).equals(itemsList)){
                            String key = data.getKey();
                            if(newState==3){
                                refOrdini.child(key).removeValue();
                                break;
                            }
                            refOrdini.child(key).child("state").setValue(newState).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(cont, "Ordine aggiornato", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(cont, "Errore Aggiornamento", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                        

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        }
    }


