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
import com.example.proyek_mobcomp.classFolder.cHeaderPurchase;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.example.proyek_mobcomp.classFolder.cReview;
import com.example.proyek_mobcomp.classFolder.cUser;
import com.example.proyek_mobcomp.databinding.ActivityCustomerDetailPurchaseBinding;
import com.example.proyek_mobcomp.databinding.ActivityCustomerHomeBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterDetailPurchase;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterReview;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerDetailPurchaseActivity extends AppCompatActivity {

    protected ActivityCustomerDetailPurchaseBinding binding;

    int idHTrans = -1;

    cHeaderPurchase htrans;
    ArrayList<cDetailPurchase> arrDTrans = new ArrayList<>();
    ArrayList<cReview> arrReview = new ArrayList<>();
    ArrayList<cUser> arrSeller = new ArrayList<>();
    ArrayList<cProduct> arrProduct = new ArrayList<>();

    RecyclerAdapterDetailPurchase recyclerAdapterDetailPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail_purchase);
        binding = ActivityCustomerDetailPurchaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        idHTrans = getIntent().getIntExtra("idHTrans", -1);

        if (idHTrans != -1){
            getTransData();
        }
    }

    public void getTransData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/customer/gettransdata",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject datahtrans = jsonObject.getJSONObject("datahtrans");
                            int id = datahtrans.getInt("id");
                            String fk_customer = datahtrans.getString("fk_customer");
                            int grandtotal = datahtrans.getInt("grandtotal");
                            String tanggal   = datahtrans.getString("tanggal");
                            //htrans = null;
                            htrans = new cHeaderPurchase(id, fk_customer, grandtotal, tanggal);

                            JSONArray dtransArray = jsonObject.getJSONArray("datadtrans");
                            arrDTrans = new ArrayList<>();
                            for (int i = 0; i < dtransArray.length(); i++){
                                id = dtransArray.getJSONObject(i).getInt("id");
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

                            JSONArray reviewArray = jsonObject.getJSONArray("datareview");
                            arrReview = new ArrayList<>();
                            for (int i = 0; i < reviewArray.length(); i++){
                                id = reviewArray.getJSONObject(i).getInt("id");
                                int fk_htrans = reviewArray.getJSONObject(i).getInt("fk_htrans");
                                int fk_dtrans = reviewArray.getJSONObject(i).getInt("fk_dtrans");
                                int star = reviewArray.getJSONObject(i).getInt("star");
                                String isi = reviewArray.getJSONObject(i).getString("isi");
                                String fk_user = reviewArray.getJSONObject(i).getString("fk_user");

                                arrReview.add(new cReview(id, fk_htrans, fk_dtrans,fk_user,star, isi));
                            }

                            JSONArray sellerArray = jsonObject.getJSONArray("dataseller");
                            arrSeller = new ArrayList<>();
                            for (int i = 0; i < sellerArray.length();i++) {
                                String username = sellerArray.getJSONObject(i).getString("username");
                                String nama = sellerArray.getJSONObject(i).getString("nama");
                                String toko = sellerArray.getJSONObject(i).getString("toko");
                                String gambar = sellerArray.getJSONObject(i).getString("gambar");
//                                Picasso.get().load(getResources().getString(R.string.url) + "/profile/" +
//                                        gambar).into(binding.imageViewProfileToko);

                                arrSeller.add(new cUser(username, nama, toko, gambar));
                            }

                            JSONArray productObject = jsonObject.getJSONArray("dataproduct");
                            for (int i = 0; i < productObject.length(); i ++){
                                id = productObject.getJSONObject(i).getInt("id");
                                String fk_seller = productObject.getJSONObject(i).getString("fk_seller");
                                int fk_kategori = productObject.getJSONObject(i).getInt("fk_kategori");
                                String nama = productObject.getJSONObject(i).getString("nama");
                                String deskripsi = productObject.getJSONObject(i).getString("deskripsi");
                                int harga = productObject.getJSONObject(i).getInt("harga");
                                int stok =productObject.getJSONObject(i).getInt("stok");
                                String gambar = productObject.getJSONObject(i).getString("gambar");
                                int is_deleted = productObject.getJSONObject(i).getInt("is_deleted");

                                cProduct product = new cProduct(id, fk_seller, fk_kategori, nama, deskripsi, harga, stok, gambar, is_deleted);
                                arrProduct.add(product);
                            }

                            showData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error get trans data in detail purchase act : " + error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function", "gettransdata");
                params.put("idhtrans", idHTrans+"");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showData() {
        binding.textViewIdHTrans.setText("Transaction ID #" + htrans.getId());
        binding.textViewGrandtotal.setText("Rp " + htrans.getGrandtotal());

        setRv();
    }

    public void setRv() {
        binding.recyclerViewCustDTrans.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewCustDTrans.setHasFixedSize(true);

        recyclerAdapterDetailPurchase = new RecyclerAdapterDetailPurchase(
                arrDTrans, arrReview,
                arrSeller, arrProduct, this
        );
        binding.recyclerViewCustDTrans.setAdapter(recyclerAdapterDetailPurchase);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }
}