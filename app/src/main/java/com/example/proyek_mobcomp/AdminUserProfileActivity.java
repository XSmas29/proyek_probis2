package com.example.proyek_mobcomp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cUser;
import com.example.proyek_mobcomp.databinding.ActivityAdminUserProfileBinding;
import com.example.proyek_mobcomp.databinding.ActivityCustomerHomeBinding;
import com.example.proyek_mobcomp.databinding.ActivityCustomerProfileBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AdminUserProfileActivity extends AppCompatActivity {

    protected ActivityAdminUserProfileBinding binding;

    String[] arrStatus = {"Active", "Ban"};

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    cUser user;
    Bitmap selectedImage = null;
    String ext = "";

    public String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_profile);

        binding = ActivityAdminUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        username = getIntent().getStringExtra("username");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getBaseContext(), android.R.layout.simple_spinner_item, arrStatus
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerStatus.setAdapter(adapter);

        //ganti warna btn
        binding.btnChangePhoto.setBackgroundColor(getResources().getColor(R.color.grey));
        binding.btnResetForm.setBackgroundColor(getResources().getColor(R.color.grey));
        binding.btnBack.setBackgroundColor(getResources().getColor(R.color.grey));

        binding.etRegisterUsername.addTextChangedListener(new TextWatcher() {
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

        binding.etRegisterEmail.addTextChangedListener(new TextWatcher() {
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


        binding.etRegisterNama.addTextChangedListener(new TextWatcher() {
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

        binding.etRegisterRekening.addTextChangedListener(new TextWatcher() {
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

//        if (binding.cbChangePassword.isChecked() == true){

        binding.etRegisterNewPassword.addTextChangedListener(new TextWatcher() {
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

        binding.etRegisterConfirm.addTextChangedListener(new TextWatcher() {
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
//        }

        binding.etRegisterToko.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (binding.etRegisterToko.isEnabled() == true){
                    checkField();
                }
            }
        });

        binding.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (user != null){
                    if (position == 0 && user.getStatus() == 1){
                        binding.btnSaveChanges.setEnabled(false);
                    }

                    if (position == 0 && user.getStatus() == 0){
                        binding.btnSaveChanges.setEnabled(true);
                    }

                    if (position == 1 && user.getStatus() == 1){
                        binding.btnSaveChanges.setEnabled(true);
                    }

                    if (position == 1 && user.getStatus() == 0){
                        binding.btnSaveChanges.setEnabled(false);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("masuk");
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 3100);
            }
        });

        binding.cbChangePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ViewGroup.LayoutParams params = binding.llPassword.getLayoutParams();
                if (isChecked){
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    binding.llPassword.setLayoutParams(params);
                    binding.btnSaveChanges.setEnabled(false);
                    checkField();
                }else{
                    params.height = 0;
                    binding.llPassword.setLayoutParams(params);

                    binding.etRegisterNewPassword.setText("");
                    binding.etRegisterConfirm.setText("");
                    checkField();
                }
            }
        });

        binding.btnResetForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
            }
        });

        getUserData();
        resetForm();
//        getPhoto();
        saveProfileChanges();
    }

    protected void checkField(){
        if (binding.etRegisterUsername.getText().toString().isEmpty() || binding.etRegisterEmail.getText().toString().isEmpty()
                || binding.etRegisterNama.getText().toString().isEmpty() || binding.etRegisterRekening.getText().toString().isEmpty()){
            binding.btnSaveChanges.setEnabled(false);
        }else{
            String username = binding.etRegisterUsername.getText().toString();
            String email = binding.etRegisterEmail.getText().toString();
            String nama = binding.etRegisterNama.getText().toString();
            String rekening = binding.etRegisterRekening.getText().toString();

            if (username.equals(user.getUsername()) && email.equals(user.getEmail()) && nama.equals(user.getNama()) &&
                    rekening.equals(user.getRekening()) && selectedImage == null){
                binding.btnSaveChanges.setEnabled(false);
                if (binding.cbChangePassword.isChecked() == true){
                    if ( binding.etRegisterNewPassword.getText().toString().isEmpty() ||
                            binding.etRegisterConfirm.getText().toString().isEmpty()){
                        binding.btnSaveChanges.setEnabled(false);
                    }else{
                        binding.btnSaveChanges.setEnabled(true);
                    }
                }

                if (binding.etRegisterToko.isEnabled() == true){
                    if (binding.etRegisterToko.getText().toString().isEmpty()){
                        binding.btnSaveChanges.setEnabled(false);
                    }else{
                        if (binding.etRegisterToko.getText().toString().equals(user.getToko())){
                            binding.btnSaveChanges.setEnabled(false);
                        }else{
                            binding.btnSaveChanges.setEnabled(true);
                        }
                    }
                }
            }
            else{
                binding.btnSaveChanges.setEnabled(true);
                if (binding.cbChangePassword.isChecked() == true){
                    if ( binding.etRegisterNewPassword.getText().toString().isEmpty() ||
                            binding.etRegisterConfirm.getText().toString().isEmpty()){
                        binding.btnSaveChanges.setEnabled(false);
                    }else{
                        if (binding.etRegisterToko.getText().toString().equals(user.getToko())){
                            binding.btnSaveChanges.setEnabled(false);
                        }else{
                            binding.btnSaveChanges.setEnabled(true);
                        }
                    }
                }

                if (binding.etRegisterToko.isEnabled() == true){
                    if (binding.etRegisterToko.getText().toString().isEmpty()){
                        binding.btnSaveChanges.setEnabled(false);
                    }else{
                        binding.btnSaveChanges.setEnabled(true);
                    }
                }

            }

        }
    }

    public void resetForm(){
        selectedImage = null;
        ext = "";
        getUserData();
        binding.cbChangePassword.setChecked(false);

        binding.etRegisterNewPassword.setText("");
        binding.etRegisterConfirm.setText("");

    }


    public void showData() {
        Picasso.get().load(getResources().getString(R.string.url) + "/profile/" +
                user.getGambar()).into(binding.imageViewProfilePicture);
        binding.etRegisterUsername.setText(user.getUsername());
        binding.etRegisterEmail.setText(user.getEmail());
        binding.etRegisterNama.setText(user.getNama());
        binding.etRegisterRekening.setText(user.getRekening());

        if (!user.getToko().isEmpty()){
            ViewGroup.LayoutParams params = binding.etRegisterToko.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            binding.etRegisterToko.setLayoutParams(params);
            binding.etRegisterToko.setText(user.getToko());
            binding.etRegisterToko.setEnabled(true);
        }

        if (user.getStatus() == 1){
            binding.spinnerStatus.setSelection(0);
        }else{
            binding.spinnerStatus.setSelection(1);
        }

        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    public void getUserData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/customer/getuserdata",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject userobject = jsonObject.getJSONObject("datauser");

                            String username = userobject.getString("username");
                            String password = userobject.getString("password");
                            String email = userobject.getString("email");
                            String nama = userobject.getString("nama");
                            String rekening = userobject.getString("rekening");
                            int saldo = userobject.getInt("saldo");
                            String toko = userobject.getString("toko");
                            String role = userobject.getString("role");
                            String gambar = userobject.getString("gambar");
                            int status = userobject.getInt("status");
                            int is_verified = userobject.getInt("is_verified");

                            user = new cUser(username,password,email,nama,rekening,saldo+"",toko,role, gambar, status, is_verified);
                            showData();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error get user data " + error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function","getuserdata");
                params.put("username", username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this );
        requestQueue.add(stringRequest);
    }

    protected void saveProfileChanges() {
        binding.btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String username = binding.etRegisterUsername.getText().toString();
                String email = binding.etRegisterEmail.getText().toString();
                String nama = binding.etRegisterNama.getText().toString();
                String rekening = binding.etRegisterRekening.getText().toString();

                String newpass = binding.etRegisterNewPassword.getText().toString();
                String conf = binding.etRegisterConfirm.getText().toString();

                int isReady = 1;
                if (!binding.etRegisterEmail.getText().toString().matches(emailPattern)){
                    isReady = 0;
                    Toast.makeText(getBaseContext(), "Email tidak valid!", Toast.LENGTH_LONG).show();
                }else{
                    if (binding.cbChangePassword.isChecked()){
//                        if (!oldpass.equals(user.getPassword())){
//                            isReady = 0;
//                            Toast.makeText(getBaseContext(), "Password Lama tidak sesuai!",  Toast.LENGTH_LONG).show();
//                        }else{
                        if (!newpass.equals(conf)){
                            isReady = 0;
                            Toast.makeText(getBaseContext(), "Password baru harus sesuai dengan konfirmasi password!",  Toast.LENGTH_LONG).show();
                        }else{
//                                if (newpass.equals(oldpass)){
//                                    isReady = 0;
//                                    Toast.makeText(getBaseContext(), "Password baru tidak boleh sama dengan password lama!",  Toast.LENGTH_LONG).show();
//                                }
                        }
//                        }
                    }
                }

                if (isReady == 1){
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            getResources().getString(R.string.url) + "/admin/updateprofile",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    System.out.println(response);

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);

                                        int code = jsonObject.getInt("code");
                                        String message = jsonObject.getString("message");
                                        if (code == 1){
                                            if (!binding.etRegisterUsername.getText().toString().equals(user.getUsername())){
                                                username = binding.etRegisterUsername.getText().toString();
                                            }
                                            //getUserData();
                                            resetForm();

                                        }

                                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("error update profile : " + error);
                                }
                            }
                    ){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            //params.put("function", binding.etRegisterUsername.getText().toString());
                            params.put("oldusername", user.getUsername());
                            params.put("username", binding.etRegisterUsername.getText().toString());
                            params.put("email", binding.etRegisterEmail.getText().toString());
                            params.put("namalengkap", binding.etRegisterNama.getText().toString());
                            params.put("rekening", binding.etRegisterRekening.getText().toString());

                            if (binding.cbChangePassword.isChecked()){
                                params.put("password", binding.etRegisterNewPassword.getText().toString());
                            }

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

                            if (!binding.etRegisterToko.getText().toString().equals("")){
                                params.put("toko", binding.etRegisterToko.getText().toString());
                            }

                            if (binding.spinnerStatus.getSelectedItem().toString().equalsIgnoreCase("Active")){
                                params.put("status", 1+"");
                            }else if (binding.spinnerStatus.getSelectedItem().toString().equalsIgnoreCase("Ban")){
                                params.put("status", 0+"");
                            }

                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                    requestQueue.add(stringRequest);
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3100) {
            if(resultCode == Activity.RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    selectedImage = BitmapFactory.decodeStream(imageStream);
                    binding.imageViewProfilePicture.setImageBitmap(selectedImage);
//                    ViewGroup.LayoutParams params = binding.imageViewBuktiTopUp.getLayoutParams();
//                    params.height = 200;
//                    binding.imageViewBuktiTopUp.setLayoutParams(params);
                    //binding.textViewNamaBukti.setText(imageUri.);
                    ext = GetFileExtension(imageUri);
                    checkField();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Get Extension
    public String GetFileExtension(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();

        // Return file Extension
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}