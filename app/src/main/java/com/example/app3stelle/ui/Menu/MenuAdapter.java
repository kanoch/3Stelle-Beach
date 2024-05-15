package com.example.app3stelle.ui.Menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.R;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private List<Piatto> piatti;

    public MenuAdapter(List<Piatto> piatti) {
        this.piatti = piatti;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_piatto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Piatto piatto = piatti.get(position);
        holder.textViewNome.setText(piatto.getNome());
        holder.textViewPrice.setText(String.valueOf(piatto.getPrice()));
    }

    @Override
    public int getItemCount() {
        return piatti.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNome;
        public TextView textViewPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.textViewPlateName);
            textViewPrice = itemView.findViewById(R.id.textViewPlatePrice);
        }
    }
}
