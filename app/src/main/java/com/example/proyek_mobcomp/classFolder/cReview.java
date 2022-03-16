package com.example.proyek_mobcomp.classFolder;

import android.os.Parcel;
import android.os.Parcelable;

public class cReview implements Parcelable {
    protected int id;
    protected int fk_htrans;
    protected int fk_dtrans;
    protected String fk_user;
    protected int star;
    protected String isi;
    protected String tanggal;
    protected String namalengkap;

    public cReview(int id, int fk_htrans, int fk_dtrans, String fk_user, int star, String isi, String tanggal, String namalengkap) {
        this.id = id;
        this.fk_htrans = fk_htrans;
        this.fk_dtrans = fk_dtrans;
        this.fk_user = fk_user;
        this.star = star;
        this.isi = isi;
        this.tanggal = tanggal;
        this.namalengkap = namalengkap;
    }

    public cReview(int id, int fk_htrans, int fk_dtrans, String fk_user, int star, String isi) {
        this.id = id;
        this.fk_htrans = fk_htrans;
        this.fk_dtrans = fk_dtrans;
        this.fk_user = fk_user;
        this.star = star;
        this.isi = isi;
    }

    protected cReview(Parcel in) {
        id = in.readInt();
        fk_htrans = in.readInt();
        fk_dtrans = in.readInt();
        fk_user = in.readString();
        star = in.readInt();
        isi = in.readString();
        tanggal = in.readString();
        namalengkap = in.readString();
    }

    public static final Creator<cReview> CREATOR = new Creator<cReview>() {
        @Override
        public cReview createFromParcel(Parcel in) {
            return new cReview(in);
        }

        @Override
        public cReview[] newArray(int size) {
            return new cReview[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFk_htrans() {
        return fk_htrans;
    }

    public void setFk_htrans(int fk_htrans) {
        this.fk_htrans = fk_htrans;
    }

    public int getFk_dtrans() {
        return fk_dtrans;
    }

    public void setFk_dtrans(int fk_dtrans) {
        this.fk_dtrans = fk_dtrans;
    }

    public String getFk_user() {
        return fk_user;
    }

    public void setFk_user(String fk_user) {
        this.fk_user = fk_user;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNamalengkap() {
        return namalengkap;
    }

    public void setNamalengkap(String namalengkap) {
        this.namalengkap = namalengkap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(fk_htrans);
        dest.writeInt(fk_dtrans);
        dest.writeString(fk_user);
        dest.writeInt(star);
        dest.writeString(isi);
        dest.writeString(tanggal);
        dest.writeString(namalengkap);
    }
}
