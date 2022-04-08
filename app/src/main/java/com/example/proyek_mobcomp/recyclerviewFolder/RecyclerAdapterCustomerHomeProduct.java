package com.example.proyek_mobcomp.recyclerviewFolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyek_mobcomp.CustomerDetailProduct;
import com.example.proyek_mobcomp.CustomerHomeActivity;
import com.example.proyek_mobcomp.R;
import com.example.proyek_mobcomp.classFolder.cKategori;
import com.example.proyek_mobcomp.classFolder.cProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapterCustomerHomeProduct extends RecyclerView.Adapter<RecyclerAdapterCustomerHomeProduct.ViewHolder> {

    ArrayList<cKategori> arrKategori = new ArrayList<>();
    ArrayList<cProduct> arrProduct = new ArrayList<>();

    public RecyclerAdapterCustomerHomeProduct(ArrayList<cKategori> arrKategori, ArrayList<cProduct> arrProduct) {
        this.arrKategori = arrKategori;
        this.arrProduct = arrProduct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_layout_barang_by_kategori, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cKategori kategori = arrKategori.get(position);
        holder.bind(kategori, position);
    }

    @Override
    public int getItemCount() {
        return arrKategori.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llKategoriContainer, llProductContainer;
        TextView txtNamaKategori;
        LinearLayout[] arrLlProduct = new LinearLayout[5];
        ImageView[] arrImageView = new ImageView[5];
        TextView[] arrTxtNamaProduct = new TextView[5];
        TextView[] arrTxtHargaProduct = new TextView[5];
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            llKategoriContainer = itemView.findViewById(R.id.llKategoriContainer);
            llProductContainer = itemView.findViewById(R.id.llProductContainer);
            txtNamaKategori = itemView.findViewById(R.id.textView_namaKategori);

            arrLlProduct[0] = itemView.findViewById(R.id.ll0);
            arrLlProduct[1] = itemView.findViewById(R.id.ll1);
            arrLlProduct[2] = itemView.findViewById(R.id.ll2);
            arrLlProduct[3] = itemView.findViewById(R.id.ll3);
            arrLlProduct[4] = itemView.findViewById(R.id.ll4);

            arrImageView[0] = itemView.findViewById(R.id.imageView_product0);
            arrImageView[1] = itemView.findViewById(R.id.imageView_product1);
            arrImageView[2] = itemView.findViewById(R.id.imageView_product2);
            arrImageView[3] = itemView.findViewById(R.id.imageView_product3);
            arrImageView[4] = itemView.findViewById(R.id.imageView_product4);

            arrTxtNamaProduct[0] = itemView.findViewById(R.id.textView_namaProduct0);
            arrTxtNamaProduct[1] = itemView.findViewById(R.id.textView_namaProduct1);
            arrTxtNamaProduct[2] = itemView.findViewById(R.id.textView_namaProduct2);
            arrTxtNamaProduct[3] = itemView.findViewById(R.id.textView_namaProduct3);
            arrTxtNamaProduct[4] = itemView.findViewById(R.id.textView_namaProduct4);

            arrTxtHargaProduct[0] = itemView.findViewById(R.id.textView_hargaProduct0);
            arrTxtHargaProduct[1] = itemView.findViewById(R.id.textView_hargaProduct1);
            arrTxtHargaProduct[2] = itemView.findViewById(R.id.textView_hargaProduct2);
            arrTxtHargaProduct[3] = itemView.findViewById(R.id.textView_hargaProduct3);
            arrTxtHargaProduct[4] = itemView.findViewById(R.id.textView_hargaProduct4);
        }

        public void bind(cKategori kategori, int position) {
            int ctrBarang = 0;

            txtNamaKategori.setText("Produk di " + kategori.getNama() + " yang mungkin kamu suka");


            for (int j = 0; j < arrProduct.size();j++){
                if (ctrBarang < 5 && arrProduct.get(j).getStok() > 0){
                    if (arrProduct.get(j).getFk_kategori() == kategori.getId()){
                        Picasso.get().load(itemView.getResources().getString(R.string.url) + "/produk/" +
                                arrProduct.get(j).getGambar()).into(arrImageView[ctrBarang]);

                        ViewGroup.LayoutParams params = arrImageView[ctrBarang].getLayoutParams();
                        params.height = 120;
                        arrImageView[ctrBarang].setLayoutParams(params);

                        arrTxtNamaProduct[ctrBarang].setText(arrProduct.get(j).getNama());
                        arrTxtHargaProduct[ctrBarang].setText("Rp " + arrProduct.get(j).getHargaInString());

                        int idProduct = arrProduct.get(j).getId();
                        arrLlProduct[ctrBarang].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(itemView.getContext(), CustomerDetailProduct.class);
                                i.putExtra("idproduct", idProduct);
                                i.putExtra("login", CustomerHomeActivity.login);
                                ((Activity)itemView.getContext()).startActivityForResult(i, 100);
                            }
                        });
                        ctrBarang++;
                    }
                }
            }

            if (ctrBarang < 4) {
                for (int i = 4; i >= ctrBarang; i--) {
                    ViewGroup.LayoutParams params = arrLlProduct[ctrBarang].getLayoutParams();
                    params.height = 0;
                    arrLlProduct[ctrBarang].setLayoutParams(params);
                }
            }

        }
    }
}
