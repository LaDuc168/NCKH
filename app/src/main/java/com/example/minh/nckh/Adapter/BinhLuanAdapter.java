package com.example.minh.nckh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.minh.nckh.Model.BinhLuan;
import com.example.minh.nckh.R;

import java.util.ArrayList;

/**
 * Created by Minh on 4/10/2018.
 */

public class BinhLuanAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<BinhLuan> binhLuanArrayList;


    public BinhLuanAdapter(Context context, int layout, ArrayList<BinhLuan> binhLuanArrayList) {
        this.context = context;
        this.layout = layout;
        this.binhLuanArrayList = binhLuanArrayList;
    }

    @Override
    public int getCount() {
        return binhLuanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.row_coment,null);
            viewHolder.txtTaiKhoan = convertView.findViewById(R.id.txt_taikhoan);
            viewHolder.txtBinhLuan = convertView.findViewById(R.id.txt_binhluan);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BinhLuan binhLuan = binhLuanArrayList.get(position);
        viewHolder.txtTaiKhoan.setText(binhLuan.getTaikhoan());
        viewHolder.txtBinhLuan.setText(binhLuan.getBinhluan());
        return convertView;
    }

    private class ViewHolder{
        TextView txtTaiKhoan;
        TextView txtBinhLuan;
    }
}
