package com.example.proyek_mobcomp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cDetailPurchase;
import com.example.proyek_mobcomp.classFolder.cHeaderPurchase;
import com.example.proyek_mobcomp.classFolder.cKategori;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.example.proyek_mobcomp.databinding.ActivitySellerDetailTransaksiBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterSellerDetailTransaksi;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/*

    Update Changes :
    - 21 April 2022 : mengubah dari hanya menampilkan per barang (dtrans) menjadi menampilkan per transaksi (htrans)

 */

public class SellerDetailTransaksiActivity extends AppCompatActivity {

    ActivitySellerDetailTransaksiBinding binding;

    cHeaderPurchase header;
    ArrayList<cProduct> produk;
    public ArrayList<cDetailPurchase> detail;

    RecyclerAdapterSellerDetailTransaksi recyclerAdapterSellerDetailTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_detail_transaksi);

        binding = ActivitySellerDetailTransaksiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        header = getIntent().getParcelableExtra("header");
        produk = getIntent().getParcelableArrayListExtra("produk");
        detail = getIntent().getParcelableArrayListExtra("detail");
        //System.out.println("index " + produk.size() + " " + detail.size());

        loadData();

        binding.imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });






//        Picasso.get().load(getResources().getString(R.string.url) + "/produk/" +
//                produk.getGambar()).into(binding.imgSellerGambarDetail);
//
//        binding.lbSellerDetailNamaTransaksi.setText("Nama Barang : " + produk.getNama());
//        binding.lbSellerDetailJumlahTransaksi.setText("Jumlah : " + detail.getJumlah());
//        binding.edNotesCustomer.setText(detail.getNotes_customer());
//
//
//        refreshData();
//
        binding.btnSellerRejectTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTransaksi("rejected");
            }
        });

        binding.btnSellerAcceptTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTransaksi("processing");
            }
        });

        binding.btnSellerSendTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int isAllFilled = 1;
                for (int i = 0; i < detail.size(); i++){
                    if (detail.get(i).getNotes_seller().equalsIgnoreCase("")){
                        isAllFilled = 0;
                    }
                }
                if (isAllFilled == 0){
                    Toast.makeText(getApplicationContext(), "Notes Seller Harus diisi!", Toast.LENGTH_SHORT).show();
                }
                else{
                    updateTransaksi("sent");
                    //Toast.makeText(getBaseContext(), "OTW SENT ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void loadData() {
        //set text
        binding.textViewIdHTrans.setText("Transaksi #" + header.getId());
        binding.textViewGrandtotal.setText("Rp " + header.getGrandtotalInString());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter.parse(header.getTanggal());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            SimpleDateFormat dmyFormat = new SimpleDateFormat("dd MMM yyyy");
            String dateFormated = dmyFormat.format(date);

            binding.textViewTanggalTrans.setText(dateFormated.toString());
        }

        binding.recyclerViewDetailTransaksi.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerViewDetailTransaksi.setHasFixedSize(true);
        recyclerAdapterSellerDetailTransaksi = new RecyclerAdapterSellerDetailTransaksi(detail, produk, this);

        binding.recyclerViewDetailTransaksi.setAdapter(recyclerAdapterSellerDetailTransaksi);

        refreshData();
    }

    private void updateTransaksi(String status) {
        Gson gson = new Gson();
        cDetailPurchase[] arrDetail = new cDetailPurchase[detail.size()];
        for (int i = 0; i < detail.size(); i ++){
            arrDetail[i] = detail.get(i);
        }

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/seller/updatetransaksi",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int code = jsonObject.getInt("code");
                            if (code == 1){
                                Toast.makeText(getApplicationContext(), "Berhasil Reject Transaksi!", Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < detail.size(); i ++){
                                    detail.get(i).setStatus("rejected");
                                }

                            }
                            else if (code == 2){
                                Toast.makeText(getApplicationContext(), "Berhasil Accept Transaksi!", Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < detail.size(); i ++) {
                                    detail.get(i).setStatus("processing");
                                }
                            }
                            else if (code == 3){
                                Toast.makeText(getApplicationContext(), "Berhasil Send Produk!", Toast.LENGTH_SHORT).show();
                                for (int i = 0; i < detail.size(); i ++) {
                                    detail.get(i).setStatus("sent");
                                }
//                                detail.setNotes_seller(binding.edNotesSeller.getText().toString());

                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Stok Barang Tidak Cukup!", Toast.LENGTH_SHORT).show();
                            }
                            loadData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error update transaksi : " + error.getMessage());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", status);
