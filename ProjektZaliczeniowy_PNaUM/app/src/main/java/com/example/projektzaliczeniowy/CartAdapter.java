package com.example.projektzaliczeniowy;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CartAdapter extends CursorAdapter {

    private DatabaseHelper dbHelper;
    private Context context;

    public CartAdapter(Context context, Cursor c, DatabaseHelper dbHelper) {
        super(context, c, 0);
        this.dbHelper = dbHelper;
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = view.findViewById(R.id.tvCartItemName);
        TextView tvQty = view.findViewById(R.id.tvCartItemQty);
        TextView tvPrice = view.findViewById(R.id.tvCartItemPrice);
        Button btnMinus = view.findViewById(R.id.btnMinus);
        Button btnPlus = view.findViewById(R.id.btnPlus);

        int idIndex = cursor.getColumnIndex("_id");
        int nameIndex = cursor.getColumnIndex("name");
        int qtyIndex = cursor.getColumnIndex("quantity");
        int priceIndex = cursor.getColumnIndex("price");

        if (idIndex == -1 || nameIndex == -1 || qtyIndex == -1 || priceIndex == -1) {
            return;
        }

        final int id = cursor.getInt(idIndex);
        String name = cursor.getString(nameIndex);
        final int quantity = cursor.getInt(qtyIndex);
        double price = cursor.getDouble(priceIndex);

        final double pricePerItem = quantity > 0 ? (price / quantity) : 0.0;

        tvName.setText(name);
        tvQty.setText(String.valueOf(quantity));
        tvPrice.setText(String.format("%.2f", price) + " zł");

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.updateQuantity(id, quantity - 1, pricePerItem);
                updateParentActivity();
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.updateQuantity(id, quantity + 1, pricePerItem);
                updateParentActivity();
            }
        });
    }

    private void updateParentActivity() {
        if (context instanceof CategoryActivity) {
            ((CategoryActivity) context).loadCartData();
        }
    }
}