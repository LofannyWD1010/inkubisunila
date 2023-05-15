package com.unila.inkubis.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.unila.inkubis.Adapter.ItemPesananAdapter;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.DetailPesananActivity;
import com.unila.inkubis.Model.ItemPesanan;
import com.unila.inkubis.R;
import com.unila.inkubis.SharedPrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesananKirimFragment extends Fragment {


    ListView lvDaftarKirim;
    private ArrayList<ItemPesanan> daftarPesanan;
    SwipeRefreshLayout refresh, refresh1;
    RelativeLayout rlKosong;

    public PesananKirimFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesanan_kirim, container, false);
        lvDaftarKirim = (ListView) view.findViewById(R.id.lv_daftar_kirim);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);
        refresh1 = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh1);
        rlKosong = (RelativeLayout) view.findViewById(R.id.kosong);

        daftarPesanan = new ArrayList<>();

        ambilPesanan();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ambilPesanan();
            }
        });
        refresh1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ambilPesanan();
            }
        });

        lvDaftarKirim.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), DetailPesananActivity.class);
                intent.putExtra("STATUS", daftarPesanan.get(i).getStatus());
                intent.putExtra("ID_PESANAN", daftarPesanan.get(i).getIdPesanan());
                intent.putExtra("ID_PRODUK", daftarPesanan.get(i).getIdProduk());
                startActivity(intent);
            }
        });

        return view;
    }

    public void ambilPesanan(){
        daftarPesanan.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.daftarPesananDikirim,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        refresh.setRefreshing(false);
                        refresh1.setRefreshing(false);
                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray pesanan = obj.getJSONArray("success");

                            if(pesanan.length() == 0){
                                refresh1.setVisibility(View.VISIBLE);
                                rlKosong.setVisibility(View.VISIBLE);
                            }else{
                                refresh1.setVisibility(View.GONE);
                                rlKosong.setVisibility(View.GONE);
                            }

//                            Toast.makeText(getContext(), events.getString(0), Toast.LENGTH_SHORT).show();

                            for(int i = 0 ; i < pesanan.length() ; i ++){
                                JSONObject pesanans = pesanan.getJSONObject(i);

//                                JSONArray produks = pesanans.getJSONArray("produk");
//
//                                String produk = "";
//
//                                for(int j = 0 ; j < produks.length() ; j++){
//                                    JSONObject produkku = produks.getJSONObject(j);
//                                    if(j == 0){
//                                        produk = produk + "\t - " + produkku.getString("nama_produk") + " (" + produkku.getString("jumlah") + " " + produkku.getString("satuan") + ")";
//                                    }else{
//                                        produk = produk + "\n\t - " + produkku.getString("nama_produk") + " (" + produkku.getString("jumlah") + " " + produkku.getString("satuan") + ")";
//                                    }
//                                }

                                daftarPesanan.add(new ItemPesanan(
                                        pesanans.getInt("id_detail"),
                                        Integer.parseInt(pesanans.getString("id_penjual")),
                                        Integer.parseInt(pesanans.getString("id_pembeli")),
                                        pesanans.getString("id_pesanan"),
                                        pesanans.getString("tanggal"),
                                        pesanans.getString("nama_penjual"),
                                        pesanans.getString("telp_penjual"),
                                        pesanans.getString("foto_penjual"),
                                        pesanans.getString("nama_pembeli"),
                                        pesanans.getString("telp_pembeli"),
                                        pesanans.getString("foto_pembeli"),
                                        pesanans.getString("total_keuntungan"),
                                        pesanans.getString("status"),
                                        pesanans.getString("nama_produk") + " " + pesanans.getString("jumlah") + " " + pesanans.getString("satuan"),
                                        Integer.parseInt(pesanans.getString("id_produk"))
                                ));
                            }

                            ItemPesananAdapter adapter =  new ItemPesananAdapter(daftarPesanan,getContext());
                            lvDaftarKirim.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(getContext()).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(getContext()).getPengguna().getIdPengguna()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
