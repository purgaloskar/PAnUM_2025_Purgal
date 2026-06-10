package com.example.kawiarnia_projekt_lab9101112;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Kawiarnia.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAPOJE = "Napoje";
    public static final String TABLE_PRZEKASKI = "Przekaski";
    public static final String TABLE_LOKALE = "Lokale";

    public static final String COL_ID = "id";
    public static final String COL_NAZWA = "nazwa";
    public static final String COL_OPIS = "opis";
    public static final String COL_CENA = "cena";
    public static final String COL_ZDJECIE = "zdjecie_id";

    public static final String COL_ADRES = "adres";
    public static final String COL_GODZINY = "godziny_otwarcia";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAPOJE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAZWA + " TEXT, " +
                COL_OPIS + " TEXT, " +
                COL_ZDJECIE + " INTEGER, " +
                COL_CENA + " REAL)");

        db.execSQL("CREATE TABLE " + TABLE_PRZEKASKI + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAZWA + " TEXT, " +
                COL_OPIS + " TEXT, " +
                COL_ZDJECIE + " INTEGER, " +
                COL_CENA + " REAL)");

        db.execSQL("CREATE TABLE " + TABLE_LOKALE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAZWA + " TEXT, " +
                COL_ADRES + " TEXT, " +
                COL_GODZINY + " TEXT, " +
                COL_ZDJECIE + " INTEGER)");

        seedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAPOJE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRZEKASKI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOKALE);
        onCreate(db);
    }

    private void seedData(SQLiteDatabase db) {
        insertNapoj(db, "Espresso", "Mocna, klasyczna czarna kawa.", R.drawable.ic_kawa, 8.50);
        insertNapoj(db, "Cappuccino", "Kawa z puszysta mleczna pianka.", R.drawable.ic_kawa, 14.00);

        insertPrzekaska(db, "Sernik", "Domowy sernik z polewa czekoladowa.", R.drawable.ic_ciasto, 12.00);
        insertPrzekaska(db, "Croissant", "Maslany rogalik podawany na cieplo.", R.drawable.ic_ciasto, 7.50);

        insertLokal(db, "Kawiarnia Centrum", "ul. Marszalkowska 10, Warszawa", "08:00 - 22:00", R.drawable.ic_mapa);
        insertLokal(db, "Kawiarnia Rynek", "Rynek Glowny 5, Krakow", "09:00 - 23:00", R.drawable.ic_mapa);
    }

    private void insertNapoj(SQLiteDatabase db, String nazwa, String opis, int zdjecieId, double cena) {
        ContentValues values = new ContentValues();
        values.put(COL_NAZWA, nazwa);
        values.put(COL_OPIS, opis);
        values.put(COL_ZDJECIE, zdjecieId);
        values.put(COL_CENA, cena);
        db.insert(TABLE_NAPOJE, null, values);
    }

    private void insertPrzekaska(SQLiteDatabase db, String nazwa, String opis, int zdjecieId, double cena) {
        ContentValues values = new ContentValues();
        values.put(COL_NAZWA, nazwa);
        values.put(COL_OPIS, opis);
        values.put(COL_ZDJECIE, zdjecieId);
        values.put(COL_CENA, cena);
        db.insert(TABLE_PRZEKASKI, null, values);
    }

    private void insertLokal(SQLiteDatabase db, String nazwa, String adres, String godziny, int zdjecieId) {
        ContentValues values = new ContentValues();
        values.put(COL_NAZWA, nazwa);
        values.put(COL_ADRES, adres);
        values.put(COL_GODZINY, godziny);
        values.put(COL_ZDJECIE, zdjecieId);
        db.insert(TABLE_LOKALE, null, values);
    }

    public Cursor getDataByTable(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COL_ID + " AS _id, " + COL_NAZWA + " FROM " + tableName, null);
    }

    public Cursor getItemDetails(String tableName, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + tableName + " WHERE " + COL_ID + " = " + id, null);
    }
}