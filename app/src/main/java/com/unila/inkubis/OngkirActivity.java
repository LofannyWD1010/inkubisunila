package com.unila.inkubis;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

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

import com.unila.inkubis.Adapter.ItemKecamatanAdapter;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.Model.Kecamatan;
import com.unila.inkubis.R;

public class OngkirActivity extends AppCompatActivity {

    Spinner spKabupaten;
    ListView lvKecamatan;

    ArrayAdapter<String> adapter;
    ArrayList<Kabupaten> daftarKabupaten;
    ArrayList<Kecamatan> daftarKecamatan;
    ItemKecamatanAdapter adapterListview;

    int idKabupaten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongkir);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daftar Lokasi Pengiriman");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        daftarKabupaten = new ArrayList<>();
        daftarKecamatan = new ArrayList<>();

        spKabupaten = (Spinner) findViewById(R.id.sp_kabupaten);
        lvKecamatan = (ListView) findViewById(R.id.lv_kecamatan);
        adapterListview = new ItemKecamatanAdapter(daftarKecamatan, OngkirActivity.this);

        daftarKabupaten();

        spKabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                daftarKecamatan.clear();

                String itemTerpilih = adapterView.getItemAtPosition(i).toString();
                if(!itemTerpilih.equalsIgnoreCase("Pilih Kabupaten")){
                    daftarKecamatan(daftarKabupaten.get(i-1).getIdKabupaten());
                    idKabupaten = daftarKabupaten.get(i-1).getIdKabupaten();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lvKecamatan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(OngkirActivity.this, "Tarif : " + daftarKecamatan.get(i).getOngkir(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OngkirActivity.this, AturOngkirActivity.class);
                intent.putExtra("ID_KECAMATAN", daftarKecamatan.get(i).getIdKecamatan());
                intent.putExtra("ID_KABUPATEN", daftarKecamatan.get(i).getIdKabupaten());
                intent.putExtra("NAMA_KECAMATAN", daftarKecamatan.get(i).getNamaKecamatan());
                intent.putExtra("NAMA_KABUPATEN", daftarKecamatan.get(i).getNamaKabupaten());
                intent.putExtra("ONGKIR", daftarKecamatan.get(i).getOngkir());
                startActivityForResult(intent,1);
            }
        });

    }

    public void daftarKabupaten(){
        StringRequest stringRequest = new StringRequest(BuildConfig.host + BuildConfig.daftarKabupaten,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            JSONArray kabupaten = obj.getJSONArray("success");

//                            Toast.makeText(getContext(), events.getString(0), Toast.LENGTH_SHORT).show();

                            for(int i = 0 ; i < kabupaten.length() ; i ++){
                                JSONObject detail = kabupaten.getJSONObject(i);
                                daftarKabupaten.add(new Kabupaten(detail.getInt("id_kabupaten"), detail.getString("nama")));
                            }

                            ArrayList<String> daftarNama = new ArrayList<>();
                            daftarNama.add("Pilih Kabupaten");
                            for(int i = 0 ; i < daftarKabupaten.size() ; i++){
                                daftarNama.add(daftarKabupaten.get(i).getNama());
                            }

                            adapter = new ArrayAdapter<String>(OngkirActivity.this,
                                    android.R.layout.simple_spinner_item, daftarNama);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spKabupaten.setAdapter(adapter);
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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(OngkirActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void daftarKecamatan(int idKabupaten){
        StringRequest stringRequest = new StringRequest(BuildConfig.host + BuildConfig.daftarKecamatan + idKabupaten,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            JSONArray kecamatan = obj.getJSONArray("success");

                            for(int i = 0 ; i < kecamatan.length() ; i ++){
                                JSONObject detail = kecamatan.getJSONObject(i);
                                daftarKecamatan.add(new Kecamatan(detail.getInt("id_kecamatan"), Integer.parseInt(detail.getString("id_kabupaten")), detail.getString("nama_kecamatan"), detail.getString("nama_kabupaten"), -1));
                            }

                            lvKecamatan.setAdapter(adapterListview);
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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(OngkirActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if (resultCode == Activity.RESULT_CANCELED) {
                daftarKecamatan.clear();
                daftarKecamatan(idKabupaten);
            }
        }
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

    public class Kabupaten{
        int idKabupaten;
        String nama;

        public Kabupaten(int idKabupaten, String nama){
            this.idKabupaten = idKabupaten;
            this.nama = nama;
        }

        public int getIdKabupaten() {
            return idKabupaten;
        }

        public String getNama() {
            return nama;
        }
    }
}
