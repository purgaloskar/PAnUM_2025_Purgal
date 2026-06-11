package com.example.projektzaliczeniowy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cafeteria.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_PRODUCTS = "products";
    public static final String COL_PROD_ID = "_id";
    public static final String COL_PROD_NAME = "name";
    public static final String COL_PROD_CATEGORY = "category";
    public static final String COL_PROD_PRICE = "price";

    public static final String TABLE_CART = "cart";
    public static final String COL_CART_ID = "_id";
    public static final String COL_CART_PROD_ID = "product_id";
    public static final String COL_CART_NAME = "name";
    public static final String COL_CART_QUANTITY = "quantity";
    public static final String COL_CART_PRICE = "price";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createProductsTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_PROD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PROD_NAME + " TEXT, " +
                COL_PROD_CATEGORY + " TEXT, " +
                COL_PROD_PRICE + " REAL)";
        db.execSQL(createProductsTable);

        String createCartTable = "CREATE TABLE " + TABLE_CART + " (" +
                COL_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CART_PROD_ID + " INTEGER, " +
                COL_CART_NAME + " TEXT, " +
                COL_CART_QUANTITY + " INTEGER, " +
                COL_CART_PRICE + " REAL)";
        db.execSQL(createCartTable);

        insertInitialProducts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    private void insertInitialProducts(SQLiteDatabase db) {
        insertProduct(db, "Kawa Espresso", "drinks", 7.00);
        insertProduct(db, "Kawa Latte", "drinks", 12.50);
        insertProduct(db, "Herbata Owocowa", "drinks", 9.00);

        insertProduct(db, "Rogalik Croissant", "snacks", 6.50);
        insertProduct(db, "Sernik Domowy", "snacks", 12.00);
        insertProduct(db, "Muffin Czekoladowy", "snacks", 8.00);

        insertProduct(db, "Kotlet Schabowy", "dinners", 24.00);
        insertProduct(db, "Pierogi Ruskie", "dinners", 18.50);
        insertProduct(db, "Zupa Pomidorowa", "dinners", 12.00);
    }

    private void insertProduct(SQLiteDatabase db, String name, String category, double price) {
        ContentValues values = new ContentValues();
        values.put(COL_PROD_NAME, name);
        values.put(COL_PROD_CATEGORY, category);
        values.put(COL_PROD_PRICE, price);
        db.insert(TABLE_PRODUCTS, null, values);
    }

    public Cursor getProductsByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COL_PROD_ID + ", " + COL_PROD_NAME + ", " + COL_PROD_PRICE + " FROM " + TABLE_PRODUCTS + " WHERE " + COL_PROD_CATEGORY + " = ?", new String[]{category});
    }

    public boolean addOrder(String name, int quantity, double price) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + COL_CART_NAME + "=?", new String[]{name});
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COL_CART_ID);
            int qtyIndex = cursor.getColumnIndex(COL_CART_QUANTITY);

            if (idIndex != -1 && qtyIndex != -1) {
                int id = cursor.getInt(idIndex);
                int currentQty = cursor.getInt(qtyIndex);
                int newQty = currentQty + quantity;
                double newPrice = (price / quantity) * newQty;

                ContentValues values = new ContentValues();
                values.put(COL_CART_QUANTITY, newQty);
                values.put(COL_CART_PRICE, newPrice);

                int result = db.update(TABLE_CART, values, COL_CART_ID + "=?", new String[]{String.valueOf(id)});
                cursor.close();
                return result > 0;
            }
        }
        if (cursor != null) cursor.close();

        ContentValues values = new ContentValues();
        values.put(COL_CART_NAME, name);
        values.put(COL_CART_QUANTITY, quantity);
        values.put(COL_CART_PRICE, price);

        long result = db.insert(TABLE_CART, null, values);
        return result != -1;
    }

    public Cursor getCartItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT " + COL_CART_ID + ", " + COL_CART_NAME + ", " + COL_CART_QUANTITY + ", " + COL_CART_PRICE + " FROM " + TABLE_CART, null);
    }

    public void updateQuantity(int id, int newQty, double pricePerItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (newQty <= 0) {
            db.delete(TABLE_CART, COL_CART_ID + "=?", new String[]{String.valueOf(id)});
        } else {
            ContentValues values = new ContentValues();
            values.put(COL_CART_QUANTITY, newQty);
            values.put(COL_CART_PRICE, newQty * pricePerItem);
            db.update(TABLE_CART, values, COL_CART_ID + "=?", new String[]{String.valueOf(id)});
        }
    }

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CART);
    }
}