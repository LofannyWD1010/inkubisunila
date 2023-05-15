package com.unila.inkubis;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

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

import com.unila.inkubis.Adapter.GridLainnyaAdapter;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.Customable.ExpandableHeightGridView;
import com.unila.inkubis.Model.Pakan;
import com.unila.inkubis.R;

public class LainnyaActivity extends AppCompatActivity {

    ExpandableHeightGridView gvPakan;
    SearchView searchView;
    Handler handler;
    Button btnHalaman;
    ProgressDialog progressDialog;
    GridLainnyaAdapter adapter;
    ScrollView svAda;
    RelativeLayout rlKosong;

    boolean dariPencarian = false;

    ArrayList<Pakan> daftarPakan;
    String kategori, kataPencarian;
    int halaman = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lainnya);

        Intent intent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(intent.getStringExtra("TITLE"));

        gvPakan = (ExpandableHeightGridView) findViewById(R.id.gv_pakan);
        btnHalaman = (Button) findViewById(R.id.btn_halaman);
        rlKosong = (RelativeLayout) findViewById(R.id.kosong);
        svAda = (ScrollView) findViewById(R.id.ada);

        daftarPakan = new ArrayList<>();

        adapter = new GridLainnyaAdapter(LainnyaActivity.this, R.layout.item_grid_pakan, daftarPakan);
        gvPakan.setExpanded(true);

        if(!dariPencarian){
            kategori = "Semua Kategori";
            StringRequest stringRequest = new StringRequest(BuildConfig.host + BuildConfig.daftarProdukKategori + "Semua Kategori/" + halaman,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject obj = new JSONObject(response);

                                JSONArray barang = obj.getJSONArray("success");

                                if(barang.length() == 0){
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
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(LainnyaActivity.this);
            requestQueue.add(stringRequest);
        }else{
            kategori = intent.getStringExtra("TITLE");
            StringRequest stringRequest = new StringRequest(BuildConfig.host + BuildConfig.daftarProdukKategori + intent.getStringExtra("TITLE") + "/" + halaman,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject obj = new JSONObject(response);

                                JSONArray barang = obj.getJSONArray("success");

                                if(barang.length() == 0){
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

                                gvPakan.setAdapter(adapter);

//                                GridLainnyaAdapter adapter = new GridLainnyaAdapter(LainnyaActivity.this, R.layout.item_grid_pakan, daftarPakan);
//                                gvPakan.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(LainnyaActivity.this);
            requestQueue.add(stringRequest);
        }





        btnHalaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                halaman++;
                progressDialog = new ProgressDialog(LainnyaActivity.this);
                progressDialog.setMessage("Mengambil Daftar Produk...");
                progressDialog.show();
                if(!dariPencarian){

                    StringRequest stringRequest = new StringRequest(BuildConfig.host + BuildConfig.daftarProdukKategori + kategori + "/" + halaman,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        progressDialog.dismiss();
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

//                                GridLainnyaAdapter adapter = new GridLainnyaAdapter(LainnyaActivity.this, R.layout.item_grid_pakan, daftarPakan);
                                        gvPakan.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });

                    RequestQueue requestQueue = Volley.newRequestQueue(LainnyaActivity.this);
                    requestQueue.add(stringRequest);

                }else{
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.pencarianProduk,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    try {
                                        JSONObject obj = new JSONObject(response);

                                        JSONArray barang = obj.getJSONArray("success");

//                            Toast.makeText(getContext(), events.getString(0), Toast.LENGTH_SHORT).show();

                                        for(int i = 0 ; i < barang.length() ; i ++){
                                            JSONObject product = barang.getJSONObject(i);
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
                                                    product.getString("foto2"),
                                                    product.getString("foto3"),
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

                                        gvPakan.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();

//                            GridLainnyaAdapter adapter = new GridLainnyaAdapter(LainnyaActivity.this, R.layout.item_grid_pakan, daftarPakan);
//                            gvPakan.setAdapter(adapter);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                }
                            }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("nama", kataPencarian);
                            params.put("kategori", kategori);
                            params.put("halaman", String.valueOf(halaman));

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(LainnyaActivity.this);
                    requestQueue.add(stringRequest);
                }
            }
        });

    }

    public void cariProduk(final String kataKunci){
        halaman = 1;
        kataPencarian = kataKunci;
        dariPencarian = true;
        daftarPakan.clear();
        progressDialog = new ProgressDialog(LainnyaActivity.this);
        progressDialog.setMessage("Mencari Produk...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.pencarianProduk,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray barang = obj.getJSONArray("success");

//                            Toast.makeText(getContext(), events.getString(0), Toast.LENGTH_SHORT).show();

                            for(int i = 0 ; i < barang.length() ; i ++){
                                JSONObject product = barang.getJSONObject(i);
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
                                        product.getString("foto2"),
                                        product.getString("foto3"),
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

//                            GridLainnyaAdapter adapter = new GridLainnyaAdapter(LainnyaActivity.this, R.layout.item_grid_pakan, daftarPakan);
                            gvPakan.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", kataKunci);
                params.put("kategori", kategori);
                params.put("halaman", String.valueOf(halaman));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LainnyaActivity.this);
        requestQueue.add(stringRequest);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.searchable, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) LainnyaActivity.this.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(LainnyaActivity.this.getComponentName()));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cariProduk(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
//                if(!newText.isEmpty()){
//                    handler.removeCallbacksAndMessages(null);
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            daftarPakan.clear();
//                            rekomendasiNama(newText);
//                            kunciPencarian.setText(newText);
//                            slideUp.animateIn();
//                        }
//                    }, 1500);
//                }else{
//                    slideUp.animateOut();
//                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
