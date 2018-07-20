package com.example.minh.nckh.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.minh.nckh.Model.GioHangModel;
import com.example.minh.nckh.Model.SanPham;
import com.example.minh.nckh.R;
import com.example.minh.nckh.Ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ThanhToanGioHangActivity extends AppCompatActivity {

    EditText edtHoTen, edtSDT, edtDiaChi;
    TextView txtTongTien;
    Button btnThanhToan;
    Toolbar toolBar;
    Spinner spinNgay,spinThang,spinNam;
    ArrayList<GioHangModel> sanPhamArrayList;
    long tongtien;
    String taikhoandangnhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan_gio_hang);
        khoitao();
        changeColorStausBar();
        CatchEvenSpiner();
        SetEditText();
        ActionBar();
        LayDuLieuTuGioHang();
        SuKienThanhToan();

    }

    private void SetEditText() {
        Cursor taikhoan = TrangChuActivity.database.GetData("SELECT * FROM DangNhap");
        String user="";
        while (taikhoan.moveToNext()){
            user = taikhoan.getString(0);
            String hoten = taikhoan.getString(2);
            int sdt = taikhoan.getInt(3);
            String diachi = taikhoan.getString(4);
            edtHoTen.setText(hoten);
            edtSDT.setText(sdt+"");
            edtDiaChi.setText(diachi);
        }
        if(user.equals("")){
            taikhoandangnhap = "unknow";
        }else {
            taikhoandangnhap = user;
        }
        Calendar calendar = Calendar.getInstance();
        spinNgay.setSelection(calendar.get(Calendar.DATE)-1);
        spinThang.setSelection(calendar.get(Calendar.MONTH));
        spinNam.setSelection(calendar.get(Calendar.YEAR)-2018);
    }


    private void SuKienThanhToan() {
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtHoTen.getText().toString().equals("") || edtSDT.getText().toString().equals("") || edtDiaChi.getText().toString().equals("")) {
                    Toast.makeText(ThanhToanGioHangActivity.this, "Bạn cần nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    String url = Server.urlDatHang;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String madonhang) {
                            if (Integer.parseInt(madonhang) > 0) {
                                RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                                String url1 = Server.urlChiTietDatHang;
                                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equals("1")){
                                            sanPhamArrayList.clear();
                                            TrangChuActivity.database.QueryData("delete from MangGioHang");
                                            GioHangActivity.SetTongTien();
                                            Toast.makeText(ThanhToanGioHangActivity.this, "Bạn đã đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(),TrangChuActivity.class);
                                            startActivity(intent);
                                            Toast.makeText(ThanhToanGioHangActivity.this, "Mời bạn tiếp tục mua hàng", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(ThanhToanGioHangActivity.this, "Dữ liệu giỏ hàng của bạn đã bị lỗi", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        JSONArray jsonArray = new JSONArray();
                                        for (int i = 0; i < sanPhamArrayList.size(); i++) {
                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("maphieudat", madonhang);
                                                jsonObject.put("mahang", sanPhamArrayList.get(i).getMasp());
                                                jsonObject.put("dongia", sanPhamArrayList.get(i).getGiasanpham());
                                                jsonObject.put("soluong",sanPhamArrayList.get(i).getSoluong());
                                                jsonArray.put(jsonObject);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        HashMap<String, String> hashMap = new HashMap<String, String>();
                                        hashMap.put("json", jsonArray.toString());
                                        return hashMap;
                                    }
                                };
                                requestQueue1.add(stringRequest1);
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
                            param.put("taikhoan",taikhoandangnhap);
                            param.put("tenkhachhang", edtHoTen.getText().toString());
                            param.put("sodienthoai", edtSDT.getText().toString());
                            param.put("diachiship", edtDiaChi.getText().toString());
                            param.put("tongtien", String.valueOf(tongtien));
                            int nam = Integer.parseInt(spinNam.getSelectedItem().toString());
                            int thang = Integer.parseInt(spinThang.getSelectedItem().toString());
                            int ngay = Integer.parseInt(spinNgay.getSelectedItem().toString());
                            param.put("ngaydat",nam+"-"+thang+"-"+ngay);
                            return param;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            }
        });
    }
    private void LayDuLieuTuGioHang() {
        Intent intent = getIntent();
        tongtien = intent.getLongExtra("tongtien", 0);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTongTien.setText(decimalFormat.format(tongtien) + "VNĐ");

        Cursor manggiohang = TrangChuActivity.database.GetData("Select * from MangGioHang");
        while (manggiohang.moveToNext()) {
            String mahang = manggiohang.getString(0);
            String tenhang = manggiohang.getString(1);
            String hinhanh = manggiohang.getString(3);
            Long giaSP = manggiohang.getLong(2);
            int soLuong = manggiohang.getInt(4);
            sanPhamArrayList.add(new GioHangModel(mahang, tenhang, giaSP, hinhanh, soLuong));
        }
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
    private void khoitao() {
        edtHoTen = (EditText) findViewById(R.id.edt_hoten);
        edtSDT = (EditText) findViewById(R.id.edt_sdt);
        btnThanhToan = (Button) findViewById(R.id.btn_thanhtoan);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        txtTongTien = (TextView) findViewById(R.id.txt_tongtien);
        edtDiaChi = (EditText) findViewById(R.id.edt_diachigiaohang);
        spinNgay = (Spinner) findViewById(R.id.spin_ngay);
        spinThang = (Spinner) findViewById(R.id.spin_thang);
        spinNam = (Spinner) findViewById(R.id.spin_nam);
        sanPhamArrayList = new ArrayList<>();
    }
    private void CatchEvenSpiner() {
        Integer[] soluong = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,soluong);
        spinThang.setAdapter(arrayAdapter);


        Integer[] soluong1 = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30};
        ArrayAdapter<Integer> arrayAdapter1 = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,soluong1);
        spinNgay.setAdapter(arrayAdapter1);

        Integer[] soluong2 = new Integer[]{2018,2019,2020,2021,2022,2023};
        ArrayAdapter<Integer> arrayAdapter2 = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item,soluong2);
        spinNam.setAdapter(arrayAdapter2);
    }

    public  void changeColorStausBar(){
        if(Build.VERSION.SDK_INT>=21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
    }
}
