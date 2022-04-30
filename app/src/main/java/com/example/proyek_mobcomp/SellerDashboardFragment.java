package com.example.proyek_mobcomp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cKategori;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.example.proyek_mobcomp.databinding.FragmentCustomerHomeBinding;
import com.example.proyek_mobcomp.databinding.FragmentSellerDashboardBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SellerDashboardFragment extends Fragment {

    public static SellerDashboardFragment newInstance() {
        SellerDashboardFragment fragment = new SellerDashboardFragment();
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

    protected FragmentSellerDashboardBinding binding;
    int jumlahcomplete = 0;
    int jumlahreject = 0;
    int jumlahprocess = 0;
    int totalincome = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_seller_dashboard, container, false);
        binding = FragmentSellerDashboardBinding.inflate(inflater, container, false);

        binding.btnMoreSellerHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v);
            }
        });

        binding.btnSellerPendapatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SellerPendapatanActivity.class);
                getActivity().startActivityForResult(i, 300);
            }
        });

        getData();
        return binding.getRoot();
    }

    private void getData() {
        jumlahcomplete = 0;
        jumlahreject = 0;
        jumlahprocess = 0;
        totalincome = 0;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/seller/getdata",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject datauser = jsonObject.getJSONObject("datauser");
                            binding.txtWelcome.setText("Welcome, " + datauser.getString("username"));

                            String money = moneySeparator(datauser.getInt("saldo"));
                            binding.txtDashboardSaldo.setText("Rp. " + money);

                            JSONArray datadetail = jsonObject.getJSONArray("datadetail");
                            for (int i = 0; i < datadetail.length(); i++) {
                                if (datadetail.getJSONObject(i).getString("status").equalsIgnoreCase("completed")){
                                    jumlahcomplete += 1;
                                    totalincome += datadetail.getJSONObject(i).getInt("subtotal");
                                }
                                else if (datadetail.getJSONObject(i).getString("status").equalsIgnoreCase("rejected")){
                                    jumlahreject += 1;
                                }
                                else{
                                    jumlahprocess += 1;
                                }
                            }

                            binding.txtDashboardJumlah1.setText(jumlahcomplete + "");
                            binding.txtDashboardJumlah2.setText(jumlahreject+ "");
                            binding.txtDashboardJumlah3.setText(jumlahprocess + "");
                            money = moneySeparator(totalincome);
                            binding.txtDashboardJumlah4.setText("Rp. " + money);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error get data : " + error.getMessage());
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


    private void showPopUp(View v) {
        //buat popup menu nya dulu
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        getActivity().getMenuInflater().inflate(R.menu.optionsmenuseller, popupMenu.getMenu());

        //event saat menu diklik
        //alt + enter kedua -> implements onMenuItemClick
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.logout){
                    SharedPreferences sharedpreferences = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove("login");
                    editor.commit();
                    getActivity().finish();
                }
                else if(item.getItemId() == R.id.mutasi_saldo){
                    Intent i = new Intent(getActivity(), SellerMutasiSaldoActivity.class);
                    startActivity(i);
                }
                else if(item.getItemId() == R.id.profile){
                    Intent i = new Intent(getActivity(), SellerProfileActivity.class);
                    startActivity(i);
                }
                return true;
            }
        });

        //munculkan popupmenu
        popupMenu.show();
    }

    protected String moneySeparator(int harga) {
        int ctr = 0; // untuk menghitung berapa digit yg sudah masuk ke variable hasil
        String hasil = "";

        String awal = String.valueOf(harga);
        String temp = ""; // penampung sementara untuk dalam for
//        System.out.println(awal.length());
        for (int i = awal.length(); i > 0 ; i--){
            if (ctr % 3 == 0) {
                if (i != awal.length()) {
                    hasil = "." + hasil;
                }
            }
            if (i != awal.length()) {
                hasil = awal.substring((i - 1), (i)) + hasil;
            }else{
                hasil = awal.substring((i - 1)) + hasil;
            }
            ctr++;
        }

        return hasil;
    }
}