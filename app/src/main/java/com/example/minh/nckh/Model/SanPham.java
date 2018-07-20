package com.example.minh.nckh.Model;

import java.io.Serializable;

/**
 * Created by Minh on 3/22/2018.
 */

public class SanPham implements Serializable {
    public String mahang;
    public String tenhang;
    public int dongiaban;
    public String size;
    public String mieuta;
    public String anh;
    public String loai;
    public String manhacungcap;



    public SanPham(String mahang, String tenhang, int dongiaban, String size, String mieuta, String anh,String loai,String manhacungcap) {
        this.mahang = mahang;
        this.tenhang = tenhang;
        this.dongiaban = dongiaban;
        this.size = size;
        this.mieuta = mieuta;
        this.anh = anh;
        this.loai = loai;
        this.manhacungcap=manhacungcap;
    }

    public String getManhacungcap() {
        return manhacungcap;
    }

    public void setManhacungcap(String manhacungcap) {
        this.manhacungcap = manhacungcap;
    }

    public String getMahang() {
        return mahang;
    }

    public void setMahang(String mahang) {
        this.mahang = mahang;
    }

    public String getTenhang() {
        return tenhang;
    }

    public void setTenhang(String tenhang) {
        this.tenhang = tenhang;
    }

    public int getDongiaban() {
        return dongiaban;
    }

    public void setDongiaban(int dongiaban) {
        this.dongiaban = dongiaban;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMieuta() {
        return mieuta;
    }

    public void setMieuta(String mieuta) {
        this.mieuta = mieuta;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }
}
