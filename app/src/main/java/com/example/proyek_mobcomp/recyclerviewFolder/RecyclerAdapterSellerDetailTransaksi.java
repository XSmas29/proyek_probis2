package com.example.proyek_mobcomp.recyclerviewFolder;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.R;
import com.example.proyek_mobcomp.SellerDetailTransaksiActivity;
import com.example.proyek_mobcomp.classFolder.cDetailPurchase;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerAdapterSellerDetailTransaksi extends RecyclerView.Adapter<RecyclerAdapterSellerDetailTransaksi.ViewHolder> {
    ArrayList<cDetailPurchase> arrDetail;
    ArrayList<cProduct> arrProduk;
    SellerDetailTransaksiActivity sellerDetailTransaksiActivity;

    public RecyclerAdapterSellerDetailTransaksi(ArrayList<cDetailPurchase> arrDetail, ArrayList<cProduct> arrProduk, SellerDetailTransaksiActivity sellerDetailTransaksiActivity) {
        this.arrDetail = arrDetail;
        this.arrProduk = arrProduk;
        this.sellerDetailTransaksiActivity = sellerDetailTransaksiActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_layout_seller_detail_transaksi, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cDetailPurchase detailPurchase = arrDetail.get(position);
        cProduct product = arrProduk.get(position);
        
        holder.bind(detailPurchase, product, position);
        //System.out.println("rv " + position);
    }

    @Override
    public int getItemCount() {
        return arrDetail.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduk;
        TextView txtIDDetail, txtNamaBarang, txtJumlah;
        EditText txtNotesCust, txtNotesSeller;
//        Button btnAccept, btnReject, btnSend;
        LinearLayout linearLayoutReview;
        ImageView[] arrStar;
        TextView isiReview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduk = itemView.findViewById(R.id.imgSellerGambarDetail);
            txtIDDetail = itemView.findViewById(R.id.lbSellerDetailIDTransaksi);
            txtNamaBarang = itemView.findViewById(R.id.lbSellerDetailNamaTransaksi);
            txtJumlah = itemView.findViewById(R.id.lbSellerDetailJumlahTransaksi);
            txtNotesCust = itemView.findViewById(R.id.edNotesCustomer);
            txtNotesSeller = itemView.findViewById(R.id.edNotesSeller);
//            btnAccept = itemView.findViewById(R.id.btnSellerAcceptTransaksi);
//            btnReject = itemView.findViewById(R.id.btnSellerRejectTransaksi);
//            btnSend = itemView.findViewById(R.id.btnSellerSendTransaksi);
            linearLayoutReview = itemView.findViewById(R.id.layoutSellerDetail4);

            arrStar = new ImageView[5];
            arrStar[0] = itemView.findViewById(R.id.imgStarReview1);
            arrStar[1] = itemView.findViewById(R.id.imgStarReview2);
            arrStar[2] = itemView.findViewById(R.id.imgStarReview3);
            arrStar[3] = itemView.findViewById(R.id.imgStarReview4);
            arrStar[4] = itemView.findViewById(R.id.imgStarReview5);
            isiReview = itemView.findViewById(R.id.lbSellerReviewIsi);

        }

        public void bind(cDetailPurchase detail, cProduct produk, int position) {
            Picasso.get().load(itemView.getResources().getString(R.string.url) + "/produk/" +
                produk.getGambar()).into(imgProduk);

            txtIDDetail.setText("Transaksi #" + detail.getId());
            txtNamaBarang.setText("Nama Barang : " + produk.getNama());
            txtJumlah.setText("Jumlah : " + detail.getJumlah() + " x " + moneySeparator(detail.getSubtotal() / detail.getJumlah()));
            txtNotesCust.setText(detail.getNotes_customer());

            refreshData(detail, linearLayoutReview, txtNotesSeller, itemView, arrStar, isiReview);

            txtNotesSeller.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    sellerDetailTransaksiActivity.detail.get(position).setNotes_seller(txtNotesSeller.getText().toString());
                }
            });
        }
    }

    protected void refreshData(cDetailPurchase detail, LinearLayout linearLayoutReview, EditText txtNotesSeller, View itemView, ImageView[] arrStar, TextView isiReview) {
        if (detail.getStatus().equalsIgnoreCase("pending")){
//            binding.layoutSellerDetail2.setVisibility(View.GONE);
            linearLayoutReview.setVisibility(View.GONE);
//            binding.layoutSellerDetail1.setVisibility(View.VISIBLE);
//
//            binding.lbSellerStatusDetail.setTextColor(getResources().getColor(R.color.yellow));
//            binding.lbSellerStatusDetail.setText("Status : Pending");
        }
        else if (detail.getStatus().equalsIgnoreCase("processing")){
//            binding.layoutSellerDetail1.setVisibility(View.GONE);
            linearLayoutReview.setVisibility(View.GONE);
//            binding.layoutSellerDetail2.setVisibility(View.VISIBLE);
//
//            binding.lbSellerStatusDetail.setTextColor(getResources().getColor(R.color.yellow));
//            binding.lbSellerStatusDetail.setText("Status : Processing");
        }
        else if (detail.getStatus().equalsIgnoreCase("sent")){
//            binding.layoutSellerDetail2.setVisibility(View.GONE);
//            binding.layoutSellerDetail1.setVisibility(View.GONE);
            linearLayoutReview.setVisibility(View.GONE);

            txtNotesSeller.setEnabled(false);
            txtNotesSeller.setText(detail.getNotes_seller());

//            binding.lbSellerStatusDetail.setTextColor(getResources().getColor(R.color.yellow));
//            binding.lbSellerStatusDetail.setText("Status : Sent");
        }
        else if (detail.getStatus().equalsIgnoreCase("completed")){
//            binding.layoutSellerDetail2.setVisibility(View.GONE);
//            binding.layoutSellerDetail1.setVisibility(View.GONE);

            txtNotesSeller.setEnabled(false);
            txtNotesSeller.setText(detail.getNotes_seller());
//
//            binding.lbSellerStatusDetail.setTextColor(getResources().getColor(R.color.green));
//            binding.lbSellerStatusDetail.setText("Status : Completed");
            cekReview(detail,itemView, arrStar, isiReview, linearLayoutReview);
        }
        else{
//            binding.layoutSellerDetail2.setVisibility(View.GONE);
//            binding.layoutSellerDetail1.setVisibility(View.GONE);
            linearLayoutReview.setVisibility(View.GONE);

            txtNotesSeller.setEnabled(false);
//            binding.lbSellerStatusDetail.setTextColor(getResources().getColor(R.color.red));
//            binding.lbSellerStatusDetail.setText("Status : Rejected");
        }
    }


    protected void cekReview(cDetailPurchase detail, View itemView, ImageView[] arrStar, TextView isiReview, LinearLayout linearLayoutReview) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                itemView.getContext().getResources().getString(R.string.url) + "/seller/getreview",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject review = jsonObject.getJSONObject("review");
                            if (review != null){
                                linearLayoutReview.setVisibility(View.VISIBLE);
                                //ImageView[] arrStar = {binding.imgStarReview1, binding.imgStarReview2, binding.imgStarReview3, binding.imgStarReview4, binding.imgStarReview5};
                                for (int i = 0; i < 5; i++) {
                                    if (review.getInt("star") > i){
                                        Picasso.get()
                                                .load(itemView.getContext().getResources().getString(R.string.url) + "/star.png")
                                                .into(arrStar[i]);
                                    }
                                    else{
                                        Picasso.get()
                                                .load(itemView.getContext().getResources().getString(R.string.url) + "/star-nocolor.png")
                                                .into(arrStar[i]);
                                    }
                                }
                                isiReview.setText(review.getString("isi"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error get review : " + error.getMessage());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", detail.getId() + "");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
        requestQueue.add(stringRequest);
    }


    protected String moneySeparator(int money) {
        int ctr = 0; // untuk menghitung berapa digit yg sudah masuk ke variable hasil
        String hasil = "";

        String awal = String.valueOf(money);
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
