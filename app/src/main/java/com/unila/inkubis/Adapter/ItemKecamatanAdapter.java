package com.unila.inkubis.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.Model.Kecamatan;
import com.unila.inkubis.R;
import com.unila.inkubis.SharedPrefManager;

public class ItemKecamatanAdapter extends ArrayAdapter<Kecamatan> {

    private ArrayList<Kecamatan> daftarKecamatan;
    private Context context;

    public static class ViewHolder{
        TextView tvNama, tvOngkir;
    }

    public ItemKecamatanAdapter(ArrayList<Kecamatan> daftarKecamatan, Context context){
        super(context, R.layout.item_kecamatan, daftarKecamatan);
        this.daftarKecamatan = daftarKecamatan;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Kecamatan kecamatan = getItem(position);

        final ItemKecamatanAdapter.ViewHolder viewHolder;

        final View result;

        if(convertView == null){
            viewHolder = new ItemKecamatanAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_kecamatan,parent,false);

            viewHolder.tvNama = (TextView) convertView.findViewById(R.id.tv_nama);
            viewHolder.tvOngkir = (TextView) convertView.findViewById(R.id.tv_ongkir);

            result = convertView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ItemKecamatanAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }


        viewHolder.tvNama.setText(kecamatan.getNamaKecamatan());



        if(kecamatan.getOngkir() == -1){
            StringRequest stringRequest = new StringRequest(BuildConfig.host + BuildConfig.tarifOngkirKecamatan + SharedPrefManager.getInstance(context).getPengguna().getIdPengguna() + "/" + kecamatan.getIdKecamatan(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject obj = new JSONObject(response);

                                if(obj.getString("success").equalsIgnoreCase("Tarif Belum Diatur")){
                                    viewHolder.tvOngkir.setText(obj.getString("success"));
                                    kecamatan.setOngkir(-2);
                                }else{
                                    JSONObject ongkir = obj.getJSONObject("success");

                                    if(Integer.parseInt(ongkir.getString("ongkir")) == 0){
                                        viewHolder.tvOngkir.setText("Gratis !");
                                    }else{
                                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                                        String yourFormattedString = formatter.format(Integer.parseInt(ongkir.getString("ongkir")));
                                        yourFormattedString = yourFormattedString.replace("," , ".");
                                        viewHolder.tvOngkir.setText("Rp. " + yourFormattedString);
                                    }


                                    kecamatan.setOngkir(Integer.parseInt(ongkir.getString("ongkir")));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(context).getPengguna().getRememberToken());
                    params.put("Accept", "application/json");

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }else{
            if(kecamatan.getOngkir() == -2){
                viewHolder.tvOngkir.setText("Tarif Belum Diatur");
            }else if(kecamatan.getOngkir() == 0){
                viewHolder.tvOngkir.setText("Gratis !");
            }else{
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                String yourFormattedString = formatter.format(kecamatan.getOngkir());
                yourFormattedString = yourFormattedString.replace("," , ".");
                viewHolder.tvOngkir.setText("Rp. " + yourFormattedString);
            }
        }



        return result;
    }
}
