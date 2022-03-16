package com.example.proyek_mobcomp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cKategori;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.example.proyek_mobcomp.databinding.ActivitySellerAddEditBarangBinding;
import com.example.proyek_mobcomp.databinding.FragmentSellerListBarangBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterCustomerSearchProduct;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterSellerListProduct;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellerListBarangFragment extends Fragment {

    FragmentSellerListBarangBinding binding;

    ArrayList<cProduct> listProduk = new ArrayList<>();
    ArrayList<cKategori> listKategori = new ArrayList<>();
    RecyclerAdapterSellerListProduct recyclerAdapterSellerListProduct;
    ArrayList<String> listNamaKategori;
    public static SellerListBarangFragment newInstance() {
        SellerListBarangFragment fragment = new SellerListBarangFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!binding.spFilterSellerKategori.getSelectedItem().toString().equalsIgnoreCase("semua kategori")){
            loadproduk(binding.spFilterSellerKategori.getSelectedItem().toString());
        }
        else{
            loadproduk("All");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSellerListBarangBinding.inflate(getLayoutInflater());
        getkategori();
        loadproduk("All");

        binding.btnAddProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SellerAddEditBarangActivity.class);
                startActivityForResult(i, 1);
            }
        });

        binding.spFilterSellerKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(adapterView.getSelectedItem().toString());
                if (!adapterView.getSelectedItem().toString().equalsIgnoreCase("semua kategori")){
                    loadproduk(adapterView.getSelectedItem().toString());
                }
                else{
                    loadproduk("All");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return binding.getRoot();
    }

    private void getkategori(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/seller/getkategori",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray arraykategori = jsonObject.getJSONArray("datakategori");

                            for (int i = 0; i < arraykategori.length(); i++) {
                                int id = arraykategori.getJSONObject(i).getInt("id");
                                String nama = arraykategori.getJSONObject(i).getString("nama");
                                String tipe = arraykategori.getJSONObject(i).getString("tipe");

                                listKategori.add(new cKategori(id, nama, tipe));
                            }

                            listNamaKategori = new ArrayList<>();
                            listNamaKategori.add("Semua Kategori");
                            for (int i = 0; i < listKategori.size(); i++) {
                                listNamaKategori.add(listKategori.get(i).getNama());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listNamaKategori);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spFilterSellerKategori.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error load kategori : " + error.getMessage());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void loadproduk(String kategori) {
        listProduk = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/seller/listproduk",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray arrayproduk = jsonObject.getJSONArray("dataproduk");
                            for (int i = 0; i < arrayproduk.length(); i++){
                                int id = arrayproduk.getJSONObject(i).getInt("id");
                                String fk_seller = arrayproduk.getJSONObject(i).getString("fk_seller");
                                int fk_kategori = arrayproduk.getJSONObject(i).getInt("fk_kategori");
                                String nama = arrayproduk.getJSONObject(i).getString("nama");
                                String deskripsi = arrayproduk.getJSONObject(i).getString("deskripsi");
                                int harga = arrayproduk.getJSONObject(i).getInt("harga");
                                int stok = arrayproduk.getJSONObject(i).getInt("stok");
                                String gambar = arrayproduk.getJSONObject(i).getString("gambar");
                                int is_deleted = arrayproduk.getJSONObject(i).getInt("is_deleted");

                                listProduk.add(new cProduct(id, fk_seller, fk_kategori, nama, deskripsi, harga, stok, gambar, is_deleted));
                            }
                            SetupRvProduk();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error load my produk : " + error.getMessage());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("login", SellerActivity.login);
                params.put("kategori", kategori);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void SetupRvProduk() {
        binding.rvListProduk.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvListProduk.setHasFixedSize(true);

        recyclerAdapterSellerListProduct = new RecyclerAdapterSellerListProduct(listProduk);
        recyclerAdapterSellerListProduct.setOnItemClickCallback(new RecyclerAdapterSellerListProduct.OnItemClickCallback() {
            @Override
            public void onItemClicked(cProduct product) {
                Intent i = new Intent(getActivity(), SellerAddEditBarangActivity.class);
                i.putExtra("produk", product);
                startActivityForResult(i, 2);
            }
        });
        binding.rvListProduk.setAdapter(recyclerAdapterSellerListProduct);
    }
}