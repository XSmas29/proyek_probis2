package com.example.proyek_mobcomp.classFolder;

import android.os.Parcel;
import android.os.Parcelable;

public class cKategori implements Parcelable {
    int id;
    String nama;
    String tipe;

    @Override
    public String toString() {
        return nama;
    }

    public cKategori(int id, String nama, String tipe) {
        this.id = id;
        this.nama = nama;
        this.tipe = tipe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    protected cKategori(Parcel in) {
        id = in.readInt();
        nama = in.readString();
        tipe = in.readString();
    }

    public static final Creator<cKategori> CREATOR = new Creator<cKategori>() {
        @Override
        public cKategori createFromParcel(Parcel in) {
            return new cKategori(in);
        }

        @Override
        public cKategori[] newArray(int size) {
            return new cKategori[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nama);
        parcel.writeString(tipe);
    }
}
