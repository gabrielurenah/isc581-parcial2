package com.pucmm.isc581_parcial2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ManageCategoriesActivity extends AppCompatActivity {

    private EditText mEditTextCategory;
    private Button mButtonSave;

    private DBManager mDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);
        mEditTextCategory = findViewById(R.id.input_category);
        mButtonSave = findViewById(R.id.btn_save_category);

        mButtonSave.setOnClickListener(v -> {
            String category = mEditTextCategory.getText().toString();

            if (category.trim().length() <= 0) {
                Toast.makeText(getApplicationContext(),
                        "Favor introducir una categoria", Toast.LENGTH_SHORT).show();

                return;
            }

            mDBManager = new DBManager(getApplicationContext());
            mDBManager.createCategory(category);

            mEditTextCategory.setText("");

            Toast.makeText(getApplicationContext(),
                    "Categoria agregada con exito", Toast.LENGTH_SHORT).show();

            finish();
        });
    }

    @Override
    protected void onDestroy() {
        mDBManager.close();
        super.onDestroy();
    }
}