package com.example.minh.nckh.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.minh.nckh.R;
import com.example.minh.nckh.Ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DangNhapActivity extends AppCompatActivity {
    Toolbar toolBar;
    EditText edtUser, edtPass;
    CheckBox chkRemember;
    Button btnSignIn;
    TextView txtSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        khoitao();
        changeColorStausBar();
        ActionBar();
        SetEven();
    }

    private void SetEven() {
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangNhapActivity.this,DangKyActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtUser.getText().toString().equals("") || edtPass.getText().toString().equals("")) {
                    Toast.makeText(DangNhapActivity.this, "Bạn cần nhập tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    String url = Server.urlDangNhap;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.toString().equals("error")) {
                                Toast.makeText(DangNhapActivity.this, "Bạn đã nhập sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                            } else {
                                TrangChuActivity.database.QueryData("DELETE FROM DangNhap");
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String tentaikhoan = jsonObject.getString("TaiKhoan");
                                        String matkhau = jsonObject.getString("MatKhau");
                                        String hoten = jsonObject.getString("TenKhach");
                                        String SDT = jsonObject.getString("SDT");
                                        String diachi = jsonObject.getString("DCShip");
                                        TrangChuActivity.database.QueryData("INSERT INTO DangNhap VALUES('" + tentaikhoan + "','" + matkhau + "','" + hoten + "','" + SDT + "','" + diachi + "')");
                                        startActivity(new Intent(DangNhapActivity.this, TrangChuActivity.class));

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
                            param.put("user", edtUser.getText().toString());
                            param.put("pass", edtPass.getText().toString());
                            return param;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            }
        });
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
        edtUser = (EditText) findViewById(R.id.edt_user);
        edtPass = (EditText) findViewById(R.id.edt_pass);
        chkRemember = (CheckBox) findViewById(R.id.chk_remember);
        btnSignIn = (Button) findViewById(R.id.btn_signin);
        txtSignUp = (TextView) findViewById(R.id.txt_signup);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
    }

    public  void changeColorStausBar(){
        if(Build.VERSION.SDK_INT>=21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
    }
}
