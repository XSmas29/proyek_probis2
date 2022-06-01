package com.example.proyek_mobcomp;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.example.proyek_mobcomp.databinding.FragmentCustomerHeaderPurchaseHistoryBinding;
import com.example.proyek_mobcomp.recyclerviewFolder.RecyclerAdapterHeaderPurchase;

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

 /*

    Update Change =
    16 Maret 2022 : dari menampilkan list header transaksi menjadi menampilkan detail transaksi

 */


public class CustomerHeaderPurchaseHistoryFragment extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 200;
    protected FragmentCustomerHeaderPurchaseHistoryBinding binding;
    AppDatabase db;
    int pageHeight = 1120;
    int pagewidth = 792;
    Bitmap bmp, scaledbmp;
    ArrayList<cHeaderPurchase> arrHTrans = new ArrayList<>();
    ArrayList<cDetailPurchase> arrDTrans = new ArrayList<>();
    HashMap<Integer, String> mapBarang = new HashMap<>();
    RecyclerAdapterHeaderPurchase recyclerAdapterHeaderPurchase;

//    RecyclerAdapterSellerTransaksi recyclerAdapterSellerTransaksi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_customer_header_purchase_history, container, false);

        binding = FragmentCustomerHeaderPurchaseHistoryBinding.inflate(inflater, container, false);

        filterBulan();
        filterTahun();
        resetFilter();
        getHeaderPurchase();
        arrHTrans = new ArrayList<>();
        arrDTrans = new ArrayList<>();
        mapBarang = new HashMap<>();
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);
        // below code is used for
        // checking our permissions.
        if (checkPermission()) {
            //Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        binding.btnLaporanTransaksiCustomer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                generatePDF();
            }
        });

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void generatePDF() {
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // berapa banyak page yg akan dicetak berdasarkan isi
        int datacount = arrHTrans.size();
        int pageNumber = (int) Math.ceil(arrHTrans.size() / 15.0);
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
            title.setColor(ContextCompat.getColor(getContext(), R.color.black));

            canvas.drawText("Laporan Transaksi", 209, 85, title);
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
            title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            title.setColor(ContextCompat.getColor(getContext(), R.color.black));
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
                title.setColor(ContextCompat.getColor(getContext(), R.color.black));


                input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                output = new SimpleDateFormat("dd MMMM yyyy");

                canvas.drawText(idxNumber + "", 100, 219 + (50 * (j+1)), title);
                canvas.drawText("#" + arrHTrans.get((i * 15) + j).getId(), 200, 219 + (50 * (j+1)), title);
                canvas.drawText(arrHTrans.get((i * 15) + j).getTanggal()+"", 375, 219 + (50 * (j+1)), title);
                canvas.drawText("Rp " + arrHTrans.get((i * 15) + j).getGrandtotalInString(), 575, 219 + (50 * (j+1)), title);

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
        File file = new File(Environment.getExternalStorageDirectory(), "Laporan Transaksi - " + formatted+".pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(getContext(), "Laporan Transaksi berhasil diunduh.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();
    }
//    public void getDetailPurchase() {
//        binding.progressBar.setVisibility(View.VISIBLE);
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.POST,
//                getResources().getString(R.string.url) + "/customer/getdtrans",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println(response);
//
//                        try {
//                            ArrayList<cDetailPurchase> listTrans = new ArrayList<>();
//                            ArrayList<cProduct> listBarangTrans = new ArrayList<>();
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray arraytrans = jsonObject.getJSONArray("datadtrans");
//
//                            for (int i = 0; i < arraytrans.length(); i++) {
//                                int id = arraytrans.getJSONObject(i).getInt("id");
//                                int fk_htrans = arraytrans.getJSONObject(i).getInt("fk_htrans");
//                                int fk_barang = arraytrans.getJSONObject(i).getInt("fk_barang");
//                                int jumlah = arraytrans.getJSONObject(i).getInt("jumlah");
//                                int subtotal = arraytrans.getJSONObject(i).getInt("subtotal");
//                                int rating = arraytrans.getJSONObject(i).optInt("rating", -1);
//                                String review = arraytrans.getJSONObject(i).getString("review");
//                                String fk_seller = arraytrans.getJSONObject(i).getString("fk_seller");
//                                String status = arraytrans.getJSONObject(i).getString("status");
//                                String notes_seller = arraytrans.getJSONObject(i).getString("notes_seller");
//                                String notes_customer = arraytrans.getJSONObject(i).getString("notes_customer");
//
//                                listTrans.add(new cDetailPurchase(id, fk_htrans, fk_barang, jumlah, subtotal, rating, review, fk_seller, status, notes_seller, notes_customer));
//
//
//                                JSONObject barangtrans = arraytrans.getJSONObject(i).getJSONObject("product");
//
//                                int idbarang = barangtrans.getInt("id");
//                                String fk_sellerbarang = barangtrans.getString("fk_seller");
//                                int fk_kategori = barangtrans.getInt("fk_kategori");
//                                String nama = barangtrans.getString("nama");
//                                String desc = barangtrans.getString("deskripsi");
//                                int harga = barangtrans.getInt("harga");
//                                int stok = barangtrans.getInt("stok");
//                                String gambar = barangtrans.getString("gambar");
//                                int is_deleted = barangtrans.getInt("is_deleted");
//
//                                listBarangTrans.add(new cProduct(idbarang, fk_sellerbarang, fk_kategori, nama, desc, harga, stok, gambar, is_deleted));
//                            }
//                            setRv(listTrans, listBarangTrans);
//
//                            binding.progressBar.setVisibility(View.INVISIBLE);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        System.out.println("error get detail transaction/ detail purchase " + error);
//                    }
//                }
//        ){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("function","getdtrans");
//                params.put("username", CustomerHomeActivity.login+"");
//                params.put("status", binding.spFilterTransaksiCust.getSelectedItem().toString());
//                return params;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
//    }
//
//    protected void setRv(ArrayList<cDetailPurchase> listTrans, ArrayList<cProduct> listBarangTrans) {
//        binding.recyclerViewCustPurchaseHistory.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.recyclerViewCustPurchaseHistory.setHasFixedSize(true);
//        recyclerAdapterSellerTransaksi = new RecyclerAdapterSellerTransaksi(listTrans, listBarangTrans);
//        recyclerAdapterSellerTransaksi.setOnItemClickCallback(new RecyclerAdapterSellerTransaksi.OnItemClickCallback() {
//            @Override
//            public void onItemClicked(cProduct produk, cDetailPurchase detail) {
////                Intent i = new Intent(getActivity(), SellerDetailTransaksiActivity.class);
////                i.putExtra("detail", detail);
////                i.putExtra("produk", produk);
////                getActivity().startActivityForResult(i, 200);
//                Intent i = new Intent(getActivity(), CustomerDetailPurchaseActivity.class);
//                i.putExtra("idHTrans", detail.getFk_htrans());
//                i.putExtra("idDTrans", detail.getId());
//                startActivity(i);
//            }
//        });
//        binding.recyclerViewCustPurchaseHistory.setAdapter(recyclerAdapterSellerTransaksi);
//    }


    public void setRv(ArrayList<cHeaderPurchase> arrHTrans) {
        binding.recyclerViewCustPurchaseHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewCustPurchaseHistory.setHasFixedSize(true);

        recyclerAdapterHeaderPurchase = new RecyclerAdapterHeaderPurchase(
                arrHTrans
        );
        binding.recyclerViewCustPurchaseHistory.setAdapter(recyclerAdapterHeaderPurchase);
    }

    public void filterBulan() {
        binding.spFilterBulan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getHeaderPurchase();
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
                getHeaderPurchase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getHeaderPurchase() {  // get history customer yang lama. ini mendapatkan headernya
        binding.progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                getResources().getString(R.string.url) + "/customer/gethtrans",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray htransArray = jsonObject.getJSONArray("datahtrans");
                            JSONArray dtransArray = jsonObject.getJSONArray("datadtrans");
                            JSONArray barangArray = jsonObject.getJSONArray("databarang");
                            arrHTrans = new ArrayList<>();
                            arrDTrans = new ArrayList<>();
                            mapBarang = new HashMap<>();
                            System.out.println(dtransArray.length());
                            for (int i = 0; i < htransArray.length(); i++){
                                int id = htransArray.getJSONObject(i).getInt("id");
                                String fk_customer = htransArray.getJSONObject(i).getString("fk_customer");
                                int grandtotal = htransArray.getJSONObject(i).getInt("grandtotal");
                                String tanggal   = htransArray.getJSONObject(i).getString("tanggal");
                                arrHTrans.add(new cHeaderPurchase(id, fk_customer, grandtotal, tanggal));
                            }

                            for (int i = 0; i < barangArray.length(); i++){
                                mapBarang.put(barangArray.getJSONObject(i).getInt("id"), barangArray.getJSONObject(i).getString("nama"));
                            }

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

                            setRv(arrHTrans);
                            binding.progressBar.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error get header transaction/ header purchase " + error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function","gethtrans");
                params.put("username", CustomerHomeActivity.login+"");
                if (binding.spFilterBulan.getSelectedItemPosition() != 0 && binding.spFilterTahun.getSelectedItemPosition() != 0){
                    params.put("bulan", String.valueOf(binding.spFilterBulan.getSelectedItemPosition()));
                    params.put("tahun", String.valueOf(binding.spFilterTahun.getSelectedItem()));
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void resetFilter(){
        binding.btnResetAll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.spFilterBulan.setSelection(0);
                binding.spFilterTahun.setSelection(0);
            }
        });
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                }
            }
        }
    }
}