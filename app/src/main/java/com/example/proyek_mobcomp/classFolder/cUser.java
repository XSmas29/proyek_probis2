package com.example.proyek_mobcomp.classFolder;

import android.os.Parcel;
import android.os.Parcelable;

public class cUser implements Parcelable {
    protected String username, password, email, nama, rekening, saldo, toko, role, gambar;
    protected int status, is_verified;

    public cUser(String username, String password, String email
            , String nama, String rekening, String saldo, String toko
            , String role, String gambar, int status, int is_verified) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nama = nama;
        this.rekening = rekening;
        this.saldo = saldo;
        this.toko = toko;
        this.role = role;
        this.gambar = gambar;
        this.status = status;
        this.is_verified = is_verified;
    }

    public cUser(String username, String password, String email, String nama, String rekening, String saldo, String toko, String role, String gambar) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nama = nama;
        this.rekening = rekening;
        this.saldo = saldo;
        this.toko = toko;
        this.role = role;
        this.gambar = gambar;
    }

    public cUser(String username, String nama, String toko, String gambar) {
        this.username = username;
        this.nama = nama;
        this.toko = toko;
        this.gambar = gambar;
    }

    protected cUser(Parcel in) {
        username = in.readString();
        password = in.readString();
        email = in.readString();
        nama = in.readString();
        rekening = in.readString();
        saldo = in.readString();
        toko = in.readString();
        role = in.readString();
        gambar = in.readString();
    }

    public static final Creator<cUser> CREATOR = new Creator<cUser>() {
        @Override
        public cUser createFromParcel(Parcel in) {
            return new cUser(in);
        }

        @Override
        public cUser[] newArray(int size) {
            return new cUser[size];
        }
    };

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(int is_verified) {
        this.is_verified = is_verified;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getRekening() {
        return rekening;
    }

    public void setRekening(String rekening) {
        this.rekening = rekening;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(nama);
        dest.writeString(rekening);
        dest.writeString(saldo);
        dest.writeString(toko);
        dest.writeString(role);
        dest.writeString(gambar);
    }
}
