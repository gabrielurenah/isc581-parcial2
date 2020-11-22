package com.pucmm.isc581_parcial2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ManageCategoriesFragment extends Fragment {

    private EditText mEditTextCategory;
    private Button mButtonSave;

    private DBManager mDBManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        View view = inflater.inflate(R.layout.fragment_manage_categories, container, false);

        mEditTextCategory = view.findViewById(R.id.input_category);
        mButtonSave = view.findViewById(R.id.btn_save_category);

        mButtonSave.setOnClickListener(v -> {
            String category = mEditTextCategory.getText().toString();

            if (category.trim().length() <= 0) {
                Toast.makeText(getContext(),
                        "Favor introducir una categoria", Toast.LENGTH_SHORT).show();

                return;
            }

            mDBManager = new DBManager(getContext());
            mDBManager.createCategory(category);

            mEditTextCategory.setText("");

            Toast.makeText(getContext(),
                    "Categoria agregada con exito", Toast.LENGTH_SHORT).show();

            getFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment, new ManageProductsFragment(), "MANAGE_PRODUCTS")
                    .commit();
        });
        return view;
    }

    @Override
    public void onDestroy() {
        mDBManager.close();
        super.onDestroy();
    }
}