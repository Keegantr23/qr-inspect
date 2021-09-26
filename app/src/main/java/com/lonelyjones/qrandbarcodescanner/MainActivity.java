package com.lonelyjones.qrandbarcodescanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.ResultSet;

public class MainActivity extends AppCompatActivity {

    ImageView img_qr_logo;
    Button btn_history;
    Button btn_scan;
    public static final int CAMERA_REQUEST_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setTitle("QR & Barcode Scanner");

        img_qr_logo = findViewById(R.id.img_QR_logo);
        btn_history = findViewById(R.id.btn_history);
        btn_scan = findViewById(R.id.btn_scan);

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HistoryCodes.class);
                startActivity(i);
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanActivity();
            }
        });
    }

    private void startScanActivity(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_PERMISSION);
            }else{
                startActivity(new Intent(MainActivity.this, CodeScanningActivity.class));
            }
        }else{
            startActivity(new Intent(MainActivity.this, CodeScanningActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CAMERA_REQUEST_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startActivity(new Intent(MainActivity.this, CodeScanningActivity.class));
            }
        }
    }

}