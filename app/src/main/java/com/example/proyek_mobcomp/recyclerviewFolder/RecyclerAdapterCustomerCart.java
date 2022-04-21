package com.example.proyek_mobcomp.recyclerviewFolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.proyek_mobcomp.AppDatabase;
import com.example.proyek_mobcomp.CustomerCartFragment;
import com.example.proyek_mobcomp.CustomerDetailProduct;
import com.example.proyek_mobcomp.CustomerHomeActivity;
import com.example.proyek_mobcomp.R;
import com.example.proyek_mobcomp.classFolder.cCart;
import com.example.proyek_mobcomp.classFolder.cKategori;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerAdapterCustomerCart extends RecyclerView.Adapter<RecyclerAdapterCustomerCart.ViewHolder> {
    ArrayList<cCart> arrCart = new ArrayList<>();
    CustomerCartFragment customerCartFragment;

    public RecyclerAdapterCustomerCart(ArrayList<cCart> arrCart, CustomerCartFragment customerCartFragment) {
        this.arrCart = arrCart;
        this.customerCartFragment = customerCartFragment;
    }

    @NonNull
    @Override
    public RecyclerAdapterCustomerCart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_layout_cart, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterCustomerCart.ViewHolder holder, int position) {
        cCart cart = arrCart.get(position);
        holder.bind(cart, position);
    }

    @Override
    public int getItemCount() {
        return arrCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppDatabase db;
        LinearLayout ll;
        ImageView profileToko, fotoProduct;
        TextView txtNamaToko, txtNamaProduct, txtHargaProduct, txtJumlah, txtTotal;
        ImageButton btnDelete;
        EditText txtCatatan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            db = AppDatabase.getInstance(itemView.getContext());
            ll = itemView.findViewById(R.id.ll);
            profileToko = itemView.findViewById(R.id.imageView_profileToko);
            fotoProduct = itemView.findViewById(R.id.imageView_productPicture);
            txtNamaToko = itemView.findViewById(R.id.textView_namaToko);
            txtNamaProduct = itemView.findViewById(R.id.textView_productName);
            txtHargaProduct = itemView.findViewById(R.id.textView_productPrice);
            txtJumlah = itemView.findViewById(R.id.textView_productJumlah);
            txtTotal = itemView.findViewById(R.id.textView_totalPrice);
            btnDelete = itemView.findViewById(R.id.imageButton_delete);
            txtCatatan = itemView.findViewById(R.id.editText_catatan);
        }

        public void bind(cCart cart, int position) {
            int harga = 0;
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
                                // update harga product di db cart
                                cart.setHarga(product.getInt("harga"));
                                db.cartDao().updateCart(cart);
                                gambar = product.getString("gambar");
                                Picasso.get().load(itemView.getResources().getString(R.string.url) + "/produk/" +
                                        gambar).into(fotoProduct);

                                int total = cart.getJumlah() * cart.getHarga();

                                txtHargaProduct.setText("Rp " + cart.getHargaInString());
                                txtJumlah.setText(cart.getJumlah()+"");
                                String money = moneySeparator(total);
                                txtTotal.setText("Rp " + money);
                                txtCatatan.setText(cart.getCatatan());
                                CustomerCartFragment.total += total;
                                customerCartFragment.setTotal();
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
                    params.put("idproduct", cart.getIdProduct()+"");

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(itemView.getContext());
            requestQueue.add(stringRequest);

            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), CustomerDetailProduct.class);
                    i.putExtra("idproduct", cart.getIdProduct());
                    i.putExtra("login", CustomerHomeActivity.login);
                    ((Activity)itemView.getContext()).startActivityForResult(i, 130);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.cartDao().deleteCart(cart);
                    customerCartFragment.setRv();
                    customerCartFragment.setTotal();
                }
            });

            txtCatatan.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 150){
                        Toast.makeText(itemView.getContext(), "Catatan telah mencapai 150 karakter", Toast.LENGTH_LONG).show();
                        txtCatatan.clearFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    cart.setCatatan(txtCatatan.getText().toString());
                    db.cartDao().updateCart(cart);
                }
            });
        }
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
