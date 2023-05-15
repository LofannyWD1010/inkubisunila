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

import com.unila.inkubis.Model.PakanBeli;
import com.unila.inkubis.R;

public class DaftarBeliAdapter extends ArrayAdapter<PakanBeli> {

    private Context context;
    private ArrayList<PakanBeli> daftarBeli;

    public class ViewHolder{
        TextView tvNama, tvProdusen, tvBerat;
    }

    public DaftarBeliAdapter(ArrayList<PakanBeli> daftarBeli, Context context){
        super(context, R.layout.item_pakan_lokasi, daftarBeli);
        this.daftarBeli = daftarBeli;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final PakanBeli panganBeli = getItem(position);

        final DaftarBeliAdapter.ViewHolder viewHolder;

        final View result;

        if(convertView == null){
            viewHolder = new DaftarBeliAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_pakan_lokasi,parent,false);

            viewHolder.tvNama = (TextView) convertView.findViewById(R.id.tv_nama_pakan);
            viewHolder.tvProdusen = (TextView) convertView.findViewById(R.id.tv_nama_produsen);
            viewHolder.tvBerat = (TextView) convertView.findViewById(R.id.tv_berat);

            result = convertView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (DaftarBeliAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.tvNama.setText(panganBeli.getNama());
        viewHolder.tvProdusen.setText(panganBeli.getProdusen());

        if (panganBeli.getBerat().equalsIgnoreCase("")){
            viewHolder.tvBerat.setText(panganBeli.getBerat());
        }else{
            viewHolder.tvBerat.setText(panganBeli.getBerat() + " Kg");
        }

        return result;
    }


}
