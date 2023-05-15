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

import com.unila.inkubis.Model.Pakan;
import com.unila.inkubis.R;
import com.unila.inkubis.UbahProdukActivity;

public class GridProdukSayaAdapter extends ArrayAdapter<Pakan> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Pakan> data = new ArrayList<>();


    public GridProdukSayaAdapter(Context context, int layoutResourceId, ArrayList<Pakan> data) {
        super(context, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        final GridProdukSayaAdapter.ViewHolder holder;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new GridProdukSayaAdapter.ViewHolder();
            holder.ivFotoPakan = (ImageView) row.findViewById(R.id.iv_gambar_pakan);
            holder.tvNamaPakan = (TextView) row.findViewById(R.id.tv_nama_pakan);
            holder.tvTipePakan = (TextView) row.findViewById(R.id.tv_tipe_pakan);
            holder.tvHargaPakan = (TextView) row.findViewById(R.id.tv_harga_pakan);
            holder.btnUbah = (Button) row.findViewById(R.id.btn_ubah);
            holder.tvTayang = (TextView) row.findViewById(R.id.tv_iklan_tayang);
            holder.tvTarik = (TextView) row.findViewById(R.id.tv_iklan_tarik);
            holder.ivLoading = (ImageView) row.findViewById(R.id.iv_loading);
            row.setTag(holder);
        }else{
            holder = (GridProdukSayaAdapter.ViewHolder) row.getTag();
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

        if(itemPakan.getIklan() == 1){
            holder.tvTarik.setVisibility(View.GONE);
        }else{
            holder.tvTayang.setVisibility(View.GONE);
        }

        holder.tvTipePakan.setText(itemPakan.getKategori());
        holder.tvNamaPakan.setText(itemPakan.getNama());
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(itemPakan.getHarga());
        yourFormattedString = yourFormattedString.replace(",", ".");
        holder.tvHargaPakan.setText("Rp. " + yourFormattedString + " / "+ itemPakan.getSatuan());

        holder.btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UbahProdukActivity.class);
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
                intent.putExtra("IKLAN", itemPakan.getIklan());
                intent.putExtra("MINIMUM", itemPakan.getMinimum());
                intent.putExtra("STOK", itemPakan.getStok());
                intent.putExtra("BERAT", itemPakan.getBerat());

                ((Activity) context).startActivityForResult(intent,1);
//                Toast.makeText(context, itemPakan.getNamaPangan(), Toast.LENGTH_SHORT).show();
//                if(SharedPrefManager.getInstance(getContext()).getPengguna().getTipePengguna().equalsIgnoreCase("produsen")){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                    builder.setMessage("Maaf, akun anda adalah akun produsen, sehingga anda tidak dapat membeli produk").setTitle("");
//                    builder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//
//                        }
//
//                    });
//
//
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                }else{
//                    Intent intent = new Intent(context, DetailPanganActivity.class);
//                    intent.putExtra("PANGAN_NAMA", itemPakan.getNamaPangan());
//                    intent.putExtra("PANGAN_FOTO", itemPakan.getFotoPangan());
//                    intent.putExtra("PANGAN_LOKASI", itemPakan.getLokasiPangan());
//                    intent.putExtra("PANGAN_HARGA", itemPakan.getHargaPangan());
//                    intent.putExtra("PANGAN_FOTO_PENJUAL", itemPakan.getFotoPetani());
//                    intent.putExtra("PANGAN_NAMA_PENJUAL", itemPakan.getNamaPetani());
//                    intent.putExtra("PANGAN_ID", itemPakan.getIdPangan());
//                    intent.putExtra("PANGAN_DESKRIPSI", itemPakan.getDeskripsiPangan());
//                    intent.putExtra("PANGAN_LAT", itemPakan.getLatPangan());
//                    intent.putExtra("PANGAN_LNG", itemPakan.getLngPangan());
//                    intent.putExtra("PANGAN_ID_PENJUAL", itemPakan.getIdPetani());
//                    intent.putExtra("PANGAN_ESTIMASI", itemPakan.getEstimasiPanen());
//                    intent.putExtra("PANGAN_TELP_PENJUAL", itemPakan.getTelpPetani());
//                    context.startActivity(intent);
//                }
            }
        });

        holder.ivFotoPakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UbahProdukActivity.class);
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
                intent.putExtra("IKLAN", itemPakan.getIklan());
                intent.putExtra("MINIMUM", itemPakan.getMinimum());
                intent.putExtra("STOK", itemPakan.getStok());
                intent.putExtra("BERAT", itemPakan.getBerat());

                ((Activity) context).startActivityForResult(intent,1);
            }
        });

        holder.tvNamaPakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UbahProdukActivity.class);
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
                intent.putExtra("IKLAN", itemPakan.getIklan());
                intent.putExtra("MINIMUM", itemPakan.getMinimum());
                intent.putExtra("STOK", itemPakan.getStok());
                intent.putExtra("BERAT", itemPakan.getBerat());

                ((Activity) context).startActivityForResult(intent,1);
            }
        });

        holder.tvHargaPakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UbahProdukActivity.class);
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
                intent.putExtra("IKLAN", itemPakan.getIklan());
                intent.putExtra("MINIMUM", itemPakan.getMinimum());
                intent.putExtra("STOK", itemPakan.getStok());
                intent.putExtra("BERAT", itemPakan.getBerat());

                ((Activity) context).startActivityForResult(intent,1);
            }
        });

        return row;
    }

    static class ViewHolder {
        ImageView ivFotoPakan, ivLoading;
        TextView tvNamaPakan, tvTipePakan, tvHargaPakan, tvTayang, tvTarik;
        Button btnUbah;
    }
}