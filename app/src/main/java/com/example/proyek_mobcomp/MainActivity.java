package com.example.proyek_mobcomp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cUser;
import com.example.proyek_mobcomp.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*

    Update Changes =
    23 Maret 2022 : perbaikan jika ter-banned maka tidak bisa login, baik itu manual login maupun auto-login (remember me)

 */

public class MainActivity extends AppCompatActivity {
    protected ActivityMainBinding bind;

    public static cUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());


//        SharedPreferences sharedpreferences = getSharedPreferences("data", getBaseContext().MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.remove("login");
//        editor.commit();


        SharedPreferences sharedpreferences = getSharedPreferences("data", getBaseContext().MODE_PRIVATE);
        if(sharedpreferences.getString("login",null) != null) {
            Gson gson = new Gson();
            String loginjson = sharedpreferences.getString("login", null);
            System.out.println(loginjson);

            user = gson.fromJson(loginjson, new TypeToken<cUser>(){}.getType());

            if (user.getRole().equalsIgnoreCase("CUSTOMER")){
                doLogin(user.getUsername(), 0);
                /*Intent i = new Intent(this, CustomerHomeActivity.class);
                i.putExtra("login", user.getUsername());
                startActivity(i);*/
            }else if (user.getRole().equalsIgnoreCase("SELLER")){
                doLogin(user.getUsername(), 0);
//                Intent i = new Intent(this, SellerActivity.class);
//                i.putExtra("login", user.getUsername());
//                startActivity(i);
            }
        }

        bind.etLoginUsername.addTextChangedListener(new TextWatcher() {
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

        bind.etLoginPassword.addTextChangedListener(new TextWatcher() {
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

        bind.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bind.etLoginUsername.getText().toString().equalsIgnoreCase("admin")){
                    if(bind.etLoginPassword.getText().toString().equalsIgnoreCase("admin")){
                        Intent i = new Intent(MainActivity.this, AdminActivity.class);
                        i.putExtra("login", bind.etLoginUsername.getText().toString());
                        startActivity(i);
                    }
                }else{
                    doLogin(bind.etLoginUsername.getText().toString(), 1);
                }
            }
        });
    }

    protected void doLogin(String txtUsername, int isManual) {
        /*
            note :
            - is Manual = 1 (manual), 0 (auto)
         */
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                            if (code == 1 || code == 2){
                                SharedPreferences sharedpreferences = getSharedPreferences("data", getBaseContext().MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("login",jsonObject.getString("datauser"));
                                editor.commit();
                            }


                            if (code == 1){ // customer
                                Intent i = new Intent(MainActivity.this, CustomerHomeActivity.class);
                                i.putExtra("login", txtUsername);
                                startActivity(i);
                            }else if (code == 2){ // seller
                                Intent i = new Intent(MainActivity.this, SellerActivity.class);
                                i.putExtra("login", txtUsername);
                                startActivity(i);
                            }else if (code == -4){
                                if (isManual == 0) { // delete data remember me dari android apabila user di-banned
                                    SharedPreferences sharedpreferences = getSharedPreferences("data", getBaseContext().MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.remove("login");
                                    editor.commit();
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
                        System.out.println("error login " + error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", txtUsername);
                params.put("ismanual", String.valueOf(isManual));
                if (isManual == 1){ // send password for verification
                    params.put("password", bind.etLoginPassword.getText().toString());
                }

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        requestQueue.add(stringRequest);
    }

    protected void checkField(){
        if (bind.etLoginUsername.getText().toString().isEmpty()
                || bind.etLoginPassword.getText().toString().isEmpty()){
            bind.btnLogin.setEnabled(false);
        }else{
            bind.btnLogin.setEnabled(true);
        }
    }

    public void ToRegister(View v){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}