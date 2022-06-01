package com.example.proyek_mobcomp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cKategori;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.example.proyek_mobcomp.databinding.ActivitySellerAddEditBarangBinding;
import com.example.proyek_mobcomp.databinding.ActivitySellerBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellerAddEditBarangActivity extends AppCompatActivity {

    ActivitySellerAddEditBarangBinding binding;
    private Bitmap bitmap;

    cProduct produk;

    ArrayList<cKategori> listKategori = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySellerAddEditBarangBinding.inflate(getLayoutInflater());

        if (getIntent().hasExtra("produk")){
            produk = getIntent().getParcelableExtra("produk");
            binding.textView11.setText("Edit Produk");
            binding.btnUploadProduk.setText("Edit Produk");
            binding.edAddNamaProduk.setText(produk.getNama());
            binding.edAddDeskripsiProduk.setText(produk.getDeskripsi());
            binding.edAddHargaProduk.setText(produk.getHargaInString() + "");
            binding.edAddStokProduk.setText(produk.getStok() + "");
            Picasso.get()
                    .load(getResources().getString(R.string.url) + "/produk/" + produk.getGambar())
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(binding.imgPlaceholderProduk, new Callback() {
                        @Override
                        public void onSuccess() {
                            BitmapDrawable currentImage = (BitmapDrawable)binding.imgPlaceholderProduk.getDrawable();
                            bitmap = currentImage.getBitmap();
                            System.out.println("berhasil");
                        }

                        @Override
                        public void onError(Exception e) {
                            System.out.println("gagal");
                        }
                    });
        }
        getkategori();
        setContentView(binding.getRoot());
    }

    public void chooseFile(View v){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                binding.imgPlaceholderProduk.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //add atau edit produk
    public void addProduk(View v){
        if (binding.edAddNamaProduk.getText().toString().isEmpty() || binding.edAddDeskripsiProduk.getText().toString().isEmpty() ||
            binding.edAddHargaProduk.getText().toString().isEmpty() || binding.edAddStokProduk.getText().toString().isEmpty() || bitmap == null){
            Toast.makeText(this, "Masih ada Field kosong!", Toast.LENGTH_SHORT).show();
        }
        else{
            if (getIntent().hasExtra("produk")){

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        getResources().getString(R.string.url) + "/seller/editproduk",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
                                Toast.makeText(getApplicationContext(), "Berhasil Edit Produk!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("error edit produk : " + error.getMessage());
                            }
                        }
                ){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("nama", binding.edAddNamaProduk.getText().toString());
                        params.put("desc", binding.edAddDeskripsiProduk.getText().toString());
                        params.put("harga", binding.edAddHargaProduk.getText().toString());
                        params.put("stok", binding.edAddStokProduk.getText().toString());
                        params.put("kategori", binding.spFilterSellerKategori3.getSelectedItem().toString());
                        params.put("gambar", getStringImage(bitmap));
                        params.put("id", produk.getId() + "");
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }
            else{
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        getResources().getString(R.string.url) + "/seller/addproduk",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
                                Toast.makeText(getApplicationContext(), "Berhasil Add Produk!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("error add produk : " + error.getMessage());
                            }
                        }
                ){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("nama", binding.edAddNamaProduk.getText().toString());
                        params.put("desc", binding.edAddDeskripsiProduk.getText().toString());
                        params.put("harga", binding.edAddHargaProduk.getText().toString());
                        params.put("stok", binding.edAddStokProduk.getText().toString());
                        params.put("kategori", binding.spFilterSellerKategori3.getSelectedItem().toString());
                        params.put("gambar", getStringImage(bitmap));
                        params.put("seller", SellerActivity.login);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void getkategori(){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/seller/getkategori",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray arraykategori = jsonObject.getJSONArray("datakategori");

                            for (int i = 0; i < arraykategori.length(); i++) {
                                int id = arraykategori.getJSONObject(i).getInt("id");
                                String nama = arraykategori.getJSONObject(i).getString("nama");
                                String tipe = arraykategori.getJSONObject(i).getString("tipe");

                                listKategori.add(new cKategori(id, nama, tipe));
                            }

                            ArrayList<String> listNamaKategori = new ArrayList<>();
                            for (int i = 0; i < listKategori.size(); i++) {
                                listNamaKategori.add(listKategori.get(i).getNama());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, listNamaKategori);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.spFilterSellerKategori3.setAdapter(adapter);

                            String selected = "";
                            if (getIntent().hasExtra("produk")){
                                for (int i = 0; i < listKategori.size(); i++) {
                                    if (listKategori.get(i).getId() == produk.getFk_kategori()){
                                        selected = listKategori.get(i).getNama();
                                        break;
                                    }
                                }
                                for (int i = 0; i < listNamaKategori.size(); i++) {
                                    if (selected.equalsIgnoreCase(listNamaKategori.get(i))){
                                        binding.spFilterSellerKategori3.setSelection(i);
                                        break;
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error load kategori : " + error.getMessage());
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}