package com.unila.inkubis;

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

import com.unila.inkubis.Model.PakanKeranjang;
import com.unila.inkubis.R;

public class ItemPakanBeliAdapter extends ArrayAdapter<PakanKeranjang> {

    private Context context;
    private ArrayList<PakanKeranjang> daftarBeli;

    public class ViewHolder{
        TextView tvNama, tvHarga, tvBerat;
    }

    public ItemPakanBeliAdapter(ArrayList<PakanKeranjang> daftarBeli, Context context){
        super(context, R.layout.item_pakan_beli, daftarBeli);
        this.daftarBeli = daftarBeli;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final PakanKeranjang panganBeli = getItem(position);

        final ItemPakanBeliAdapter.ViewHolder viewHolder;

        final View result;

        if(convertView == null){
            viewHolder = new ItemPakanBeliAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_pakan_beli,parent,false);

            viewHolder.tvNama = (TextView) convertView.findViewById(R.id.tv_nama);
            viewHolder.tvHarga = (TextView) convertView.findViewById(R.id.tv_harga);
            viewHolder.tvBerat = (TextView) convertView.findViewById(R.id.tv_berat);

            result = convertView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ItemPakanBeliAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.tvNama.setText(panganBeli.getNama());
        viewHolder.tvBerat.setText(panganBeli.getJumlah() + " " + panganBeli.getSatuan());
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(panganBeli.getHarga());
        yourFormattedString = yourFormattedString.replace(",", ".");
        viewHolder.tvHarga.setText("Rp. " + yourFormattedString + " / " + panganBeli.getSatuan());

        return result;
    }


}