package com.pucmm.isc581_parcial2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class ManageProductsFragment extends Fragment {

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        View view = inflater.inflate(R.layout.fragment_manage_products, container, false);

        getActivity().setTitle("Agregar Producto");
        mEditTextName = view.findViewById(R.id.input_name);
        mEditTextPrice = view.findViewById(R.id.input_price);
        mBtnAdd = view.findViewById(R.id.btn_add);
        mBtnSave = view.findViewById(R.id.btn_save);
        mBtnUpdate = view.findViewById(R.id.btn_update);
        mBtnRemove = view.findViewById(R.id.btn_remove);

        //load spinner
        mSpinnerCategory = view.findViewById(R.id.spinner_category);
        loadSpinnerData();

        dbManager = new DBManager(getContext());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            getActivity().setTitle("Modificar Producto");

            //load data
            mEditTextName.setText(bundle.getString("name"));
            mEditTextPrice.setText(bundle.getString("price"));
            _id = Long.parseLong(bundle.getString("id"));
            mSpinnerCategory.post(() ->
                    mSpinnerCategory.setSelection(mAdapter.getPosition((String)bundle.get("category"))));

            // manage buttons visibility
            mBtnSave.setVisibility(View.GONE);
            mBtnUpdate.setVisibility(View.VISIBLE);
            mBtnRemove.setVisibility(View.VISIBLE);
        }

        mBtnAdd.setOnClickListener(v -> startActivity(new Intent(getContext(),ManageCategoriesActivity.class)));

        mBtnSave.setOnClickListener(v ->  manageProduct(bundle));
        mBtnUpdate.setOnClickListener(v -> manageProduct(bundle));
        mBtnRemove.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext()).setTitle("Borrar Producto")
            .setMessage("Esta seguro que desea remover este producto?")
            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                dbManager.removeProduct(_id);
                Toast.makeText(getContext(), "Producto removido con exito", Toast.LENGTH_SHORT).show();

                // go to previous fragment
                getFragmentManager().beginTransaction()
                        .replace(R.id.list_product_fragment, new ListProductsFragment(), "LIST_PRODUCTS")
                        .addToBackStack("LIST_PRODUCTS").commit();
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show();
        });
        return view;
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

        Toast.makeText(getContext(), "Producto " + ACTION + " con exito", Toast.LENGTH_SHORT).show();

        getFragmentManager().beginTransaction()
                .replace(R.id.list_product_fragment, new ListProductsFragment(), "LIST_PRODUCTS")
                .addToBackStack("LIST_PRODUCTS").commit();
    }

    public void loadSpinnerData() {
        DBManager dbManager = new DBManager(getContext());

        List<String> allCategories = dbManager.getCategories();

        mAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, allCategories);

        mAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        mSpinnerCategory.setAdapter(mAdapter);
    }
}
