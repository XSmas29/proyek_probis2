package com.example.proyek_mobcomp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cDetailPurchase;
import com.example.proyek_mobcomp.classFolder.cKategori;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.example.proyek_mobcomp.databinding.ActivitySellerBarangPalingLakuBinding;
import com.example.proyek_mobcomp.databinding.ActivitySellerKategoriPalingLakuBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterSellerBarangPalingLaku;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterSellerKategoriPalingLaku;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SellerBarangPalingLakuActivity extends AppCompatActivity {

    protected ActivitySellerBarangPalingLakuBinding binding;
    ArrayList<cProduct> arrProduct = new ArrayList<>();
    ArrayList<cDetailPurchase> arrDTrans = new ArrayList<>();
    RecyclerAdapterSellerBarangPalingLaku recyclerAdapterSellerBarangPalingLaku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_barang_paling_laku);
        binding = ActivitySellerBarangPalingLakuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getBarangPalingLaku();
    }

    private void getBarangPalingLaku() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/seller/getbarangpalinglaku",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray arrbarang = jsonObject.getJSONArray("databarang");
                            JSONArray arrjumlah = jsonObject.getJSONArray("datajumlah");

                            ArrayList<String> listBarang = new ArrayList<>();
                            ArrayList<Integer> listJumlah = new ArrayList<>();
                            if (arrbarang != null) {
                                int len = arrbarang.length();
                                for (int i=0;i<len;i++){
                                    listBarang.add(arrbarang.get(i).toString());
                                }
                            }
                            if (arrjumlah != null) {
                                int len = arrjumlah.length();
                                for (int i=0;i<len;i++){
                                    listJumlah.add(Integer.parseInt((arrjumlah.get(i).toString())));
                                }
                            }
                            System.out.println(listBarang);
                            System.out.println(listJumlah);

                            setRv(listBarang, listJumlah);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error get barang paling laku " + error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", SellerActivity.login+"");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this );
        requestQueue.add(stringRequest);
    }

    protected void setRv(ArrayList<String> listBarang, ArrayList<Integer> listJumlah) {
        binding.recyclerViewBarangPalingLaku.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewBarangPalingLaku.setHasFixedSize(true);
        recyclerAdapterSellerBarangPalingLaku = new RecyclerAdapterSellerBarangPalingLaku(
                listBarang, listJumlah
        );
        binding.recyclerViewBarangPalingLaku.setAdapter(recyclerAdapterSellerBarangPalingLaku);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }
}