package com.example.proyek_mobcomp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.databinding.FragmentAdminDashboardBinding;
import com.example.proyek_mobcomp.databinding.FragmentCustomerHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminDashboardFragment extends Fragment {

    protected FragmentAdminDashboardBinding binding;
    int jumlahcust = 0;
    int jumlahseller = 0;
    int jumlahtopup = 0;
    int jumlahwithdraw = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false);

        binding.btnMoreAdminHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v);
            }
        });

        getData();

        return binding.getRoot();
    }

    private void getData() {
        jumlahcust = 0;
        jumlahseller = 0;
        jumlahtopup = 0;
        jumlahwithdraw = 0;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/admin/getdata",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray datauser = jsonObject.getJSONArray("datauser");
                            for (int i = 0; i < datauser.length(); i++) {
                                if (datauser.getJSONObject(i).getString("role").equalsIgnoreCase("seller")){
                                    jumlahseller += 1;
                                }
                                else if (datauser.getJSONObject(i).getString("role").equalsIgnoreCase("customer")){
                                    jumlahcust += 1;
                                }
                            }
                            binding.textViewJumCust.setText("Ada " + jumlahcust + " Customer yang sudah terdaftar di MyItem");
                            binding.textViewJumSeller.setText("Ada " + jumlahseller + " Seller yang sudah terdaftar di MyItem");

                            JSONArray datatopup = jsonObject.getJSONArray("datatopup");
                            for (int i = 0; i < datatopup.length(); i++) {
                                if (datatopup.getJSONObject(i).getInt("status_topup") == 0){
                                    if (datatopup.getJSONObject(i).getString("bukti_topup").equalsIgnoreCase("null")){
                                        jumlahwithdraw += 1;
                                    }
                                    else if (!datatopup.getJSONObject(i).getString("bukti_topup").equalsIgnoreCase("null")){
                                        jumlahtopup += 1;
                                    }
                                }
                            }
                            binding.textViewJumTopUpBaru.setText("Ada " + jumlahtopup + " Top Up yang belum dikonfirmasi!");
                            binding.textViewJumWithdrawBaru.setText("Ada " + jumlahwithdraw + " Withdraw yang belum dikonfirmasi!");

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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void showPopUp(View v) {
        //buat popup menu nya dulu
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        getActivity().getMenuInflater().inflate(R.menu.optionmenuadmin, popupMenu.getMenu());

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
                return true;
            }
        });

        //munculkan popupmenu
        popupMenu.show();
    }
}