package com.unila.inkubis;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AturLokasiAntarActivity extends AppCompatActivity {
    
    TextView tvTarifOngkir;
    EditText etDetailAlamat;
    Button btnAturLokasi;
    Spinner spWilayah, spWilayahKota, spKurir, spLayanan;
    LinearLayout llPengiriman,llKota, llKurir, llLayanan;
    View separatorKota, separatorKurir, separatorLayanan, separatorPengiriman;
    
    ArrayAdapter<String> adapterSpinner;

    ArrayList<Provinsi> daftarProvinsi;
    ArrayList<Kota> daftarKota;
    ArrayList<Layanan> daftarLayanan;
    ArrayList<Kurir> daftarKurir;
    
    int idPenjual, ongkir = -1, id, idDaerah, berat, idWilayahKota, idKurir;
    String alamatAntar, kurir, namaKurir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atur_lokasi_antar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Atur Lokasi Antar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        daftarKurir = new ArrayList<>();

        llPengiriman = (LinearLayout) findViewById(R.id.ll_pengiriman);
        llKota = findViewById(R.id.ll_kota);
        llKurir = findViewById(R.id.ll_kurir);
        llLayanan = findViewById(R.id.ll_layanan);
        separatorKota = findViewById(R.id.separator_kota);
        separatorKurir = findViewById(R.id.separator_kurir);
        separatorLayanan = findViewById(R.id.separator_layanan);
        separatorPengiriman = (View) findViewById(R.id.separator_pengiriman);
        spWilayah = (Spinner) findViewById(R.id.sp_wilayah);
        spWilayahKota = findViewById(R.id.sp_kota);
        spKurir = findViewById(R.id.sp_kurir);
        spLayanan = findViewById(R.id.sp_layanan);
        tvTarifOngkir = (TextView) findViewById(R.id.tv_tarif_ongkir);
        etDetailAlamat = (EditText) findViewById(R.id.et_detail_alamat);
        btnAturLokasi = (Button) findViewById(R.id.btn_atur_lokasi);
        

        Intent intent = getIntent();
        idPenjual = intent.getIntExtra("ID_PENJUAL",0);
        idDaerah = intent.getIntExtra("DAERAH_ID",0);
        berat = intent.getIntExtra("BERAT",0) * intent.getIntExtra("JUMLAH",0);
        
        id = intent.getIntExtra("ID", 0);
        daftarProvinsi();

        spWilayah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i>0){
//                        Toast.makeText(AturLokasiAntarActivity.this, daftarProvinsi.get(i-1).getId()+"", Toast.LENGTH_SHORT).show();
                    daftarKota(daftarProvinsi.get(i-1).getId());
                    alamatAntar = daftarProvinsi.get(i-1).getNama();
                }
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spWilayahKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    idWilayahKota = daftarKota.get(i-1).getId();
                    alamatAntar = alamatAntar + " , " + daftarKota.get(i-1).getNama();
                    ambilKurir(idWilayahKota);

                    llKurir.setVisibility(View.VISIBLE);
                    separatorKurir.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spKurir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    idKurir = daftarKurir.get(i-1).getIdKurir();
                    namaKurir = daftarKurir.get(i-1).getNamaKurir();
                    llLayanan.setVisibility(View.VISIBLE);
                    separatorLayanan.setVisibility(View.VISIBLE);
                    Log.d("ID KURIR", String.valueOf(idKurir));
                    Log.d("Nama Kurir",namaKurir);
                    if(i==1){
                        kurir = "jne";
                        Log.d("cost", idDaerah+"");
                        Log.d("cost", idWilayahKota+"");
                        Log.d("cost", berat+"");
                        Log.d("cost", kurir);
                        daftarLayanan(kurir);
                    }else if(i==2){
                        kurir = "tiki";
                        Log.d("cost", idDaerah+"");
                        Log.d("cost", idWilayahKota+"");
                        Log.d("cost", berat+"");
                        Log.d("cost", kurir);
                        daftarLayanan(kurir);
                    }else if(i==3){
                        kurir = "pos";
                        Log.d("cost", idDaerah+"");
                        Log.d("cost", idWilayahKota+"");
                        Log.d("cost", berat+"");
                        Log.d("cost", kurir);
                        daftarLayanan(kurir);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spLayanan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    ongkir = daftarLayanan.get(i-1).getValue();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        
//        daftarOngkir();

//        spLokasiAntar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                String itemTerpilih = adapterView.getItemAtPosition(i).toString();
//                if(!itemTerpilih.equalsIgnoreCase("Pilih Lokasi Pengiriman")){
//                    ongkir = daftarOngkir.get(i-1).getOngkir();
//                    alamatAntar = daftarOngkir.get(i-1).getNamaKecamatan() + " ," + daftarOngkir.get(i-1).getNamaKabupaten();
//                    DecimalFormat formatter = new DecimalFormat("#,###,###");
//                    String yourFormattedString = formatter.format(ongkir);
//                    yourFormattedString = yourFormattedString.replace(",", ".");
//                    if(ongkir == 0){
//                        tvTarifOngkir.setText("Gratis !");
//                    }else{
//                        tvTarifOngkir.setText("Rp. " + yourFormattedString);
//                    }
//                }else{
//                    tvTarifOngkir.setText("Harap Atur Lokasi Antar");
//                }
//
//
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        btnAturLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ongkir < 0 || TextUtils.isEmpty(etDetailAlamat.getText().toString())){
                    Toast.makeText(AturLokasiAntarActivity.this, "harap pilih lokasi antar dan isi detail alamat!", Toast.LENGTH_SHORT).show();
                }else{
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("ONGKIR",ongkir);
                    returnIntent.putExtra("ID",id);
                    returnIntent.putExtra("LOKASI",etDetailAlamat.getText().toString() + " ," + alamatAntar);
                    returnIntent.putExtra("KURIR", idKurir);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            }
        });
    }

    public class Provinsi{
        String nama;
        int id;
        public Provinsi(int id, String nama){
            this.id = id;
            this.nama = nama;
        }

        public int getId() {
            return id;
        }

        public String getNama() {
            return nama;
        }
    }
    
    public void daftarProvinsi(){

        daftarProvinsi = new ArrayList<>();

        StringRequest stringRequest = new StringRequest("https://api.rajaongkir.com/starter/province",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response).getJSONObject("rajaongkir");
                            JSONArray provinsi = obj.getJSONArray("results");

                            for(int i = 0 ; i < provinsi.length() ; i ++){
                                JSONObject detail = provinsi.getJSONObject(i);
                                daftarProvinsi.add(new AturLokasiAntarActivity.Provinsi(Integer.parseInt(detail.getString("province_id")), detail.getString("province")));
                            }

                            ArrayList<String> daftarNama = new ArrayList<>();

                            daftarNama.add("Pilih Provinsi");

                            for(int i = 0 ; i < daftarProvinsi.size() ; i++){
                                daftarNama.add(daftarProvinsi.get(i).getNama());
                            }

                            ArrayAdapter adapterSpinnerProvinsi = new ArrayAdapter<String>(AturLokasiAntarActivity.this,
                                    android.R.layout.simple_spinner_item, daftarNama);

                            adapterSpinnerProvinsi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spWilayah.setAdapter(adapterSpinnerProvinsi);
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
                params.put("key", "4ff31860548c84e27435440393c6439d");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public class Kota{
        String nama;
        int id;
        public Kota(int id, String nama){
            this.id = id;
            this.nama = nama;
        }

        public int getId() {
            return id;
        }

        public String getNama() {
            return nama;
        }
    }

    public void daftarKota(int idProvinsi){

        daftarKota = new ArrayList<>();
        llKota.setVisibility(View.VISIBLE);
        separatorKota.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest("https://api.rajaongkir.com/starter/city?province="+idProvinsi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response).getJSONObject("rajaongkir");
                            Log.d("KOTA",obj.toString());
                            JSONArray kota = obj.getJSONArray("results");


                            Log.d("KOTA_LENGTH",kota.length()+"");
                            for(int i = 0 ; i < kota.length() ; i ++){
                                JSONObject detail = kota.getJSONObject(i);
                                daftarKota.add(new AturLokasiAntarActivity.Kota(Integer.parseInt(detail.getString("city_id")), detail.getString("city_name")));
                            }

                            Log.d("KOTA_SIZE",daftarKota.size()+"");
                            ArrayList<String> daftarNamaKota = new ArrayList<>();

                            daftarNamaKota.add("Pilih Kota");

                            for(int i = 0 ; i < daftarKota.size() ; i++){
                                daftarNamaKota.add(daftarKota.get(i).getNama());
                            }

                            ArrayAdapter adapterSpinnerKota = new ArrayAdapter<String>(AturLokasiAntarActivity.this,
                                    android.R.layout.simple_spinner_item, daftarNamaKota);

                            adapterSpinnerKota.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spWilayahKota.setAdapter(adapterSpinnerKota);

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
                params.put("key", "4ff31860548c84e27435440393c6439d");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public class Layanan{
        String service, etd;
        int value;
        public Layanan(String service, int value, String etd){
            this.service = service;
            this.value = value;
            this.etd = etd;
        }

        public String getService() {
            return service;
        }

        public int getValue() {
            return value;
        }

        public String getEtd() {
            return etd;
        }
    }

    public void daftarLayanan(final String kurir){
        daftarLayanan = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.rajaongkir.com/starter/cost",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response).getJSONObject("rajaongkir");
                            JSONArray results = obj.getJSONArray("results");
                            JSONObject objResult = results.getJSONObject(0);
                            JSONArray layanans = objResult.getJSONArray("costs");
                            for (int i = 0; i < layanans.length() - 1; i++) {
                                JSONObject jsonObject = layanans.getJSONObject(i);

                                JSONArray biayas = jsonObject.getJSONArray("cost");
                                JSONObject biaya = biayas.getJSONObject(0);

                                String service = jsonObject.getString("service");
                                String estimasi = biaya.getString("etd");
                                int ongkirnya = biaya.getInt("value");

                                daftarLayanan.add(new AturLokasiAntarActivity.Layanan(service,ongkirnya,estimasi));


                            }

                            ArrayList<String> daftarNamaLayanan = new ArrayList<>();

                            daftarNamaLayanan.add("Pilih Layanan");

                            for(int i = 0 ; i < daftarLayanan.size() ; i++){
                                daftarNamaLayanan.add(daftarLayanan.get(i).getService() + ", " + daftarLayanan.get(i).getEtd() + " hari, Ongkir : Rp." + daftarLayanan.get(i).getValue());
                            }

                            ArrayAdapter adapterSpinnerLayanan = new ArrayAdapter<String>(AturLokasiAntarActivity.this,
                                    android.R.layout.simple_spinner_item, daftarNamaLayanan);

                            adapterSpinnerLayanan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spLayanan.setAdapter(adapterSpinnerLayanan);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AturLokasiAntarActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("key", "4ff31860548c84e27435440393c6439d");

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("origin", String.valueOf(idDaerah));
                params.put("destination", String.valueOf(idWilayahKota));
                params.put("weight", String.valueOf(berat));
                params.put("courier", kurir);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AturLokasiAntarActivity.this);
        requestQueue.add(stringRequest);
    }

    public class Kurir{
        String namaKurir;
        int id_kurir;
        public Kurir(String namaKurir,int id_kurir){
            this.namaKurir = namaKurir;
            this.id_kurir = id_kurir;
        }

        public String getNamaKurir() {

            return namaKurir;
        }

        public int getIdKurir() {

            return id_kurir;
        }
    }

    public void ambilKurir(final int idWilayahKota){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.getKurirBeli,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            JSONArray objKurir = obj.getJSONArray("success");
                            for(int i = 0 ; i < objKurir.length() ; i ++){
                                JSONObject detail = objKurir.getJSONObject(i);
                                daftarKurir.add(new Kurir(
                                        detail.getString("nama_kurir"),
                                        Integer.parseInt(detail.getString("id_kurir"))));
                            }

                            ArrayList<String> daftarNama = new ArrayList<>();
                            daftarNama.add("Pilih Kurir");
                            for(int i = 0 ; i < daftarKurir.size() ; i ++) {
                                daftarNama.add(daftarKurir.get(i).getNamaKurir());
                            }
                            ArrayAdapter adapterKurir = new ArrayAdapter<String>(AturLokasiAntarActivity.this,
                                    android.R.layout.simple_spinner_item, daftarNama);

                            adapterKurir.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spKurir.setAdapter(adapterKurir);


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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(AturLokasiAntarActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id_penjual", String.valueOf(idPenjual));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
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
