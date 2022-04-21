package com.example.proyek_mobcomp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cCart;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.example.proyek_mobcomp.databinding.FragmentCustomerCartBinding;
import com.example.proyek_mobcomp.databinding.FragmentCustomerHomeBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterCustomerCart;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterCustomerHomeProduct;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerCartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerCartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerCartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerCartFragment newInstance(String param1, String param2) {
        CustomerCartFragment fragment = new CustomerCartFragment();
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


    public static int total = 0;

    protected FragmentCustomerCartBinding binding;

    RecyclerAdapterCustomerCart recyclerAdapterCustomerCart;

    AppDatabase db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_customer_cart, container, false);

        binding = FragmentCustomerCartBinding.inflate(inflater, container, false);
        total = 0;
        getAllProduct();

        db = AppDatabase.getInstance(getContext());

        setRv();

        checkout();

        return binding.getRoot();
    }

    protected void checkout() {
        binding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                String isicart = gson.toJson(db.cartDao().getCartByUsername(CustomerHomeActivity.login));
                System.out.println(isicart);
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        getResources().getString(R.string.url) + "/customer/checkout",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    int code = jsonObject.getInt("code");
                                    String message = jsonObject.getString("message");

                                    if (code == 1){
                                        db.cartDao().deleteCartByUsername(CustomerHomeActivity.login);
                                        setRv();
                                    }

                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                    binding.progressBar.setVisibility(View.INVISIBLE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("error customer checkout " + error);
                            }
                        }
                ){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("function", "checkout");
                        params.put("username", CustomerHomeActivity.login);
                        params.put("cart", isicart);
                        params.put("grandtotal", total+"");

                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
            }
        });
    }

    public void setRv() {
        //System.out.println("masuk set rv");
        total = 0;
        binding.recyclerViewCustCart.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCustCart.setHasFixedSize(true);

        List<cCart> cart = db.cartDao().getCartByUsername(CustomerHomeActivity.login);
        ArrayList<cCart> arrCart = new ArrayList<>();
        arrCart.addAll(cart);

        if (arrCart.size() == 0){
            setTotal();
        }

        recyclerAdapterCustomerCart = new RecyclerAdapterCustomerCart(
                arrCart,
                this
        );
        binding.recyclerViewCustCart.setAdapter(recyclerAdapterCustomerCart);
        //setTotal();
    }

    public void setTotal(){
//        System.out.println("masuk set total");
        binding.btnCheckout.setText("Checkout Rp " + total);
        binding.btnCheckout.setEnabled(true);
        if (total == 0){
            binding.btnCheckout.setText("Checkout");
            binding.btnCheckout.setEnabled(false);
        }
        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    private void getAllProduct() {
        binding.progressBar.setVisibility(View.VISIBLE);
        //binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/customer/getallproduct",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            // get data product yg akan ditampilkan
                            JSONArray arrayProduct = jsonObject.getJSONArray("dataproduct");
                            CustomerHomeActivity.arrayListProduct.clear();
                            //System.out.println("panjang array " + arrayProduct);
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

                                CustomerHomeActivity.arrayListProduct.add(
                                        new cProduct(id, fk_seller, fk_kategori, nama, deskripsi, harga, stok, gambar, is_deleted)
                                );
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error update stock product in cart : " + error);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}