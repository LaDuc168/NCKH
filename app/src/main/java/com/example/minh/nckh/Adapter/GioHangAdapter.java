package com.example.minh.nckh.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minh.nckh.Activity.GioHangActivity;
import com.example.minh.nckh.Activity.TrangChuActivity;
import com.example.minh.nckh.Model.GioHangModel;
import com.example.minh.nckh.Model.SanPham;
import com.example.minh.nckh.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Minh on 3/24/2018.
 */

public class GioHangAdapter extends BaseAdapter {
    private GioHangActivity context;
    private ArrayList<GioHangModel> sanPhamArrayList;
    private int layout;

    public GioHangAdapter(GioHangActivity context, ArrayList<GioHangModel> sanPhamArrayList, int layout) {
        this.context = context;
        this.sanPhamArrayList = sanPhamArrayList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return sanPhamArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return sanPhamArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.img_sp = (ImageView) convertView.findViewById(R.id.img_sp);
            holder.txt_tensp = convertView.findViewById(R.id.txt_tensp);
            holder.txt_giasp = convertView.findViewById(R.id.txt_giasp);
            holder.btnTru = convertView.findViewById(R.id.btn_tru);
            holder.btnCong = convertView.findViewById(R.id.btn_cong);
            holder.btnSoLuong = convertView.findViewById(R.id.btn_soluong);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GioHangModel sanPham = sanPhamArrayList.get(position);
        holder.txt_tensp.setText(sanPham.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txt_giasp.setText("Giá : " + decimalFormat.format(sanPham.getGiasanpham()) + "đ");
        Picasso.with(context).load(sanPham.getHinhanhsanpham()).placeholder(R.drawable.load)
                .error(R.drawable.error)
                .into(holder.img_sp);
        holder.btnSoLuong.setText(sanPham.getSoluong()+"");
        final int sl = Integer.parseInt(holder.btnSoLuong.getText().toString());
        if (sl <=1){
            holder.btnCong.setVisibility(View.VISIBLE);
            holder.btnTru.setVisibility(View.INVISIBLE);
        }
        holder.btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int soluong = Integer.parseInt(holder.btnSoLuong.getText().toString());
                int slmoi = soluong+1;
                TrangChuActivity.database.QueryData("UPDATE MangGioHang set SoLuong = '"+slmoi+"' WHERE MaSP ='"+sanPham.getMasp()+"'");
                holder.btnSoLuong.setText(slmoi+"");
                GioHangActivity.SetTongTien();
                if(slmoi>4){
                    holder.btnCong.setVisibility(View.INVISIBLE);
                    holder.btnTru.setVisibility(View.VISIBLE);
                }else {
                    holder.btnCong.setVisibility(View.VISIBLE);
                    holder.btnTru.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.btnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int soluong = Integer.parseInt(holder.btnSoLuong.getText().toString());
                int slmoi = soluong-1;
                TrangChuActivity.database.QueryData("UPDATE MangGioHang set SoLuong = '"+slmoi+"' WHERE MaSP ='"+sanPham.getMasp()+"'");
                holder.btnSoLuong.setText(slmoi+"");
                GioHangActivity.SetTongTien();
                if(slmoi<2){
                    holder.btnCong.setVisibility(View.VISIBLE);
                    holder.btnTru.setVisibility(View.INVISIBLE);
                }else {
                    holder.btnCong.setVisibility(View.VISIBLE);
                    holder.btnTru.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.img_sp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                context.XacNhanXoa(position,sanPhamArrayList.get(position).getTensp(),sanPhamArrayList.get(position).getMasp());
                return false;
            }
        });
        return convertView;
    }

    public class ViewHolder {
        ImageView img_sp;
        TextView txt_tensp, txt_giasp;
        Button btnTru,btnCong,btnSoLuong;
    }

}
