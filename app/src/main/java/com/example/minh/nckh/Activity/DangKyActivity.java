package com.example.minh.nckh.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class DangKyActivity extends AppCompatActivity {
    EditText edtName, edtPhoneEmail, edtPass, edtPass2,edtSDT,edtDiaChi;
    Button btnSignUp;
    TextView txtSignIn;
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        khoitao();
        changeColorStausBar();
        ActionBar();
        SetEven();

    }

    private void SetEven() {
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class));
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtName.getText().toString().equals("") || edtPhoneEmail.getText().toString().equals("") || edtPass.getText().toString().equals("") || edtPass2.getText().toString().equals("")||edtSDT.getText().toString().equals("")||edtDiaChi.getText().toString().equals("")) {
                    Toast.makeText(DangKyActivity.this, "Bạn cần nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {

                    if (edtPass.getText().toString().equals(edtPass2.getText().toString())==false) {
                        Toast.makeText(DangKyActivity.this, "Mật khẩu nhập lại không trùng", Toast.LENGTH_SHORT).show();
                    } else {
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        String url = Server.urlDangKy;
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.toString().equals("success")) {
                                    startActivity(new Intent(DangKyActivity.this, DangNhapActivity.class));
                                } else {
                                    Toast.makeText(DangKyActivity.this, "Đã có tài khoản này rồi", Toast.LENGTH_SHORT).show();
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
                                param.put("ten", edtName.getText().toString());
                                param.put("taikhoan", edtPhoneEmail.getText().toString());
                                param.put("matkhau", edtPass.getText().toString());
                                param.put("sdt",edtSDT.getText().toString());
                                param.put("diachi",edtDiaChi.getText().toString());
                                return param;
                            }
                        };
                        requestQueue.add(stringRequest);
                    }
                }
            }
        });
    }

    private void khoitao() {
        edtName = (EditText) findViewById(R.id.edt_name);
        edtPass = (EditText) findViewById(R.id.edt_pass);
        edtPass2 = (EditText) findViewById(R.id.edt_pass2);
        edtPhoneEmail = (EditText) findViewById(R.id.edt_phone_email);
        edtSDT = (EditText) findViewById(R.id.edt_sdt);
        edtDiaChi = (EditText) findViewById(R.id.edt_diachi);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        txtSignIn = (TextView) findViewById(R.id.txt_signin);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
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

    public  void changeColorStausBar(){
        if(Build.VERSION.SDK_INT>=21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
    }
}
