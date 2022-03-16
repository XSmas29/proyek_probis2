package com.example.proyek_mobcomp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyek_mobcomp.databinding.ActivityAdminBinding;
import com.example.proyek_mobcomp.databinding.ActivityMainBinding;
import com.example.proyek_mobcomp.databinding.FragmentAdminConfTopUpBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    protected ActivityAdminBinding binding;

    public static String login;

    AdminConfTopUpFragment adminConfTopUpFragment;
    AdminConfWithdrawFragment adminConfWithdrawFragment;

    public void showFragment(int idx) {
        Bundle bundle = new Bundle();
        bundle.putString("login", login);
        if (idx == 0){
            AdminDashboardFragment adminDashboardFragment = new AdminDashboardFragment();
            adminDashboardFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frContainer, adminDashboardFragment);
            fragmentTransaction.commit();
        }else if (idx == 1){
            AdminMasterUserFragment adminMasterUserFragment = new AdminMasterUserFragment();
            adminMasterUserFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frContainer, adminMasterUserFragment);
            fragmentTransaction.commit();
        }else if (idx == 2){
            adminConfTopUpFragment = new AdminConfTopUpFragment();
            adminConfTopUpFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frContainer, adminConfTopUpFragment);
            fragmentTransaction.commit();
        }else if (idx == 3){
            adminConfWithdrawFragment = new AdminConfWithdrawFragment();
            adminConfWithdrawFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frContainer, adminConfWithdrawFragment);
            fragmentTransaction.commit();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        login = getIntent().getStringExtra("login");

        showFragment(0);

        binding.navBottom.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.bottomdashboard) {
                    showFragment(0);
                }
                else if(item.getItemId() == R.id.bottommasteruser) {
                    showFragment(1);
                }
                else if(item.getItemId() == R.id.bottomconftopup) {
                    showFragment(2);
                }
                else if(item.getItemId() == R.id.bottomconfwithdraw) {
                    showFragment(3);
                }
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100){ // dari konfirmasi top up
            adminConfTopUpFragment.getTopUpData();
        }else if (requestCode == 110){ // dari konfirmasi withdraw
            adminConfWithdrawFragment.getTopUpData();
        }
    }
}