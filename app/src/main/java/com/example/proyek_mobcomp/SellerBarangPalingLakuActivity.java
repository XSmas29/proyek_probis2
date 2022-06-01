package com.example.proyek_mobcomp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import android.util.Log;
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
import com.example.proyek_mobcomp.databinding.ActivitySellerBarangPalingLakuBinding;
import com.example.proyek_mobcomp.databinding.ActivitySellerKategoriPalingLakuBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterSellerBarangPalingLaku;
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
import java.util.Iterator;
import java.util.Map;

public class SellerBarangPalingLakuActivity extends AppCompatActivity {

    protected ActivitySellerBarangPalingLakuBinding binding;
    ArrayList<cProduct> arrProduct = new ArrayList<>();
    ArrayList<cDetailPurchase> arrDTrans = new ArrayList<>();
    RecyclerAdapterSellerBarangPalingLaku recyclerAdapterSellerBarangPalingLaku;

    ArrayList<String> listBarang = new ArrayList<>();
    ArrayList<Integer> listJumlah = new ArrayList<>();

    int pageHeight = 1120;
    int pagewidth = 792;
    Bitmap bmp, scaledbmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_barang_paling_laku);
        binding = ActivitySellerBarangPalingLakuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);
        binding.imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnLaporanBarangPalingLaku.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                generatePDF();
            }
        });
        getBarangPalingLaku();
    }

    private void getBarangPalingLaku() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/seller/getbarangpalinglaku",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray arrbarang = jsonObject.getJSONArray("databarang");
                            JSONArray arrjumlah = jsonObject.getJSONArray("datajumlah");

                            listBarang = new ArrayList<>();
                            listJumlah = new ArrayList<>();
                            if (arrbarang != null) {
                                int len = arrbarang.length();
                                for (int i=0;i<len;i++){
                                    listBarang.add(arrbarang.get(i).toString());
                                }
                            }
                            if (arrjumlah != null) {
                                int len = arrjumlah.length();
                                for (int i=0;i<len;i++){
                                    listJumlah.add(Integer.parseInt((arrjumlah.get(i).toString())));
                                }
                            }
                            System.out.println(listBarang);
                            System.out.println(listJumlah);

                            setRv(listBarang, listJumlah);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error get barang paling laku " + error);
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

    protected void setRv(ArrayList<String> listBarang, ArrayList<Integer> listJumlah) {
        binding.recyclerViewBarangPalingLaku.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewBarangPalingLaku.setHasFixedSize(true);
        recyclerAdapterSellerBarangPalingLaku = new RecyclerAdapterSellerBarangPalingLaku(
                listBarang, listJumlah
        );
        binding.recyclerViewBarangPalingLaku.setAdapter(recyclerAdapterSellerBarangPalingLaku);
        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generatePDF() {
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // berapa banyak page yg akan dicetak berdasarkan isi
        int datacount = listBarang.size();
        int pageNumber = (int) Math.ceil(listBarang.size() / 15.0);
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

            canvas.drawText("Laporan Barang Paling Laku", 209, 85, title);
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
            canvas.drawText("Nama Barang", 200, 219, title);
            canvas.drawText("Jumlah Transaksi ", 545, 219, title);
            canvas.drawLine(50, 230, 742, 230, paint);


            int pagecount = datacount;
            if (pagecount > 15) pagecount = 15;
            for (int j = 0; j < pagecount; j++){
                title.setColor(ContextCompat.getColor(this, R.color.black));

                input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                output = new SimpleDateFormat("dd MMMM yyyy");

                canvas.drawText(idxNumber + "", 100, 219 + (50 * (j+1)), title);
                canvas.drawText(listBarang.get((i * 15) + j), 200, 219 + (50 * (j+1)), title);
                canvas.drawText(listJumlah.get((i * 15) + j)+"", 600, 219 + (50 * (j+1)), title);

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
        File file = new File(Environment.getExternalStorageDirectory(), "Laporan Barang Paling Laku - " + formatted+".pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "Laporan Barang Paling Laku berhasil diunduh.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
    }
}