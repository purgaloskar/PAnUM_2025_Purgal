package com.example.kawiarnia_projekt_lab9101112;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle("Szczegóły");

        Button btnBack = findViewById(R.id.btnBackDetails);
        btnBack.setOnClickListener(v -> finish());

        ImageView imgDetails = findViewById(R.id.imgDetails);
        TextView txtTitle = findViewById(R.id.txtTitle);
        TextView txtSub1 = findViewById(R.id.txtSub1);
        TextView txtSub2 = findViewById(R.id.txtSub2);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int itemId = getIntent().getIntExtra("ITEM_ID", -1);
        String tableName = getIntent().getStringExtra("TABLE_NAME");

        Cursor cursor = dbHelper.getItemDetails(tableName, itemId);

        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COL_NAZWA);
            int imgIndex = cursor.getColumnIndex(DatabaseHelper.COL_ZDJECIE);

            if (nameIndex != -1) {
                txtTitle.setText(cursor.getString(nameIndex));
            }
            if (imgIndex != -1) {
                imgDetails.setImageResource(cursor.getInt(imgIndex));
            }

            if (tableName.equals(DatabaseHelper.TABLE_LOKALE)) {
                int addrIndex = cursor.getColumnIndex(DatabaseHelper.COL_ADRES);
                int hoursIndex = cursor.getColumnIndex(DatabaseHelper.COL_GODZINY);

                String adres = addrIndex != -1 ? cursor.getString(addrIndex) : "";
                String godziny = hoursIndex != -1 ? cursor.getString(hoursIndex) : "";

                txtSub1.setText("Adres: " + adres);
                txtSub2.setText("Godziny otwarcia: " + godziny);
            } else {
                int descIndex = cursor.getColumnIndex(DatabaseHelper.COL_OPIS);
                int priceIndex = cursor.getColumnIndex(DatabaseHelper.COL_CENA);

                String opis = descIndex != -1 ? cursor.getString(descIndex) : "";
                double cena = priceIndex != -1 ? cursor.getDouble(priceIndex) : 0.0;

                txtSub1.setText(opis);
                txtSub2.setText("Cena: " + String.format("%.2f", cena) + " zł");
            }
            cursor.close();
        }
    }
}