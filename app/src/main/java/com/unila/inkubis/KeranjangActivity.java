package com.unila.inkubis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.unila.inkubis.Adapter.ItemKeranjangAdapter;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.Model.PakanKeranjang;
import com.unila.inkubis.R;

public class KeranjangActivity extends AppCompatActivity {

    ListView lvDaftarKeranjang;
    Button btnSelanjutnya;
    TextView tvTotalHarga;

    ArrayList<PakanKeranjang> daftarKeranjang;
    int totalHarga = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Daftar Keranjang");

        btnSelanjutnya = (Button) findViewById(R.id.btn_selanjutnya); 
        lvDaftarKeranjang = (ListView) findViewById(R.id.lv_daftar_keranjang);
        tvTotalHarga = (TextView) findViewById(R.id.tv_total_harga);

        daftarKeranjang = new ArrayList<>();

        ambilKeranjang();

        btnSelanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<Integer> unik = new ArrayList<>();
                ArrayList<Integer> unikDaerah = new ArrayList<>();
                ArrayList<Integer> berat = new ArrayList<>();

                for(int i = 0 ; i < daftarKeranjang.size() ; i++){
                    Log.d("ID_PENJUAL", String.valueOf(daftarKeranjang.get(i).getIdPenjual()));
                    unik.add(daftarKeranjang.get(i).getIdPenjual());
                    unikDaerah.add(daftarKeranjang.get(i).getIdDaerah());
                    berat.add(daftarKeranjang.get(i).getBerat());
                }

                ArrayList<Integer> daftarPenjualnya = new ArrayList<>();
                daftarPenjualnya.addAll(unik);

                ArrayList<Integer> daftarDaerahPenjual = new ArrayList<>();
                daftarDaerahPenjual.addAll(unikDaerah);

                for(int i = 0 ; i < unik.size() ; i++){
                    Log.d("UNIK", String.valueOf(daftarPenjualnya.get(i)));
                }

                if(totalHarga<=0){
                    Toast.makeText(KeranjangActivity.this, "Harap Tambah Produk Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(KeranjangActivity.this, LokasiPengirimanActivity.class);
                    intent.putExtra("TOTAL_HARGA", totalHarga);
                    intent.putExtra("ASAL", "keranjang");
                    ArrayList<Integer> daftarJumlah = new ArrayList<>();

                    for (int i = 0 ; i < daftarKeranjang.size() ; i++){
                        daftarJumlah.add(daftarKeranjang.get(i).getJumlah());
                    }

                    intent.putExtra("JUMLAH", daftarJumlah);
                    intent.putExtra("ID_PENJUAL_ARRAY", daftarPenjualnya);
                    intent.putExtra("DAERAH_ID", daftarDaerahPenjual);
                    intent.putExtra("BERAT", berat);
                    startActivity(intent);
                }

            }
        });


    }

    public void ambilKeranjang(){

        StringRequest stringRequest = new StringRequest(BuildConfig.host + BuildConfig.daftarKeranjang + SharedPrefManager.getInstance(KeranjangActivity.this).getPengguna().getIdPengguna(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            JSONArray barang = obj.getJSONArray("success");

//                            Toast.makeText(getContext(), events.getString(0), Toast.LENGTH_SHORT).show();

                            for(int i = 0 ; i < barang.length() ; i ++){
                                JSONObject product = barang.getJSONObject(i);
                                daftarKeranjang.add(new PakanKeranjang(
                                    Integer.parseInt(product.getString("id_produk")),
                                    product.getString("nama"),
                                    product.getString("satuan"),
                                    Integer.valueOf(product.getString("harga")),
                                    product.getString("foto"),
                                    1,
                                    product.getString("jenis"),
                                    Integer.parseInt(product.getString("id_penjual")),
                                    product.getInt("id"),
                                    Integer.parseInt(product.getString("daerah_id")),
                                    Integer.parseInt(product.getString("berat"))
                                ));

                            }



                            ItemKeranjangAdapter adapter = new ItemKeranjangAdapter(daftarKeranjang, KeranjangActivity.this);
                            lvDaftarKeranjang.setAdapter(adapter);
                            hitungTotalHarga();
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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(KeranjangActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(KeranjangActivity.this);
        requestQueue.add(stringRequest);

    }

    public void hitungTotalHarga(){
        totalHarga = 0;

        for(int i = 0 ; i<daftarKeranjang.size() ; i++){
            int harganya = daftarKeranjang.get(i).getHarga() * daftarKeranjang.get(i).getJumlah();
            totalHarga+=harganya;
        }

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(totalHarga);
        yourFormattedString = yourFormattedString.replace(",", ".");
        tvTotalHarga.setText("Rp. " + yourFormattedString);
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
