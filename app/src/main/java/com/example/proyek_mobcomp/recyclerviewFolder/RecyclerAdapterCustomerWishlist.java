package com.example.proyek_mobcomp.recyclerviewFolder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.AppDatabase;
import com.example.proyek_mobcomp.CustomerCartFragment;
import com.example.proyek_mobcomp.CustomerDetailProduct;
import com.example.proyek_mobcomp.CustomerHomeActivity;
import com.example.proyek_mobcomp.CustomerWishlistFragment;
import com.example.proyek_mobcomp.R;
import com.example.proyek_mobcomp.classFolder.cCart;
import com.example.proyek_mobcomp.classFolder.cWishlist;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
    Update Changes =
        20 Maret 2022 : Muncul message box ketika ingin menghapus barang dari wishlist
 */

public class RecyclerAdapterCustomerWishlist extends RecyclerView.Adapter<RecyclerAdapterCustomerWishlist.ViewHolder> {
    ArrayList<cWishlist> arrWishlist = new ArrayList<>();
    CustomerWishlistFragment customerWishlistFragment;


    public RecyclerAdapterCustomerWishlist(ArrayList<cWishlist> arrWishlist, CustomerWishlistFragment customerWishlistFragment) {
        this.arrWishlist = arrWishlist;
        this.customerWishlistFragment = customerWishlistFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_layout_wishlist, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cWishlist wishlist = arrWishlist.get(position);
        holder.bind(wishlist, position);
    }

    @Override
    public int getItemCount() {
        return arrWishlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppDatabase db;
        LinearLayout ll;
        ImageView profileToko, fotoProduct;
        TextView txtNamaToko, txtNamaProduct, txtHargaProduct;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            db = AppDatabase.getInstance(itemView.getContext());
            ll = itemView.findViewById(R.id.ll);
            profileToko = itemView.findViewById(R.id.imageView_profileToko);
            fotoProduct = itemView.findViewById(R.id.imageView_productPicture);
            txtNamaToko = itemView.findViewById(R.id.textView_namaToko);
            txtNamaProduct = itemView.findViewById(R.id.textView_productName);
            txtHargaProduct = itemView.findViewById(R.id.textView_productPrice);
            btnDelete = itemView.findViewById(R.id.imageButton_delete);

        }

        public void bind(cWishlist wishlist, int position) {
            // mendapatkan data product/barang
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    itemView.getResources().getString(R.string.url) + "/customer/getoneproductandseller",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                // seller
                                JSONObject seller = jsonObject.getJSONObject("dataseller");

                                txtNamaToko.setText(seller.getString("toko"));
                                String gambar = seller.getString("gambar");
                                Picasso.get().load(itemView.getResources().getString(R.string.url) + "/profile/" +
                                        gambar).into(profileToko);

                                // product
                                JSONObject product = jsonObject.getJSONObject("dataproduct");
                                txtNamaProduct.setText(product.getString("nama"));
                                gambar = product.getString("gambar");
                                Picasso.get().load(itemView.getResources().getString(R.string.url) + "/produk/" +
                                        gambar).into(fotoProduct);

                                String money = moneySeparator(product.getString("harga"));
                                txtHargaProduct.setText("Rp " + money);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("error saat getoneproductandseller " + error);
                        }
                    }
            ){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("function", "getoneproductandseller");
                    params.put("idproduct", wishlist.getFk_barang()+"");

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
            requestQueue.add(stringRequest);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder
                            = new AlertDialog
                            .Builder(itemView.getContext());

                    // Set the message show for the Alert time
                    builder.setMessage("Anda yakin ingin menghapus barang ini dari wishlist ?");

                    // Set Alert Title
                    builder.setTitle("Peringatan");

                    // Set Cancelable false
                    // for when the user clicks on the outside
                    // the Dialog Box then it will remain show
                    builder.setCancelable(false);

                    // Set the positive button with yes name
                    // OnClickListener method is use of
                    // DialogInterface interface.

                    builder
                            .setPositiveButton(
                                    "Yes",
                                    new DialogInterface
                                            .OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which)
                                        {
                                            StringRequest stringRequest = new StringRequest(
                                                    Request.Method.POST,
                                                    itemView.getResources().getString(R.string.url) + "/customer/updatewishlist",
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            System.out.println(response);

                                                            try {
                                                                JSONObject jsonObject = new JSONObject(response);

                                                                int code = jsonObject.getInt("code");
                                                                String message = jsonObject.getString("message");

                                                                //                                        // seller
                                                                //                                        JSONObject seller = jsonObject.getJSONObject("dataseller");
                                                                //
                                                                //                                        txtNamaToko.setText(seller.getString("toko"));
                                                                //                                        String gambar = seller.getString("gambar");
                                                                //                                        Picasso.get().load(itemView.getResources().getString(R.string.url) + "/profile/" +
                                                                //                                                gambar).into(profileToko);
                                                                //
                                                                //                                        // product
                                                                //                                        JSONObject product = jsonObject.getJSONObject("dataproduct");
                                                                //                                        txtNamaProduct.setText(product.getString("nama"));
                                                                //                                        gambar = product.getString("gambar");
                                                                //                                        Picasso.get().load(itemView.getResources().getString(R.string.url) + "/produk/" +
                                                                //                                                gambar).into(fotoProduct);
                                                                //
                                                                //
                                                                //                                        txtHargaProduct.setText("Rp " + product.getString("harga"));

                                                                if (code == 1){
                                                                    Toast.makeText(itemView.getContext(), message+"", Toast.LENGTH_LONG).show();
                                                                    customerWishlistFragment.getWishlistData();
                                                                }


                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            System.out.println("error saat update wishlist di wishlist fragment " + error);
                                                        }
                                                    }
                                            ){
                                                @Nullable
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("function", "deletewishlist");
                                                    params.put("idwishlist", wishlist.getId()+"");

                                                    return params;
                                                }
                                            };

                                            RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
                                            requestQueue.add(stringRequest);
                                        }
                                    });

                    // Set the Negative button with No name
                    // OnClickListener method is use
                    // of DialogInterface interface.
                    builder
                            .setNegativeButton(
                                    "No",
                                    new DialogInterface
                                            .OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which)
                                        {

                                            // If user click no
                                            // then dialog box is canceled.
                                            dialog.cancel();
                                        }
                                    });

                    // Create the Alert dialog
                    AlertDialog alertDialog = builder.create();

                    // Show the Alert Dialog box
                    alertDialog.show();

                }

            });

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), CustomerDetailProduct.class);
                    i.putExtra("idproduct", wishlist.getFk_barang());
                    i.putExtra("login", CustomerHomeActivity.login);
                    ((Activity)itemView.getContext()).startActivityForResult(i, 120);
                }
            });

        }


    }

    protected String moneySeparator(String harga) {
        int ctr = 0; // untuk menghitung berapa digit yg sudah masuk ke variable hasil
        String hasil = "";

        String awal = String.valueOf(harga);
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
