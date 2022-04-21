package com.example.proyek_mobcomp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cTopup;
import com.example.proyek_mobcomp.databinding.ActivityCustomerTopUpSaldoBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterCustomerHistoryTopUp;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterHeaderPurchase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerTopUpSaldoActivity extends AppCompatActivity {

    protected ActivityCustomerTopUpSaldoBinding binding;

    ArrayList<cTopup> arrTopUp = new ArrayList<cTopup>();

    RecyclerAdapterCustomerHistoryTopUp recyclerAdapterCustomerHistoryTopUp;

    Bitmap selectedImage = null;
    String ext = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_top_up_saldo);

        binding = ActivityCustomerTopUpSaldoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSelectBukti.setBackgroundColor(getResources().getColor(R.color.grey));
        binding.btnClear.setBackgroundColor(getResources().getColor(R.color.grey));

        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
            }
        });

        binding.imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getJumlahSaldo();
        getBukti();
        tambahSaldo();
    }

    public void getBukti() {
        binding.btnSelectBukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 3000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3000) {
            if(resultCode == Activity.RESULT_OK) {
                try {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    selectedImage = BitmapFactory.decodeStream(imageStream);
                    binding.imageViewBuktiTopUp.setImageBitmap(selectedImage);
                    ViewGroup.LayoutParams params = binding.imageViewBuktiTopUp.getLayoutParams();
                    params.height = 200;
                    binding.imageViewBuktiTopUp.setLayoutParams(params);
                    //binding.textViewNamaBukti.setText(imageUri.);
                    ext = GetFileExtension(imageUri);

//                    GetFileExtension(imageUri);
//                    Toast.makeText(this, "Exten: "+GetFileExtension(imageUri), Toast.LENGTH_LONG).show();

                    binding.progressBar.setVisibility(View.INVISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    binding.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void getJumlahSaldo() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/customer/getjumlahsaldo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("message");
                            JSONObject user = jsonObject.getJSONObject("datauser");
                            int saldo = user.getInt("saldo");


                            if (code != 1){
                                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                            }else{
                                String money = moneySeparator(saldo);
                                binding.textViewJumlahSaldo.setText("MyWallet Balance : Rp " + money);

                                JSONArray historyarray = jsonObject.getJSONArray("datatopup");
                                arrTopUp = new ArrayList<>();
                                for (int i = 0; i < historyarray.length();i++){
                                    int id = historyarray.getJSONObject(i).getInt("id");
                                    String fk_username = historyarray.getJSONObject(i).getString("fk_username");
                                    int jumlah_topup = historyarray.getJSONObject(i).getInt("jumlah_topup");
                                    String bukti_topup = historyarray.getJSONObject(i).getString("bukti_topup");
                                    int status_topup = historyarray.getJSONObject(i).getInt("status_topup");
                                    String created_at = historyarray.getJSONObject(i).getString("created_at");
                                    String updated_at = historyarray.getJSONObject(i).getString("updated_at");

                                    arrTopUp.add(new cTopup(id, fk_username, jumlah_topup, bukti_topup, status_topup, created_at, updated_at));
                                }

                                setRv();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error get saldo " + error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function","getjumlahsaldo");
                params.put("username", CustomerHomeActivity.login+"");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this );
        requestQueue.add(stringRequest);
    }

    protected String moneySeparator(int harga) {
        int ctr = 0; // untuk menghitung berapa digit yg sudah masuk ke variable hasil
        String hasil = "";

        String awal = String.valueOf(harga);
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

    protected void setRv() {
        binding.recyclerViewHistoryTopUp.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewHistoryTopUp.setHasFixedSize(true);

        recyclerAdapterCustomerHistoryTopUp = new RecyclerAdapterCustomerHistoryTopUp(
                arrTopUp
        );
        binding.recyclerViewHistoryTopUp.setAdapter(recyclerAdapterCustomerHistoryTopUp);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    private void tambahSaldo() {
        binding.btnConfirmAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editTextTopUpSaldo.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getBaseContext(), "Nominal tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }else{
                    int ammount = Integer.parseInt(binding.editTextTopUpSaldo.getText().toString());

                    if (ammount < 10000){
                        Toast.makeText(getBaseContext(), "Nominal tidak boleh dibawah Rp 10.000", Toast.LENGTH_SHORT).show();
                    }else{
                        if (selectedImage == null){
                            Toast.makeText(getBaseContext(), "Bukti transfer wajib disertakan!", Toast.LENGTH_SHORT).show();
                        }else{
                            binding.progressBar.setVisibility(View.VISIBLE);
                            StringRequest stringRequest = new StringRequest(
                                    Request.Method.POST,
                                    getResources().getString(R.string.url) + "/customer/tambahsaldo",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            System.out.println(response);

                                            try {
                                                JSONObject jsonObject = new JSONObject(response);

                                                int code = jsonObject.getInt("code");
                                                String message = jsonObject.getString("message");


                                                if (code == 1){
                                                    getJumlahSaldo();
                                                    resetForm();
                                                }
                                                Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                                                binding.progressBar.setVisibility(View.INVISIBLE);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            System.out.println("error tambah saldo " + error);
                                            binding.progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }
                            ){
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("function","tambahsaldo");
                                    params.put("username", CustomerHomeActivity.login+"");
                                    params.put("ammount", ammount+"");
                                    if (selectedImage != null){
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg")){
                                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                        }else if (ext.equalsIgnoreCase("png")){
                                            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                        }else{
                                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                        }

                                        byte[] byteArray = baos.toByteArray();
                                        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

                                        params.put("foto", base64);
                                        params.put("ext", ext);
                                    }
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext() );
                            requestQueue.add(stringRequest);
                        }
                    }
                }

            }
        });
    }

    // Get Extension
    public String GetFileExtension(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();

        // Return file Extension
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void resetForm(){
        binding.editTextTopUpSaldo.setText("");
        binding.textViewNamaBukti.setText("");
        ViewGroup.LayoutParams params = binding.imageViewBuktiTopUp.getLayoutParams();
        params.height = 0;
        binding.imageViewBuktiTopUp.setLayoutParams(params);
        selectedImage = null;
        ext = "";
        binding.imageViewBuktiTopUp.setImageResource(0);
    }


}