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

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Minh on 3/22/2018.
 */

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ItemHolder> {
    Context context;
    ArrayList<SanPham> sanPhamArrayList;

    public SanPhamAdapter(Context context, ArrayList<SanPham> sanPhamArrayList) {
        this.context = context;
        this.sanPhamArrayList = sanPhamArrayList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_new_product,null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        SanPham sanPham = sanPhamArrayList.get(position);
        holder.txtTenSP.setText(sanPham.getTenhang());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGiaSP.setText("Giá : "+decimalFormat.format(sanPham.getDongiaban())+"Đ");
        Picasso.with(context).load(sanPham.getAnh()).placeholder(R.drawable.load)
                .error(R.drawable.error)
                .into(holder.imgSP);
    }



    @Override
    public int getItemCount() {
        return sanPhamArrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ImageView imgSP;
        public TextView txtTenSP,txtGiaSP;

        public ItemHolder(View itemView) {
            super(itemView);
            imgSP = (ImageView) itemView.findViewById(R.id.imgSP);
            txtTenSP = (TextView) itemView.findViewById(R.id.txtTenSP);
            txtGiaSP = (TextView) itemView.findViewById(R.id.txtGiaSP);
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
