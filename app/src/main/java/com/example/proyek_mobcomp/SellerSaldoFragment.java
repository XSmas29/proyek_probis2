package com.example.proyek_mobcomp;

import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyek_mobcomp.classFolder.cKategori;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.example.proyek_mobcomp.classFolder.cTopup;
import com.example.proyek_mobcomp.databinding.FragmentCustomerHeaderPurchaseHistoryBinding;
import com.example.proyek_mobcomp.databinding.FragmentSellerSaldoBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterSellerListProduct;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterSellerTopup;

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


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SellerSaldoFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 200;

    /*

        Update changes :
        - 23 Mei 2022 : menambah download report. https://www.geeksforgeeks.org/how-to-generate-a-pdf-file-in-android-app/

     */

    FragmentSellerSaldoBinding binding;
    ArrayList<cTopup> listTopup = new ArrayList<>();
    RecyclerAdapterSellerTopup recyclerAdapterSellerTopup;


    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;

    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;


    public static SellerSaldoFragment newInstance() {
        SellerSaldoFragment fragment = new SellerSaldoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSellerSaldoBinding.inflate(inflater, container, false);

        binding.btnDownloadWithdrawReport.setBackgroundColor(getResources().getColor(R.color.grey));

        // initializing our variables.
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        // below code is used for
        // checking our permissions.
        if (checkPermission()) {
            Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        getData();
        binding.btnCairkanSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cairkanSaldo();
            }
        });

        binding.btnDownloadWithdrawReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePDF();
            }
        });

        return binding.getRoot();
    }

    public void cairkanSaldo(){
        if (binding.edCairkanSaldo.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Jumlah Pencairan Harus diisi!", Toast.LENGTH_SHORT).show();
        }
        else if (Integer.parseInt(binding.edCairkanSaldo.getText().toString()) < 10000){
            Toast.makeText(getActivity(), "Jumlah Pencairan Minimal Rp. 10000!", Toast.LENGTH_SHORT).show();
        }
        else{
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    getResources().getString(R.string.url) + "/seller/cairkansaldo",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                int code = jsonObject.getInt("code");
                                if (code == 1){
                                    Toast.makeText(getActivity(), "Berhasil Melakukan Request Pencairan Dana!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getActivity(), "Saldo Tidak Mencukupi!", Toast.LENGTH_SHORT).show();
                                }
                                getData();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("error cairkan saldo : " + error);
                        }
                    }
            ){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("login", SellerActivity.login);
                    params.put("jumlah", binding.edCairkanSaldo.getText().toString());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/seller/getsaldo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray arraytopup = jsonObject.getJSONArray("datatopup");
                            JSONObject datasaldo = jsonObject.getJSONObject("saldo");
                            int saldo = datasaldo.getInt("saldo");

                            String money = moneySeparator(saldo);
                            binding.txtSaldoSeller.setText("Saldo Anda : Rp. " + money);

                            for (int i = 0; i < arraytopup.length(); i++) {
                                int id = arraytopup.getJSONObject(i).getInt("id");
                                String username = arraytopup.getJSONObject(i).getString("fk_username");
                                int jumlah = arraytopup.getJSONObject(i).getInt("jumlah_topup");
                                String bukti = arraytopup.getJSONObject(i).getString("bukti_topup");
                                int status = arraytopup.getJSONObject(i).getInt("status_topup");
                                String created = arraytopup.getJSONObject(i).getString("created_at");
                                String updated = arraytopup.getJSONObject(i).getString("updated_at");
                                listTopup.add(new cTopup(id, username, jumlah, bukti, status, created, updated));
                            }
                            SetupRv();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error load data saldo : " + error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("login", SellerActivity.login);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void SetupRv() {
        binding.rvHistorySaldo.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvHistorySaldo.setHasFixedSize(true);

        recyclerAdapterSellerTopup = new RecyclerAdapterSellerTopup(listTopup);
        binding.rvHistorySaldo.setAdapter(recyclerAdapterSellerTopup);
    }

    protected String moneySeparator(int harga) {
        int ctr = 0; // untuk menghitung berapa digit yg sudah masuk ke variable hasil
        String hasil = "";

        String awal = String.valueOf(harga);
        String temp = ""; // penampung sementara untuk dalam for
        System.out.println(awal.length());
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generatePDF() {
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // berapa banyak page yg akan dicetak berdasarkan isi
        int pageNumber = (int) Math.ceil( listTopup.size() / 15.0);
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
            title.setColor(ContextCompat.getColor(getContext(), R.color.black));

            // below line is used to draw text in our PDF file.
            // the first parameter is our text, second parameter
            // is position from start, third parameter is position from top
            // and then we are passing our variable of paint which is title.
            canvas.drawText("Laporan Withdraw", 209, 85, title);
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
            title.setColor(ContextCompat.getColor(getContext(), R.color.black));
            title.setTextSize(14);

            // below line is used for setting
            // our text to center of PDF.
//            title.setTextAlign(Paint.Align.CENTER);
//            canvas.drawText("This is sample document which we have created.", 396, 560, title);

            // header table
            canvas.drawLine(52, 200, 740, 200, paint);
            canvas.drawText("No. ", 78, 219, title);
            canvas.drawText("Withdraw ID ", 150, 219, title);
            canvas.drawText("Tanggal ", 270, 219, title);
            canvas.drawText("Jumlah ", 460, 219, title);
            canvas.drawText("Status ", 620, 219, title);
            canvas.drawLine(52, 230, 740, 230, paint);

            // content table
            for (int j = idxArrWritten; j < listTopup.size();j++){

                title.setColor(ContextCompat.getColor(getContext(), R.color.black));

                input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                output = new SimpleDateFormat("dd MMMM yyyy");

                d = null;
                try {
                    d = input.parse(listTopup.get(j).getCreated());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                formatted = output.format(d);


                canvas.drawText(idxNumber + "", 78, 219 + (50 * ((j % 15) + 1)), title);
                canvas.drawText(listTopup.get(j).getId() + "", 150, 219 + (50 * ((j % 15) + 1)), title);
                canvas.drawText(formatted + "", 270, 219 + (50 * ((j % 15) + 1)), title);
                canvas.drawText("Rp " + listTopup.get(j).getJumlahInString(), 460, 219 + (50 * ((j % 15) + 1)), title);

                if (listTopup.get(j).getStatus() == 0) {
                    title.setColor(ContextCompat.getColor(getContext(), R.color.yellow));
                    canvas.drawText("Pending ", 620, 219 + (50 * ((j % 15) + 1)), title);
                } else if (listTopup.get(j).getStatus() == 1) {
                    title.setColor(ContextCompat.getColor(getContext(), R.color.green));
                    canvas.drawText("Success ", 620, 219 + (50 * ((j % 15) + 1)), title);
                } else if (listTopup.get(j).getStatus() == -1) {
                    title.setColor(ContextCompat.getColor(getContext(), R.color.red));
                    canvas.drawText("Rejected ", 620, 219 + (50 * ((j % 15) + 1)), title);
                }

                idxArrWritten++;
                idxNumber++;

                if (idxArrWritten % 15 == 0){
                    break;
                }

            }

            // nomor halaman / page numbering
            title.setColor(ContextCompat.getColor(getContext(), R.color.black));
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
        File file = new File(Environment.getExternalStorageDirectory(), "Laporan Withdraw - " + formatted+".pdf");

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(getContext(), "Laporan Withdraw berhasil diunduh.", Toast.LENGTH_SHORT).show();
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
        int permission1 = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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
                    Toast.makeText(getContext(), "Permission Granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Permission Denied.", Toast.LENGTH_SHORT).show();
                    //finish();
                }
            }
        }
    }
}