package com.pucmm.isc581_parcial2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_product) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.list_product_fragment,
                            new ManageProductsFragment(),
                            "MANAGE_PRODUCTS")
                    .addToBackStack("MANAGE_PRODUCTS")
                    .commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("LIST_PRODUCTS");
        Log.d("KLK_COUNT", String.valueOf(count));
        if (count == 0) {
            super.onBackPressed();
        } else {
//            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.list_product_fragment, new ListProductsFragment()).commit();

            if (fragment instanceof ListProductsFragment) {
                Log.d("KLK", "YESSs");
            }
        }
    }
}