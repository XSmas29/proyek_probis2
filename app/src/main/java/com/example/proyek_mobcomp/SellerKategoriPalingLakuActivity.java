package com.example.proyek_mobcomp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.example.proyek_mobcomp.classFolder.cTopup;
import com.example.proyek_mobcomp.databinding.ActivityCustomerTopUpSaldoBinding;
import com.example.proyek_mobcomp.databinding.ActivitySellerKategoriPalingLakuBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterCustomerHistoryTopUp;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterSellerKategoriPalingLaku;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellerKategoriPalingLakuActivity extends AppCompatActivity {

    protected ActivitySellerKategoriPalingLakuBinding binding;

    ArrayList<cKategori> arrKategori = new ArrayList<>();
    ArrayList<cProduct> arrProduct = new ArrayList<>();
    ArrayList<cDetailPurchase> arrDTrans = new ArrayList<>();

    RecyclerAdapterSellerKategoriPalingLaku recyclerAdapterSellerKategoriPalingLaku;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_kategori_paling_laku);
        binding = ActivitySellerKategoriPalingLakuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getDataKategori();
    }

    public void getDataKategori(){
        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/seller/getkategoripalinglaku",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("message");

                            arrKategori = new ArrayList<>();
                            arrProduct = new ArrayList<>();
                            arrDTrans = new ArrayList<>();

                            JSONArray kategoriarray = jsonObject.getJSONArray("datakategori");
                            for (int i = 0; i < kategoriarray.length(); i++){
                                int id = kategoriarray.getJSONObject(i).getInt("id");
                                String nama = kategoriarray.getJSONObject(i).getString("nama");
                                String tipe = kategoriarray.getJSONObject(i).getString("tipe");

                                arrKategori.add(new cKategori(id, nama, tipe));
                            }

                            JSONArray arrayProduct = jsonObject.getJSONArray("dataproduct");
                            for (int i = 0; i < arrayProduct.length(); i++){
                                int id = arrayProduct.getJSONObject(i).getInt("id");
                                String fk_seller = arrayProduct.getJSONObject(i).getString("fk_seller");
                                int fk_kategori = arrayProduct.getJSONObject(i).getInt("fk_kategori");
                                String nama = arrayProduct.getJSONObject(i).getString("nama");
                                String deskripsi = arrayProduct.getJSONObject(i).getString("deskripsi");
                                int harga = arrayProduct.getJSONObject(i).getInt("harga");
                                int stok = arrayProduct.getJSONObject(i).getInt("stok");
                                String gambar = arrayProduct.getJSONObject(i).getString("gambar");
                                int is_deleted = arrayProduct.getJSONObject(i).getInt("is_deleted");

                                arrProduct.add(
                                        new cProduct(id, fk_seller, fk_kategori, nama, deskripsi, harga, stok, gambar, is_deleted)
                                );
                            }

                            JSONArray dtransArray = jsonObject.getJSONArray("datadtrans");
                            arrDTrans = new ArrayList<>();
                            for (int i = 0; i < dtransArray.length(); i++){
                                int id = dtransArray.getJSONObject(i).getInt("id");
                                int fk_htrans = dtransArray.getJSONObject(i).getInt("fk_htrans");
                                int fk_barang = dtransArray.getJSONObject(i).getInt("fk_barang");
                                int jumlah = dtransArray.getJSONObject(i).getInt("jumlah");
                                int subtotal = dtransArray.getJSONObject(i).getInt("subtotal");
                                int rating = 0;
                                String review = "";
                                String fk_seller = dtransArray.getJSONObject(i).getString("fk_seller");
                                String status = dtransArray.getJSONObject(i).getString("status");
                                String notes_seller = dtransArray.getJSONObject(i).getString("notes_seller");
                                if (notes_seller == null){
                                    notes_seller = "";
                                }
                                String notes_customer = dtransArray.getJSONObject(i).getString("notes_customer");
                                if (notes_customer== null){
                                    notes_customer = "";
                                }

                                arrDTrans.add(new cDetailPurchase(id, fk_htrans, fk_barang, jumlah,
                                        subtotal, rating, review, fk_seller, status, notes_seller, notes_customer));
                            }

                            setRv();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error get saldo " + error);
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

    protected void setRv() {
        binding.recyclerViewKategoriPalingLaku.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewKategoriPalingLaku.setHasFixedSize(true);

        recyclerAdapterSellerKategoriPalingLaku = new RecyclerAdapterSellerKategoriPalingLaku(
                arrKategori, arrProduct, arrDTrans
        );
        binding.recyclerViewKategoriPalingLaku.setAdapter(recyclerAdapterSellerKategoriPalingLaku);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }
}