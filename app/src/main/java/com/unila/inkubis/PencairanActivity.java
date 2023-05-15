package com.unila.inkubis;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
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

import com.unila.inkubis.Adapter.ItemPencairanAdapter;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.Model.Pencairan;
import com.unila.inkubis.R;

public class PencairanActivity extends AppCompatActivity {

    ListView lvPencairan;
    SwipeRefreshLayout refresh;
    ArrayList<Pencairan> daftarPencairan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencairan);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daftar Pencairan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvPencairan = (ListView) findViewById(R.id.lv_pencairan);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                daftarPencairan.clear();
                StringRequest stringRequest = new StringRequest("http://inkubator.sikubis.com/mobile/daftarPencairan/" + SharedPrefManager.getInstance(PencairanActivity.this).getPengguna().getIdPengguna(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    refresh.setRefreshing(false);
                                    JSONObject obj = new JSONObject(response);
                                    JSONArray pencairan = obj.getJSONArray("success");

//                            Toast.makeText(getContext(), events.getString(0), Toast.LENGTH_SHORT).show();

                                    for(int i = 0 ; i < pencairan.length() ; i ++){
                                        JSONObject cair = pencairan.getJSONObject(i);
                                        daftarPencairan.add(new Pencairan(
                                                cair.getInt("id"),
                                                cair.getString("tanggal"),
                                                cair.getString("id_pengguna"),
                                                cair.getString("saldo"),
                                                cair.getString("status")
                                        ));
                                    }

                                    ItemPencairanAdapter adapter = new ItemPencairanAdapter(daftarPencairan, PencairanActivity.this);
                                    lvPencairan.setAdapter(adapter);
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

                RequestQueue requestQueue = Volley.newRequestQueue(PencairanActivity.this);
                requestQueue.add(stringRequest);
            }
        });

        daftarPencairan = new ArrayList<>();
        ambilPencairan();
    }

    public void ambilPencairan(){
//        daftarPencairan.clear();
        StringRequest stringRequest = new StringRequest(BuildConfig.host + BuildConfig.daftarPencairan + SharedPrefManager.getInstance(PencairanActivity.this).getPengguna().getIdPengguna(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            JSONArray pencairan = obj.getJSONArray("success");

//                            Toast.makeText(getContext(), events.getString(0), Toast.LENGTH_SHORT).show();

                            for(int i = 0 ; i < pencairan.length() ; i ++){
                                JSONObject cair = pencairan.getJSONObject(i);
                                daftarPencairan.add(new Pencairan(
                                        cair.getInt("id"),
                                        cair.getString("tanggal"),
                                        cair.getString("id_pengguna"),
                                        cair.getString("saldo"),
                                        cair.getString("status")
                                ));
                            }

                            ItemPencairanAdapter adapter = new ItemPencairanAdapter(daftarPencairan, PencairanActivity.this);
                            lvPencairan.setAdapter(adapter);
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
                    params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(PencairanActivity.this).getPengguna().getRememberToken());
                    params.put("Accept", "application/json");

                    return params;
                }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(PencairanActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profil, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                return true;
            case R.id.tambah_produk :
                startActivityForResult(new Intent(PencairanActivity.this, TambahPencairanActivity.class),1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if (resultCode == Activity.RESULT_CANCELED) {
                daftarPencairan.clear();
                StringRequest stringRequest = new StringRequest("http://inkubator.sikubis.com/mobile/daftarPencairan/" + SharedPrefManager.getInstance(PencairanActivity.this).getPengguna().getIdPengguna(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject obj = new JSONObject(response);

                                    JSONArray pencairan = obj.getJSONArray("success");

//                            Toast.makeText(getContext(), events.getString(0), Toast.LENGTH_SHORT).show();

                                    for(int i = 0 ; i < pencairan.length() ; i ++){
                                        JSONObject cair = pencairan.getJSONObject(i);
                                        daftarPencairan.add(new Pencairan(
                                                cair.getInt("id"),
                                                cair.getString("tanggal"),
                                                cair.getString("id_pengguna"),
                                                cair.getString("saldo"),
                                                cair.getString("status")
                                        ));
                                    }

                                    ItemPencairanAdapter adapter = new ItemPencairanAdapter(daftarPencairan, PencairanActivity.this);
                                    lvPencairan.setAdapter(adapter);
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

                RequestQueue requestQueue = Volley.newRequestQueue(PencairanActivity.this);
                requestQueue.add(stringRequest);
            }
        }
    }
}
