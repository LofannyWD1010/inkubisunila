package com.unila.inkubis.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.unila.inkubis.Model.ItemPesanan;
import com.unila.inkubis.R;
import com.unila.inkubis.SharedPrefManager;

public class ItemPesananAdapter extends ArrayAdapter<ItemPesanan> {

    private ArrayList<ItemPesanan> daftarPesanan;
    private Context context;

    public static class ViewHolder{
        TextView tvKodePesanan, tvTanggalPesan, tvTotalBayar, tvDaftarProduk, tvTotalBayarAsli, tvPembeli, tvPenjual;
    }

    public ItemPesananAdapter(ArrayList<ItemPesanan> daftarPesanan, Context context){
        super(context, R.layout.item_pesanan, daftarPesanan);
        this.daftarPesanan = daftarPesanan;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ItemPesanan pesanan = getItem(position);

        ItemPesananAdapter.ViewHolder viewHolder;

        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_pesanan,parent,false);

            viewHolder.tvKodePesanan = (TextView) convertView.findViewById(R.id.tv_kode_pesan);
            viewHolder.tvTanggalPesan = (TextView) convertView.findViewById(R.id.tv_tanggal);
            viewHolder.tvTotalBayar = (TextView) convertView.findViewById(R.id.tv_total_bayar);
            viewHolder.tvDaftarProduk = (TextView) convertView.findViewById(R.id.tv_daftar_produk);
            viewHolder.tvTotalBayarAsli = (TextView) convertView.findViewById(R.id.tv_total_bayar_asli);
            viewHolder.tvPembeli = (TextView) convertView.findViewById(R.id.tv_pembeli);
            viewHolder.tvPenjual = (TextView) convertView.findViewById(R.id.tv_penjual);

            result = convertView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        viewHolder.tvKodePesanan.setText(pesanan.getIdPesanan());
        viewHolder.tvTanggalPesan.setText(pesanan.getTanggal());
        viewHolder.tvDaftarProduk.setText(pesanan.getProduk());

        if(pesanan.getIdPenjual() == SharedPrefManager.getInstance(getContext()).getPengguna().getIdPengguna()){
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String yourFormattedString1 = formatter.format(Integer.parseInt(pesanan.getTotal_keuntungan()) - (Integer.parseInt(pesanan.getTotal_keuntungan()) * 0.05));
            yourFormattedString1 = yourFormattedString1.replace("," , ".");
            String yourFormattedString2 = formatter.format(Integer.parseInt(pesanan.getTotal_keuntungan()));
            yourFormattedString2 = yourFormattedString2.replace("," , ".");
            viewHolder.tvTotalBayar.setText("Rp. " + yourFormattedString1);
            viewHolder.tvTotalBayarAsli.setText("Rp. " + yourFormattedString2 + " - 5% (pendapatan transaksi)");
            viewHolder.tvPembeli.setVisibility(View.GONE);
        }else{
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String yourFormattedString1 = formatter.format(Integer.parseInt(pesanan.getTotal_keuntungan()));
            yourFormattedString1 = yourFormattedString1.replace("," , ".");
            viewHolder.tvTotalBayar.setText("Rp. " + yourFormattedString1);
            viewHolder.tvTotalBayarAsli.setVisibility(View.GONE);
            viewHolder.tvPenjual.setVisibility(View.GONE);
        }



        return result;
    }
}
