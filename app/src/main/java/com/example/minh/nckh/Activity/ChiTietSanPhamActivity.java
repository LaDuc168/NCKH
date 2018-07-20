package com.example.minh.nckh.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.minh.nckh.Adapter.BinhLuanAdapter;
import com.example.minh.nckh.Adapter.SanPhamHorizontalAdapter;
import com.example.minh.nckh.Database.Database;
import com.example.minh.nckh.Model.BinhLuan;
import com.example.minh.nckh.Model.SanPham;
import com.example.minh.nckh.R;
import com.example.minh.nckh.Ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    Toolbar toolBar;
    String masp = "";
    String tensp = "";
    int giasp = 0;
    String hinhanhsp = "";
    String manhacungcap;
    String size = "";
    String motasp = "";
    String loaisp = "";
    RecyclerView recyclerView;
    private ArrayList<SanPham> sanPhamArrayList;
    SanPhamHorizontalAdapter adapter;
    ImageView imgThongTinSP;
    Button btnxemThem, btnThanhToan, btnBinhLuan;
    EditText edtBinhLuan;
    TextView txtThongTin;
    ListView listBinhLuan;
    BinhLuanAdapter binhLuanAdapter;
    ArrayList<BinhLuan> binhLuanArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        khoitao();
        ActionBar();
        changeColorStausBar();
        Intent intent = getIntent();
        masp = intent.getStringExtra("qrcode");
        Qrcode();

        ThanhToan();
    }

    private void ActionBar() {
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void ThanhToan() {
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor manggiohang = TrangChuActivity.database.GetData("Select * from MangGioHang where MaSP = '" + masp + "'");
                if (manggiohang.getCount() == 0) {
                    TrangChuActivity.database.QueryData("INSERT INTO MangGioHang VALUES('" + masp + "','" + tensp + "','" + giasp + "','" + hinhanhsp + "','1')");
                } else {
                    int SoLuong = 0;
                    while (manggiohang.moveToNext()) {
                        SoLuong = manggiohang.getInt(4);
                    }
                    SoLuong = SoLuong + 1;
                    TrangChuActivity.database.QueryData("UPDATE MangGioHang set SoLuong = '" + SoLuong + "' WHERE MaSP ='" + masp + "'");
                }

                startActivity(new Intent(ChiTietSanPhamActivity.this, GioHangActivity.class));
            }
        });
    }

    private void khoitao() {
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        imgThongTinSP = (ImageView) findViewById(R.id.img_thongtinsp);
        btnxemThem = (Button) findViewById(R.id.btn_xemthem);
        btnBinhLuan = (Button) findViewById(R.id.btn_binhluan);
        btnThanhToan = (Button) findViewById(R.id.btn_thanhtoan);
        edtBinhLuan = (EditText) findViewById(R.id.edt_binhluan);
        txtThongTin = (TextView) findViewById(R.id.txt_thongtin);
        btnThanhToan = (Button) findViewById(R.id.btn_thanhtoan);
        //database = new Database(this, "giohang.sqlite", null, 1);
        //database.QueryData("CREATE TABLE IF NOT EXISTS MangGioHang(MaSP VARCHAR(50), TenSP VARCHAR(50),GiaSP LONG, HinhAnhSP VARCHAR(300),SoLuong INTEGER )");

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        sanPhamArrayList = new ArrayList<>();
        adapter = new SanPhamHorizontalAdapter(getApplicationContext(), sanPhamArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        binhLuanArrayList = new ArrayList<>();
//        binhLuanArrayList.add(new BinhLuan("sad","ádsa"));
        binhLuanAdapter = new BinhLuanAdapter(this, R.layout.row_coment, binhLuanArrayList);
        listBinhLuan = (ListView) findViewById(R.id.list_binhluan);
        listBinhLuan.setAdapter(binhLuanAdapter);
    }

    private void Qrcode() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Server.urlQrcode;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            masp = jsonObject.getString("MaHang");
                            tensp = jsonObject.getString("TenHang");
                            giasp = jsonObject.getInt("DonGiaBan");
                            size = jsonObject.getString("Size");
                            motasp = jsonObject.getString("MieuTa");
                            loaisp = jsonObject.getString("Loai");
                            hinhanhsp = jsonObject.getString("Anh");
                            manhacungcap = jsonObject.getString("MaNhaCungCap");
                            txtThongTin.setText(motasp);
                            Picasso.with(getApplicationContext()).load(hinhanhsp)
                                    .placeholder(R.drawable.load)
                                    .error(R.drawable.error)
                                    .into(imgThongTinSP);
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            String url = Server.urlSanPhamLienQuan;
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String masp = "";
                                    String tensp = "";
                                    int giasp = 0;
                                    String hinhanhsp = "";
                                    String size = "";
                                    String motasp = "";
                                    String loaisp = "";
                                    String manhacungcap = "";
                                    if (response != null) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                masp = jsonObject.getString("MaHang");
                                                tensp = jsonObject.getString("TenHang");
                                                giasp = jsonObject.getInt("DonGiaBan");
                                                size = jsonObject.getString("Size");
                                                motasp = jsonObject.getString("MieuTa");
                                                loaisp = jsonObject.getString("Loai");
                                                hinhanhsp = jsonObject.getString("Anh");
                                                sanPhamArrayList.add(new SanPham(masp, tensp, giasp, size, motasp, hinhanhsp, loaisp, manhacungcap));
                                                adapter.notifyDataSetChanged();
                                                XemBinhLuan();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> param = new HashMap<String, String>();
                                    param.put("loai", String.valueOf(loaisp));
                                    param.put("manhacungcap", manhacungcap);
                                    return param;
                                }
                            };
                            requestQueue.add(stringRequest);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("qrcode", String.valueOf(masp));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void XemBinhLuan() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Server.url_xembinhluan;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String taikhoan, binhluan;
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            taikhoan = jsonObject.getString("TaiKhoan");
                            binhluan = jsonObject.getString("NoiDung");
                            binhLuanArrayList.add(new BinhLuan(taikhoan, binhluan));
                            binhLuanAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("mahang", masp);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void changeColorStausBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
    }
}
