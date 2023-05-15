package com.unila.inkubis.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ItemPakanAdapter extends RecyclerView.Adapter<ItemPakanAdapter.ViewHolder> {
    private ArrayList<Pakan> daftarItem;
    private Context context;
    private LayoutInflater mInflater;

    public ItemPakanAdapter(Context context, ArrayList<Pakan> daftarItem){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.daftarItem = daftarItem;
    }

    @Override
    public ItemPakanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_pakan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemPakanAdapter.ViewHolder holder, int position) {

        final Pakan itemPakan = daftarItem.get(position);

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

//        holder.btnBeli.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(context, itemPakan.getNamaPangan(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context, DetailPanganActivity.class);
//                intent.putExtra("PANGAN_NAMA", itemPakan.getNamaPangan());
//                intent.putExtra("PANGAN_FOTO", itemPakan.getFotoPangan());
//                intent.putExtra("PANGAN_LOKASI", itemPakan.getLokasiPangan());
//                intent.putExtra("PANGAN_HARGA", itemPakan.getHargaPangan());
//                intent.putExtra("PANGAN_FOTO_PENJUAL", itemPakan.getFotoPetani());
//                intent.putExtra("PANGAN_NAMA_PENJUAL", itemPakan.getNamaPetani());
//                intent.putExtra("PANGAN_ID", itemPakan.getIdPangan());
//                intent.putExtra("PANGAN_DESKRIPSI", itemPakan.getDeskripsiPangan());
//                intent.putExtra("PANGAN_LAT", itemPakan.getLatPangan());
//                intent.putExtra("PANGAN_LNG", itemPakan.getLngPangan());
//                intent.putExtra("PANGAN_ID_PENJUAL", itemPakan.getIdPetani());
//                intent.putExtra("PANGAN_ESTIMASI", itemPakan.getEstimasiPanen());
//                intent.putExtra("PANGAN_TELP_PENJUAL", itemPakan.getTelpPetani());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return daftarItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivFotoPakan, ivLoading;
        TextView tvNamaPakan, tvTipePakan, tvHargaPakan;
        Button btnBeli;


        public ViewHolder(View itemView) {
            super(itemView);
            ivFotoPakan = (ImageView) itemView.findViewById(R.id.iv_gambar_pakan);
            tvNamaPakan = (TextView) itemView.findViewById(R.id.tv_nama_pakan);
            tvTipePakan = (TextView) itemView.findViewById(R.id.tv_tipe_pakan);
            tvHargaPakan = (TextView) itemView.findViewById(R.id.tv_harga_pakan);
            btnBeli = (Button) itemView.findViewById(R.id.btn_pesan);
            ivLoading = (ImageView) itemView.findViewById(R.id.iv_loading);
        }
    }
}