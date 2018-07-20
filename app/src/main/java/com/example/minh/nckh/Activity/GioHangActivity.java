package com.example.minh.nckh.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minh.nckh.Adapter.GioHangAdapter;
import com.example.minh.nckh.Database.Database;
import com.example.minh.nckh.Model.GioHangModel;
import com.example.minh.nckh.Model.SanPham;
import com.example.minh.nckh.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GioHangActivity extends AppCompatActivity {
    ListView listGioHang;
    static TextView txtTongTien;
    Button btnThanhToan,btnTiepTucMua;
    ArrayList<GioHangModel> sanPhamArrayList;
    GioHangAdapter gioHangAdapter;
    static long tongtien=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        khoitao();
        changeColorStausBar();
        SetTongTien();
        SuKien();

    }

    private void SuKien() {
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtTongTien.getText().toString().equals("0 VNĐ")){
                    Toast.makeText(GioHangActivity.this, "Giỏ hàng của bạn đang trống", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(GioHangActivity.this, ThanhToanGioHangActivity.class);
                    intent.putExtra("tongtien", tongtien);
                    startActivity(intent);
                }
            }
        });

    }
    public static void SetTongTien() {
        tongtien=0;
        Cursor manggiohang = TrangChuActivity.database.GetData("Select * from MangGioHang");
        while (manggiohang.moveToNext()){
            Long GiaSP = manggiohang.getLong(2);
            int SoLuong = manggiohang.getInt(4);
            tongtien = tongtien+SoLuong*GiaSP;
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTongTien.setText(decimalFormat.format(tongtien)+" VNĐ");
    }
    private void khoitao() {
        listGioHang = (ListView) findViewById(R.id.list_giohang);
        txtTongTien = (TextView) findViewById(R.id.txt_tongtien);
        btnThanhToan = (Button) findViewById(R.id.btn_thanhtoan);
        btnTiepTucMua = (Button) findViewById(R.id.btn_tieptucmua);
        //database = new Database(this,"giohang.sqlite",null,1);
        sanPhamArrayList = new ArrayList<>();
        Cursor manggiohang = TrangChuActivity.database.GetData("Select * from MangGioHang");
        if(manggiohang.getCount()>0){
            while (manggiohang.moveToNext()){
                String MaSP = manggiohang.getString(0);
                String TenSP = manggiohang.getString(1);
                Long GiaSP = manggiohang.getLong(2);
                String AnhSP = manggiohang.getString(3);
                int SoLuong = manggiohang.getInt(4);
                sanPhamArrayList.add(new GioHangModel(MaSP,TenSP,GiaSP,AnhSP,SoLuong));
            }
            gioHangAdapter = new GioHangAdapter(GioHangActivity.this,sanPhamArrayList,R.layout.row_payment);
            listGioHang.setAdapter(gioHangAdapter);
        }else {
            Toast.makeText(this, "Không có data", Toast.LENGTH_SHORT).show();
        }


    }
    public  void XacNhanXoa(final int position, String ten, final String masp){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thông báo!");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage("Bạn có muốn xóa mặt hàng "+ten+" này không!");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sanPhamArrayList.remove(position);
                gioHangAdapter.notifyDataSetChanged();
                TrangChuActivity.database.QueryData("delete from MangGioHang where MaSP = '"+masp+"'");
                SetTongTien();
            }
        });

        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    public  void changeColorStausBar(){
        if(Build.VERSION.SDK_INT>=21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
    }
}
