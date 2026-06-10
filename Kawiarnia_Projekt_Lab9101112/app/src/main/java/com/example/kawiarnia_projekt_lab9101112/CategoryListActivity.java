package com.example.kawiarnia_projekt_lab9101112;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class CategoryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Button btnBack = findViewById(R.id.btnBackList);
        btnBack.setOnClickListener(v -> finish());

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String selectedTable = getIntent().getStringExtra("CATEGORY_TABLE");
        setTitle(selectedTable);

        ListView listView = findViewById(R.id.listView);
        Cursor cursor = dbHelper.getDataByTable(selectedTable);

        String[] fromColumns = {DatabaseHelper.COL_NAZWA};
        int[] toViews = {android.R.id.text1};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursor,
                fromColumns,
                toViews,
                0
        );

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(CategoryListActivity.this, DetailsActivity.class);
            intent.putExtra("ITEM_ID", (int) id);
            intent.putExtra("TABLE_NAME", selectedTable);
            startActivity(intent);
        });
    }
}