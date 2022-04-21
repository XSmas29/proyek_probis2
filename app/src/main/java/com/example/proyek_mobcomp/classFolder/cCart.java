package com.example.proyek_mobcomp.classFolder;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart")
public class cCart {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    protected int idCart;

    protected int idProduct;
    protected String username;
    protected int jumlah;
    protected int harga;
    protected String catatan;
    protected String fk_seller;

    public cCart(int idProduct, String username, int jumlah, int harga, String fk_seller) {
        this.idProduct = idProduct;
        this.username = username;
        this.jumlah = jumlah;
        this.harga = harga;
        this.catatan = "";
        this.fk_seller = fk_seller;
    }

    public String getFk_seller() {
        return fk_seller;
    }

    public void setFk_seller(String fk_seller) {
        this.fk_seller = fk_seller;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIdCart() {
        return idCart;
    }

    public void setIdCart(int idCart) {
        this.idCart = idCart;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getHarga() {
        return harga;
    }

    public String getHargaInString(){
        int ctr = 0; // untuk menghitung berapa digit yg sudah masuk ke variable hasil
        String hasil = "";

        String awal = String.valueOf(this.harga);
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

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
}
