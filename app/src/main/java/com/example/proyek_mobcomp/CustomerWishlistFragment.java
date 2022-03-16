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
import com.example.proyek_mobcomp.classFolder.cWishlist;
import com.example.proyek_mobcomp.databinding.FragmentCustomerCartBinding;
import com.example.proyek_mobcomp.databinding.FragmentCustomerWishlistBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterCustomerWishlist;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterHeaderPurchase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerWishlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerWishlistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerWishlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerWishlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerWishlistFragment newInstance(String param1, String param2) {
        CustomerWishlistFragment fragment = new CustomerWishlistFragment();
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



    protected FragmentCustomerWishlistBinding binding;

    ArrayList<cWishlist> arrWishlist = new ArrayList<>();

    RecyclerAdapterCustomerWishlist recyclerAdapterCustomerWishlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_customer_wishlist, container, false);
        binding = FragmentCustomerWishlistBinding.inflate(inflater, container, false);

        getWishlistData();

        return binding.getRoot();
    }

    public void getWishlistData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/customer/getwishlist",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray wishlistArray = jsonObject.getJSONArray("datawishlist");
                            arrWishlist = new ArrayList<>();
                            for (int i = 0; i < wishlistArray.length();i++){
                                int id = wishlistArray.getJSONObject(i).getInt("id");
                                String fk_user = wishlistArray.getJSONObject(i).getString("fk_user");
                                int fk_barang = wishlistArray.getJSONObject(i).getInt("fk_barang");

                                arrWishlist.add(new cWishlist(id, fk_user, fk_barang));
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
                        System.out.println("error get wishlist data in wishlist fragment : " + error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function","getwishlist");
                params.put("username", CustomerHomeActivity.login+"");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    public void setRv() {
        binding.recyclerViewCustWishlist.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCustWishlist.setHasFixedSize(true);

        recyclerAdapterCustomerWishlist = new RecyclerAdapterCustomerWishlist(
                arrWishlist, this
        );
        binding.recyclerViewCustWishlist.setAdapter(recyclerAdapterCustomerWishlist);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }
}