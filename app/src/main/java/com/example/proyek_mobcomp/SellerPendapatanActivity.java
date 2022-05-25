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

import android.content.ContentResolver;
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
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cDetailPurchase;
import com.example.proyek_mobcomp.classFolder.cHeaderPurchase;
import com.example.proyek_mobcomp.databinding.ActivitySellerPendapatanBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterSellerPendapatan;

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

public class SellerPendapatanActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    /*

        Created at : 28 April 2022 untuk menampilkan data pendapatan seller

     */
    int pageHeight = 1120;
    int pagewidth = 792;
    Bitmap bmp, scaledbmp;
    ActivitySellerPendapatanBinding binding;

    RecyclerAdapterSellerPendapatan recyclerAdapterSellerPendapatan;


    ArrayList<cHeaderPurchase> listHTrans = new ArrayList<>();
    ArrayList<cDetailPurchase> listDTrans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_pendapatan);

        binding = ActivitySellerPendapatanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        filterStatus();
        filterBulan();
        filterTahun();
        resetAllFilter();

        getDataPendapatan();
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

        binding.btnLaporanTransaksiSeller.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                generatePDF();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generatePDF() {
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // berapa banyak page yg akan dicetak berdasarkan isi
        int datacount = listHTrans.size();
        int pageNumber = (int) Math.ceil(listHTrans.size() / 15.0);
        int idxArrWritten = 0;
        int idxNumber = 1;

        for (int i = 0; i < pageNumber; i++){
            Paint paint = new Paint();
            Paint title = new Paint();

            PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 2).create();
            PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
            Canvas canvas = myPage.getCanvas();

            // HEADER PDF
            canvas.drawBitmap(scaledbmp, 56, 40, paint);
            title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            title.setTextSize(18);
            title.setColor(ContextCompat.getColor(this, R.color.black));

            canvas.drawText("Laporan Penjualan", 209, 85, title);
            canvas.drawText("Kepada Seller dengan nama toko " + SellerActivity.login, 209, 110, title);

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
            title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            title.setColor(ContextCompat.getColor(this, R.color.black));
            title.setTextSize(14);

            // header table
            canvas.drawLine(50, 200, 742, 200, paint);
            canvas.drawText("No. ", 100, 219, title);
            canvas.drawText("ID Transaksi ", 200, 219, title);
            canvas.drawText("Tanggal ", 375, 219, title);
            canvas.drawText("Grandotal", 575, 219, title);
            canvas.drawLine(50, 230, 742, 230, paint);


            int pagecount = datacount;
            if (pagecount > 15) pagecount = 15;
            for (int j = 0; j < pagecount; j++){
                title.setColor(ContextCompat.getColor(this, R.color.black));


                input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                output = new SimpleDateFormat("dd MMMM yyyy");

                canvas.drawText(idxNumber + "", 100, 219 + (50 * (j+1)), title);
                canvas.drawText("#" + listHTrans.get(j).getId(), 200, 219 + (50 * (j+1)), title);
                canvas.drawText(listHTrans.get(j).getTanggal()+"", 375, 219 + (50 * (j+1)), title);
                canvas.drawText("Rp " + listHTrans.get(j).getGrandtotalInString(), 575, 219 + (50 * (j+1)), title);

                idxArrWritten++;
                idxNumber++;
            }
            Log.d("V", pagecount + "");
            datacount -= 15;
            // after adding all attributes to our
            // PDF file we will be finishing our page.
            pdfDocument.finishPage(myPage);
        }
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd MMMM yyyy");

        Calendar calendar = Calendar.getInstance();

        String formatted = output.format(calendar.getTime());
        File file = new File(Environment.getExternalStorageDirectory(), "Laporan Penjualan - " + formatted+".pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "Laporan Penjualan berhasil diunduh.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    public void resetAllFilter() {
        binding.btnResetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.spFilterSellerPendapatan.setSelection(0);
                binding.spFilterBulan.setSelection(0);
                binding.spFilterTahun.setSelection(0);
            }
        });
    }

    public void getDataPendapatan() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/seller/getdatapendapatan",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            listDTrans = new ArrayList<>();
                            listHTrans = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray arraytrans = jsonObject.getJSONArray("datatrans");

                            for (int i = 0; i < arraytrans.length(); i++) {
                                int id = arraytrans.getJSONObject(i).getInt("id");
                                int fk_htrans = arraytrans.getJSONObject(i).getInt("fk_htrans");
                                int fk_barang = arraytrans.getJSONObject(i).getInt("fk_barang");
                                int jumlah = arraytrans.getJSONObject(i).getInt("jumlah");
                                int subtotal = arraytrans.getJSONObject(i).getInt("subtotal");
                                int rating = arraytrans.getJSONObject(i).optInt("rating", -1);
                                String review = arraytrans.getJSONObject(i).getString("review");
                                String fk_seller = arraytrans.getJSONObject(i).getString("fk_seller");
                                String status = arraytrans.getJSONObject(i).getString("status");
                                String notes_seller = arraytrans.getJSONObject(i).getString("notes_seller");
                                String notes_customer = arraytrans.getJSONObject(i).getString("notes_customer");

                                listDTrans.add(new cDetailPurchase(id, fk_htrans, fk_barang, jumlah, subtotal, rating, review, fk_seller, status, notes_seller, notes_customer));

                            }

                            JSONArray arrayhtrans = jsonObject.getJSONArray("datahtrans");

                            for (int i = 0; i < arrayhtrans.length(); i++) {
                                /*
                                    ditambahkan pada tanggal : 18 April

                                 */
                                int id = arrayhtrans.getJSONObject(i).getInt("id");
                                String fk_customer = arrayhtrans.getJSONObject(i).getString("fk_customer");
                                int grandtotal = arrayhtrans.getJSONObject(i).getInt("grandtotal");
                                String tanggal = arrayhtrans.getJSONObject(i).getString("tanggal");

                                listHTrans.add(new cHeaderPurchase(id, fk_customer, grandtotal, tanggal));
                            }

//                            System.out.println("index " + listBarangTrans.size() + "  " + listTrans.size() + "  " + listHTrans.size() + "  ");
                            setupRvTrans();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error get Pendapatan Seller : " + error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", SellerActivity.login+"");
                params.put("status", binding.spFilterSellerPendapatan.getSelectedItem().toString());

                if (binding.spFilterBulan.getSelectedItemPosition() != 0 && binding.spFilterTahun.getSelectedItemPosition() != 0){
                    params.put("bulan", String.valueOf(binding.spFilterBulan.getSelectedItemPosition()));
                    params.put("tahun", String.valueOf(binding.spFilterTahun.getSelectedItem()));
                    System.out.println( "test " + String.valueOf(binding.spFilterBulan.getSelectedItemPosition()) + "  " + binding.spFilterTahun.getSelectedItem());
                }

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        requestQueue.add(stringRequest);
    }

    protected void setupRvTrans() {
        binding.rvSellerPendapatan.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        binding.rvSellerPendapatan.setHasFixedSize(true);
        recyclerAdapterSellerPendapatan = new RecyclerAdapterSellerPendapatan(listHTrans, listDTrans);
        binding.rvSellerPendapatan.setAdapter(recyclerAdapterSellerPendapatan);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    public void filterBulan() {
        binding.spFilterBulan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDataPendapatan();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void filterTahun() {
        binding.spFilterTahun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDataPendapatan();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void filterStatus() {
        binding.spFilterSellerPendapatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDataPendapatan();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}