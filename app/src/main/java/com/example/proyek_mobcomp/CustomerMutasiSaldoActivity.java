package com.example.proyek_mobcomp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cMutasi;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.example.proyek_mobcomp.databinding.ActivityCustomerMutasiSaldoBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterMutasiSaldo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerMutasiSaldoActivity extends AppCompatActivity {

    ActivityCustomerMutasiSaldoBinding binding;
    RecyclerAdapterMutasiSaldo recyclerAdapterMutasiSaldo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_mutasi_saldo);
        binding = ActivityCustomerMutasiSaldoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadMutasi();
    }

    private void loadMutasi() {
        ArrayList<cMutasi> listMutasi = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/customer/getmutasisaldo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            System.out.println(jsonObject);
                            JSONArray arraymutasi = jsonObject.getJSONArray("datamutasi");
                            for (int i = 0; i < arraymutasi.length(); i++){
                                int id = arraymutasi.getJSONObject(i).getInt("id");
                                int jumlah = arraymutasi.getJSONObject(i).getInt("jumlah");
                                String fk_user = arraymutasi.getJSONObject(i).getString("fk_user");
                                String tanggal = arraymutasi.getJSONObject(i).getString("tanggal");
                                String keterangan = arraymutasi.getJSONObject(i).getString("keterangan");

                                listMutasi.add(new cMutasi(id, jumlah, fk_user, tanggal, keterangan));
                            }
                            SetupRvMutasiSaldo(listMutasi);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error load mutasi : " + error.getMessage());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", CustomerHomeActivity.login);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void SetupRvMutasiSaldo(ArrayList<cMutasi> listMutasi) {
        binding.rvMutasiSaldoCustomer.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMutasiSaldoCustomer.setHasFixedSize(true);
        System.out.println(listMutasi.size());
        recyclerAdapterMutasiSaldo = new RecyclerAdapterMutasiSaldo(listMutasi);
        binding.rvMutasiSaldoCustomer.setAdapter(recyclerAdapterMutasiSaldo);
    }
}