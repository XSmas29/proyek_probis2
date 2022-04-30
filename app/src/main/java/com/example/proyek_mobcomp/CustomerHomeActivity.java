package com.example.proyek_mobcomp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SearchView;

import com.example.proyek_mobcomp.classFolder.cKategori;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.example.proyek_mobcomp.classFolder.cWishlist;
import com.example.proyek_mobcomp.databinding.ActivityCustomerHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class CustomerHomeActivity extends AppCompatActivity {
    protected ActivityCustomerHomeBinding binding;

    public static ArrayList<cKategori> arrayListKategori = new ArrayList<>();
    public static ArrayList<cProduct> arrayListProduct = new ArrayList<>();
    public static ArrayList<cWishlist> arrayListWishlist = new ArrayList<>();

    public static ArrayList<cProduct> listCarousel = new ArrayList<>();

    public static String login;

    CustomerWishlistFragment customerWishlistFragment;
    CustomerCartFragment customerCartFragment;

    public void showFragment(int idx) {

        Bundle bundle = new Bundle();
        bundle.putString("login", login);
        if (idx == 0){
            CustomerHomeFragment customerHomeFragment = new CustomerHomeFragment();
            customerHomeFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frContainer, customerHomeFragment);
            fragmentTransaction.commit();
        }else if (idx == 1){
            CustomerHeaderPurchaseHistoryFragment customerHeaderPurchaseHistoryFragment = new CustomerHeaderPurchaseHistoryFragment();
            customerHeaderPurchaseHistoryFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frContainer, customerHeaderPurchaseHistoryFragment);
            fragmentTransaction.commit();
        }else if (idx == 2){
            customerWishlistFragment = new CustomerWishlistFragment();
            customerWishlistFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frContainer, customerWishlistFragment);
            fragmentTransaction.commit();
        }else if (idx == 3){
            customerCartFragment = new CustomerCartFragment();
            customerCartFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frContainer, customerCartFragment);
            fragmentTransaction.commit();
        }
    }


    private void showPopUp(View v) {
        //buat popup menu nya dulu
        PopupMenu popupMenu = new PopupMenu(this, v);
        this.getMenuInflater().inflate(R.menu.optionsmenucustomer, popupMenu.getMenu());

        //event saat menu diklik
        //alt + enter kedua -> implements onMenuItemClick
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.profile){
                    Intent i = new Intent(CustomerHomeActivity.this, CustomerProfileActivity.class);
                    startActivity(i);
                }else if(item.getItemId() == R.id.topup_saldo){
                    Intent i = new Intent(CustomerHomeActivity.this, CustomerTopUpSaldoActivity.class);
                    startActivity(i);
                }else if(item.getItemId() == R.id.mutasi_saldo){
                    Intent i = new Intent(CustomerHomeActivity.this, CustomerMutasiSaldoActivity.class);
                    startActivity(i);
                }else if(item.getItemId() == R.id.logout){
                    SharedPreferences sharedpreferences = getSharedPreferences("data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove("login");
                    editor.commit();
                    finish();
                }
                return true;
            }
        });

        //munculkan popupmenu
        popupMenu.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        binding = ActivityCustomerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        login = getIntent().getStringExtra("login");

        showFragment(0);

        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.customerhome) {
                    showFragment(0);
                }else if(item.getItemId() == R.id.customertransaksi) {
                    showFragment(1);
                }
                else if(item.getItemId() == R.id.customerwishlist) {
                    showFragment(2);
                }
                else if(item.getItemId() == R.id.customercart) {
                    showFragment(3);
                }
                return true;
            }
        });


        binding.btnMoreCustHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v);
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent i = new Intent(CustomerHomeActivity.this, CustomerSearchActivity.class);
                i.putExtra("keyword", s);
                startActivity(i);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 120) { // dari customer wishlist fragment
            customerWishlistFragment.getWishlistData();
        }else if(requestCode == 130) { // dari csutomer cart fragment
//            if(resultCode == Activity.RESULT_OK) {
                customerCartFragment.setRv();
//            }
        }
    }
}