package com.unila.inkubis.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.unila.inkubis.DetailActivity;
import com.unila.inkubis.Model.Pakan;
import com.unila.inkubis.R;

public class GridLainnyaAdapter extends ArrayAdapter<Pakan> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Pakan> data = new ArrayList<>();


    public GridLainnyaAdapter(Context context, int layoutResourceId, ArrayList<Pakan> data) {
        super(context, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        final GridLainnyaAdapter.ViewHolder holder;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new GridLainnyaAdapter.ViewHolder();
            holder.ivFotoPakan = (ImageView) row.findViewById(R.id.iv_gambar_pakan);
            holder.tvNamaPakan = (TextView) row.findViewById(R.id.tv_nama_pakan);
            holder.tvTipePakan = (TextView) row.findViewById(R.id.tv_tipe_pakan);
            holder.tvHargaPakan = (TextView) row.findViewById(R.id.tv_harga_pakan);
            holder.btnBeli = (Button) row.findViewById(R.id.btn_pesan);
            holder.ivLoading =(ImageView) row.findViewById(R.id.iv_loading);
            row.setTag(holder);
        }else{
            holder = (GridLainnyaAdapter.ViewHolder) row.getTag();
        }

        final Pakan itemPakan = getItem(position);

        Picasso.get()
                .load("http://inkubator.sikubis.com/uploads/file/"+itemPakan.getFoto())
                .resize(1366,768)
                .centerCrop()
                .into(holder.ivFotoPakan, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.ivLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

        holder.tvTipePakan.setText(itemPakan.getKategori());
        holder.tvNamaPakan.setText(itemPakan.getNama());
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(itemPakan.getHarga());
        yourFormattedString = yourFormattedString.replace(",", ".");
        holder.tvHargaPakan.setText("Rp. " + yourFormattedString + " / " + itemPakan.getSatuan());

        holder.btnBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("ID", itemPakan.getId());
                intent.putExtra("NAMA", itemPakan.getNama());
                intent.putExtra("HARGA", itemPakan.getHarga());
                intent.putExtra("SATUAN", itemPakan.getSatuan());
                intent.putExtra("STATUS", itemPakan.getStatus());
                intent.putExtra("KATEGORI", itemPakan.getKategori());
                intent.putExtra("LOKASI", itemPakan.getLokasi());
                intent.putExtra("DESKRIPSI", itemPakan.getDeskripsi());
                intent.putExtra("FOTO", itemPakan.getFoto());
                intent.putExtra("FOTO2", itemPakan.getFoto2());
                intent.putExtra("FOTO3", itemPakan.getFoto3());
                intent.putExtra("ID_PENJUAL", itemPakan.getIdPengguna());
                intent.putExtra("NAMA_PENJUAL", itemPakan.getNamaPenjual());
                intent.putExtra("FOTO_PENJUAL", itemPakan.getFotoPenjual());
                intent.putExtra("NO_TELP", itemPakan.getNoTelpPenjual());
                intent.putExtra("MINIMUM", itemPakan.getMinimum());
                intent.putExtra("STOK", itemPakan.getStok());
                intent.putExtra("BERAT", itemPakan.getBerat());
                intent.putExtra("DAERAH_ID", itemPakan.getIdDaerah());
                context.startActivity(intent);
            }
        });

        holder.ivFotoPakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("ID", itemPakan.getId());
                intent.putExtra("NAMA", itemPakan.getNama());
                intent.putExtra("HARGA", itemPakan.getHarga());
                intent.putExtra("SATUAN", itemPakan.getSatuan());
                intent.putExtra("STATUS", itemPakan.getStatus());
                intent.putExtra("KATEGORI", itemPakan.getKategori());
                intent.putExtra("LOKASI", itemPakan.getLokasi());
                intent.putExtra("DESKRIPSI", itemPakan.getDeskripsi());
                intent.putExtra("FOTO", itemPakan.getFoto());
                intent.putExtra("FOTO2", itemPakan.getFoto2());
                intent.putExtra("FOTO3", itemPakan.getFoto3());
                intent.putExtra("ID_PENJUAL", itemPakan.getIdPengguna());
                intent.putExtra("NAMA_PENJUAL", itemPakan.getNamaPenjual());
                intent.putExtra("FOTO_PENJUAL", itemPakan.getFotoPenjual());
                intent.putExtra("NO_TELP", itemPakan.getNoTelpPenjual());
                intent.putExtra("MINIMUM", itemPakan.getMinimum());
                intent.putExtra("STOK", itemPakan.getStok());
                intent.putExtra("BERAT", itemPakan.getBerat());
                intent.putExtra("DAERAH_ID", itemPakan.getIdDaerah());
                context.startActivity(intent);
            }
        });

        holder.tvNamaPakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("ID", itemPakan.getId());
                intent.putExtra("NAMA", itemPakan.getNama());
                intent.putExtra("HARGA", itemPakan.getHarga());
                intent.putExtra("SATUAN", itemPakan.getSatuan());
                intent.putExtra("STATUS", itemPakan.getStatus());
                intent.putExtra("KATEGORI", itemPakan.getKategori());
                intent.putExtra("LOKASI", itemPakan.getLokasi());
                intent.putExtra("DESKRIPSI", itemPakan.getDeskripsi());
                intent.putExtra("FOTO", itemPakan.getFoto());
                intent.putExtra("FOTO2", itemPakan.getFoto2());
                intent.putExtra("FOTO3", itemPakan.getFoto3());
                intent.putExtra("ID_PENJUAL", itemPakan.getIdPengguna());
                intent.putExtra("NAMA_PENJUAL", itemPakan.getNamaPenjual());
                intent.putExtra("FOTO_PENJUAL", itemPakan.getFotoPenjual());
                intent.putExtra("NO_TELP", itemPakan.getNoTelpPenjual());
                intent.putExtra("MINIMUM", itemPakan.getMinimum());
                intent.putExtra("STOK", itemPakan.getStok());
                intent.putExtra("BERAT", itemPakan.getBerat());
                intent.putExtra("DAERAH_ID", itemPakan.getIdDaerah());
                context.startActivity(intent);
            }
        });

        holder.tvHargaPakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("ID", itemPakan.getId());
                intent.putExtra("NAMA", itemPakan.getNama());
                intent.putExtra("HARGA", itemPakan.getHarga());
                intent.putExtra("SATUAN", itemPakan.getSatuan());
                intent.putExtra("STATUS", itemPakan.getStatus());
                intent.putExtra("KATEGORI", itemPakan.getKategori());
                intent.putExtra("LOKASI", itemPakan.getLokasi());
                intent.putExtra("DESKRIPSI", itemPakan.getDeskripsi());
                intent.putExtra("FOTO", itemPakan.getFoto());
                intent.putExtra("FOTO2", itemPakan.getFoto2());
                intent.putExtra("FOTO3", itemPakan.getFoto3());
                intent.putExtra("ID_PENJUAL", itemPakan.getIdPengguna());
                intent.putExtra("NAMA_PENJUAL", itemPakan.getNamaPenjual());
                intent.putExtra("FOTO_PENJUAL", itemPakan.getFotoPenjual());
                intent.putExtra("NO_TELP", itemPakan.getNoTelpPenjual());
                intent.putExtra("MINIMUM", itemPakan.getMinimum());
                intent.putExtra("STOK", itemPakan.getStok());
                intent.putExtra("BERAT", itemPakan.getBerat());
                intent.putExtra("DAERAH_ID", itemPakan.getIdDaerah());
                context.startActivity(intent);
            }
        });

        return row;
    }

    static class ViewHolder {
        ImageView ivFotoPakan, ivLoading;
        TextView tvNamaPakan, tvTipePakan, tvHargaPakan;
        Button btnBeli;
    }
}