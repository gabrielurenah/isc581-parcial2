package com.pucmm.isc581_parcial2;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ListProductsFragment extends Fragment {

    private DBManager mDBManager;
    private ListView lv;
    private SimpleCursorAdapter adapter;

    private final String[] columns = new String[] { "_id", "name", "price", "category" };
    private final int[] txts = new int[] { R.id.product_id , R.id.product_name, R.id.product_price, R.id.product_category };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_products, container, false);

        lv = v.findViewById(R.id.listView);
        lv.setEmptyView(v.findViewById(R.id.txt_empty));

        mDBManager = new DBManager(getContext());
        mDBManager.open();

        adapter = new SimpleCursorAdapter(getContext(),
                R.layout.list_item, mDBManager.getProducts(), columns, txts, 0);
        adapter.notifyDataSetChanged();

        lv.setAdapter(adapter);

        lv.setOnItemClickListener((parent, view, position, id) -> {
            TextView name = view.findViewById(R.id.product_name);
            TextView price = view.findViewById(R.id.product_price);
            TextView category = view.findViewById(R.id.product_category);
            TextView productId = view.findViewById(R.id.product_id);


            Intent i = new Intent(getContext(), ManageProductsActivity.class);
            i.putExtra("id", productId.getText().toString());
            i.putExtra("name", name.getText().toString());
            i.putExtra("price", price.getText().toString());
            i.putExtra("category", category.getText().toString());

            startActivity(i);
        });

        return v;
    }

    @Override
    public void onDestroy() {
        mDBManager.close();
        super.onDestroy();
    }
}