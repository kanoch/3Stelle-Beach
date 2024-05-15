package com.example.app3stelle.ui;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<Prenotazione> prenotazioni;
    private MySharedData sharedData = MySharedData.getInstance();
    private HashMap<String,Double> sharedCartHashMap = sharedData.getSharedUmbrellaCartList();

    public CustomAdapter(ArrayList<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Prenotazione prenotazione = prenotazioni.get(position);
        holder.imageViewLettino.setImageResource(R.drawable.lettino);
        String dataReservation ="Data: "+ prenotazione.getDataPrenotazione()+", "+prenotazione.getPeriod();
        String umbrellaNumber = "Ombrellone Numero: "+prenotazione.getNumeroLettino().substring(1);
        String umbrellaPrice = prenotazione.getPrezzo()+"€";
        holder.textViewNumeroLettino.setText(umbrellaNumber);
        holder.textViewDataPrenotazione.setText(dataReservation);
        holder.textViewPrezzo.setText(umbrellaPrice);

        holder.imageViewCestino.setOnClickListener(v -> {
            sharedCartHashMap.remove(prenotazioni.get(position).getNumeroLettino());
            prenotazioni.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, prenotazioni.size());
            Activity activity = (Activity) v.getContext();
            TextView textView = activity.findViewById(R.id.totalTextView);
            textView.setText(sharedData.getTotalUmbrellaCart()+"€");
        });
    }

    @Override
    public int getItemCount() {
        return prenotazioni.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

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