//                params.put("id", detail.getId() + "");
//                params.put("noteseller", binding.edNotesSeller.getText().toString());
                params.put("detail", gson.toJson(arrDetail));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void refreshData() {
        if (detail.get(0).getStatus().equalsIgnoreCase("pending")){
            binding.layoutSellerDetail2.setVisibility(View.GONE);
//            binding.layoutSellerDetail4.setVisibility(View.GONE);
            binding.layoutSellerDetail1.setVisibility(View.VISIBLE);

            binding.lbSellerStatusDetail.setTextColor(getResources().getColor(R.color.yellow));
            binding.lbSellerStatusDetail.setText("Status : Pending");
        }
        else if (detail.get(0).getStatus().equalsIgnoreCase("processing")){
            binding.layoutSellerDetail1.setVisibility(View.GONE);
//            binding.layoutSellerDetail4.setVisibility(View.GONE);
            binding.layoutSellerDetail2.setVisibility(View.VISIBLE);

            binding.lbSellerStatusDetail.setTextColor(getResources().getColor(R.color.yellow));
            binding.lbSellerStatusDetail.setText("Status : Processing");
        }
        else if (detail.get(0).getStatus().equalsIgnoreCase("sent")){
            binding.layoutSellerDetail2.setVisibility(View.GONE);
            binding.layoutSellerDetail1.setVisibility(View.GONE);
//            binding.layoutSellerDetail4.setVisibility(View.GONE);
//
//            binding.edNotesSeller.setEnabled(false);
//            binding.edNotesSeller.setText(detail.getNotes_seller());

            binding.lbSellerStatusDetail.setTextColor(getResources().getColor(R.color.yellow));
            binding.lbSellerStatusDetail.setText("Status : Sent");
        }
        else if (detail.get(0).getStatus().equalsIgnoreCase("completed")){
            binding.layoutSellerDetail2.setVisibility(View.GONE);
            binding.layoutSellerDetail1.setVisibility(View.GONE);

//            binding.edNotesSeller.setEnabled(false);
//            binding.edNotesSeller.setText(detail.getNotes_seller());

            binding.lbSellerStatusDetail.setTextColor(getResources().getColor(R.color.green));
            binding.lbSellerStatusDetail.setText("Status : Completed");
//            cekReview();
        }
        else{
            binding.layoutSellerDetail2.setVisibility(View.GONE);
            binding.layoutSellerDetail1.setVisibility(View.GONE);
//            binding.layoutSellerDetail4.setVisibility(View.GONE);
//
//            binding.edNotesSeller.setEnabled(false);
            binding.lbSellerStatusDetail.setTextColor(getResources().getColor(R.color.red));
            binding.lbSellerStatusDetail.setText("Status : Rejected");
        }
    }
//
//    private void cekReview() {
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.POST,
//                getResources().getString(R.string.url) + "/seller/getreview",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println(response);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONObject review = jsonObject.getJSONObject("review");
//                            if (review != null){
//                                binding.layoutSellerDetail4.setVisibility(View.VISIBLE);
//                                ImageView[] arrStar = {binding.imgStarReview1, binding.imgStarReview2, binding.imgStarReview3, binding.imgStarReview4, binding.imgStarReview5};
//                                for (int i = 0; i < 5; i++) {
//                                    if (review.getInt("star") > i){
//                                        Picasso.get()
//                                                .load(getResources().getString(R.string.url) + "/star.png")
//                                                .into(arrStar[i]);
//                                    }
//                                    else{
//                                        Picasso.get()
//                                                .load(getResources().getString(R.string.url) + "/star-nocolor.png")
//                                                .into(arrStar[i]);
//                                    }
//                                }
//                                binding.lbSellerReviewIsi.setText(review.getString("isi"));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        System.out.println("error get review : " + error.getMessage());
//                    }
//                }
//        ){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("id", detail.getId() + "");
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }
}