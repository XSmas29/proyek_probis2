package com.example.proyek_mobcomp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.proyek_mobcomp.databinding.FragmentCustomerCartBinding;
import com.example.proyek_mobcomp.databinding.FragmentCustomerHeaderPurchaseHistoryBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterHeaderPurchase;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterReview;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterSellerTransaksi;

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

//    RecyclerAdapterHeaderPurchase recyclerAdapterHeaderPurchase;

    RecyclerAdapterSellerTransaksi recyclerAdapterSellerTransaksi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_customer_header_purchase_history, container, false);

        binding = FragmentCustomerHeaderPurchaseHistoryBinding.inflate(inflater, container, false);

        binding.spFilterTransaksiCust.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getDetailPurchase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        resetSpinner();

        //getHeaderPurchase();

        return binding.getRoot();
    }

    public void resetSpinner(){
        binding.spFilterTransaksiCust.setSelection(0);
        getDetailPurchase();
    }

    public void getDetailPurchase() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/customer/getdtrans",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            ArrayList<cDetailPurchase> listTrans = new ArrayList<>();
                            ArrayList<cProduct> listBarangTrans = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray arraytrans = jsonObject.getJSONArray("datadtrans");

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
                            setRv(listTrans, listBarangTrans);

                            binding.progressBar.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error get detail transaction/ detail purchase " + error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function","getdtrans");
                params.put("username", CustomerHomeActivity.login+"");
                params.put("status", binding.spFilterTransaksiCust.getSelectedItem().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    protected void setRv(ArrayList<cDetailPurchase> listTrans, ArrayList<cProduct> listBarangTrans) {
        binding.recyclerViewCustPurchaseHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCustPurchaseHistory.setHasFixedSize(true);
        recyclerAdapterSellerTransaksi = new RecyclerAdapterSellerTransaksi(listTrans, listBarangTrans);
        recyclerAdapterSellerTransaksi.setOnItemClickCallback(new RecyclerAdapterSellerTransaksi.OnItemClickCallback() {
            @Override
            public void onItemClicked(cProduct produk, cDetailPurchase detail) {
//                Intent i = new Intent(getActivity(), SellerDetailTransaksiActivity.class);
//                i.putExtra("detail", detail);
//                i.putExtra("produk", produk);
//                getActivity().startActivityForResult(i, 200);
                Intent i = new Intent(getActivity(), CustomerDetailPurchaseActivity.class);
                i.putExtra("idHTrans", detail.getFk_htrans());
                i.putExtra("idDTrans", detail.getId());
                startActivity(i);
            }
        });
        binding.recyclerViewCustPurchaseHistory.setAdapter(recyclerAdapterSellerTransaksi);
    }


//    public void setRv(ArrayList<cHeaderPurchase> arrHTrans) {
//        binding.recyclerViewCustPurchaseHistory.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.recyclerViewCustPurchaseHistory.setHasFixedSize(true);
//
//        recyclerAdapterHeaderPurchase = new RecyclerAdapterHeaderPurchase(
//                arrHTrans
//        );
//        binding.recyclerViewCustPurchaseHistory.setAdapter(recyclerAdapterHeaderPurchase);
//    }



//    public void getHeaderPurchase() {  // get history customer yang lama. ini mendapatkan headernya
//        binding.progressBar.setVisibility(View.VISIBLE);
//
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.POST,
//                getResources().getString(R.string.url) + "/customer/gethtrans",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println(response);
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            ArrayList<cHeaderPurchase> arrHTrans = new ArrayList<>();
//                            JSONArray htransArray = jsonObject.getJSONArray("datahtrans");
//                            for (int i = 0; i < htransArray.length(); i++){
//                                int id = htransArray.getJSONObject(i).getInt("id");
//                                String fk_customer = htransArray.getJSONObject(i).getString("fk_customer");
//                                int grandtotal = htransArray.getJSONObject(i).getInt("grandtotal");
//                                String tanggal   = htransArray.getJSONObject(i).getString("tanggal");
//
//                                arrHTrans.add(new cHeaderPurchase(id, fk_customer, grandtotal, tanggal));
//                            }
//
//                            setRv(arrHTrans);
//                            binding.progressBar.setVisibility(View.INVISIBLE);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        System.out.println("error get header transaction/ header purchase " + error);
//                    }
//                }
//        ){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("function","gethtrans");
//                params.put("username", CustomerHomeActivity.login+"");
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
//    }

}