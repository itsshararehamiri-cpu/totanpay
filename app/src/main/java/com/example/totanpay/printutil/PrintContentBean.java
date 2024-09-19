package com.example.totanpay.printutil;

import android.graphics.Bitmap;

import com.urovo.sdk.print.PrintFormat;

public class PrintContentBean {
    public static final int PrintType_Text = 0;//text
    public static final int PrintType_QRCode = 1; //QRCode
    public static final int PrintType_BarCode = 2; //QRCode
    public static final int PrintType_Bitmap = 3; //Image
    public static final int PrintType_BLANK = 4; //Blank

    public String content = "";
    public int font = PrintFormat.FONT_NORMAL;
    public int fontSize = 0;
    public int align = 0;
    private int printType; //print type
    private int height; // for barcode/qrcode/bitmap
    private int width; // for barcode/qrcode/bitmap
    private int offset;
    private boolean isBold;
    private Bitmap bitmap;

    private String fontName = "";

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPrintType() {
        return printType;
    }

    public void setPrintType(int printType) {
        this.printType = printType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFont() {
        return font;
    }

    public void setFont(int font) {
        this.font = font;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }


}
