package com.lonelyjones.qrandbarcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.encoder.QRCode;

import java.io.File;
import java.io.FileOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class DisplayCodeActivity extends AppCompatActivity {

    TextView txt_code_display;
    ImageView img_code;
    Button btn_copy_clipboard;
    Button btn_open_webView;
    Button btn_open_browser;
    Button btn_scan_new;
    boolean backToMain;
    String resultCodeStringCopy;
    Bitmap code_img_to_save;

    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_code);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_code_display = findViewById(R.id.txt_code_display);
        img_code = findViewById(R.id.img_code);
        btn_copy_clipboard = findViewById(R.id.btn_copy_clipboard);
        btn_open_webView = findViewById(R.id.btn_open_webView);
        btn_open_browser = findViewById(R.id.btn_open_browser);
        btn_scan_new = findViewById(R.id.btn_scan_new);

        Intent i = getIntent();
        String resultCodeString = i.getStringExtra("codeResult");
        resultCodeStringCopy = resultCodeString;
        backToMain = i.getBooleanExtra("backToMain", true);
        txt_code_display.setText(resultCodeString);

        if(!(URLUtil.isHttpsUrl(resultCodeString) || URLUtil.isHttpUrl(resultCodeString))) {
            btn_open_browser.setVisibility(View.GONE);
            btn_open_webView.setVisibility(View.GONE);
        }

        txt_code_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(URLUtil.isHttpsUrl(resultCodeString) || URLUtil.isHttpUrl(resultCodeString)) {
                    Uri uriUrl = Uri.parse(resultCodeString);
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                    startActivity(launchBrowser);
                }else{
                    Toast.makeText(DisplayCodeActivity.this, "Not a valid URL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_code_display.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copied_string", resultCodeString);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(DisplayCodeActivity.this, "Copied to Clipboard.", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        QRGEncoder qrgEncoder = new QRGEncoder(resultCodeString, null, QRGContents.Type.TEXT, 200);
        qrgEncoder.setColorBlack(Color.BLACK);
        qrgEncoder.setColorWhite(Color.WHITE);
        // Getting QR-Code as Bitmap
        Bitmap bitmap = qrgEncoder.getBitmap();
        code_img_to_save = bitmap;
        // Setting Bitmap to ImageView
        img_code.setImageBitmap(bitmap);

        img_code.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(Build.VERSION.SDK_INT >= 23){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION);
                    }else{
                        saveImage(resultCodeString);
                    }

                }else{
                    saveImage(resultCodeString);
                }
                return false;
            }
        });

        btn_open_webView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(URLUtil.isHttpsUrl(resultCodeString) || URLUtil.isHttpUrl(resultCodeString)) {
                    Intent i = new Intent(DisplayCodeActivity.this, webViewActivity.class);
                    i.putExtra("url_link", resultCodeString);
                    startActivity(i);
                }else{
                    Toast.makeText(DisplayCodeActivity.this, "Not a valid URL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_open_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(URLUtil.isHttpsUrl(resultCodeString) || URLUtil.isHttpUrl(resultCodeString)) {
                    Uri uriUrl = Uri.parse(resultCodeString);
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                    startActivity(launchBrowser);
                }else{
                    Toast.makeText(DisplayCodeActivity.this, "Not a valid URL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_scan_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DisplayCodeActivity.this, CodeScanningActivity.class);
                startActivity(i);
            }
        });

        btn_copy_clipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copied_string", resultCodeString);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(DisplayCodeActivity.this, "Copied to Clipboard.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveImage(String resultCodeString){
        if(URLUtil.isHttpsUrl(resultCodeString)){
            resultCodeStringCopy = resultCodeStringCopy.substring(8);
        }else if(URLUtil.isHttpUrl(resultCodeString)){
            resultCodeStringCopy = resultCodeStringCopy.substring(7);
        }
        MediaStore.Images.Media.insertImage(getContentResolver(), code_img_to_save, "Generated_QR-Code_"+resultCodeStringCopy, "QR Code");
        Toast.makeText(DisplayCodeActivity.this, "QR Code Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if(backToMain) {
            Intent i = new Intent(DisplayCodeActivity.this, MainActivity.class);
            startActivity(i);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==WRITE_EXTERNAL_STORAGE_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                saveImage(resultCodeStringCopy);
            }
        }
    }

}