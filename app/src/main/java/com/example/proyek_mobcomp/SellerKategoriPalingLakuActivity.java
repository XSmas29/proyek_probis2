package com.example.proyek_mobcomp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.proyek_mobcomp.databinding.ActivityCustomerTopUpSaldoBinding;
import com.example.proyek_mobcomp.databinding.ActivitySellerKategoriPalingLakuBinding;

public class SellerKategoriPalingLakuActivity extends AppCompatActivity {

    protected ActivitySellerKategoriPalingLakuBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_kategori_paling_laku);
        binding = ActivitySellerKategoriPalingLakuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}