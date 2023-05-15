package com.unila.inkubis.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.unila.inkubis.Model.Kategori;
import com.unila.inkubis.R;

public class GridBerandaAdapter extends ArrayAdapter<Kategori> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Kategori> data = new ArrayList<>();

    public GridBerandaAdapter(Context context, int layoutResourceId, ArrayList<Kategori> data){
        super(context, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.foto = (ImageView) row.findViewById(R.id.iv_foto);
            holder.nama = (TextView) row.findViewById(R.id.tv_nama);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        final Kategori item = getItem(position);

        Picasso.get()
                .load("http://sigerdev.com/dipangan/api/upload/"+item.getFotoKategori())
                .into(holder.foto);


        holder.nama.setText(item.getNamakategori());

        return row;
    }

    static class ViewHolder {
        TextView nama;
        ImageView foto;
    }
}
