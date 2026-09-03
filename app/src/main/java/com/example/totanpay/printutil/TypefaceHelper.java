package com.example.totanpay.printutil;

import android.graphics.Typeface;
import android.util.Log;

import androidx.collection.SimpleArrayMap;

public class TypefaceHelper {

    private static final String TAG = "TypefaceHelper===>";
    private static final SimpleArrayMap<String, Typeface> TYPEFACE_CACHE = new SimpleArrayMap<String, Typeface>();

    public static Typeface get(boolean bold, String fontName) {
        String name = bold + "_" + fontName;
        synchronized (TYPEFACE_CACHE) {
            if (!TYPEFACE_CACHE.containsKey(name)) {
                Log.e(TAG, "get TYPEFACE_CACHE not contains " + name);
                try {
                    Typeface typeface = Typeface.create(Typeface.createFromFile(fontName), bold ? Typeface.BOLD : Typeface.NORMAL);
                    TYPEFACE_CACHE.put(name, typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                    return getDefault(bold, fontName);
                }
            } else {
                Log.e(TAG, "TYPEFACE_CACHE contains " + name);
            }
            return TYPEFACE_CACHE.get(name);
        }
    }

    public static Typeface getDefault(boolean bold, String fontName) {
        String name = bold + "_" + fontName;
        synchronized (TYPEFACE_CACHE) {
            if (!TYPEFACE_CACHE.containsKey(name)) {
                try {
                    Typeface typeface = Typeface.defaultFromStyle(bold ? Typeface.BOLD : Typeface.NORMAL);
                    TYPEFACE_CACHE.put(name, typeface);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                Log.e(TAG, "TYPEFACE_CACHE contains " + name);
            }
            return TYPEFACE_CACHE.get(name);
        }
    }

}
