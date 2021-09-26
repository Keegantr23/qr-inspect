package com.lonelyjones.qrandbarcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import java.text.DateFormat;
import java.util.Calendar;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class CodeScanningActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanning);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.startPreview();

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {

                //Add new codeItem upon successful decode
                CodeItem codeItem = new CodeItem(result.getText(), getCurrentTime(), qrGenerator(result.getText()));
                boolean isSuccessful = new CodeItemHandler(CodeScanningActivity.this).create(codeItem);

                if(!isSuccessful){
                    Toast.makeText(CodeScanningActivity.this, "Failed to add codeItem", Toast.LENGTH_SHORT).show();
                }

                Intent i = new Intent(CodeScanningActivity.this, DisplayCodeActivity.class);
                i.putExtra("codeResult", result.getText());
                i.putExtra("backToMain", true);
                startActivity(i);
            }
        });

    }

    private byte[] qrGenerator(String stringToConvert){
        QRGEncoder qrgEncoder = new QRGEncoder(stringToConvert, null, QRGContents.Type.TEXT, 200);
        qrgEncoder.setColorBlack(Color.BLACK);
        qrgEncoder.setColorWhite(Color.WHITE);
        // Getting QR-Code as Bitmap
        Bitmap bitmap = qrgEncoder.getBitmap();
        // Setting Bitmap to ImageView
        return DbBitmapUtility.getBytes(bitmap);
    }

    private String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
        return currentDate;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}