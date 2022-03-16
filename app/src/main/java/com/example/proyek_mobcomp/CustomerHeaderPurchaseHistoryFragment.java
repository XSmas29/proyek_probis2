package com.example.proyek_mobcomp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cHeaderPurchase;
import com.example.proyek_mobcomp.databinding.FragmentCustomerCartBinding;
import com.example.proyek_mobcomp.databinding.FragmentCustomerHeaderPurchaseHistoryBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterHeaderPurchase;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerHeaderPurchaseHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerHeaderPurchaseHistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerHeaderPurchaseHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerPurchaseHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerHeaderPurchaseHistoryFragment newInstance(String param1, String param2) {
        CustomerHeaderPurchaseHistoryFragment fragment = new CustomerHeaderPurchaseHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    protected FragmentCustomerHeaderPurchaseHistoryBinding binding;
    AppDatabase db;

    RecyclerAdapterHeaderPurchase recyclerAdapterHeaderPurchase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_customer_header_purchase_history, container, false);

        binding = FragmentCustomerHeaderPurchaseHistoryBinding.inflate(inflater, container, false);

        getHeaderPurchase();

        return binding.getRoot();
    }

    public void getHeaderPurchase() {
        binding.progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/customer/gethtrans",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            ArrayList<cHeaderPurchase> arrHTrans = new ArrayList<>();
                            JSONArray htransArray = jsonObject.getJSONArray("datahtrans");
                            for (int i = 0; i < htransArray.length(); i++){
                                int id = htransArray.getJSONObject(i).getInt("id");
                                String fk_customer = htransArray.getJSONObject(i).getString("fk_customer");
                                int grandtotal = htransArray.getJSONObject(i).getInt("grandtotal");
                                String tanggal   = htransArray.getJSONObject(i).getString("tanggal");

                                arrHTrans.add(new cHeaderPurchase(id, fk_customer, grandtotal, tanggal));
                            }

                            setRv(arrHTrans);
                            binding.progressBar.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error get header transaction/ header purchase " + error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function","gethtrans");
                params.put("username", CustomerHomeActivity.login+"");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void setRv(ArrayList<cHeaderPurchase> arrHTrans) {
        binding.recyclerViewCustPurchaseHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCustPurchaseHistory.setHasFixedSize(true);

        recyclerAdapterHeaderPurchase = new RecyclerAdapterHeaderPurchase(
                arrHTrans
        );
        binding.recyclerViewCustPurchaseHistory.setAdapter(recyclerAdapterHeaderPurchase);
    }
}