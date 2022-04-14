package com.example.proyek_mobcomp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.example.proyek_mobcomp.classFolder.cReview;
import com.example.proyek_mobcomp.classFolder.cWishlist;
import com.example.proyek_mobcomp.databinding.ActivityCustomerDetailProductBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterCustomerHomeProduct;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterReview;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDetailProduct extends AppCompatActivity {
    protected ActivityCustomerDetailProductBinding binding;
    int idProduct = -1;
    int jumlah = 1;

    AppDatabase db;

    cProduct product = null;
    ArrayList<cProduct> arrRecommendationProduct = new ArrayList<>();
    ArrayList<cReview> arrReview = new ArrayList<>();

    int isWishlisted  = 0; // 0 = not wishlist, 1 = wishlisted
    int idWishlist = -1; // id wishlist, jika diwishlist

    String username = CustomerHomeActivity.login;

    LinearLayout llKategoriContainer, llProductContainer;
    TextView txtNamaKategori;
    LinearLayout[] arrLlProduct = new LinearLayout[5];
    ImageView[] arrImageView = new ImageView[5];
    TextView[] arrTxtNamaProduct = new TextView[5];
    TextView[] arrTxtHargaProduct = new TextView[5];

    RecyclerAdapterReview recyclerAdapterReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail_product);

        binding = ActivityCustomerDetailProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getInstance(getBaseContext());

        idProduct = getIntent().getIntExtra("idproduct", -1);
        username = getIntent().getStringExtra("login");

        getAllProduct();

        //Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
        if (idProduct != -1) {
            getProductDetail();
        }

        jumlah = 1;

        binding.imageButtonMinus.setEnabled(false);

        binding.editTextJumlah.setText(jumlah+"");

        binding.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumlah--;
                binding.imageButtonPlus.setEnabled(true);
                if (jumlah <= 1){
                    jumlah = 1;
                    binding.imageButtonMinus.setEnabled(false);
                }
                binding.editTextJumlah.setText(jumlah+"");
            }
        });

        binding.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    jumlah++;
                    binding.imageButtonMinus.setEnabled(true);

                    if (jumlah >= product.getStok()) {
                        jumlah = product.getStok();
                        //Toast.makeText(getBaseContext(), "Jumlah lebih dari stok", Toast.LENGTH_SHORT).show();
                        binding.imageButtonPlus.setEnabled(false);
                    }
                    binding.editTextJumlah.setText(jumlah + "");
                }
            }
        });

        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jumlah > 0 && jumlah <= product.getStok()){
                    //int code = 1;
                    //cek stok product
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            getResources().getString(R.string.url) + "/customer/checkstockproduct",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    System.out.println(response);

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);

                                        int code = jsonObject.getInt("code");
                                        String message = jsonObject.getString("message");

                                        if (code == 0){
                                            getProductDetail();
                                            getAllProduct();
                                            Toast.makeText(getBaseContext(), message+"", Toast.LENGTH_SHORT).show();
                                        }else{
                                            new AsyncTask<Void, Void, String>(){
                                                @Override
                                                protected String doInBackground(Void... voids) {
                                                    //cek ada barang yg sama gk di cart
                                                    List<cCart> checkProductResult = db.cartDao().getCartByIdProductAndUsername(idProduct, username);
                                                    if (checkProductResult.size() > 0){
                                                        //System.out.println("ada");
                                                        cCart productCart = checkProductResult.get(0);
                                                        productCart.setJumlah(productCart.getJumlah() + jumlah);
                                                        productCart.setHarga(product.getHarga());
                                                        db.cartDao().updateCart(productCart);
                                                    }else{
                                                        //System.out.println("gk ada");
                                                        cCart cart = new cCart(idProduct, username, jumlah, product.getHarga(), product.getFk_seller());
                                                        db.cartDao().insertCart(cart);
                                                    }

                                                    return "sukses";
                                                }

                                                @Override
                                                protected void onPostExecute(String status) {
                                                    System.out.println(status);
                                                    if (status.equalsIgnoreCase("sukses")) {
                                                        Toast.makeText(getBaseContext(), "Berhasil menambahkan ke cart!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }.execute();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("error check stock product in detail product : " + error);
                                }
                            }

                    ){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("function","updatestock");
                            params.put("idproduct", idProduct+"");
                            params.put("jumlah", String.valueOf(jumlah));
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                    requestQueue.add(stringRequest);


                }else{
                    //Toast.makeText(getBaseContext(), "Pastikan jumlah yang dibeli tepat", Toast.LENGTH_SHORT).show();
                }
            }
        });


        llProductContainer = findViewById(R.id.llProductContainer);

        arrLlProduct[0] = findViewById(R.id.ll0);
        arrLlProduct[1] = findViewById(R.id.ll1);
        arrLlProduct[2] = findViewById(R.id.ll2);
        arrLlProduct[3] = findViewById(R.id.ll3);
        arrLlProduct[4] = findViewById(R.id.ll4);

        arrImageView[0] = findViewById(R.id.imageView_product0);
        arrImageView[1] = findViewById(R.id.imageView_product1);
        arrImageView[2] = findViewById(R.id.imageView_product2);
        arrImageView[3] = findViewById(R.id.imageView_product3);
        arrImageView[4] = findViewById(R.id.imageView_product4);

        arrTxtNamaProduct[0] = findViewById(R.id.textView_namaProduct0);
        arrTxtNamaProduct[1] = findViewById(R.id.textView_namaProduct1);
        arrTxtNamaProduct[2] = findViewById(R.id.textView_namaProduct2);
        arrTxtNamaProduct[3] = findViewById(R.id.textView_namaProduct3);
        arrTxtNamaProduct[4] = findViewById(R.id.textView_namaProduct4);

        arrTxtHargaProduct[0] = findViewById(R.id.textView_hargaProduct0);
        arrTxtHargaProduct[1] = findViewById(R.id.textView_hargaProduct1);
        arrTxtHargaProduct[2] = findViewById(R.id.textView_hargaProduct2);
        arrTxtHargaProduct[3] = findViewById(R.id.textView_hargaProduct3);
        arrTxtHargaProduct[4] = findViewById(R.id.textView_hargaProduct4);




        // btn wishilist
        binding.imageButtonWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        getResources().getString(R.string.url) + "/customer/updatewishlist",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    int code = jsonObject.getInt("code");
                                    String message = jsonObject.getString("message");

                                    JSONArray arrayWishlist = jsonObject.getJSONArray("datawishlist");
                                    CustomerHomeActivity.arrayListWishlist.clear();
                                    for (int i = 0; i < arrayWishlist.length(); i++){
                                        int id = arrayWishlist.getJSONObject(i).getInt("id");
                                        String fk_user = arrayWishlist.getJSONObject(i).getString("fk_user");
                                        int fk_barang = arrayWishlist.getJSONObject(i).getInt("fk_barang");

                                        CustomerHomeActivity.arrayListWishlist.add(
                                                new cWishlist(id, fk_user, fk_barang)
                                        );
                                    }

                                    if (isWishlisted == 1){
                                        binding.imageButtonWishlist.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                                        isWishlisted = 0;
                                        idWishlist = -1;
                                    }


                                    showIsWishlist();
                                    Toast.makeText(getBaseContext(), message+"", Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("error update wishlist : " + error);
                            }
                        }

                ){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        if (isWishlisted == 0){
                            //System.out.println("test1");
                            params.put("function","addwishlist");
                            params.put("idproduct", String.valueOf(idProduct));
                            params.put("username", username);
                        }else {
                            //System.out.println("test2");
                            params.put("function","deletewishlist");
                            params.put("idwishlist", idWishlist+"");
                        }

                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                requestQueue.add(stringRequest);
            }
        });
    }

    private void getAllProduct() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnAddToCart.setEnabled(false);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        requestQueue.add(stringRequest);
    }

    protected void getProductDetail(){ // mendapatkan data detail product tersebut juga recommendnya
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/customer/getdetailproduct",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject productObject = jsonObject.getJSONObject("datadetailproduct");
//                            for (int i = 0; i < arrayProduct.length(); i ++){
                                int id = productObject.getInt("id");
                                String fk_seller = productObject.getString("fk_seller");
                                int fk_kategori = productObject.getInt("fk_kategori");
                                String nama = productObject.getString("nama");
                                String deskripsi = productObject.getString("deskripsi");
                                int harga = productObject.getInt("harga");
                                int stok =productObject.getInt("stok");
                                String gambar = productObject.getString("gambar");
                                int is_deleted = productObject.getInt("is_deleted");

                                product = new cProduct(id, fk_seller, fk_kategori, nama, deskripsi, harga, stok, gambar, is_deleted);
//                            }

                            JSONArray sellerArray = jsonObject.getJSONArray("dataseller");
                            for (int i = 0; i < sellerArray.length();i++) {
                                nama = sellerArray.getJSONObject(i).getString("toko");
                                gambar = sellerArray.getJSONObject(i).getString("gambar");
                                binding.textViewNamaToko.setText(nama);
                                Picasso.get().load(getResources().getString(R.string.url) + "/profile/" +
                                        gambar).into(binding.imageViewProfileToko);
                            }

                            JSONArray reviewArray = jsonObject.getJSONArray("datareview");
                            arrReview.clear();
                            for (int i = 0; i < reviewArray.length();i++) {
                                id = reviewArray.getJSONObject(i).getInt("id");
                                int fk_htrans = reviewArray.getJSONObject(i).getInt("fk_htrans");
                                int fk_dtrans = reviewArray.getJSONObject(i).getInt("fk_dtrans");
                                String fk_user = reviewArray.getJSONObject(i).getString("fk_user");
                                int star = reviewArray.getJSONObject(i).getInt("star");
                                String isi = reviewArray.getJSONObject(i).getString("isi");
                                nama = reviewArray.getJSONObject(i).getString("nama");
                                String tanggal = reviewArray.getJSONObject(i).getString("tanggal");

                                arrReview.add(new cReview(id, fk_htrans, fk_dtrans,fk_user,star, isi, tanggal, nama));
                            }

                            JSONArray arrayRecProduct = jsonObject.getJSONArray("datarecommendproduct");
                            arrRecommendationProduct.clear();
                            for (int i = 0; i < arrayRecProduct.length(); i ++){
                                id = arrayRecProduct.getJSONObject(i).getInt("id");
                                fk_seller = arrayRecProduct.getJSONObject(i).getString("fk_seller");
                                fk_kategori = arrayRecProduct.getJSONObject(i).getInt("fk_kategori");
                                nama = arrayRecProduct.getJSONObject(i).getString("nama");
                                deskripsi = arrayRecProduct.getJSONObject(i).getString("deskripsi");
                                harga = arrayRecProduct.getJSONObject(i).getInt("harga");
                                stok = arrayRecProduct.getJSONObject(i).getInt("stok");
                                gambar = arrayRecProduct.getJSONObject(i).getString("gambar");
                                is_deleted = arrayRecProduct.getJSONObject(i).getInt("is_deleted");

                                arrRecommendationProduct.add(new cProduct(id, fk_seller, fk_kategori, nama, deskripsi, harga, stok, gambar, is_deleted));
                            }

                            showProduct();
                            setReviewRv();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error getdetailproduct = " + error.getMessage());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function","getdetailproduct");
                params.put("idproduct", String.valueOf(idProduct));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setReviewRv() {
        binding.recyclerViewCustReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewCustReview.setHasFixedSize(true);

        recyclerAdapterReview = new RecyclerAdapterReview(
                arrReview
        );
        binding.recyclerViewCustReview.setAdapter(recyclerAdapterReview);
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.btnAddToCart.setEnabled(true);
    }

    protected void showProduct() {
        Picasso.get().load(getResources().getString(R.string.url) + "/produk/" +
                product.getGambar()).into(binding.imageViewProductPicture);

        binding.textViewProductName.setText(product.getNama());
        binding.textViewProductPrice.setText("Rp " + product.getHargaInString());
        binding.textViewProductStock.setText("Stok : " + product.getStok());
        binding.textViewProductDescription.setText("Detail produk : \n"+product.getDeskripsi());

        if (product.getStok() <= 0){
            binding.imageButtonMinus.setEnabled(false);
            binding.imageButtonPlus.setEnabled(false);
            jumlah = 0;
            binding.editTextJumlah.setText(jumlah);
        }

        showIsWishlist();

        int ctrBarang = 0;
        // show recommendation product
        for (int i = 0; i < arrRecommendationProduct.size(); i++){
            if (ctrBarang < 5){
                Picasso.get().load(getResources().getString(R.string.url) + "/produk/" +
                        arrRecommendationProduct.get(i).getGambar()).into(arrImageView[ctrBarang]);

                ViewGroup.LayoutParams params = arrImageView[ctrBarang].getLayoutParams();
                params.height = 120;
                arrImageView[ctrBarang].setLayoutParams(params);

                arrTxtNamaProduct[ctrBarang].setText(arrRecommendationProduct.get(i).getNama());
                arrTxtHargaProduct[ctrBarang].setText("Rp " + arrRecommendationProduct.get(i).getHargaInString());

                int idProduct = arrRecommendationProduct.get(i).getId();
                arrLlProduct[ctrBarang].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(CustomerDetailProduct.this, CustomerDetailProduct.class);
                        i.putExtra("idproduct", idProduct);
                        i.putExtra("login", CustomerHomeActivity.login);
                        startActivityForResult(i, 100);
                    }
                });
                ctrBarang++;

            }
        }
    }

    protected void showIsWishlist(){
        for (int i = 0; i < CustomerHomeActivity.arrayListWishlist.size(); i++){
            if (CustomerHomeActivity.arrayListWishlist.get(i).getFk_barang() == product.getId()
                    && CustomerHomeActivity.arrayListWishlist.get(i).getFk_user().equals(CustomerHomeActivity.login)){
                binding.imageButtonWishlist.setImageResource(R.drawable.ic_baseline_favorite_24);
                isWishlisted = 1;
                idWishlist = CustomerHomeActivity.arrayListWishlist.get(i).getId();
            }
        }

    }
}