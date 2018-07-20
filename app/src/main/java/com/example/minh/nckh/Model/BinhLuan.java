package com.example.minh.nckh.Model;

/**
 * Created by Minh on 4/10/2018.
 */

public class BinhLuan {
    private String taikhoan;
    private String binhluan;

    public BinhLuan(String taikhoan, String binhluan) {
        this.taikhoan = taikhoan;
        this.binhluan = binhluan;
    }

    public BinhLuan() {
    }

    public String getTaikhoan() {
        return taikhoan;
    }

    public void setTaikhoan(String taikhoan) {
        this.taikhoan = taikhoan;
    }

    public String getBinhluan() {
        return binhluan;
    }

    public void setBinhluan(String binhluan) {
        this.binhluan = binhluan;
    }
}
