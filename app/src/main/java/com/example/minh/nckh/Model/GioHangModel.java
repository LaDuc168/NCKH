package com.example.minh.nckh.Model;

/**
 * Created by Minh on 3/24/2018.
 */

public class GioHangModel {
    public String masp;
    public String tensp;
    public Long giasanpham;
    public String hinhanhsanpham;
    public int soluong;

    public GioHangModel(String masp, String tensp, Long giasanpham, String hinhanhsanpham, int soluong) {
        this.masp = masp;
        this.tensp = tensp;
        this.giasanpham = giasanpham;
        this.hinhanhsanpham = hinhanhsanpham;
        this.soluong = soluong;
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public Long getGiasanpham() {
        return giasanpham;
    }

    public void setGiasanpham(Long giasanpham) {
        this.giasanpham = giasanpham;
    }

    public String getHinhanhsanpham() {
        return hinhanhsanpham;
    }

    public void setHinhanhsanpham(String hinhanhsanpham) {
        this.hinhanhsanpham = hinhanhsanpham;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
