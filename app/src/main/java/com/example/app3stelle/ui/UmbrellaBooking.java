package com.example.app3stelle.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.app3stelle.MainActivity;
import com.example.app3stelle.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UmbrellaBooking extends AppCompatActivity {

    private Button buttonAfternoon;
    private Button buttonMorning;
    private ImageButton buttonBack;
    private Button buttonDaily;
    private Button buttonCheckAvaible;
    private String duration ;
    private CalendarView calendar;
    private String dataSelezionata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.umbrella_book);
        buttonCheckAvaible= findViewById(R.id.buttonAvaible);
        calendar= findViewById(R.id.calendarView);
        calendar.setMinDate(System.currentTimeMillis());
        buttonAfternoon= findViewById(R.id.buttonAfternoon);
        buttonMorning= findViewById(R.id.buttonMorning);
        buttonDaily= findViewById(R.id.buttonDay);
        buttonBack=findViewById(R.id.imageButtonBack);
        addListenerDuration(buttonMorning);
        addListenerDuration(buttonAfternoon);
        addListenerDuration(buttonDaily);
        buttonCheckAvaible.setEnabled(false);

        buttonCheckAvaible.setOnClickListener(v -> {
            Intent intent = new Intent(UmbrellaBooking.this, UmbrellaSelectionMap.class);
            intent.putExtra("umbrella_duration", duration);
            intent.putExtra("umbrella_date", dataSelezionata);
            startActivity(intent);
        });

        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(UmbrellaBooking.this, MainActivity.class);
            startActivity(intent);
        });

        Calendar cal = Calendar.getInstance();
        Date todayDate = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        dataSelezionata = sdf.format(todayDate);
        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> dataSelezionata = dayOfMonth + "-" + (month + 1) + "-" + year);
    }

    private void addListenerDuration(Button btn){
        btn.setOnClickListener(v -> {
            duration =  btn.getText().toString();
            buttonAfternoon.setBackgroundColor(ContextCompat.getColor(UmbrellaBooking.this, R.color.purple_500));
            buttonDaily.setBackgroundColor(ContextCompat.getColor(UmbrellaBooking.this, R.color.purple_500));
            buttonMorning.setBackgroundColor(ContextCompat.getColor(UmbrellaBooking.this, R.color.purple_500));
            btn.setBackgroundColor(ContextCompat.getColor(UmbrellaBooking.this, R.color.green));
            buttonCheckAvaible.setEnabled(true);

        });
    }
}
