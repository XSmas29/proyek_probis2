package com.example.proyek_mobcomp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cKategori;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.example.proyek_mobcomp.classFolder.cTopup;
import com.example.proyek_mobcomp.databinding.FragmentCustomerHeaderPurchaseHistoryBinding;
import com.example.proyek_mobcomp.databinding.FragmentSellerSaldoBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterSellerListProduct;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterSellerTopup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellerSaldoFragment extends Fragment {

    FragmentSellerSaldoBinding binding;
    ArrayList<cTopup> listTopup = new ArrayList<>();
    RecyclerAdapterSellerTopup recyclerAdapterSellerTopup;

    public static SellerSaldoFragment newInstance() {
        SellerSaldoFragment fragment = new SellerSaldoFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentSellerSaldoBinding.inflate(inflater, container, false);

        getData();
        binding.btnCairkanSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cairkanSaldo();
            }
        });

        return binding.getRoot();
    }

    public void cairkanSaldo(){
        if (binding.edCairkanSaldo.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Jumlah Pencairan Harus diisi!", Toast.LENGTH_SHORT).show();
        }
        else if (Integer.parseInt(binding.edCairkanSaldo.getText().toString()) < 10000){
            Toast.makeText(getActivity(), "Jumlah Pencairan Minimal Rp. 10000!", Toast.LENGTH_SHORT).show();
        }
        else{
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    getResources().getString(R.string.url) + "/seller/cairkansaldo",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                int code = jsonObject.getInt("code");
                                if (code == 1){
                                    Toast.makeText(getActivity(), "Berhasil Melakukan Request Pencairan Dana!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getActivity(), "Saldo Tidak Mencukupi!", Toast.LENGTH_SHORT).show();
                                }
                                getData();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("error cairkan saldo : " + error);
                        }
                    }
            ){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("login", SellerActivity.login);
                    params.put("jumlah", binding.edCairkanSaldo.getText().toString());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/seller/getsaldo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray arraytopup = jsonObject.getJSONArray("datatopup");
                            JSONObject datasaldo = jsonObject.getJSONObject("saldo");
                            int saldo = datasaldo.getInt("saldo");

                            binding.txtSaldoSeller.setText("Saldo Anda : Rp. " + saldo);

                            for (int i = 0; i < arraytopup.length(); i++) {
                                int id = arraytopup.getJSONObject(i).getInt("id");
                                String username = arraytopup.getJSONObject(i).getString("fk_username");
                                int jumlah = arraytopup.getJSONObject(i).getInt("jumlah_topup");
                                String bukti = arraytopup.getJSONObject(i).getString("bukti_topup");
                                int status = arraytopup.getJSONObject(i).getInt("status_topup");
                                String created = arraytopup.getJSONObject(i).getString("created_at");
                                String updated = arraytopup.getJSONObject(i).getString("updated_at");
                                listTopup.add(new cTopup(id, username, jumlah, bukti, status, created, updated));
                            }
                            SetupRv();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error load data saldo : " + error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("login", SellerActivity.login);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void SetupRv() {
        binding.rvHistorySaldo.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvHistorySaldo.setHasFixedSize(true);

        recyclerAdapterSellerTopup = new RecyclerAdapterSellerTopup(listTopup);
        binding.rvHistorySaldo.setAdapter(recyclerAdapterSellerTopup);
    }
}