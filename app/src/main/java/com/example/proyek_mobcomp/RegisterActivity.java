package com.example.proyek_mobcomp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding bind;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bind = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        bind.rgRole.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rbCustomer){
                    bind.etRegisterToko.setText("");
                    bind.etRegisterToko.setEnabled(false);
                }
                else{
                    bind.etRegisterToko.setEnabled(true);
                }
            }
        });


        bind.etRegisterUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkField();
            }
        });

        bind.etRegisterEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkField();
            }
        });


        bind.etRegisterNama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkField();
            }
        });

        bind.etRegisterRekening.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkField();
            }
        });

        bind.etRegisterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkField();
            }
        });

        bind.etRegisterConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkField();
            }
        });

        if (bind.rbCustomer.isChecked() == false && bind.rbSeller.isChecked() == true) {
            bind.etRegisterToko.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    checkField();
                }
            });
        }
    }

    public void ToLogin(View v){
//        Intent i = new Intent(this, MainActivity.class);
//        startActivity(i);
        finish();
    }

    protected void checkField(){
        if (bind.etRegisterUsername.getText().toString().isEmpty() || bind.etRegisterEmail.getText().toString().isEmpty()
                || bind.etRegisterNama.getText().toString().isEmpty() || bind.etRegisterRekening.getText().toString().isEmpty()
                || bind.etRegisterPassword.getText().toString().isEmpty() || bind.etRegisterConfirm.getText().toString().isEmpty()
                ){
            bind.btnRegister.setEnabled(false);
        }else{
            bind.btnRegister.setEnabled(true);
        }
    }

    public void Register(View v){


        if (!bind.etRegisterEmail.getText().toString().matches(emailPattern)){
            Toast.makeText(this, "Email tidak valid!", Toast.LENGTH_SHORT).show();
        }else{
            if (bind.etRegisterPassword.getText().toString().equals(bind.etRegisterConfirm.getText().toString())){
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        getResources().getString(R.string.url) + "/register",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    int code = jsonObject.getInt("code");
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
                                System.out.println("error register " + error);
                            }
                        }
                ){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", bind.etRegisterUsername.getText().toString());
                        params.put("email", bind.etRegisterEmail.getText().toString());
                        params.put("namalengkap", bind.etRegisterNama.getText().toString());
                        params.put("rekening", bind.etRegisterRekening.getText().toString());
                        params.put("password", bind.etRegisterPassword.getText().toString());
                        if (bind.rbCustomer.isChecked() == false && bind.rbSeller.isChecked() == true){
                            params.put("registeras", "seller");
                            params.put("namatoko", bind.etRegisterToko.getText().toString());
                        }else{
                            params.put("registeras", "customer");
                        }


                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }else{
                Toast.makeText(this, "Pastikan Password dan Confirm Password sama!", Toast.LENGTH_SHORT).show();
                bind.btnRegister.setEnabled(false);
            }
        }
    }
}