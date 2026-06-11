package com.example.projektzaliczeniowy;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tvMainTitle;
    private ListView lvProducts;
    private Button btnAddToCart, btnBack, btnSubmitFromMain;
    private EditText etQuantity;
    private Button[] keyButtons = new Button[10];
    private Button btnKeyDel;

    private DatabaseHelper dbHelper;
    private Cursor productsCursor;
    private SimpleCursorAdapter adapter;

    private int selectedProductId = -1;
    private String selectedProductName = "";
    private double selectedProductPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View mainView = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        tvMainTitle = findViewById(R.id.tvMainTitle);
        lvProducts = findViewById(R.id.lvProducts);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBack = findViewById(R.id.btnBack);
        btnSubmitFromMain = findViewById(R.id.btnSubmitFromMain);
        etQuantity = findViewById(R.id.etQuantity);

        keyButtons[0] = findViewById(R.id.btnKey0);
        keyButtons[1] = findViewById(R.id.btnKey1);
        keyButtons[2] = findViewById(R.id.btnKey2);
        keyButtons[3] = findViewById(R.id.btnKey3);
        keyButtons[4] = findViewById(R.id.btnKey4);
        keyButtons[5] = findViewById(R.id.btnKey5);
        keyButtons[6] = findViewById(R.id.btnKey6);
        keyButtons[7] = findViewById(R.id.btnKey7);
        keyButtons[8] = findViewById(R.id.btnKey8);
        keyButtons[9] = findViewById(R.id.btnKey9);
        btnKeyDel = findViewById(R.id.btnKeyDel);

        String category = getIntent().getStringExtra("CATEGORY_KEY");
        if (category == null) {
            category = "drinks";
        }

        if (category.equals("drinks")) {
            tvMainTitle.setText("Napoje");
        } else if (category.equals("snacks")) {
            tvMainTitle.setText("Desery");
        } else if (category.equals("dinners")) {
            tvMainTitle.setText("Obiady");
        }

        productsCursor = dbHelper.getProductsByCategory(category);

        String[] fromColumns = {DatabaseHelper.COL_PROD_NAME, DatabaseHelper.COL_PROD_PRICE};
        int[] toViews = {android.R.id.text1, android.R.id.text2};

        adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                productsCursor,
                fromColumns,
                toViews,
                0
        );

        adapter.setViewBinder((view, cursor, columnIndex) -> {
            if (columnIndex == cursor.getColumnIndex(DatabaseHelper.COL_PROD_PRICE)) {
                double price = cursor.getDouble(columnIndex);
                TextView tv = (TextView) view;
                tv.setText(String.format("%.2f zł", price));
                return true;
            }
            return false;
        });

        lvProducts.setAdapter(adapter);

        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (productsCursor != null && productsCursor.moveToPosition(position)) {
                    selectedProductId = productsCursor.getInt(productsCursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_ID));
                    selectedProductName = productsCursor.getString(productsCursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_NAME));
                    selectedProductPrice = productsCursor.getDouble(productsCursor.getColumnIndexOrThrow(DatabaseHelper.COL_PROD_PRICE));

                    Toast.makeText(MainActivity.this, "Wybrano: " + selectedProductName, Toast.LENGTH_SHORT).show();
                }
            }
        });

        View.OnClickListener numColorListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                String currentText = etQuantity.getText().toString();
                if (currentText.length() < 3) {
                    etQuantity.setText(currentText + b.getText().toString());
                }
            }
        };

        for (int i = 0; i < 10; i++) {
            if (keyButtons[i] != null) {
                keyButtons[i].setOnClickListener(numColorListener);
            }
        }

        if (btnKeyDel != null) {
            btnKeyDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentText = etQuantity.getText().toString();
                    if (currentText.length() > 0) {
                        etQuantity.setText(currentText.substring(0, currentText.length() - 1));
                    }
                }
            });
        }

        if (btnAddToCart != null) {
            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String qtyStr = etQuantity.getText().toString().trim();
                    if (selectedProductId == -1) {
                        Toast.makeText(MainActivity.this, "Najpierw wybierz produkt z listy!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (qtyStr.isEmpty() || qtyStr.equals("0")) {
                        Toast.makeText(MainActivity.this, "Wpisz poprawną ilość!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int quantity = Integer.parseInt(qtyStr);
                    double totalPrice = selectedProductPrice * quantity;

                    boolean success = dbHelper.addOrder(selectedProductName, quantity, totalPrice);
                    if (success) {
                        Toast.makeText(MainActivity.this, "Dodano do koszyka!", Toast.LENGTH_SHORT).show();
                        etQuantity.setText("");
                    } else {
                        Toast.makeText(MainActivity.this, "Błąd dodawania do koszyka", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (btnSubmitFromMain != null) {
            btnSubmitFromMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (productsCursor != null) {
            productsCursor.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}