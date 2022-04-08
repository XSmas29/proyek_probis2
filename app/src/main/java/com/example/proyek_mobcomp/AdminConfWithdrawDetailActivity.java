package com.example.proyek_mobcomp;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cTopup;
import com.example.proyek_mobcomp.classFolder.cUser;
import com.example.proyek_mobcomp.databinding.ActivityAdminConfTopUpDetailBinding;
import com.example.proyek_mobcomp.databinding.ActivityAdminConfWithdrawDetailBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdminConfWithdrawDetailActivity extends AppCompatActivity {

    private ActivityAdminConfWithdrawDetailBinding binding;

    int idwithdraw = -1;

    cTopup topup = null;
    cUser user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_conf_withdraw_detail);

        binding = ActivityAdminConfWithdrawDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCancel.setBackgroundColor(getResources().getColor(R.color.grey));
        binding.btnReject.setBackgroundColor(getResources().getColor(R.color.red));

        idwithdraw = getIntent().getIntExtra("idwithdraw", -1);

        if (idwithdraw != -1){
            getTopUpData();
        }


        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusTopUpChange(-1);
            }
        });

        binding.btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusTopUpChange(1);
            }
        });
    }

    private void statusTopUpChange(int i) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/admin/statuswithdrawchange",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("message");

                            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                            if (code == 1){
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error get top up data in conf fragment " + error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function", "statuswithdrawchange");
                params.put("idwithdraw", idwithdraw+"");
                params.put("status_topup", i+"");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void getTopUpData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnReject.setEnabled(false);
        binding.btnKonfirmasi.setEnabled(false);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/admin/getonetopupdata",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject historyarray = jsonObject.getJSONObject("datatopup");

                            int id = historyarray.getInt("id");
                            String fk_username = historyarray.getString("fk_username");
                            int jumlah_topup = historyarray.getInt("jumlah_topup");
                            String bukti_topup = historyarray.getString("bukti_topup");
                            int status_topup = historyarray.getInt("status_topup");
                            String created_at = historyarray.getString("created_at");
                            String updated_at = historyarray.getString("updated_at");

                            topup = new cTopup(id, fk_username, jumlah_topup, bukti_topup, status_topup, created_at, updated_at);


                            JSONObject userarray = jsonObject.getJSONObject("datauser");

                            String username = userarray.getString("username");
                            String password = userarray.getString("password");
                            String email = userarray.getString("email");
                            String nama = userarray.getString("nama");
                            String rekening = userarray.getString("rekening");
                            String saldo = userarray.getString("saldo");
                            String toko = userarray.getString("toko");
                            String role = userarray.getString("role");
                            String gambar = userarray.getString("gambar");
                            int status = userarray.getInt("status");
                            int is_verified = userarray.getInt("is_verified");

                            user =  new cUser(username, password, email, nama, rekening, saldo, toko, role, gambar, status, is_verified);

                            showData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error get one withdraw data in conf activity " + error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function", "getonetopupdata");
                params.put("idtopup", idwithdraw+"");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showData() {
        binding.textViewNamaToko.setText("Nama Toko : " + user.getToko());
        binding.textViewUsernameUser.setText("Username : " + user.getUsername());
        binding.textViewIdWithdraw.setText("ID Withdraw : " + topup.getId());
        binding.textViewJumTopUp.setText("Jumlah Withdraw : Rp " + topup.getJumlahInString());

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");

        Date d = null;
        try
        {
            d = input.parse(topup.getCreated());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        System.out.println(formatted);

        binding.textViewTglWithdraw.setText("Tanggal Request Withdraw : " + formatted);


        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.btnReject.setEnabled(true);
        binding.btnKonfirmasi.setEnabled(true);
    }
}