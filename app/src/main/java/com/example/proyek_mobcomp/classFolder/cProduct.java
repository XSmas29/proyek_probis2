package com.example.proyek_mobcomp.classFolder;

import android.os.Parcel;
import android.os.Parcelable;

public class cProduct implements Parcelable{
    protected int id, fk_kategori, harga, stok, is_deleted;
    protected String fk_seller, nama, deskripsi, gambar;

    public cProduct(int id, String fk_seller, int fk_kategori, String nama, String deskripsi, int harga, int stok, String gambar, int is_deleted) {
        this.id = id;
        this.fk_seller = fk_seller;
        this.fk_kategori = fk_kategori;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.stok = stok;
        this.gambar = gambar;
        this.is_deleted = is_deleted;
    }

    public cProduct(Parcel in) {
        id = in.readInt();
        fk_kategori = in.readInt();
        harga = in.readInt();
        stok = in.readInt();
        is_deleted = in.readInt();
        fk_seller = in.readString();
        nama = in.readString();
        deskripsi = in.readString();
        gambar = in.readString();
    }

    public static final Creator<cProduct> CREATOR = new Creator<cProduct>() {
        @Override
        public cProduct createFromParcel(Parcel in) {
            return new cProduct(in);
        }

        @Override
        public cProduct[] newArray(int size) {
            return new cProduct[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(fk_kategori);
        parcel.writeInt(harga);
        parcel.writeInt(stok);
        parcel.writeInt(is_deleted);
        parcel.writeString(fk_seller);
        parcel.writeString(nama);
        parcel.writeString(deskripsi);
        parcel.writeString(gambar);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFk_kategori() {
        return fk_kategori;
    }

    public void setFk_kategori(int fk_kategori) {
        this.fk_kategori = fk_kategori;
    }

    public int getHarga() {
        return harga;
    }

    public String getHargaInString(){
        int ctr = 0; // untuk menghitung berapa digit yg sudah masuk ke variable hasil
        String hasil = "";

        String awal = String.valueOf(this.harga);
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

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getFk_seller() {
        return fk_seller;
    }

    public void setFk_seller(String fk_seller) {
        this.fk_seller = fk_seller;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
