package com.example.proyek_mobcomp;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cDetailPurchase;
import com.example.proyek_mobcomp.classFolder.cKategori;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.example.proyek_mobcomp.classFolder.cTopup;
import com.example.proyek_mobcomp.databinding.ActivityCustomerTopUpSaldoBinding;
import com.example.proyek_mobcomp.databinding.ActivitySellerKategoriPalingLakuBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterCustomerHistoryTopUp;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterSellerKategoriPalingLaku;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SellerKategoriPalingLakuActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    /*

        Update changes :
        - 09 Juni 2022 : menambah download report. https://www.geeksforgeeks.org/how-to-generate-a-pdf-file-in-android-app/

     */

    protected ActivitySellerKategoriPalingLakuBinding binding;

    ArrayList<cKategori> arrKategori = new ArrayList<>();
    ArrayList<cProduct> arrProduct = new ArrayList<>();
    ArrayList<cDetailPurchase> arrDTrans = new ArrayList<>();

    RecyclerAdapterSellerKategoriPalingLaku recyclerAdapterSellerKategoriPalingLaku;


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
        setContentView(R.layout.activity_seller_kategori_paling_laku);
        binding = ActivitySellerKategoriPalingLakuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnDownloadKategoriPalingLakuReport.setBackgroundColor(getResources().getColor(R.color.grey));

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

        binding.imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnDownloadKategoriPalingLakuReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePDF();
            }
        });

        getDataKategori();
    }

    public void getDataKategori(){
        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/seller/getkategoripalinglaku",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int code = jsonObject.getInt("code");
                            String message = jsonObject.getString("message");

                            arrKategori = new ArrayList<>();
                            arrProduct = new ArrayList<>();
                            arrDTrans = new ArrayList<>();

                            JSONArray kategoriarray = jsonObject.getJSONArray("datakategori");
                            for (int i = 0; i < kategoriarray.length(); i++){
                                int id = kategoriarray.getJSONObject(i).getInt("id");
                                String nama = kategoriarray.getJSONObject(i).getString("nama");
                                String tipe = kategoriarray.getJSONObject(i).getString("tipe");

                                arrKategori.add(new cKategori(id, nama, tipe));
                            }

                            JSONArray arrayProduct = jsonObject.getJSONArray("dataproduct");
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

                                arrProduct.add(
                                        new cProduct(id, fk_seller, fk_kategori, nama, deskripsi, harga, stok, gambar, is_deleted)
                                );
                            }

                            JSONArray dtransArray = jsonObject.getJSONArray("datadtrans");
                            arrDTrans = new ArrayList<>();
                            for (int i = 0; i < dtransArray.length(); i++){
                                int id = dtransArray.getJSONObject(i).getInt("id");
                                int fk_htrans = dtransArray.getJSONObject(i).getInt("fk_htrans");
                                int fk_barang = dtransArray.getJSONObject(i).getInt("fk_barang");
                                int jumlah = dtransArray.getJSONObject(i).getInt("jumlah");
                                int subtotal = dtransArray.getJSONObject(i).getInt("subtotal");
                                int rating = 0;
                                String review = "";
                                String fk_seller = dtransArray.getJSONObject(i).getString("fk_seller");
                                String status = dtransArray.getJSONObject(i).getString("status");
                                String notes_seller = dtransArray.getJSONObject(i).getString("notes_seller");
                                if (notes_seller == null){
                                    notes_seller = "";
                                }
                                String notes_customer = dtransArray.getJSONObject(i).getString("notes_customer");
                                if (notes_customer== null){
                                    notes_customer = "";
                                }

                                arrDTrans.add(new cDetailPurchase(id, fk_htrans, fk_barang, jumlah,
                                        subtotal, rating, review, fk_seller, status, notes_seller, notes_customer));
                            }

                            setRv();

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
                params.put("username", SellerActivity.login+"");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this );
        requestQueue.add(stringRequest);
    }

    protected void setRv() {
        binding.recyclerViewKategoriPalingLaku.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewKategoriPalingLaku.setHasFixedSize(true);

        recyclerAdapterSellerKategoriPalingLaku = new RecyclerAdapterSellerKategoriPalingLaku(
                arrKategori, arrProduct, arrDTrans
        );
        binding.recyclerViewKategoriPalingLaku.setAdapter(recyclerAdapterSellerKategoriPalingLaku);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generatePDF() {
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // berapa banyak page yg akan dicetak berdasarkan isi
        int pageNumber = (int) Math.ceil( arrKategori.size() / 15.0 );
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
            canvas.drawText("Laporan Kategori yang Paling Laku", 209, 85, title);
            canvas.drawText("Kepada Seller dengan username " + SellerActivity.login, 209, 110, title);

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
            canvas.drawText("Nama Kategori ", 150, 219, title);
//            canvas.drawText("Tanggal ", 270, 219, title);
            canvas.drawText("Jumlah ", 460, 219, title);
//            canvas.drawText("Status ", 620, 219, title);
            canvas.drawLine(52, 230, 740, 230, paint);

            // content table
            for (int j = idxArrWritten; j < arrKategori.size();j++){
                title.setColor(ContextCompat.getColor(this, R.color.black));

                // menghitung jumlah penjualan per kategori
                int ctrPenjualan = 0;
                for (int k = 0; k < arrDTrans.size(); k++){
                    for (int l = 0; l < arrProduct.size(); l++){
                        if (arrProduct.get(l).getId() == arrDTrans.get(k).getFk_barang()){
                            if (arrProduct.get(l).getFk_kategori() == arrKategori.get(j).getId()){
                                ctrPenjualan++;
                            }
                        }
                    }
                }


                canvas.drawText(idxNumber + "", 78, 219 + (50 * ((j % 15)+1)), title);
                canvas.drawText(arrKategori.get(j).getNama()+"", 150, 219 + (50 * ((j % 15)+1)), title);
                canvas.drawText(ctrPenjualan+"", 460, 219 + (50 * ((j % 15)+1)), title);

                idxArrWritten++;
                idxNumber++;
                
                if (idxArrWritten % 15 == 0){
                    break;
                }
            }

            // nomor halaman / page numbering
            title.setColor(ContextCompat.getColor(this, R.color.black));
            title.setTextSize(12);
            canvas.drawText(  i+1 + "", 705, 1050, title);



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
        File file = new File(Environment.getExternalStorageDirectory(), "Laporan Kategori Paling Laku - " + formatted+".pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(this, "Laporan Kategori Paling Laku berhasil diunduh.", Toast.LENGTH_SHORT).show();
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
}