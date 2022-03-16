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
import com.example.proyek_mobcomp.databinding.ActivitySellerProfileBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SellerProfileActivity extends AppCompatActivity {

    protected ActivitySellerProfileBinding binding;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    cUser user;
    Bitmap selectedImage = null;
    String ext = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_profile);
        binding = ActivitySellerProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //ganti warna btn
        binding.btnChangePhotoSeller.setBackgroundColor(getResources().getColor(R.color.grey));
        binding.btnResetFormSeller.setBackgroundColor(getResources().getColor(R.color.grey));
        binding.btnBackSeller.setBackgroundColor(getResources().getColor(R.color.grey));

        binding.etRegisterUsernameSeller.addTextChangedListener(new TextWatcher() {
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

        binding.etRegisterEmailSeller.addTextChangedListener(new TextWatcher() {
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


        binding.etRegisterNamaSeller.addTextChangedListener(new TextWatcher() {
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

        binding.etRegisterRekeningSeller.addTextChangedListener(new TextWatcher() {
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

        binding.etNamaTokoSeller.addTextChangedListener(new TextWatcher() {
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
        binding.etRegisterOldPasswordSeller.addTextChangedListener(new TextWatcher() {
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

        binding.etRegisterNewPasswordSeller.addTextChangedListener(new TextWatcher() {
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

        binding.etRegisterConfirmSeller.addTextChangedListener(new TextWatcher() {
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

        binding.btnBackSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.imageButtonBackSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnChangePhotoSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("masuk");
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 3101);
            }
        });

        binding.cbChangePasswordSeller.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ViewGroup.LayoutParams params = binding.llPasswordSeller.getLayoutParams();
                if (isChecked){
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    binding.llPasswordSeller.setLayoutParams(params);
                    binding.btnSaveChangesSeller.setEnabled(false);
                    checkField();
                }else{
                    params.height = 0;
                    binding.llPasswordSeller.setLayoutParams(params);
                    binding.etRegisterOldPasswordSeller.setText("");
                    binding.etRegisterNewPasswordSeller.setText("");
                    binding.etRegisterConfirmSeller.setText("");
                    checkField();
                }
            }
        });

        binding.btnResetFormSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
            }
        });


        getUserData();
        resetForm();
        getPhoto();
        saveProfileChanges();
    }


    protected void checkField(){
        if (binding.etRegisterUsernameSeller.getText().toString().isEmpty() || binding.etRegisterEmailSeller.getText().toString().isEmpty()
                || binding.etRegisterNamaSeller.getText().toString().isEmpty() || binding.etRegisterRekeningSeller.getText().toString().isEmpty()
                || binding.etNamaTokoSeller.getText().toString().isEmpty()){
            binding.btnSaveChangesSeller.setEnabled(false);
        }else{
            String username = binding.etRegisterUsernameSeller.getText().toString();
            String email = binding.etRegisterEmailSeller.getText().toString();
            String nama = binding.etRegisterNamaSeller.getText().toString();
            String rekening = binding.etRegisterRekeningSeller.getText().toString();
            String toko = binding.etNamaTokoSeller.getText().toString();

            if (username.equals(user.getUsername()) && email.equals(user.getEmail()) && nama.equals(user.getNama()) &&
                    rekening.equals(user.getRekening()) && toko.equals(user.getToko()) && selectedImage == null){
                binding.btnSaveChangesSeller.setEnabled(false);
                if (binding.cbChangePasswordSeller.isChecked() == true){
                    if (binding.etRegisterOldPasswordSeller.getText().toString().isEmpty() || binding.etRegisterNewPasswordSeller.getText().toString().isEmpty() ||
                            binding.etRegisterConfirmSeller.getText().toString().isEmpty()){
                        binding.btnSaveChangesSeller.setEnabled(false);
                    }else{
                        binding.btnSaveChangesSeller.setEnabled(true);
                    }
                }
            }
            else{
                binding.btnSaveChangesSeller.setEnabled(true);
                if (binding.cbChangePasswordSeller.isChecked() == true){
                    if (binding.etRegisterOldPasswordSeller.getText().toString().isEmpty() || binding.etRegisterNewPasswordSeller.getText().toString().isEmpty() ||
                            binding.etRegisterConfirmSeller.getText().toString().isEmpty()){
                        binding.btnSaveChangesSeller.setEnabled(false);
                    }else{
                        binding.btnSaveChangesSeller.setEnabled(true);
                    }
                }
//                else{
//                    binding.btnSaveChanges.setEnabled(true);
//                }

            }

        }
    }

    protected void saveProfileChanges() {
        binding.btnSaveChangesSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.etRegisterUsernameSeller.getText().toString();
                String email = binding.etRegisterEmailSeller.getText().toString();
                String nama = binding.etRegisterNamaSeller.getText().toString();
                String rekening = binding.etRegisterRekeningSeller.getText().toString();
                String toko = binding.etNamaTokoSeller.getText().toString();
                String oldpass = binding.etRegisterOldPasswordSeller.getText().toString();
                String newpass = binding.etRegisterNewPasswordSeller.getText().toString();
                String conf = binding.etRegisterConfirmSeller.getText().toString();

                int isReady = 1;
                if (!binding.etRegisterEmailSeller.getText().toString().matches(emailPattern)){
                    isReady = 0;
                    Toast.makeText(getBaseContext(), "Email tidak valid!", Toast.LENGTH_LONG).show();
                }else{
                    if (binding.cbChangePasswordSeller.isChecked()){
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
                            getResources().getString(R.string.url) + "/seller/updateprofile",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    System.out.println(response);

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);

                                        int code = jsonObject.getInt("code");
                                        String message = jsonObject.getString("message");
                                        if (code == 1){
                                            SellerActivity.login = username;
                                            SharedPreferences sharedpreferences = getSharedPreferences("data", getBaseContext().MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedpreferences.edit();

                                            editor.putString("login", jsonObject.getString("datauser"));
                                            editor.commit();
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
                            params.put("username", binding.etRegisterUsernameSeller.getText().toString());
                            params.put("email", binding.etRegisterEmailSeller.getText().toString());
                            params.put("namalengkap", binding.etRegisterNamaSeller.getText().toString());
                            params.put("rekening", binding.etRegisterRekeningSeller.getText().toString());
                            params.put("toko", binding.etNamaTokoSeller.getText().toString());

                            if (binding.cbChangePasswordSeller.isChecked()){
                                params.put("oldpassword", binding.etRegisterOldPasswordSeller.getText().toString());
                                params.put("password", binding.etRegisterNewPasswordSeller.getText().toString());
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

                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                    requestQueue.add(stringRequest);
                }


            }
        });
    }

    public void getPhoto() {

    }

    public void resetForm(){

        selectedImage = null;
        ext = "";
        getUserData();
        binding.cbChangePasswordSeller.setChecked(false);
        binding.etRegisterOldPasswordSeller.setText("");
        binding.etRegisterNewPasswordSeller.setText("");
        binding.etRegisterConfirmSeller.setText("");


    }

    public void showData() {
        Picasso.get().load(getResources().getString(R.string.url) + "/profile/" +
                user.getGambar()).into(binding.imageViewProfilePictureSeller);
        binding.etRegisterUsernameSeller.setText(user.getUsername());
        binding.etRegisterEmailSeller.setText(user.getEmail());
        binding.etRegisterNamaSeller.setText(user.getNama());
        binding.etRegisterRekeningSeller.setText(user.getRekening());
        binding.etNamaTokoSeller.setText(user.getToko());
    }

    public void getUserData() {
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

                            user = new cUser(username,password,email,nama,rekening,saldo+"",toko,role, gambar);
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
                params.put("username", SellerActivity.login+"");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this );
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3101) {
            if(resultCode == Activity.RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    selectedImage = BitmapFactory.decodeStream(imageStream);
                    binding.imageViewProfilePictureSeller.setImageBitmap(selectedImage);
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