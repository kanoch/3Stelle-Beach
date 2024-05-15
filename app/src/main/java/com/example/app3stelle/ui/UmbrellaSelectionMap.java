package com.example.app3stelle.ui;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app3stelle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class UmbrellaSelectionMap extends AppCompatActivity {
    private WebView webView;
    private ArrayList<Prenotazione> selected_umbrellas;
    private String umbrella_time;
    private String umbrella_date;
    private Button btnNext;
    private MySharedData sharedData = MySharedData.getInstance();
    private HashMap<String,Double> sharedCartHashMap;
    private ArrayList<String> lettiniCodiciList= new ArrayList<>();
    private HashMap<String,String> umbrellaPriceHashMap= new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.umbrella_map);

        sharedCartHashMap = sharedData.getSharedUmbrellaCartList();
        selected_umbrellas = new ArrayList<>();
        btnNext = findViewById(R.id.buttonProsegui);
        webView = findViewById(R.id.umbrellaView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(), "Android");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);


        Intent intent = getIntent();
        if (intent != null) {
            umbrella_time = intent.getStringExtra("umbrella_duration");
            umbrella_date = intent.getStringExtra("umbrella_date");
        }

        btnNext.setOnClickListener(v -> {
            Intent intent1 = new Intent(UmbrellaSelectionMap.this, UmbrellaCartSummary.class);
            intent1.putExtra("umbrella_list", selected_umbrellas);
            startActivity(intent1);
        });
        webView.loadUrl("file:///android_asset/spiaggia.html");
    }

    public class WebAppInterface {
        @JavascriptInterface
        public void insertSelectedUmbrella(String umbrellaId,String price,String umbrellaQuantity) {
            String totalString=price;
            if(umbrellaQuantity.equals("3")){
                int quantity = Integer.parseInt(umbrellaQuantity);
                double totalPrice=Double.parseDouble(price)+(quantity*3);
                totalString = String.valueOf(totalPrice);
            }
            final String totalFinal=totalString;
            webView.post(() -> {
                Prenotazione temp = new Prenotazione(umbrellaId,totalFinal,umbrella_date, umbrella_time,sharedData.getUserId()) ;
                selected_umbrellas.add(temp);
                sharedCartHashMap.put(umbrellaId,Double.valueOf(totalFinal));
            });
        }

        @JavascriptInterface
        public void removeSelectedUmbrella(String umbrellaId) {
            webView.post(() -> selected_umbrellas = selected_umbrellas.stream()
                    .filter(prenotazione -> !prenotazione.getNumeroLettino().equals(umbrellaId))
                    .collect(Collectors.toCollection(ArrayList::new)));
        }
        @JavascriptInterface
        public void chargeUmbrellaInfo() {
            runOnUiThread(() -> {
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Lettini");
                DatabaseReference prenotazioniRef = FirebaseDatabase.getInstance().getReference().child("Prenotazioni Lettini");
                prenotazioniRef.orderByChild("dataPrenotazione").equalTo(umbrella_date).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot umbrellaSnapshot : dataSnapshot.getChildren()) {
                            String lettinoCodice = umbrellaSnapshot.child("numeroLettino").getValue(String.class);
                            if(umbrella_time.equals("Giornaliero")){
                                lettiniCodiciList.add(lettinoCodice);
                                break;
                            }
                            if((umbrellaSnapshot.child("period").getValue(String.class).equals(umbrella_time))||
                                    (umbrellaSnapshot.child("period").getValue(String.class).equals("Giornaliero"))){
                                lettiniCodiciList.add(lettinoCodice);
                                if(umbrellaPriceHashMap.containsKey(lettinoCodice)){
                                    umbrellaPriceHashMap.remove(lettinoCodice);
                                }
                            }else if(!umbrella_time.equals("Giornaliero")){
                                if(!lettiniCodiciList.contains(lettinoCodice)){
                                    String umbrellaPrice = String.valueOf(umbrellaSnapshot.child("prezzo").getValue(Double.class));
                                    umbrellaPriceHashMap.put(lettinoCodice,umbrellaPrice);
                                }
                            }
                        }

                        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot umbrellaSnapshot : dataSnapshot.getChildren()) {
                                    String lettinoCodice = umbrellaSnapshot.getKey();
                                    if(umbrellaSnapshot.child("avaible").getValue(Boolean.class)){
                                        if((!umbrellaPriceHashMap.containsKey(lettinoCodice))&&(!lettiniCodiciList.contains(lettinoCodice))){
                                            String umbrellaPrice = String.valueOf(umbrellaSnapshot.child("price").getValue(Double.class));
                                            umbrellaPriceHashMap.put(lettinoCodice,umbrellaPrice);
                                        }
                                    }else{
                                        if(umbrellaSnapshot.hasChild("freeDays")){
                                            if(umbrellaSnapshot.child("freeDays").hasChild(umbrella_date)){
                                                if((umbrellaPriceHashMap.containsKey(lettinoCodice))||
                                                (lettiniCodiciList.contains(lettinoCodice))){

                                                }else{
                                                    lettiniCodiciList.add(lettinoCodice);
                                                }
                                            }else {
                                                lettiniCodiciList.add(lettinoCodice);
                                            }
                                        }else{
                                            lettiniCodiciList.add(lettinoCodice);
                                        }
                                    }
                                }
                                Gson gson = new Gson();
                                String jsonString = gson.toJson(lettiniCodiciList);
                                webView.evaluateJavascript("disableUmbrellas('" + jsonString + "')", null );

                                Gson gsonPrices = new Gson();
                                String jsonStringPrices = gsonPrices.toJson(umbrellaPriceHashMap);
                                webView.evaluateJavascript("setUmbrellasPrices('" + jsonStringPrices + "')", null);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });




            });
        }

    }
}
