package com.unila.inkubis.Adapter;

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

import com.unila.inkubis.Model.Informasi;
import com.unila.inkubis.R;

public class ItemInformasiAdapter extends ArrayAdapter<Informasi> {

    private ArrayList<Informasi> daftarInformasi;
    private Context context;
    String linkFoto;

    public static class ViewHolder{
        ImageView ivFoto;
        TextView tvJudul, tvIsi;
    }

    public ItemInformasiAdapter(ArrayList<Informasi> daftarInformasi, Context context){
        super(context, R.layout.item_informasi, daftarInformasi);
        this.daftarInformasi = daftarInformasi;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Informasi berita = getItem(position);

        final ItemInformasiAdapter.ViewHolder viewHolder;

        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_informasi,parent,false);
            viewHolder.ivFoto = (ImageView) convertView.findViewById(R.id.iv_foto);
            viewHolder.tvJudul = (TextView) convertView.findViewById(R.id.tv_judul);
            viewHolder.tvIsi = (TextView) convertView.findViewById(R.id.tv_isi);

            result = convertView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        linkFoto = berita.getFoto();
        Picasso.get()
                .load(linkFoto)
                .fit()
                .centerCrop()
                .into(viewHolder.ivFoto);
        viewHolder.tvJudul.setText(berita.getJudul());
        viewHolder.tvIsi.setText(berita.getIsi());

        return result;
    }
}
