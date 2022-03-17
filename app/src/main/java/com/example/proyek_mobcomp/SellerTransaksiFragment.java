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
import com.example.proyek_mobcomp.databinding.FragmentCustomerHomeBinding;
import com.example.proyek_mobcomp.databinding.FragmentSellerTransaksiBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterCustomerSearchProduct;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterSellerTransaksi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*

    Update Change =
    - 17 Maret 2022 : Sudah bisa menampilkan sesuai username seller

 */

public class SellerTransaksiFragment extends Fragment {

    FragmentSellerTransaksiBinding binding;
    ArrayList<cDetailPurchase> listTrans = new ArrayList<>();
    ArrayList<cProduct> listBarangTrans = new ArrayList<>();

    RecyclerAdapterSellerTransaksi recyclerAdapterSellerTransaksi;

    public static SellerTransaksiFragment newInstance() {
        SellerTransaksiFragment fragment = new SellerTransaksiFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSellerTransaksiBinding.inflate(inflater, container, false);
        binding.spFilterTransaksiSeller.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadHistory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        loadHistory();
        return binding.getRoot();
    }

    public void resetSpinner(){
        binding.spFilterTransaksiSeller.setSelection(0);
        loadHistory();
    }

    public void loadHistory() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/seller/gettransaksi",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            listBarangTrans = new ArrayList<>();
                            listTrans = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray arraytrans = jsonObject.getJSONArray("datatrans");

                            for (int i = 0; i < arraytrans.length(); i++) {
                                int id = arraytrans.getJSONObject(i).getInt("id");
                                int fk_htrans = arraytrans.getJSONObject(i).getInt("fk_htrans");
                                int fk_barang = arraytrans.getJSONObject(i).getInt("fk_barang");
                                int jumlah = arraytrans.getJSONObject(i).getInt("jumlah");
                                int subtotal = arraytrans.getJSONObject(i).getInt("subtotal");
                                int rating = arraytrans.getJSONObject(i).optInt("rating", -1);
                                String review = arraytrans.getJSONObject(i).getString("review");
                                String fk_seller = arraytrans.getJSONObject(i).getString("fk_seller");
                                String status = arraytrans.getJSONObject(i).getString("status");
                                String notes_seller = arraytrans.getJSONObject(i).getString("notes_seller");
                                String notes_customer = arraytrans.getJSONObject(i).getString("notes_customer");

                                listTrans.add(new cDetailPurchase(id, fk_htrans, fk_barang, jumlah, subtotal, rating, review, fk_seller, status, notes_seller, notes_customer));

                                JSONObject barangtrans = arraytrans.getJSONObject(i).getJSONObject("product");

                                int idbarang = barangtrans.getInt("id");
                                String fk_sellerbarang = barangtrans.getString("fk_seller");
                                int fk_kategori = barangtrans.getInt("fk_kategori");
                                String nama = barangtrans.getString("nama");
                                String desc = barangtrans.getString("deskripsi");
                                int harga = barangtrans.getInt("harga");
                                int stok = barangtrans.getInt("stok");
                                String gambar = barangtrans.getString("gambar");
                                int is_deleted = barangtrans.getInt("is_deleted");

                                listBarangTrans.add(new cProduct(idbarang, fk_sellerbarang, fk_kategori, nama, desc, harga, stok, gambar, is_deleted));
                            }
                            SetupRvTrans();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error load transaksi : " + error.getMessage());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", SellerActivity.login+"");
                params.put("status", binding.spFilterTransaksiSeller.getSelectedItem().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void SetupRvTrans() {
        binding.rvTrans.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTrans.setHasFixedSize(true);
        recyclerAdapterSellerTransaksi = new RecyclerAdapterSellerTransaksi(listTrans, listBarangTrans);
        recyclerAdapterSellerTransaksi.setOnItemClickCallback(new RecyclerAdapterSellerTransaksi.OnItemClickCallback() {
            @Override
            public void onItemClicked(cProduct produk, cDetailPurchase detail) {
                Intent i = new Intent(getActivity(), SellerDetailTransaksiActivity.class);
                i.putExtra("detail", detail);
                i.putExtra("produk", produk);
                getActivity().startActivityForResult(i, 200);
            }
        });
        binding.rvTrans.setAdapter(recyclerAdapterSellerTransaksi);
    }
}