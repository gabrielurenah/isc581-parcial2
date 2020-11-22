package com.pucmm.isc581_parcial2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private DBManager mDBManager;
    private ListView lv;
    private SimpleCursorAdapter adapter;

    private final String[] columns = new String[] { "_id", "name", "price", "category" };
    private final int[] txts = new int[] { R.id.product_id , R.id.product_name, R.id.product_price, R.id.product_category };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.listView);
        lv.setEmptyView(findViewById(R.id.txt_empty));

        mDBManager = new DBManager(this);
        mDBManager.open();

        adapter = new SimpleCursorAdapter(this,
                R.layout.list_item, mDBManager.getProducts(), columns, txts, 0);
        adapter.notifyDataSetChanged();

        lv.setAdapter(adapter);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            TextView name = view.findViewById(R.id.product_name);
            TextView price = view.findViewById(R.id.product_price);
            TextView category = view.findViewById(R.id.product_category);
            TextView productId = view.findViewById(R.id.product_id);


            Intent i = new Intent(getApplicationContext(), ManageProductsActivity.class);
            i.putExtra("id", productId.getText().toString());
            i.putExtra("name", name.getText().toString());
            i.putExtra("price", price.getText().toString());
            i.putExtra("category", category.getText().toString());

            startActivity(i);
        });

    }

    @Override
    protected void onDestroy() {
        mDBManager.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_product) {
            startActivity(new Intent(this, ManageProductsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}