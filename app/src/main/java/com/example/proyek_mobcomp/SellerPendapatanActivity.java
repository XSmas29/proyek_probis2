package com.example.proyek_mobcomp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

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
import com.example.proyek_mobcomp.databinding.ActivitySellerPendapatanBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterSellerPendapatan;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterSellerTransaksi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellerPendapatanActivity extends AppCompatActivity {

    /*

        Created at : 28 April 2022 untuk menampilkan data pendapatan seller

     */

    ActivitySellerPendapatanBinding binding;

    RecyclerAdapterSellerPendapatan recyclerAdapterSellerPendapatan;


    ArrayList<cHeaderPurchase> listHTrans = new ArrayList<>();
    ArrayList<cDetailPurchase> listDTrans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_pendapatan);

        binding = ActivitySellerPendapatanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        filterStatus();
        filterBulan();
        filterTahun();
        resetAllFilter();

        getDataPendapatan();

        binding.imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void resetAllFilter() {
        binding.btnResetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.spFilterSellerPendapatan.setSelection(0);
                binding.spFilterBulan.setSelection(0);
                binding.spFilterTahun.setSelection(0);
            }
        });
    }

    public void getDataPendapatan() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/seller/getdatapendapatan",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            listDTrans = new ArrayList<>();
                            listHTrans = new ArrayList<>();
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

                                listDTrans.add(new cDetailPurchase(id, fk_htrans, fk_barang, jumlah, subtotal, rating, review, fk_seller, status, notes_seller, notes_customer));

                            }

                            JSONArray arrayhtrans = jsonObject.getJSONArray("datahtrans");

                            for (int i = 0; i < arrayhtrans.length(); i++) {
                                /*
                                    ditambahkan pada tanggal : 18 April

                                 */
                                int id = arrayhtrans.getJSONObject(i).getInt("id");
                                String fk_customer = arrayhtrans.getJSONObject(i).getString("fk_customer");
                                int grandtotal = arrayhtrans.getJSONObject(i).getInt("grandtotal");
                                String tanggal = arrayhtrans.getJSONObject(i).getString("tanggal");

                                listHTrans.add(new cHeaderPurchase(id, fk_customer, grandtotal, tanggal));
                            }

//                            System.out.println("index " + listBarangTrans.size() + "  " + listTrans.size() + "  " + listHTrans.size() + "  ");
                            setupRvTrans();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error get Pendapatan Seller : " + error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", SellerActivity.login+"");
                params.put("status", binding.spFilterSellerPendapatan.getSelectedItem().toString());

                if (binding.spFilterBulan.getSelectedItemPosition() != 0 && binding.spFilterTahun.getSelectedItemPosition() != 0){
                    params.put("bulan", String.valueOf(binding.spFilterBulan.getSelectedItemPosition()));
                    params.put("tahun", String.valueOf(binding.spFilterTahun.getSelectedItem()));
                    System.out.println( "test " + String.valueOf(binding.spFilterBulan.getSelectedItemPosition()) + "  " + binding.spFilterTahun.getSelectedItem());
                }

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        requestQueue.add(stringRequest);
    }

    protected void setupRvTrans() {
        binding.rvSellerPendapatan.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        binding.rvSellerPendapatan.setHasFixedSize(true);
        recyclerAdapterSellerPendapatan = new RecyclerAdapterSellerPendapatan(listHTrans, listDTrans);
        binding.rvSellerPendapatan.setAdapter(recyclerAdapterSellerPendapatan);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    public void filterBulan() {
        binding.spFilterBulan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDataPendapatan();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void filterTahun() {
        binding.spFilterTahun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDataPendapatan();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void filterStatus() {
        binding.spFilterSellerPendapatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDataPendapatan();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}