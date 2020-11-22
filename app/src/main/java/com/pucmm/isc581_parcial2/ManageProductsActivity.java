package com.pucmm.isc581_parcial2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class ManageProductsActivity extends AppCompatActivity {

    private EditText mEditTextName;
    private EditText mEditTextPrice;
    private Button mBtnAdd;
    private Button mBtnSave;
    private Button mBtnUpdate;
    private Button mBtnRemove;
    private Spinner mSpinnerCategory;
    private ArrayAdapter<String> mAdapter;

    private DBManager dbManager;

    private long _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);
        setTitle("Agregar Producto");
        mEditTextName = findViewById(R.id.input_name);
        mEditTextPrice = findViewById(R.id.input_price);
        mBtnAdd = findViewById(R.id.btn_add);
        mBtnSave = findViewById(R.id.btn_save);
        mBtnUpdate = findViewById(R.id.btn_update);
        mBtnRemove = findViewById(R.id.btn_remove);

        //load spinner
        mSpinnerCategory = findViewById(R.id.spinner_category);
        loadSpinnerData();

        dbManager = new DBManager(this);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            setTitle("Modificar Producto");
            mEditTextName.setText((String)bundle.get("name"));
            mEditTextPrice.setText((String)bundle.get("price"));
            _id = Long.parseLong((String)bundle.get("id"));
            mSpinnerCategory.post(() ->
                    mSpinnerCategory.setSelection(mAdapter.getPosition((String)bundle.get("category"))));

            mBtnSave.setVisibility(View.GONE);
            mBtnUpdate.setVisibility(View.VISIBLE);
            mBtnRemove.setVisibility(View.VISIBLE);
        }

        mBtnAdd.setOnClickListener(v -> startActivity(new Intent(this,ManageCategoriesActivity.class)));

        mBtnSave.setOnClickListener(v ->  manageProduct(bundle));
        mBtnUpdate.setOnClickListener(v -> manageProduct(bundle));
        mBtnRemove.setOnClickListener(v -> {
            dbManager.removeProduct(_id);
            Toast.makeText(this, "Producto removido con exito", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        });
    }

    @Override
    protected void onResume() {
        loadSpinnerData();
        super.onResume();
    }

    private void loadSpinnerData() {
        DBManager dbManager = new DBManager(getApplicationContext());

        List<String> allCategories = dbManager.getCategories();

        mAdapter = new ArrayAdapter<>(
                getApplicationContext(), android.R.layout.simple_spinner_item, allCategories);

        mAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        mSpinnerCategory.setAdapter(mAdapter);
    }

    private void manageProduct(Bundle bundle) {
        String name = mEditTextName.getText().toString();
        String price = mEditTextPrice.getText().toString();
        String category = mSpinnerCategory.getSelectedItem().toString();

        String ACTION = "creado";
        if (bundle != null) {
            dbManager.updateProducts(_id, name, price, category);
            ACTION = "actualizado";
        } else {
            dbManager.createProduct(name, price, category);
        }

        Toast.makeText(this, "Producto " + ACTION + " con exito", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, MainActivity.class));
    }
}