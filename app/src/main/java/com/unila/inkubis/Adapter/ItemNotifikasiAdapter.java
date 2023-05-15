package com.unila.inkubis.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.unila.inkubis.Model.Notifikasi;
import com.unila.inkubis.R;

public class ItemNotifikasiAdapter extends ArrayAdapter<Notifikasi> {

    private ArrayList<Notifikasi> daftarNotifikasi;
    private Context context;

    public static class ViewHolder{
        TextView tvTanggal, tvIsi, tvKodePesanan;
    }

    public ItemNotifikasiAdapter(ArrayList<Notifikasi> daftarNotifikasi, Context context){
        super(context, R.layout.item_notifikasi, daftarNotifikasi);
        this.daftarNotifikasi = daftarNotifikasi;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Notifikasi notifikasi = getItem(position);

        ItemNotifikasiAdapter.ViewHolder viewHolder;

        final View result;

        if(convertView == null){
            viewHolder = new ItemNotifikasiAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_notifikasi,parent,false);

            viewHolder.tvTanggal = (TextView) convertView.findViewById(R.id.tv_tanggal);
            viewHolder.tvKodePesanan = (TextView) convertView.findViewById(R.id.tv_kode_pesanan);
            viewHolder.tvIsi = (TextView) convertView.findViewById(R.id.tv_isi);

            result = convertView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ItemNotifikasiAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.tvKodePesanan.setText(notifikasi.getKodePesanan());
        viewHolder.tvTanggal.setText(notifikasi.getTanggal());
        viewHolder.tvIsi.setText(notifikasi.getIsi());

        return result;
    }
}

