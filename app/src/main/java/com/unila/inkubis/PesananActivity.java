package com.unila.inkubis;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
import com.unila.inkubis.R;
import com.unila.inkubis.Model.ItemPesanan;

public class PesananActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<com.unila.inkubis.Model.ItemPesanan> daftarPesanan;
    String link;
    SwipeRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("JENIS"));

        listView = (ListView)findViewById(R.id.lv_pesanan);

        refresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);

        daftarPesanan = new ArrayList<>();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ambilPesanan();
            }
        });

        if(intent.getStringExtra("JENIS").equalsIgnoreCase("Belum Dibayar")){
            link = BuildConfig.host + BuildConfig.daftarPesananBelum;
        }else if(intent.getStringExtra("JENIS").equalsIgnoreCase("Diproses")){
            link = BuildConfig.host + BuildConfig.daftarPesananDiproses;
        }else if(intent.getStringExtra("JENIS").equalsIgnoreCase("Dikirim")){
            link = BuildConfig.host + BuildConfig.daftarPesananDikirim;
        }else if(intent.getStringExtra("JENIS").equalsIgnoreCase("Diterima")){
            link = BuildConfig.host + BuildConfig.daftarPesananDiterima;
        }

        ambilPesanan();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PesananActivity.this, DetailPesananActivity.class);
                intent.putExtra("STATUS", daftarPesanan.get(i).getStatus());
                intent.putExtra("ID_PESANAN", daftarPesanan.get(i).getIdPesanan());
                intent.putExtra("ID_PRODUK", daftarPesanan.get(i).getIdProduk());
                startActivity(intent);
            }
        });

    }

    public void ambilPesanan(){
        daftarPesanan.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        refresh.setRefreshing(false);
                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray pesanan = obj.getJSONArray("success");

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

                                daftarPesanan.add(new com.unila.inkubis.Model.ItemPesanan(
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

                            ItemPesananAdapter adapter =  new ItemPesananAdapter(daftarPesanan,PesananActivity.this);
                            listView.setAdapter(adapter);

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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(PesananActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(PesananActivity.this).getPengguna().getIdPengguna()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PesananActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
