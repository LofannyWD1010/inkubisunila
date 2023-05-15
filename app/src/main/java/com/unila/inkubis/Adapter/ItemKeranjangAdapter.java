package com.unila.inkubis.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.Customable.RepeatListener;
import com.unila.inkubis.DetailActivity;
import com.unila.inkubis.KeranjangActivity;
import com.unila.inkubis.Model.PakanKeranjang;
import com.unila.inkubis.R;
import com.unila.inkubis.SharedPrefManager;

public class ItemKeranjangAdapter extends ArrayAdapter<PakanKeranjang> {

private Context context;
private ArrayList<PakanKeranjang> daftarItemPakan;
private int jumlah, harga, totalHarga = 0;

public ItemKeranjangAdapter(ArrayList<PakanKeranjang> daftarItemPakan, Context context){
        super(context, R.layout.item_keranjang, daftarItemPakan);
        this.daftarItemPakan = daftarItemPakan;
        this.context = context;
}

@NonNull
@Override
public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
final PakanKeranjang itemPakan = getItem(position);


final ItemKeranjangAdapter.ViewHolder viewHolder;

final View result;

        if(convertView == null){
        viewHolder = new ItemKeranjangAdapter.ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.item_keranjang,parent,false);
        viewHolder.ivPakan = (ImageView) convertView.findViewById(R.id.iv_gambar_pakan);
        viewHolder.tvNamaPakan = (TextView) convertView.findViewById(R.id.tv_nama_pakan);
        viewHolder.tvProdusen = (TextView) convertView.findViewById(R.id.tv_nama_produsen);
        viewHolder.tvDetail = (TextView) convertView.findViewById(R.id.tv_detail);
        viewHolder.btnKurang = (Button) convertView.findViewById(R.id.btn_kurang);
        viewHolder.btnTambah = (Button) convertView.findViewById(R.id.btn_tambah);
        viewHolder.tvJumlah = (TextView) convertView.findViewById(R.id.tv_jumlah);
        result = convertView;
        convertView.setTag(viewHolder);
        }else{
        viewHolder = (ItemKeranjangAdapter.ViewHolder) convertView.getTag();
        result = convertView;
        }

        Picasso.get()
        .load("http://inkubator.sikubis.com/uploads/file/"+itemPakan.getFoto())
        .into(viewHolder.ivPakan);

        viewHolder.tvNamaPakan.setText(itemPakan.getNama());

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(itemPakan.getHarga());
        yourFormattedString = yourFormattedString.replace(",", ".");
        viewHolder.tvDetail.setText("Rp. " + yourFormattedString + " / " + itemPakan.getSatuan());
        viewHolder.tvProdusen.setText(itemPakan.getJenis());

        viewHolder.tvJumlah.setText(String.valueOf(itemPakan.getJumlah()));

        harga = itemPakan.getHarga();

        viewHolder.btnTambah.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                int jumlah = itemPakan.getJumlah() + 1;
                itemPakan.setJumlah(jumlah);
                harga = harga*jumlah;
                totalHarga = itemPakan.getHarga()*jumlah;

                viewHolder.tvJumlah.setText(String.valueOf(jumlah));
                if(context.getClass().getSimpleName().equalsIgnoreCase("KeranjangActivity")){
                        ((KeranjangActivity)context).hitungTotalHarga();
                }else if(context.getClass().getSimpleName().equalsIgnoreCase("DetailActivity")){
                        ((DetailActivity)context).hitungTotalHarga();
                }
        }
        });

        viewHolder.btnTambah.setOnTouchListener(new RepeatListener(800, 200, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        int jumlah = itemPakan.getJumlah() + 1;
                        itemPakan.setJumlah(jumlah);
                        harga = harga*jumlah;
                        totalHarga = itemPakan.getHarga()*jumlah;

                        viewHolder.tvJumlah.setText(String.valueOf(jumlah));
                        if(context.getClass().getSimpleName().equalsIgnoreCase("KeranjangActivity")){
                                ((KeranjangActivity)context).hitungTotalHarga();
                        }else if(context.getClass().getSimpleName().equalsIgnoreCase("DetailActivity")){
                                ((DetailActivity)context).hitungTotalHarga();
                        }
                }
        }));

        viewHolder.btnKurang.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                int jumlah = itemPakan.getJumlah() - 1;
                itemPakan.setJumlah(jumlah);
                harga = harga*jumlah;
                totalHarga = itemPakan.getHarga()*jumlah;

                Log.d("HAHA", context.getClass().getSimpleName());

                viewHolder.tvJumlah.setText(String.valueOf(jumlah));

                if(context.getClass().getSimpleName().equalsIgnoreCase("KeranjangActivity")){
                        ((KeranjangActivity)context).hitungTotalHarga();
                }else if(context.getClass().getSimpleName().equalsIgnoreCase("DetailActivity")){
                        ((DetailActivity)context).hitungTotalHarga();
                }

                if(jumlah<1){
                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Proses Hapus...");
                        progressDialog.show();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.hapusKeranjang,
                                new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                                progressDialog.dismiss();
                                                try {
                                                        JSONObject obj = new JSONObject(response);
                                                        Toast.makeText(getContext(), obj.getString("success"), Toast.LENGTH_LONG).show();

                                                        if(obj.getString("success").equalsIgnoreCase("Berhasil Hapus Produk")){
                                                                daftarItemPakan.remove(position);
                                                                notifyDataSetChanged();
                                                        }

                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                }
                                        }
                                },
                                new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                }) {

                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                        Map<String, String>  params = new HashMap<String, String>();
                                        params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(context).getPengguna().getRememberToken());
                                        params.put("Accept", "application/json");

                                        return params;
                                }

                                @Override
                                protected Map<String, String> getParams(){
                                        Map<String, String> params = new HashMap<>();
                                        params.put("id_keranjang", String.valueOf(itemPakan.getIdKeranjang()));

                                        return params;
                                }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        requestQueue.add(stringRequest);

                }
        }
        });

        viewHolder.btnKurang.setOnTouchListener(new RepeatListener(800, 200, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        int jumlah = itemPakan.getJumlah() - 1;
                        itemPakan.setJumlah(jumlah);
                        harga = harga*jumlah;
                        totalHarga = itemPakan.getHarga()*jumlah;



                        viewHolder.tvJumlah.setText(String.valueOf(jumlah));

                        if(context.getClass().getSimpleName().equalsIgnoreCase("KeranjangActivity")){
                                ((KeranjangActivity)context).hitungTotalHarga();
                        }else if(context.getClass().getSimpleName().equalsIgnoreCase("DetailActivity")){
                                ((DetailActivity)context).hitungTotalHarga();
                        }

                        if(jumlah<1){
                                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("Proses Hapus...");
                                progressDialog.show();

                                StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.hapusKeranjang,
                                        new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                        progressDialog.dismiss();
                                                        try {
                                                                JSONObject obj = new JSONObject(response);
                                                                Toast.makeText(getContext(), obj.getString("success"), Toast.LENGTH_LONG).show();

                                                                if(obj.getString("success").equalsIgnoreCase("Berhasil Hapus Produk")){
                                                                        daftarItemPakan.remove(position);
                                                                        notifyDataSetChanged();
                                                                }

                                                        } catch (JSONException e) {
                                                                e.printStackTrace();
                                                        }
                                                }
                                        },
                                        new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                        }) {

                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                                Map<String, String>  params = new HashMap<String, String>();
                                                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(context).getPengguna().getRememberToken());
                                                params.put("Accept", "application/json");

                                                return params;
                                        }
                                        @Override
                                        protected Map<String, String> getParams(){
                                                Map<String, String> params = new HashMap<>();
                                                params.put("id_keranjang", String.valueOf(itemPakan.getIdKeranjang()));

                                                return params;
                                        }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                requestQueue.add(stringRequest);

                        }
                }
        }));

        return result;
        }

        public class ViewHolder{
            TextView tvNamaPakan, tvDetail, tvProdusen, tvJumlah;
            ImageView ivPakan;
            Button btnKurang, btnTambah;
        }
}