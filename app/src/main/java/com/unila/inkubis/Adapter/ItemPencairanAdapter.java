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

import com.unila.inkubis.Model.Pencairan;
import com.unila.inkubis.R;

public class ItemPencairanAdapter extends ArrayAdapter<Pencairan> {

    private ArrayList<Pencairan> daftarPencairan;
    private Context context;

    public static class ViewHolder{
        TextView tvTanggal, tvStatusCair, tvStatusBelum, tvJumlahPencairan;
    }

    public ItemPencairanAdapter(ArrayList<Pencairan> daftarPencairan, Context context){
        super(context, R.layout.item_pencairan, daftarPencairan);
        this.daftarPencairan = daftarPencairan;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Pencairan pencairan = getItem(position);

        ItemPencairanAdapter.ViewHolder viewHolder;

        final View result;

        if(convertView == null){
            viewHolder = new ItemPencairanAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_pencairan,parent,false);

            viewHolder.tvTanggal = (TextView) convertView.findViewById(R.id.tv_tanggal);
            viewHolder.tvStatusBelum = (TextView) convertView.findViewById(R.id.tv_status_belum);
            viewHolder.tvStatusCair = (TextView) convertView.findViewById(R.id.tv_status_cair);
            viewHolder.tvJumlahPencairan = (TextView) convertView.findViewById(R.id.tv_jumlah_pencairan);


            result = convertView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ItemPencairanAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString1 = formatter.format(Integer.parseInt(pencairan.getSaldo()));
        yourFormattedString1 = yourFormattedString1.replace(",", ".");

        viewHolder.tvTanggal.setText(pencairan.getTanggal());
        viewHolder.tvJumlahPencairan.setText("Rp. " + yourFormattedString1);

        if(pencairan.getStatus().equalsIgnoreCase("sudah cair")){
            viewHolder.tvStatusCair.setVisibility(View.VISIBLE);
            viewHolder.tvStatusBelum.setVisibility(View.GONE);
        }else{
            viewHolder.tvStatusBelum.setVisibility(View.VISIBLE);
            viewHolder.tvStatusCair.setVisibility(View.GONE);
        }

        return result;
    }
}
