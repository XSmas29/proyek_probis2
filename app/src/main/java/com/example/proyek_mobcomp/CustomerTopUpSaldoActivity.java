package com.example.proyek_mobcomp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CustomerTopUpSaldoActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    /*

        Update changes :
        - 22 Mei 2022 : menambah download report. https://www.geeksforgeeks.org/how-to-generate-a-pdf-file-in-android-app/

     */

    protected ActivityCustomerTopUpSaldoBinding binding;

    ArrayList<cTopup> arrTopUp = new ArrayList<cTopup>();

    RecyclerAdapterCustomerHistoryTopUp recyclerAdapterCustomerHistoryTopUp;

    Bitmap selectedImage = null;
    String ext = "";


    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;

    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_top_up_saldo);

        binding = ActivityCustomerTopUpSaldoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSelectBukti.setBackgroundColor(getResources().getColor(R.color.grey));
        binding.btnClear.setBackgroundColor(getResources().getColor(R.color.grey));
        binding.btnDownloadTopUpReport.setBackgroundColor(getResources().getColor(R.color.grey));

        // initializing our variables.
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        // below code is used for
        // checking our permissions.
        if (checkPermission()) {
            //Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

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

        binding.btnDownloadTopUpReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePDF();
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generatePDF() {
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // berapa banyak page yg akan dicetak berdasarkan isi
        int pageNumber = (int) Math.ceil(15 / arrTopUp.size() );
        int idxArrWritten = 0;
        int idxNumber = 1;

        for (int i = 0; i < pageNumber; i++){
            // two variables for paint "paint" is used
            // for drawing shapes and we will use "title"
            // for adding text in our PDF file.
            Paint paint = new Paint();
            Paint title = new Paint();

            // we are adding page info to our PDF file
            // in which we will be passing our pageWidth,
            // pageHeight and number of pages and after that
            // we are calling it to create our PDF.
            PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

            // below line is used for setting
            // start page for our PDF file.
            PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

            // creating a variable for canvas
            // from our page of PDF.
            Canvas canvas = myPage.getCanvas();


            // HEADER PDF

            // below line is used to draw our image on our PDF file.
            // the first parameter of our drawbitmap method is
            // our bitmap
            // second parameter is position from left
            // third parameter is position from top and last
            // one is our variable for paint.
            canvas.drawBitmap(scaledbmp, 56, 40, paint);

            // below line is used for adding typeface for
            // our text which we will be adding in our PDF file.
            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

            // below line is used for setting text size
            // which we will be displaying in our PDF file.
            title.setTextSize(18);

            // below line is sued for setting color
            // of our text inside our PDF file.
            title.setColor(ContextCompat.getColor(this, R.color.black));

            // below line is used to draw text in our PDF file.
            // the first parameter is our text, second parameter
            // is position from start, third parameter is position from top
            // and then we are passing our variable of paint which is title.
            canvas.drawText("Laporan Top Up", 209, 85, title);
            canvas.drawText("Kepada Customer dengan username " + CustomerHomeActivity.login, 209, 110, title);

            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat output = new SimpleDateFormat("dd MMMM yyyy");

            Calendar calendar = Calendar.getInstance();

            Date d = null;
            try
            {
                d = input.parse(calendar.getTime().toString());
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            String formatted = output.format(calendar.getTime());
            canvas.drawText("Per " + formatted+"", 209, 135, title);




            // CONTENT PDF

            // similarly we are creating another text and in this
            // we are aligning this text to center of our PDF file.
            title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            title.setColor(ContextCompat.getColor(this, R.color.black));
            title.setTextSize(14);

            // below line is used for setting
            // our text to center of PDF.
//            title.setTextAlign(Paint.Align.CENTER);
//            canvas.drawText("This is sample document which we have created.", 396, 560, title);

            // header table
            canvas.drawLine(52, 200, 740, 200, paint);
            canvas.drawText("No. ", 78, 219, title);
            canvas.drawText("TopUp ID ", 150, 219, title);
            canvas.drawText("Tanggal ", 270, 219, title);
            canvas.drawText("Jumlah ", 460, 219, title);
            canvas.drawText("Status ", 620, 219, title);
            canvas.drawLine(52, 230, 740, 230, paint);

            // content table
            for (int j = idxArrWritten; j < arrTopUp.size();j++){
                title.setColor(ContextCompat.getColor(this, R.color.black));

                input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                output = new SimpleDateFormat("dd MMMM yyyy");

                d = null;
                try
                {
                    d = input.parse(arrTopUp.get(j).getCreated());
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
                formatted = output.format(d);


                canvas.drawText(idxNumber + "", 78, 219 + (50 * (j+1)), title);
                canvas.drawText(arrTopUp.get(j).getId()+"", 150, 219 + (50 * (j+1)), title);
                canvas.drawText(formatted+"", 270, 219 + (50 * (j+1)), title);
                canvas.drawText("Rp " + arrTopUp.get(j).getJumlahInString(), 460, 219 + (50 * (j+1)), title);

                if (arrTopUp.get(j).getStatus() == 0){
                    title.setColor(ContextCompat.getColor(this, R.color.yellow));
                    canvas.drawText("Pending ", 620, 219 + (50 * (j+1)), title);
                }
                else if (arrTopUp.get(j).getStatus() == 1){
                    title.setColor(ContextCompat.getColor(this, R.color.green));
                    canvas.drawText("Success ", 620, 219 + (50 * (j+1)), title);
                }else if (arrTopUp.get(j).getStatus() == -1){
                    title.setColor(ContextCompat.getColor(this, R.color.red));
                    canvas.drawText("Rejected ", 620, 219 + (50 * (j+1)), title);
                }

                idxArrWritten++;
                idxNumber++;
            }

            // nomor halaman / page numbering
            title.setColor(ContextCompat.getColor(this, R.color.black));
            title.setTextSize(12);
            canvas.drawText(  pageNumber + "", 705, 1050, title);



            // after adding all attributes to our
            // PDF file we will be finishing our page.
            pdfDocument.finishPage(myPage);
        }


        // below line is used to set the name of
        // our PDF file and its path.
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd MMMM yyyy");

        Calendar calendar = Calendar.getInstance();

        String formatted = output.format(calendar.getTime());
        File file = new File(Environment.getExternalStorageDirectory(), "Laporan Top Up - " + formatted+".pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(this, "Laporan Top Up berhasil diunduh.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
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