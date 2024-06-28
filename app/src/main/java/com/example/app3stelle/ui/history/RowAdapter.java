package com.example.app3stelle.ui.history;

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

public class RowAdapter extends RecyclerView.Adapter<RowAdapter.ViewHolder> {

    private ArrayList<RowElement> rowElements;
    private Context contex;

    public RowAdapter(ArrayList<RowElement> rowElements, Context context) {
        this.rowElements = rowElements;
        this.contex = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drink_history_row, parent, false);
        return new ViewHolder(view,contex);
    }

    @Override
    public void onBindViewHolder(@NonNull RowAdapter.ViewHolder holder, int position) {
        RowElement element = rowElements.get(position);
        String drinkDestination = element.getDestination();
        String drinkList = element.getDrinkList();
        String orderPrice = element.getDrinkPrice()+"€";
        holder.textViewDrinkList.setText(drinkList);
        holder.destinationTextView.setText(drinkDestination);
        holder.textViewPrice.setText(orderPrice);
        switch (element.getOrderState()){
            case 0:holder.cardViewContainer.setCardBackgroundColor(Color.WHITE);break;
            case 1:holder.cardViewContainer.setCardBackgroundColor(ContextCompat.getColor(contex,R.color.yellow));break;
            case 2:holder.cardViewContainer.setCardBackgroundColor(ContextCompat.getColor(contex,R.color.readyGreen));break;
        }

    }

    @Override
    public int getItemCount() {
        return rowElements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        TextView destinationTextView;
        TextView textViewDrinkList;
        TextView textViewPrice;
        CardView cardViewContainer;
        Context cont;
        DatePickerDialog datePickerDialog;
        static int y, d, m;
        private Calendar calendar, startCalendar,endCalendar;
        private String startingDate, endingDate;
        String umbrellaId;
        boolean result=false;

        public ViewHolder(@NonNull View itemView,Context cont) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            destinationTextView = itemView.findViewById(R.id.clientNameTextView);
            textViewDrinkList = itemView.findViewById(R.id.textViewDrinkList);
            textViewPrice = itemView.findViewById(R.id.textViewPriceDrink);
            cardViewContainer = itemView.findViewById(R.id.cardViewBox);
            this.cont= cont;
            calendar = Calendar.getInstance();
            y= calendar.get(Calendar.YEAR);
            d= calendar.get(Calendar.DAY_OF_MONTH);
            m= calendar.get(Calendar.MONTH);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if(destinationTextView.getText().toString().equals("Tutta la Stagione")){
                    umbrellaId = textViewDrinkList.getText().toString();
                    calendar = Calendar.getInstance();
                    showDialog(cont);
                }
            }
            return false;
        }

        private void showDialog(Context context){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.message_freeUmbrella)
                    .setPositiveButton("Prosegui", (dialog, id) -> {
                        showDatePickerDialog();
                        datePickerDialog.show();
                    })
                    .setNegativeButton("Annulla", (dialog, id) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        private void showDatePickerDialog() {
            datePickerDialog = new DatePickerDialog(cont, (view, year, month, day) -> {
                startCalendar = Calendar.getInstance();
                startCalendar.set(year, month, day);
                startingDate = day + "/" + (month + 1) + "/" + year;
                showSecondDatePickerDialog();
            }, y, m, d);

            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            long maxDate = calendar.getTimeInMillis();
            datePickerDialog.getDatePicker().setMaxDate(maxDate);
        }
        private void showSecondDatePickerDialog() {
            final Calendar currentDate = Calendar.getInstance();
            int year = currentDate.get(Calendar.YEAR);
            int month = currentDate.get(Calendar.MONTH);
            int day = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(cont,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        endCalendar = Calendar.getInstance();
                        endCalendar.set(year1, monthOfYear, dayOfMonth);
                        endingDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        calculateDateDifference(startCalendar, endCalendar);
                    }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(startCalendar.getTimeInMillis());
            long maxDate = calendar.getTimeInMillis();
            datePickerDialog.getDatePicker().setMaxDate(maxDate);
            datePickerDialog.show();
        }
        private void calculateDateDifference(Calendar startCalendar, Calendar endCalendar) {
            List<String> datesInRange = new ArrayList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Calendar calendarClone = (Calendar) startCalendar.clone();
            while (calendarClone.before(endCalendar) || calendarClone.equals(endCalendar)) {
                datesInRange.add(dateFormat.format(calendarClone.getTime()));
                calendarClone.add(Calendar.DATE, 1);
            }
            MySharedData sharedData  = MySharedData.getInstance();
            AlertDialog.Builder builder2 = new AlertDialog.Builder(cont);
            builder2.setMessage("Confermare? Una volta inviato non sarà possibile utilizzare il proprio ombrellone nelle date selezionate "+
                            startingDate+" - "+endingDate+" (compreso)" )
                    .setPositiveButton("Accetto", (dialog, id) -> {
                        DatabaseReference lettiniRef = FirebaseDatabase.getInstance()
                                .getReference().child("Lettini").child(umbrellaId).child("freeDays");

                        Map<String, Object> updates = new HashMap<>();
                        CompletableFuture<Void> lettinoPromise = new CompletableFuture<>();
                        DatabaseReference lettiniRefi = FirebaseDatabase.getInstance()
                                .getReference().child("Lettini").child(umbrellaId);
                        lettiniRefi.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot umbrellaSnapshot : snapshot.getChildren()) {
                                    if(umbrellaSnapshot.getKey().equals("freeDays")){
                                        datesInRange.forEach(elem->{
                                            if(umbrellaSnapshot.hasChild(elem)){
                                                result=true;
                                            }
                                        });
                                    }
                                }
                                lettinoPromise.complete(null);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        lettinoPromise.thenRun(() -> {
                            if(result){
                                Toast.makeText(cont, "Sembra che tu abbia scelto dei giorni già usati in passato", Toast.LENGTH_LONG).show();
                            }else {
                                datesInRange.forEach(day->{
                                    updates.put(day, true);
                                    lettiniRef.updateChildren(updates)
                                            .addOnSuccessListener(aVoid -> {
                                                Intent intent = new Intent( cont, MainActivity.class);
                                                cont.startActivity(intent);
                                            })
                                            .addOnFailureListener(e -> Log.e("TAG", "Errore durante l'aggiornamento: " + e.getMessage()));
                                });
                                CouponManager copn=CouponManager.getInstance();
                                copn.insertNewCoupon(sharedData.getUserId(),String.valueOf(datesInRange.size()));
                                Toast.makeText(cont, "Messaggio Inviato, controlla i tuoi Coupons", Toast.LENGTH_LONG).show();
                            }
                        });
                    })
                    .setNegativeButton("Annulla", (dialog, id) -> {
                        calendar = Calendar.getInstance();
                        dialog.dismiss();
                    });
            AlertDialog dialog = builder2.create();
            dialog.show();
        }
    }

}
