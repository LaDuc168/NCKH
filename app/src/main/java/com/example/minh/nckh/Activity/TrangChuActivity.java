package com.example.minh.nckh.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.minh.nckh.Adapter.SanPhamAdapter;
import com.example.minh.nckh.Database.Database;
import com.example.minh.nckh.Model.SanPham;
import com.example.minh.nckh.R;
import com.example.minh.nckh.Ultil.Server;
import com.example.minh.nckh.map.MapsActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TrangChuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBar;
    private ViewFlipper viewFlipper;
    ArrayList<SanPham> mangsanpham;
    SanPhamAdapter sanPhamAdapter;
    RecyclerView recyclerView;
    IntentIntegrator intentIntegrator;
    NavigationView navigationView;
    public static Database database;
    MenuItem nav_thongtintaikhoan;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        khoitao();
        database();
        initQrcode();
        ActionViewFlipper();
        GetSanPhamMoiNhat();

        ///GCM
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Check type of intent filter
                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    //Registration success
                    token = intent.getStringExtra("token");
                    Toast.makeText(getApplicationContext(), "GCM token:" + token, Toast.LENGTH_LONG).show();
                    Log.d("DUC",token);
                    AddTokenDatabase(Server.url_token, token);


                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    //Registration error
                    Toast.makeText(getApplicationContext(), "GCM registration error!!!", Toast.LENGTH_LONG).show();
                } else {
                    //Tobe define
                }
            }
        };

        //Check status of Google play service in device
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (ConnectionResult.SUCCESS != resultCode) {
            //Check type of error
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                //So notification
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
        } else {
            //Start service
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }
    }

    private void database(){
        database = new Database(this, "dangnhaptaikhoan.sqlite", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS DangNhap(taikhoan VARCHAR(50), matkhau VARCHAR(50),hoten VARCHAR(50), SDT INTEGER,diachigiaohang VARCHAR(100) )");
        database.QueryData("CREATE TABLE IF NOT EXISTS MangGioHang(MaSP VARCHAR(50), TenSP VARCHAR(50),GiaSP LONG, HinhAnhSP VARCHAR(300),SoLuong INTEGER )");
        Cursor taikhoan = database.GetData("SELECT * FROM DangNhap");
        while (taikhoan.moveToNext()){
            String tentaikhoan = taikhoan.getString(0);
            nav_thongtintaikhoan.setTitle(tentaikhoan);
        }
    }
    private void initQrcode() {
        intentIntegrator=new IntentIntegrator(this);
        Vibrator vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);

        vibrator.vibrate(400);
    }

    public void POST_DATA(String url, final String name, final String pass) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.toString().equals("success")){

//                    startActivity(new Intent(MainActivity.this, ShowInfoActivity.class));
                }else{
//                    Toast.makeText(MainActivity.this, "Khong phai", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> map=new HashMap<>();
                map.put("name",name);
                map.put("pass",pass);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject=new JSONObject(result.getContents());


                    String ma=jsonObject.getString("name");

                    Intent intent=new Intent(TrangChuActivity.this,ChiTietSanPhamActivity.class);
                    intent.putExtra("QRCODE",ma);
                    startActivity(intent);
//                    String address=jsonObject.getString("address");


                    Toast.makeText(this, "name="+ma, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void GetSanPhamMoiNhat() {
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Server.urlGetSanPhamMoiNhat;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlGetSanPhamMoiNhat, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    String masp = "";
                    String tensp = "";
                    int giasp = 0;
                    String hinhanhsp = "";
                    String size ="";
                    String motasp = "";
                    String loaisp = "";
                    String manhacungcap="";
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            masp = jsonObject.getString("MaHang");
                            tensp = jsonObject.getString("TenHang");
                            giasp = jsonObject.getInt("DonGiaBan");
                            size = jsonObject.getString("Size");
                            motasp = jsonObject.getString("MieuTa");
                            loaisp = jsonObject.getString("Loai");
                            hinhanhsp = jsonObject.getString("Anh");
                            manhacungcap = jsonObject.getString("MaNhaCungCap");
                            mangsanpham.add(new SanPham(masp,tensp,giasp,size,motasp,hinhanhsp,loaisp,manhacungcap));
                            sanPhamAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    void khoitao() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        actionBar = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBar);
        actionBar.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        Menu menu = navigationView.getMenu();
        nav_thongtintaikhoan = (MenuItem) menu.findItem(R.id.nav_infor);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mangsanpham = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(getApplicationContext(), mangsanpham);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(sanPhamAdapter);
    }



    //<Action bar>
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBar.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()){
            case R.id.menu_qrcode:
                intentIntegrator.initiateScan();

                break;
            case R.id.menu_map:
//                startActivity(new Intent(TrangChuActivity.this,MapsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
//</Action bar>


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_qrcode,menu);
//        inflater.inflate(R.menu.menu_map,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
        }
        if (id == R.id.nav_computer) {
            startActivity(new Intent(TrangChuActivity.this,LaptopActivity.class));
        }
        if (id == R.id.nav_infor) {
            startActivity(new Intent(TrangChuActivity.this,DangNhapActivity.class));
        }
        if (id == R.id.nav_hotline) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:0981027926"));
            startActivity(intent);

        }
        if (id == R.id.nav_map) {
            startActivity(new Intent(TrangChuActivity.this,MapsActivity.class));
        }
        if(id==R.id.nav_phone)
            startActivity(new Intent(TrangChuActivity.this,DienThoaiActivity.class));
        if(id==R.id.nav_giohang)
            startActivity(new Intent(TrangChuActivity.this,GioHangActivity.class));

        return true;
    }
    private void ActionViewFlipper() {
        ArrayList<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("http://hame.vn/wp-content/uploads/2017/09/14.jpg");
        mangquangcao.add("https://canifa.s3.amazonaws.com/media/wysiwyg/Social_chung-05.jpg");
        mangquangcao.add("http://media1.nguoiduatin.vn/media/do-thi-hue/2017/11/07/iphone-x.jpg");
        mangquangcao.add("https://g.vatgia.vn/gallery_img/0/clp1448800708.jpg");
        mangquangcao.add("http://starschanges.com/wp-content/uploads/2016/03/justin-bieber-celebrity-style-5.jpg");
        for (int i = 0; i < mangquangcao.size(); i++) {
            ImageView img = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(mangquangcao.get(i)).into(img);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(img);
        }
        viewFlipper.setFlipInterval(5000);//Chạy trong 5s
        viewFlipper.setAutoStart(true);//cho view flipper tự chạy
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);
    }



    //GCM

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }


    public void AddTokenDatabase(String url, final String token) {
        RequestQueue requestQueue = Volley.newRequestQueue(TrangChuActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.toString().trim().equals("ok")) {
                } else {
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LOI",error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("demo", token);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

}