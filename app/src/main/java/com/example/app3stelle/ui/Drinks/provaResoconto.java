package com.example.app3stelle.ui.Drinks;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.R;
import com.example.app3stelle.ui.history.RowElement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

import it.custom.printer.api.android.CustomAndroidAPI;
import it.custom.printer.api.android.CustomException;
import it.custom.printer.api.android.CustomPrinter;
import it.custom.printer.api.android.PrinterFont;

public class provaResoconto extends AppCompatActivity implements printInterface{
    private String lock="lockAccess";
    static CustomPrinter prnDevice = null;
    ArrayList<RowElement> drinksList=null;
    private int printElement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driks_resume_bar_layout);

        drinksList= new ArrayList<>();
        final RecyclerView recyclerViewBeverage = findViewById(R.id.listDrinks);
        DatabaseReference drinkRef = FirebaseDatabase.getInstance().getReference().child("Ordini Drink");
        drinkRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String description = snapshot.child("descrizione").getValue(String.class);
                    String clientName = snapshot.child("clientName").getValue(String.class);
                    Double drinkPrice= snapshot.child("prezzo").getValue(Double.class);
                    RowElement temp = new RowElement(clientName,String.valueOf(drinkPrice),description);
                    drinksList.add(temp);
                }
                orderOfDrinksAdapter adapter = new orderOfDrinksAdapter(drinksList, provaResoconto.this,provaResoconto.this);
                recyclerViewBeverage.setAdapter(adapter);
                recyclerViewBeverage.setLayoutManager(new LinearLayoutManager(provaResoconto.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        OpenDevice();

}
    /*public void onPrintText(View view)
    {
        OpenDevice();
        if (prnDevice == null)
            return;
        new PrintTextBGTask().execute();

    }*/

    //Open the device if it isn't already opened
    public boolean OpenDevice()
    {
        if (prnDevice == null)
        {
            new OpenDeviceBGTask().execute();
        }
        //Already opened
        return true;
    }

    void showAlertMsg(String title,String msg)
    {
        AlertDialog.Builder dialogBuilder;
        dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setNeutralButton( "OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(msg);
        dialogBuilder.show();

    }

    @Override
    public void printOrder(int position) {
        printElement = position;
        if (prnDevice == null)
            return;
        new PrintTextBGTask().execute();
    }

    class PrintTextBGTask extends AsyncTask<String , Integer, Void>
    {
        int pos;
        String strErrorMessage = null;
        PrinterFont fntPrinterNormal = new PrinterFont();
        PrinterFont fntPrinterName = new PrinterFont();

        @Override
        protected void onPreExecute()
        {
            try
            {
                //Fill class: NORMAL
                fntPrinterNormal.setCharHeight(PrinterFont.FONT_SIZE_X1);					//Height x1
                fntPrinterNormal.setCharWidth(PrinterFont.FONT_SIZE_X1);					//Width x1
                fntPrinterNormal.setEmphasized(false);										//No Bold
                fntPrinterNormal.setItalic(false);											//No Italic
                fntPrinterNormal.setUnderline(false);										//No Underline
                fntPrinterNormal.setJustification(PrinterFont.FONT_JUSTIFICATION_RIGHT);	//Center
                fntPrinterNormal.setInternationalCharSet(PrinterFont.FONT_CS_DEFAULT);		//Default International Chars

                fntPrinterName.setCharHeight(PrinterFont.FONT_SIZE_X2);					//Height x1
                fntPrinterName.setCharWidth(PrinterFont.FONT_SIZE_X2);					//Width x1
                fntPrinterName.setEmphasized(true);										//No Bold
                fntPrinterName.setItalic(false);											//No Italic
                fntPrinterName.setUnderline(true);										//No Underline
                fntPrinterName.setJustification(PrinterFont.FONT_JUSTIFICATION_CENTER);	//Center
                fntPrinterName.setInternationalCharSet(PrinterFont.FONT_CS_DEFAULT);		//Default International Chars
            }
            catch(CustomException e )
            {

                //Show Error
                showAlertMsg("Error...", e.getMessage());
            }
            catch(Exception e )
            {
                showAlertMsg("Error...", "Set font properties error...");
            }
        }

        @Override
        protected Void doInBackground(String... params)
        {
            synchronized (lock)
            {
                try
                {
                    String[] drinksArray = drinksList.get(printElement).getDrinkList().toString().split(",");
                    prnDevice.printTextLF(drinksList.get(printElement).getDestination().toString(), fntPrinterName);
                    for(int i=0;i<drinksArray.length;i++){
                        prnDevice.printTextLF(drinksArray[i], fntPrinterNormal);
                    }
                    prnDevice.printTextLF(drinksList.get(printElement).getDrinkPrice().toString()+"€", fntPrinterName);
                    prnDevice.feed(3);
                    //Cut (Total)
                    prnDevice.cut(CustomPrinter.CUT_TOTAL);
                }
                catch(CustomException e )
                {
                    //open error
                    strErrorMessage = e.getMessage();
                }
                catch(Exception e )
                {
                    //open error
                    strErrorMessage = "Print Text Error...";
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            if (strErrorMessage != null)
                showAlertMsg("Error...", strErrorMessage);
        }


    }

    class OpenDeviceBGTask extends AsyncTask<String , Integer, Void>
    {
        String strErrorMessage = null;
        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected Void doInBackground(String... params)
        {
            try
            {
                //Open and connect it
                prnDevice = new CustomAndroidAPI().getPrinterDriverETH("172.16.0.204", 9100);
            }
            catch(CustomException e )
            {
                //open error
                strErrorMessage = e.getMessage();
            }
            catch(Exception e )
            {
                //open error
                strErrorMessage = "Open Print Error...";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            if (prnDevice != null)
            {
                Toast.makeText(provaResoconto.this, "Device Open!",Toast.LENGTH_LONG).show();

            }
            else
            {
                if (strErrorMessage != null)
                    showAlertMsg("Error...", strErrorMessage);
            }
        }


    }
}
