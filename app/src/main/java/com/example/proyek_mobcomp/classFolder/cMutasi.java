package com.example.proyek_mobcomp.classFolder;

import android.os.Parcel;
import android.os.Parcelable;

public class cMutasi implements Parcelable {
    protected int id, jumlah;
    protected String fk_user, tanggal, keterangan;

    public cMutasi(int id, int jumlah, String fk_user, String tanggal, String keterangan) {
        this.id = id;
        this.jumlah = jumlah;
        this.fk_user = fk_user;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
    }

    public cMutasi(Parcel in) {
        id = in.readInt();
        jumlah = in.readInt();
        fk_user = in.readString();
        tanggal = in.readString();
        keterangan = in.readString();
    }

    public static final Creator<cMutasi> CREATOR = new Creator<cMutasi>() {
        @Override
        public cMutasi createFromParcel(Parcel in) {
            return new cMutasi(in);
        }

        @Override
        public cMutasi[] newArray(int size) {
            return new cMutasi[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(jumlah);
        parcel.writeString(fk_user);
        parcel.writeString(tanggal);
        parcel.writeString(keterangan);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getFk_user() {
        return fk_user;
    }

    public void setFk_user(String fk_user) {
        this.fk_user = fk_user;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
