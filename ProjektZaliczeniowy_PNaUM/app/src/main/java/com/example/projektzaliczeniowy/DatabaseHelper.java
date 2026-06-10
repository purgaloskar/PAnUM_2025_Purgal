package com.example.projektzaliczeniowy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Kafeteria.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE produkty (id INTEGER PRIMARY KEY AUTOINCREMENT, nazwa TEXT, cena REAL)");
        db.execSQL("CREATE TABLE koszyk (id INTEGER PRIMARY KEY AUTOINCREMENT, nazwa TEXT, ilosc INTEGER, cena REAL)");

        // Baza na start dostaje gotowe produkty
        db.execSQL("INSERT INTO produkty (nazwa, cena) VALUES ('Espresso', 9.00)");
        db.execSQL("INSERT INTO produkty (nazwa, cena) VALUES ('Cappuccino', 14.00)");
        db.execSQL("INSERT INTO produkty (nazwa, cena) VALUES ('Latte Macchiato', 16.00)");
        db.execSQL("INSERT INTO produkty (nazwa, cena) VALUES ('Sernik', 12.00)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS produkty");
        db.execSQL("DROP TABLE IF EXISTS koszyk");
        onCreate(db);
    }

    public Cursor getProdukty() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM produkty", null);
    }

    public void dodajDoKoszyka(String nazwa, int ilosc, double cena) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nazwa", nazwa);
        values.put("ilosc", ilosc);
        values.put("cena", cena);
        db.insert("koszyk", null, values);
    }

    public Cursor getKoszyk() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM koszyk", null);
    }

    public void wyczyscKoszyk() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM koszyk");
    }
}