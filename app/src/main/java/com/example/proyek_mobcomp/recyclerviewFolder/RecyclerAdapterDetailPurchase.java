package com.example.proyek_mobcomp.recyclerviewFolder;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.CustomerDetailPurchaseActivity;
import com.example.proyek_mobcomp.CustomerHomeActivity;
import com.example.proyek_mobcomp.R;
import com.example.proyek_mobcomp.classFolder.cCart;
import com.example.proyek_mobcomp.classFolder.cDetailPurchase;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.example.proyek_mobcomp.classFolder.cReview;
import com.example.proyek_mobcomp.classFolder.cUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerAdapterDetailPurchase extends RecyclerView.Adapter<RecyclerAdapterDetailPurchase.ViewHolder>{
    ArrayList<cDetailPurchase> arrDTrans = new ArrayList<>();
    ArrayList<cReview> arrReview = new ArrayList<>();
    ArrayList<cUser> arrSeller = new ArrayList<>();
    ArrayList<cProduct> arrProduct = new ArrayList<>();
    CustomerDetailPurchaseActivity customerDetailPurchaseActivity;

    public RecyclerAdapterDetailPurchase(ArrayList<cDetailPurchase> arrDTrans, ArrayList<cReview> arrReview, ArrayList<cUser> arrSeller, ArrayList<cProduct> arrProduct, CustomerDetailPurchaseActivity customerDetailPurchaseActivity) {
        this.arrDTrans = arrDTrans;
        this.arrReview = arrReview;
        this.arrSeller = arrSeller;
        this.arrProduct = arrProduct;
        this.customerDetailPurchaseActivity = customerDetailPurchaseActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_layout_detail_purchase, parent, false);

       ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cDetailPurchase dtrans = arrDTrans.get(position);
        holder.bind(dtrans, position);
    }

    @Override
    public int getItemCount() {
        return arrDTrans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int starCount = 5;
        // bagian detail trans
        ImageView fotoProduct, fotoToko;
        TextView txtId, txtNamaProduct, txtHargaProduct, txtNamaToko;
        TextView txtJumlah, txtStatus, txtCatatanCustomer;
        EditText txtCatatanSeller;
        Button btnCompleteOrder;

        // bagian review
        LinearLayout llReview;
        ImageButton[] btnStar = new ImageButton[5];
        EditText txtIsiReview;
        Button btnSubmitReview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // bagian detail trans
            fotoProduct = itemView.findViewById(R.id.imageView_productPicture);
            fotoToko = itemView.findViewById(R.id.imageView_profileToko);
            txtId = itemView.findViewById(R.id.textView_id);
            txtNamaProduct = itemView.findViewById(R.id.textView_productName);
            txtHargaProduct = itemView.findViewById(R.id.textView_productPrice);
            txtNamaToko = itemView.findViewById(R.id.textView_namaToko);
            txtJumlah = itemView.findViewById(R.id.textView_jumlah);
            txtStatus = itemView.findViewById(R.id.textView_status);
            txtCatatanCustomer = itemView.findViewById(R.id.textView_catatanCustomer);
            txtCatatanSeller = itemView.findViewById(R.id.editText_catatanSeller);
            btnCompleteOrder = itemView.findViewById(R.id.btnCompleteOrder);

            // bagian review
            llReview = itemView.findViewById(R.id.ll_review);
            txtIsiReview = itemView.findViewById(R.id.editText_isiReview);
            btnStar[0] = itemView.findViewById(R.id.imageButton_star0);
            btnStar[1] = itemView.findViewById(R.id.imageButton_star1);
            btnStar[2] = itemView.findViewById(R.id.imageButton_star2);
            btnStar[3] = itemView.findViewById(R.id.imageButton_star3);
            btnStar[4] = itemView.findViewById(R.id.imageButton_star4);
            btnSubmitReview = itemView.findViewById(R.id.btnSubmitReview);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(cDetailPurchase dtrans, int position) {

            // bagian detail trans
            for (int i = 0; i <  arrProduct.size(); i++){
                if (arrProduct.get(i).getId() == dtrans.getFk_barang()) {
                    Picasso.get().load(itemView.getResources().getString(R.string.url) + "/produk/" +
                            arrProduct.get(i).getGambar()).into(fotoProduct);
                    txtNamaProduct.setText(arrProduct.get(i).getNama());
                }
            }

            for (int i = 0; i <  arrSeller.size(); i++){
                if (arrSeller.get(i).getUsername().equals(dtrans.getFk_seller())){
                    Picasso.get().load(itemView.getResources().getString(R.string.url) + "/profile/" +
                            arrSeller.get(i).getGambar()).into(fotoToko);
                    txtNamaToko.setText(arrSeller.get(i).getToko());
                }
            }

            txtId.setText("Detail Order #" + dtrans.getFk_htrans() + "/" + dtrans.getId());
            String money = moneySeparator(dtrans.getSubtotal() / dtrans.getJumlah());
            txtHargaProduct.setText("Rp " + (money));
            txtJumlah.setText("Jumlah : " + dtrans.getJumlah());
            txtStatus.setText("Status : " + dtrans.getStatus());
            if (dtrans.getNotes_customer().toString().isEmpty() || dtrans.getNotes_customer().equals("")) {
                txtCatatanCustomer.setText("Tidak ada...");
            }else{
                txtCatatanCustomer.setText(dtrans.getNotes_customer());
            }

//            if (TextUtils.isEmpty(dtrans.getNotes_seller().toString()) || dtrans.getNotes_seller().equals("")) {
//                txtCatatanSeller.setText("");
//            }else{
//                txtCatatanSeller.setText(dtrans.getNotes_seller());
//            }

            if (!TextUtils.isEmpty(dtrans.getNotes_seller().toString()) || !dtrans.getNotes_seller().equals("") || !dtrans.getNotes_seller().equalsIgnoreCase("null")){
                //System.out.println(dtrans.getNotes_seller());
                txtCatatanSeller.setText(dtrans.getNotes_seller());
            }

            if(dtrans.getStatus().equalsIgnoreCase("sent")){
                ViewGroup.LayoutParams params = btnCompleteOrder.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                btnCompleteOrder.setLayoutParams(params);
                btnCompleteOrder.setEnabled(true);

                btnCompleteOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                itemView.getResources().getString(R.string.url) + "/customer/completeorder",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println(response);

                                        try {
                                            JSONObject jsonObject = new JSONObject(response);

                                            int code = jsonObject.getInt("code");
                                            String message = jsonObject.getString("message");

                                            Toast.makeText(itemView.getContext(), message, Toast.LENGTH_LONG).show();
                                            if (code == 1){
                                                customerDetailPurchaseActivity.getTransData();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("error complete order in customer " + error);
                                    }
                                }
                        ){
                            @Nullable
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("function","completeorder");
                                params.put("iddtrans", dtrans.getId()+"");
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
                        requestQueue.add(stringRequest);
                    }
                });
            }

            // bagian review
            if (dtrans.getStatus().equalsIgnoreCase("completed")){
                if (arrReview.size() > 0){
                    for (int i = 0; i<arrReview.size(); i++){
                        if (arrReview.get(i).getFk_dtrans() == dtrans.getId()){
                            llReview.setEnabled(false);
                            for (int j = 4 ; j >= 4-(4-arrReview.get(i).getStar()); j--){
                                btnStar[j].setImageResource(R.drawable.ic_baseline_star_border_24);
                            }
                            for (int j = 0 ; j < 5; j++){ //disabling star
                                btnStar[j].setEnabled(false);
                            }
                            txtIsiReview.setText(arrReview.get(i).getIsi());
                            txtIsiReview.setEnabled(false);
                            btnSubmitReview.setEnabled(false);
                            ViewGroup.LayoutParams params = btnSubmitReview.getLayoutParams();
                            params.height = 0;
                            btnSubmitReview.setLayoutParams(params);

                            if (arrReview.get(i).getIsi().equalsIgnoreCase("") || arrReview.get(i).getIsi().isEmpty()){
                                params = txtIsiReview.getLayoutParams();
                                params.height = 0;
                                txtIsiReview.setLayoutParams(params);
                            }else{
                                txtIsiReview.setTextColor(itemView.getResources().getColor(R.color.black));
                            }
                        }
                    }
                }else{
                    btnStar[0].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            if (starCount < 1) {
//
//                            }else{
                                starCount = 1;
                                for (int i = starCount; i < 5; i++) {
                                    btnStar[i].setImageResource(R.drawable.ic_baseline_star_border_24);
                                }
//                            }
                        }
                    });

                    btnStar[1].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (starCount < 2) {
                                starCount = 2;
                                for (int i = 0; i < starCount; i++) {
                                    btnStar[i].setImageResource(R.drawable.ic_baseline_star_24);
                                }
                            }else{
                                starCount = 2;
                                for (int i = starCount; i < 5; i++) {
                                    btnStar[i].setImageResource(R.drawable.ic_baseline_star_border_24);
                                }
                            }
                        }
                    });

                    btnStar[2].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (starCount < 3) {
                                starCount = 3;
                                for (int i = 0; i < starCount; i++) {
                                    btnStar[i].setImageResource(R.drawable.ic_baseline_star_24);
                                }
                            }else{
                                starCount = 3;
                                for (int i = starCount; i < 5; i++) {
                                    btnStar[i].setImageResource(R.drawable.ic_baseline_star_border_24);
                                }
                            }
                        }
                    });

                    btnStar[3].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (starCount < 4){
                                starCount = 4;
                                for (int i = 0; i < starCount; i++) {
                                    btnStar[i].setImageResource(R.drawable.ic_baseline_star_24);
                                }
                            }else{
                                starCount = 4;
                                for (int i = starCount; i < 5; i++){
                                    btnStar[i].setImageResource(R.drawable.ic_baseline_star_border_24);
                                }
                            }

                        }
                    });

                    btnStar[4].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (starCount < 5){
                                starCount = 5;
                                for (int i = 0; i < starCount; i++) {
                                    btnStar[i].setImageResource(R.drawable.ic_baseline_star_24);
                                }
                            }
                        }
                    });

                    btnSubmitReview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StringRequest stringRequest = new StringRequest(
                                    Request.Method.POST,
                                    itemView.getResources().getString(R.string.url) + "/customer/sendreview",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            System.out.println(response);

                                            try {
                                                JSONObject jsonObject = new JSONObject(response);

                                                int code = jsonObject.getInt("code");
                                                String message = jsonObject.getString("message");

                                                Toast.makeText(itemView.getContext(), message, Toast.LENGTH_LONG).show();
                                                if (code == 1){
                                                    customerDetailPurchaseActivity.getTransData();
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            System.out.println("error complete order in customer " + error);
                                        }
                                    }
                            ){
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("function","sendreview");
                                    params.put("iddtrans", dtrans.getId()+"");
                                    params.put("fk_htrans", dtrans.getFk_htrans()+"");
                                    params.put("fk_user", CustomerHomeActivity.login +"");
                                    params.put("star", starCount+"");
                                    params.put("isi", txtIsiReview.getText().toString());
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
                            requestQueue.add(stringRequest);
                        }
                    });
                }
            }else{
                ViewGroup.LayoutParams params = llReview.getLayoutParams();
                params.height = 0;
                llReview.setLayoutParams(params);
            }


        }

    }

    protected String moneySeparator(int money) {
        int ctr = 0; // untuk menghitung berapa digit yg sudah masuk ke variable hasil
        String hasil = "";

        String awal = String.valueOf(money);
        String temp = ""; // penampung sementara untuk dalam for
        System.out.println(awal.length());
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
