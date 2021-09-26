package com.lonelyjones.qrandbarcodescanner;

import android.graphics.Bitmap;

public class CodeItem {

    private int id;
    private String codeScanned;
    private String dateScanned;
    private byte[] img_code_gen;

    public CodeItem(String codeScanned, String dateScanned, byte[] img_code_gen){
        this.codeScanned = codeScanned;
        this.dateScanned = dateScanned;
        this.img_code_gen = img_code_gen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodeScanned() {
        return codeScanned;
    }

    public void setCodeScanned(String codeScanned) {
        this.codeScanned = codeScanned;
    }

    public String getDateScanned() {
        return dateScanned;
    }

    public void setDateScanned(String dateScanned) {
        this.dateScanned = dateScanned;
    }

    public byte[] getImg_code_gen() {
        return img_code_gen;
    }

    public void setImg_code_gen(byte[] img_code_gen) {
        this.img_code_gen = img_code_gen;
    }
}
