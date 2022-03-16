package com.example.proyek_mobcomp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cKategori;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.example.proyek_mobcomp.classFolder.cUser;
import com.example.proyek_mobcomp.databinding.ActivityCustomerHomeBinding;
import com.example.proyek_mobcomp.databinding.ActivityCustomerSearchBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterCustomerHomeProduct;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterCustomerSearchProduct;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerSearchActivity extends AppCompatActivity {

    ActivityCustomerSearchBinding binding;
    ArrayList<cProduct> listSearch = new ArrayList<>();
    ArrayList<cKategori> listKategori = new ArrayList<>();
    ArrayList<String[]> listowner = new ArrayList<>();
    RecyclerAdapterCustomerSearchProduct recyclerAdapterCustomerSearchProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_search);

        binding = ActivityCustomerSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().hasExtra("keyword")){
            binding.txtKeyword.setText("Search Result for '" + getIntent().getStringExtra("keyword") + "'");
            loadSearch("All", "-1", "-1");
        }

        binding.btnBackfromSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadSearch(String kategori, String min, String max) {
        listSearch = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/customer/search",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            System.out.println(jsonObject);
                            JSONArray arraysearch = jsonObject.getJSONArray("databarang");
                            JSONArray arraykategori = jsonObject.getJSONArray("datakategori");

                            for (int i = 0; i < arraysearch.length(); i++){
                                int id = arraysearch.getJSONObject(i).getInt("id");
                                String fk_seller = arraysearch.getJSONObject(i).getString("fk_seller");
                                int fk_kategori = arraysearch.getJSONObject(i).getInt("fk_kategori");
                                String nama = arraysearch.getJSONObject(i).getString("nama");
                                String deskripsi = arraysearch.getJSONObject(i).getString("deskripsi");
                                int harga = arraysearch.getJSONObject(i).getInt("harga");
                                int stok = arraysearch.getJSONObject(i).getInt("stok");
                                String gambar = arraysearch.getJSONObject(i).getString("gambar");
                                int is_deleted = arraysearch.getJSONObject(i).getInt("is_deleted");

                                //data toko
                                JSONObject dataowner = arraysearch.getJSONObject(i).getJSONObject("owner");
                                String namatoko = dataowner.getString("toko");
                                String gambartoko = dataowner.getString("gambar");

                                listSearch.add(
                                        new cProduct(id, fk_seller, fk_kategori, nama, deskripsi, harga, stok, gambar, is_deleted)
                                );

                                listowner.add(new String[] {namatoko, gambartoko});
                            }
                            for (int i = 0; i < arraykategori.length(); i++) {
                                int id = arraykategori.getJSONObject(i).getInt("id");
                                String nama = arraykategori.getJSONObject(i).getString("nama");
                                String tipe = arraykategori.getJSONObject(i).getString("tipe");

                                listKategori.add(new cKategori(id, nama, tipe));
                            }
                            setRvSearchGrid();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error search : " + error.getMessage());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("keyword", String.valueOf(getIntent().getStringExtra("keyword")));
                params.put("kategori", kategori);
                params.put("min", min);
                params.put("max", max);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setRvSearchGrid() {
        binding.rvSearch.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvSearch.setHasFixedSize(true);

        recyclerAdapterCustomerSearchProduct = new RecyclerAdapterCustomerSearchProduct(listSearch, listowner);
        binding.rvSearch.setAdapter(recyclerAdapterCustomerSearchProduct);

    }

    public void showFilter(View v){
        BottomSheetDialog filterDialog = new BottomSheetDialog(this, R.style.DialogTheme);
        View BottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_filter, (ConstraintLayout)findViewById(R.id.dialogFilterContainer));

        Spinner spKategori = BottomSheetView.findViewById(R.id.spFilterKategori);
        EditText edMin = BottomSheetView.findViewById(R.id.edFilterMin);
        EditText edMax= BottomSheetView.findViewById(R.id.edFilterMax);

        BottomSheetView.findViewById(R.id.btnApplyFilter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSearch(spKategori.getSelectedItem().toString(), edMin.getText().toString(),edMax.getText().toString());
                filterDialog.dismiss();
            }
        });
        ArrayList<String> listNamaKategori = new ArrayList<>();
        listNamaKategori.add("All");
        for (int i = 0; i < listKategori.size(); i++) {
            listNamaKategori.add(listKategori.get(i).getNama());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listNamaKategori);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKategori.setAdapter(adapter);
        filterDialog.setContentView(BottomSheetView);
        filterDialog.show();
    }
}