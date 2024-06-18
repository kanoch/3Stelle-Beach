package com.example.app3stelle.ui.Drinks;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app3stelle.R;
import com.example.app3stelle.ui.gallery.RowAdapter;
import com.example.app3stelle.ui.gallery.RowElement;
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

public class provaResoconto extends AppCompatActivity {
    private String lock="lockAccess";
    static CustomPrinter prnDevice = null;
    EditText ipText=null;
    ArrayList<RowElement> drinksList=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driks_resume_bar_layout);

        ipText=findViewById(R.id.editTextIp);

        drinksList= new ArrayList<>();
        final RecyclerView recyclerViewBeverage = findViewById(R.id.listDrinks);
        DatabaseReference drinkRef = FirebaseDatabase.getInstance().getReference().child("Ordini Drink");
        drinkRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String description = snapshot.child("descrizione").getValue(String.class);
                    String destination = snapshot.child("destinazione").getValue(String.class);
                    Double drinkPrice= snapshot.child("prezzo").getValue(Double.class);
                    RowElement temp = new RowElement(destination,String.valueOf(drinkPrice),description);
                    drinksList.add(temp);
                }
                RowAdapter adapter = new RowAdapter(drinksList, provaResoconto.this);
                recyclerViewBeverage.setAdapter(adapter);
                recyclerViewBeverage.setLayoutManager(new LinearLayoutManager(provaResoconto.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

}
    public void onPrintText(View view)
    {
        PrinterFont fntPrinterNormal = new PrinterFont();
        PrinterFont fntPrinterBold2X = new PrinterFont();
        //open device
        if (OpenDevice() == false)
            return;

        try
        {
            //Fill class: NORMAL
            fntPrinterNormal.setCharHeight(PrinterFont.FONT_SIZE_X1);					//Height x1
            fntPrinterNormal.setCharWidth(PrinterFont.FONT_SIZE_X1);					//Width x1
            fntPrinterNormal.setEmphasized(false);										//No Bold
            fntPrinterNormal.setItalic(false);											//No Italic
            fntPrinterNormal.setUnderline(false);										//No Underline
            fntPrinterNormal.setJustification(PrinterFont.FONT_JUSTIFICATION_CENTER);	//Center
            fntPrinterNormal.setInternationalCharSet(PrinterFont.FONT_CS_DEFAULT);		//Default International Chars

            //Fill class: BOLD size 2X
            fntPrinterBold2X.setCharHeight(PrinterFont.FONT_SIZE_X2);					//Height x2
            fntPrinterBold2X.setCharWidth(PrinterFont.FONT_SIZE_X2);					//Width x2
            fntPrinterBold2X.setEmphasized(true);										//Bold
            fntPrinterBold2X.setItalic(false);											//No Italic
            fntPrinterBold2X.setUnderline(false);										//No Underline
            fntPrinterBold2X.setJustification(PrinterFont.FONT_JUSTIFICATION_CENTER);	//Center
            fntPrinterBold2X.setInternationalCharSet(PrinterFont.FONT_CS_DEFAULT);		//Default International Chars
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

        //***************************************************************************
        // PRINT TEXT
        //***************************************************************************

        synchronized (lock)
        {
            try
            {
                String[] drinksArray = drinksList.get(0).getDrinkList().toString().split(",");
                for(int i=0;i<drinksArray.length;i++){
                    prnDevice.printTextLF(drinksArray[i], fntPrinterNormal);
                }
                //Print Text (NORMAL)
                //prnDevice.printText(strTextToPrint, fntPrinterNormal);

                //Print Text (BOLD size 2X)
                //prnDevice.printTextLF(strTextToPrint, fntPrinterBold2X);
            }
            catch(CustomException e )
            {
                //Show Error
                showAlertMsg("Error...", e.getMessage());
            }
            catch(Exception e )
            {
                showAlertMsg("Error...", "Print Text Error...");
            }
        }
    }

    //Open the device if it isn't already opened
    public boolean OpenDevice()
    {
        try
        {
            //Open and connect it
            prnDevice = new CustomAndroidAPI().getPrinterDriverETH(ipText.getText().toString(), 9100);
            //Save last device selected
            //lastDeviceSelected = deviceSelected;
            return true;
        }
        catch(CustomException e )
        {

            //Show Error
            showAlertMsg("Error...", e.getMessage());
            return false;
        }
        catch(Exception e )
        {
            showAlertMsg("Error...", "Open Print Error...");
            //open error
            return false;
        }
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
}
