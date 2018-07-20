package com.example.minh.nckh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.minh.nckh.Activity.ChiTietSanPhamActivity;
import com.example.minh.nckh.Model.SanPham;
import com.example.minh.nckh.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Minh on 3/27/2018.
 */

public class SanPhamHorizontalAdapter extends RecyclerView.Adapter<SanPhamHorizontalAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SanPham> sanPhamArrayList;

    public SanPhamHorizontalAdapter(Context context, ArrayList<SanPham> sanPhamArrayList) {
        this.context = context;
        this.sanPhamArrayList = sanPhamArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycleview_horizontal,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SanPham sanPham = sanPhamArrayList.get(position);
        Picasso.with(context).load(sanPham.getAnh()).placeholder(R.drawable.load)
                .error(R.drawable.error)
                .into(holder.imageView);
        holder.txtTenSP.setText(sanPham.getTenhang());
    }

    @Override
    public int getItemCount() {
        return sanPhamArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView txtTenSP;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            txtTenSP = itemView.findViewById(R.id.txt_tensp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
                    intent.putExtra("qrcode",sanPhamArrayList.get(getPosition()).getMahang());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //CheckConnection.ShowToast_short(context,sanPhamArrayList.get(getPosition()).getTensanpham());
                    context.startActivity(intent);
                }
            });
        }
    }

}
