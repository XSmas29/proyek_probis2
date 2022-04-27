package com.example.proyek_mobcomp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyek_mobcomp.databinding.ActivitySellerBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SellerActivity extends AppCompatActivity {

    protected ActivitySellerBinding binding;
    public static String login;
    SellerTransaksiFragment sellerTransaksiFragment;

    public void showFragment(int idx) {
        Bundle bundle = new Bundle();
        bundle.putString("login", login);
        if (idx == 0){
            SellerDashboardFragment sellerDashboardFragment = new SellerDashboardFragment();
            sellerDashboardFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frContainer, sellerDashboardFragment);
            fragmentTransaction.commit();
        }
        else if (idx == 1){
            SellerListBarangFragment sellerListBarangFragment = new SellerListBarangFragment();
            sellerListBarangFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frContainer, sellerListBarangFragment);
            fragmentTransaction.commit();
        }
        else if (idx == 2){
            SellerSaldoFragment sellerSaldoFragment = new SellerSaldoFragment();
            sellerSaldoFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frContainer, sellerSaldoFragment);
            fragmentTransaction.commit();
        }
        else if (idx == 3){
            sellerTransaksiFragment = new SellerTransaksiFragment();
            sellerTransaksiFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frContainer, sellerTransaksiFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        binding = ActivitySellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        login = getIntent().getStringExtra("login");

        showFragment(0);

        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.sellerdashboard) {
                    showFragment(0);
                }else if(item.getItemId() == R.id.sellerlistbarang) {
                    showFragment(1);
                }
                else if(item.getItemId() == R.id.sellersaldo) {
                    showFragment(2);
                }
                else if(item.getItemId() == R.id.sellerhistory) {
                    showFragment(3);
                }
                return true;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*

            note :
            - requestCode = 200 digunakan sellerTransaksiFragment
            - requestCode = 300 digunakan sellerPendapatanActivity

         */

        if (requestCode == 200){ // digunakan sellerTransaksiFragment
            sellerTransaksiFragment.loadHistory();
        }
    }
}