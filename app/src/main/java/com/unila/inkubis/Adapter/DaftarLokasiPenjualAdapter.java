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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.unila.inkubis.AturLokasiAntarActivity;
import com.unila.inkubis.Model.tempOngkir;
import com.unila.inkubis.R;

public class DaftarLokasiPenjualAdapter extends ArrayAdapter<tempOngkir> {

    Context context;
    ArrayList<tempOngkir> tempOngkir;

    DaftarLokasiPenjualAdapter.ViewHolder viewHolder;

    public class ViewHolder{
        TextView tvPenjualKe, tvOngkir, tvLokasiAntar, tvBelum;
        Button btnAturLokasi;
        LinearLayout llLokasiAntar, llOngkir;
    }

    public DaftarLokasiPenjualAdapter(ArrayList<tempOngkir> tempOngkir, Context context){
        super(context, R.layout.item_lokasi_penjual, tempOngkir);
        this.tempOngkir = tempOngkir;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final tempOngkir unique = getItem(position);



        final View result;

        if(convertView == null){
            viewHolder = new DaftarLokasiPenjualAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_lokasi_penjual,parent,false);

            viewHolder.tvPenjualKe = (TextView) convertView.findViewById(R.id.tv_penjual_ke);
            viewHolder.tvLokasiAntar = (TextView) convertView.findViewById(R.id.tv_lokasi_antar);
            viewHolder.tvOngkir = (TextView) convertView.findViewById(R.id.tv_ongkir);
            viewHolder.tvBelum = (TextView) convertView.findViewById(R.id.tv_belum);
            viewHolder.btnAturLokasi = (Button) convertView.findViewById(R.id.btn_atur_lokasi);
            viewHolder.llLokasiAntar = (LinearLayout) convertView.findViewById(R.id.ll_lokasi_antar);
            viewHolder.llOngkir = (LinearLayout) convertView.findViewById(R.id.ll_ongkir);



            result = convertView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (DaftarLokasiPenjualAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }


        if(unique.getOngkir() == -1){
            viewHolder.llOngkir.setVisibility(View.GONE);
            viewHolder.llLokasiAntar.setVisibility(View.GONE);
            viewHolder.tvBelum.setVisibility(View.VISIBLE);
        }else{
            viewHolder.llOngkir.setVisibility(View.VISIBLE);
            viewHolder.llLokasiAntar.setVisibility(View.VISIBLE);
            viewHolder.tvLokasiAntar.setText(unique.getLokasiAntar());
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String yourFormattedString = formatter.format(unique.getOngkir());
            yourFormattedString = yourFormattedString.replace(",", ".");
            viewHolder.tvOngkir.setText("Rp. " + yourFormattedString);
            viewHolder.tvBelum.setVisibility(View.GONE);
        }

        viewHolder.tvPenjualKe.setText("Lokasi Pengiriman Untuk Produk Ke-" + (position+1));
        viewHolder.btnAturLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AturLokasiAntarActivity.class);
                intent.putExtra("ID", position);
                intent.putExtra("ID_PENJUAL", unique.getIdUnik());
                intent.putExtra("DAERAH_ID", unique.getIdDaerah());
                intent.putExtra("BERAT", unique.getBerat());
                intent.putExtra("JUMLAH", unique.getJumlah());
                intent.putExtra("KURIR", unique.getIdKurir());
                ((Activity) context).startActivityForResult(intent,1);
            }
        });

        return result;
    }



}
