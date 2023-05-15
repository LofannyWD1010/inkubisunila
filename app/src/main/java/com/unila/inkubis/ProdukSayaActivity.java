package com.unila.inkubis;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
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

import com.unila.inkubis.Adapter.GridProdukSayaAdapter;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.Model.Pakan;
import com.unila.inkubis.R;

public class ProdukSayaActivity extends AppCompatActivity {

    GridView gvPakan;
    ArrayList<Pakan> daftarPakan;
    RelativeLayout rlKosong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk_saya);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Produk Saya");

        gvPakan = (GridView) findViewById(R.id.gv_pakan);
        rlKosong = (RelativeLayout) findViewById(R.id.kosong);

        daftarPakan = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.daftarProdukPenjual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray barang = obj.getJSONArray("success");

                            if(barang.length() == 0){
                                gvPakan.setVisibility(View.GONE);
                                rlKosong.setVisibility(View.VISIBLE);
                            }

//                            Toast.makeText(getContext(), events.getString(0), Toast.LENGTH_SHORT).show();

                            for(int i = 0 ; i < barang.length() ; i ++){
                                JSONObject product = barang.getJSONObject(i);

                                String foto2, foto3;

                                if(product.isNull("foto2")){
                                    foto2 = "kosong";
                                }else{
                                    foto2 = product.getString("foto2");
                                }

                                if(product.isNull("foto3")){
                                    foto3 = "kosong";
                                }else{
                                    foto3 = product.getString("foto3");
                                }

                                daftarPakan.add(new Pakan(
                                        product.getInt("id"),
                                        Integer.parseInt(product.getString("id_pengguna")),
                                        Integer.parseInt(product.getString("harga")),
                                        product.getString("nama"),
                                        product.getString("satuan"),
                                        product.getString("status"),
                                        product.getString("kategori"),
                                        product.getString("lokasi"),
                                        product.getString("foto"),
                                        foto2,
                                        foto3,
                                        product.getString("deskripsi"),
                                        product.getString("nama_penjual"),
                                        product.getString("foto_penjual"),
                                        product.getString("no_telp"),
                                        Integer.parseInt(product.getString("iklan")),
                                        Integer.parseInt(product.getString("minimum")),
                                        Integer.parseInt(product.getString("stok")),
                                        Integer.parseInt(product.getString("berat")),
                                        Integer.parseInt(product.getString("daerah_id"))
                                ));
                            }

                            GridProdukSayaAdapter adapter = new GridProdukSayaAdapter(ProdukSayaActivity.this, R.layout.item_grid_saya, daftarPakan);
                            gvPakan.setAdapter(adapter);

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(ProdukSayaActivity.this).getPengguna().getIdPengguna()));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProdukSayaActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                return true;
            case R.id.tambah_produk :
                startActivityForResult(new Intent(ProdukSayaActivity.this, TambahProdukActivity.class),1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if (resultCode == Activity.RESULT_CANCELED) {
                daftarPakan.clear();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.daftarProdukPenjual,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);

                                    JSONArray barang = obj.getJSONArray("success");

//                            Toast.makeText(getContext(), events.getString(0), Toast.LENGTH_SHORT).show();

                                    for(int i = 0 ; i < barang.length() ; i ++){
                                        JSONObject product = barang.getJSONObject(i);

                                        String foto2, foto3;

                                        if(product.isNull("foto2")){
                                            foto2 = "kosong";
                                        }else{
                                            foto2 = product.getString("foto2");
                                        }

                                        if(product.isNull("foto3")){
                                            foto3 = "kosong";
                                        }else{
                                            foto3 = product.getString("foto3");
                                        }

                                        daftarPakan.add(new Pakan(
                                                product.getInt("id"),
                                                Integer.parseInt(product.getString("id_pengguna")),
                                                Integer.parseInt(product.getString("harga")),
                                                product.getString("nama"),
                                                product.getString("satuan"),
                                                product.getString("status"),
                                                product.getString("kategori"),
                                                product.getString("lokasi"),
                                                product.getString("foto"),
                                                foto2,
                                                foto3,
                                                product.getString("deskripsi"),
                                                product.getString("nama_penjual"),
                                                product.getString("foto_penjual"),
                                                product.getString("no_telp"),
                                                Integer.parseInt(product.getString("iklan")),
                                                Integer.parseInt(product.getString("minimum")),
                                                Integer.parseInt(product.getString("stok")),
                                                Integer.parseInt(product.getString("berat")),
                                                Integer.parseInt(product.getString("daerah_id"))
                                        ));
                                    }

                                    GridProdukSayaAdapter adapter = new GridProdukSayaAdapter(ProdukSayaActivity.this, R.layout.item_grid_saya, daftarPakan);
                                    gvPakan.setAdapter(adapter);

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
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(ProdukSayaActivity.this).getPengguna().getIdPengguna()));

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(ProdukSayaActivity.this);
                requestQueue.add(stringRequest);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profil, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
