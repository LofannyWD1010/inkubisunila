package com.unila.inkubis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import java.util.concurrent.TimeUnit;

import com.unila.inkubis.Adapter.ItemNotifikasiAdapter;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.R;
import com.unila.inkubis.Model.Notifikasi;

public class ListNotifikasiActivity extends AppCompatActivity {

    ListView lvNotifikasi;
    ArrayList<Notifikasi> daftarNotifikasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notifikasi);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daftar Notifikasi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvNotifikasi = (ListView) findViewById(R.id.lv_notifikasi);
        daftarNotifikasi = new ArrayList<>();

        ambilNotifikasi();
        lvNotifikasi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.ubahStatusNotifikasi,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
//                                    Toast.makeText(ListNotifikasiActivity.this, obj.getString("success"), Toast.LENGTH_LONG).show();

                                    if(obj.getString("success").equalsIgnoreCase("Berhasil Ubah Status Notifikasi")){
                                        Intent intent = new Intent(ListNotifikasiActivity.this, DetailPesananActivity.class);
                                        intent.putExtra("ID_PESANAN", daftarNotifikasi.get(i).getKodePesanan());
                                        intent.putExtra("ID_PRODUK", daftarNotifikasi.get(i).getIdProduk());
                                        startActivity(intent);
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
                        }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(ListNotifikasiActivity.this).getPengguna().getRememberToken());
                        params.put("Accept", "application/json");

                        return params;
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(ListNotifikasiActivity.this).getPengguna().getIdPengguna()));
                        params.put("id_pesanan", daftarNotifikasi.get(i).getKodePesanan());
                        params.put("id_produk", String.valueOf(daftarNotifikasi.get(i).getIdProduk()));

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(ListNotifikasiActivity.this);
                requestQueue.add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(
                        (int) TimeUnit.SECONDS.toMillis(5),
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            }
        });
    }

    public void ambilNotifikasi(){
        StringRequest stringRequest = new StringRequest(BuildConfig.host + BuildConfig.daftarListNotifikasi + SharedPrefManager.getInstance(ListNotifikasiActivity.this).getPengguna().getIdPengguna(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            JSONArray pencairan = obj.getJSONArray("success");

//                            Toast.makeText(getContext(), events.getString(0), Toast.LENGTH_SHORT).show();

                            for(int i = 0 ; i < pencairan.length() ; i ++){
                                JSONObject notifikasi = pencairan.getJSONObject(i);
                                daftarNotifikasi.add(new Notifikasi(
                                        notifikasi.getString("id_pesanan"),
                                        Integer.parseInt(notifikasi.getString("id_produk")),
                                        notifikasi.getString("isi"),
                                        notifikasi.getString("tanggal")
                                ));
                            }

                            ItemNotifikasiAdapter adapter = new ItemNotifikasiAdapter(daftarNotifikasi, ListNotifikasiActivity.this);
                            lvNotifikasi.setAdapter(adapter);
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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(ListNotifikasiActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ListNotifikasiActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }
}
