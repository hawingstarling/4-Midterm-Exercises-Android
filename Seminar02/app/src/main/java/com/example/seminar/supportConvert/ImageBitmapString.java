package com.example.seminar.supportConvert;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;

public class ImageBitmapString {
    @TypeConverter
    public static byte[] getStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        return b;
    }

    @TypeConverter
    public static Bitmap getBitmapFromString(byte[] arr) {
        return BitmapFactory.decodeByteArray(arr, 0, arr.length);
    }
}
