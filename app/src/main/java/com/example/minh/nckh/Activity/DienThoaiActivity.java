package com.example.minh.nckh.Activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.minh.nckh.Adapter.DienThoaiAdapter;
import com.example.minh.nckh.Model.SanPham;
import com.example.minh.nckh.R;
import com.example.minh.nckh.Ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DienThoaiActivity extends AppCompatActivity {
    int page = 1;
    int loai = 1;
    Spinner spinHang, spinGia;
    String manhacungcap, gia;
    Toolbar toolBar;
    RecyclerView recyclerView;
    DienThoaiAdapter dienThoaiAdapter;
    ArrayList<SanPham> arrayDT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        khoitao();
        changeColorStausBar();
        ActionToolBar();
        getData(page);
        Spinner();
        EvenSpinner();
    }

    private void getData(final int page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Server.urlSanPham;
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
                            manhacungcap = jsonObject.getString("MaNhaCungCap");
                            arrayDT.add(new SanPham(masp, tensp, giasp, size, motasp, hinhanhsp, loaisp, manhacungcap));
                            dienThoaiAdapter.notifyDataSetChanged();
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
                param.put("loai", String.valueOf(loai));
                param.put("page", String.valueOf(page));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void ActionToolBar() {
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void khoitao() {
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        arrayDT = new ArrayList<>();

        dienThoaiAdapter = new DienThoaiAdapter(getApplicationContext(), arrayDT);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(dienThoaiAdapter);

        spinGia = (Spinner) findViewById(R.id.spin_timkiemgia);
        spinHang = (Spinner) findViewById(R.id.spin_timkiemhang);
    }

    private void Spinner() {
        String[] timkiemghang = {"Tìm kiếm theo hãng", "EVE", "GAS", "NANA", "OEM","ZEN"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, timkiemghang);
        spinHang.setAdapter(arrayAdapter);

        String[] timkiemgia = {"Tìm kiếm theo giá(triệu)", "0-5", "5-10", "10-15", "15-20", "20-30", "30-40"};
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, timkiemgia);
        spinGia.setAdapter(arrayAdapter1);
    }

    private void EvenSpinner() {
        spinHang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String gia1 = "";
                String gia2 = "";
                manhacungcap = (String) spinHang.getItemAtPosition(position);
                gia = spinGia.getSelectedItem().toString();
                if (manhacungcap.equals("Tìm kiếm theo hãng")) {
                    manhacungcap = "";
                }
                if (gia.equals("Tìm kiếm theo giá(triệu)")) {
                    gia = "";
                } else {
                    String[] a = gia.split("-");
                    gia1 = String.valueOf(Integer.parseInt(a[0]) * 1000000);
                    gia2 = String.valueOf(Integer.parseInt(a[1]) * 1000000);
                }
                TimKiem(gia1, gia2, manhacungcap);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinGia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String gia1 = "";
                String gia2 = "";
                gia = (String) spinGia.getItemAtPosition(position);
                manhacungcap = spinHang.getSelectedItem().toString();
                if (manhacungcap.equals("Tìm kiếm theo hãng")) {
                    manhacungcap = "";
                }
                if (gia.equals("Tìm kiếm theo giá(triệu)")) {
                    gia = "";
                } else {
                    String[] a = gia.split("-");
                    gia1 = String.valueOf(Integer.parseInt(a[0]) * 1000000);
                    gia2 = String.valueOf(Integer.parseInt(a[1]) * 1000000);
                }
                TimKiem(gia1, gia2, manhacungcap);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void TimKiem(final String gia1, final String gia2, final String manhacungcap) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Server.urlTimKiem;
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
                        arrayDT.clear();
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
                            arrayDT.add(new SanPham(masp, tensp, giasp, size, motasp, hinhanhsp, loaisp, manhacungcap));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dienThoaiAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("loai", String.valueOf(loai));
                param.put("manhacungcap", manhacungcap);
                param.put("gia1", gia1);
                param.put("gia2", gia2);
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
