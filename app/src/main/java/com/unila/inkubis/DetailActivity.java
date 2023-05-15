package com.unila.inkubis;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mancj.slideup.SlideUp;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.unila.inkubis.Adapter.GambarProdukAdapter;
import com.unila.inkubis.Adapter.ItemKeranjangAdapter;
import com.unila.inkubis.Model.PakanKeranjang;
import me.relex.circleindicator.CircleIndicator;

public class DetailActivity extends AppCompatActivity {

    ImageView ivPakan, ivFotoProdusen;
    TextView tvNamaPakan, tvStatus, tvDetailHargaBerat, tvNamaProdusen, tvDeskripsiPakan, tvTipePakan, tvTotalHarga, tvLokasiProdusen, tvMinimum, tvStok, tvBerat;
    RelativeLayout rlTelp, rlChat;
    private SlideUp slideUp;
    private View slideView;
    Button btnKeranjang, btnBeli, btnSelanjutnya;
    ListView lvDaftarKeranjang;

    private static ViewPager mPager;
    private CircleIndicator indicator;

    int idPakan, harga, totalHarga, idPenjual, idDaerah, berat, stok;
    double lat,lng;

    String nama, penjualNama, satuan;

    ArrayList<PakanKeranjang> daftarKeranjang;
    ArrayList<String> fotoBanner;

    boolean showKeranjang = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Produk");

        slideView = findViewById(R.id.slideView);

        slideUp = new SlideUp(slideView);
        slideUp.hideImmediately();

        mPager = (ViewPager) findViewById(R.id.gambar_pager);
        indicator = (CircleIndicator) findViewById(R.id.gambar_indicator);
        ivPakan = (ImageView) findViewById(R.id.iv_gambar_pakan);
        tvNamaPakan = (TextView) findViewById(R.id.tv_nama_pakan);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        tvDetailHargaBerat = (TextView) findViewById(R.id.tv_detail_harga_berat);
        tvNamaProdusen = (TextView) findViewById(R.id.tv_nama_produsen);
        ivFotoProdusen = (ImageView) findViewById(R.id.iv_foto_produsen);
        rlChat = (RelativeLayout) findViewById(R.id.rl_chat);
        rlTelp = (RelativeLayout) findViewById(R.id.rl_telepon);
        tvDeskripsiPakan = (TextView) findViewById(R.id.tv_deskripsi_pakan);
        btnKeranjang = (Button) findViewById(R.id.btn_keranjang);
        btnBeli = (Button) findViewById(R.id.btn_beli);
        btnSelanjutnya = (Button) findViewById(R.id.btn_selanjutnya);
        lvDaftarKeranjang = (ListView) findViewById(R.id.lv_daftar_keranjang);
        tvTipePakan = (TextView) findViewById(R.id.tv_tipe_pakan);
        tvTotalHarga = (TextView) findViewById(R.id.tv_total_harga);
        tvLokasiProdusen = (TextView) findViewById(R.id.tv_lokasi_produsen);
        tvMinimum = (TextView) findViewById(R.id.tv_minimum);
        tvStok = (TextView) findViewById(R.id.tv_stok);
        tvBerat = findViewById(R.id.tv_berat);

        Intent intentku = getIntent();

        idDaerah = intentku.getIntExtra("DAERAH_ID",0);
        berat = intentku.getIntExtra("BERAT", 0);
//        Toast.makeText(this, ""+intentku.getIntExtra("DAERAH_ID",0), Toast.LENGTH_SHORT).show();

        tvMinimum.setText(String.valueOf(intentku.getIntExtra("MINIMUM",0)) + " Item");
        tvLokasiProdusen.setText(intentku.getStringExtra("LOKASI"));
        idPenjual = intentku.getIntExtra("ID_PENJUAL",0);
        if(intentku.getIntExtra("STOK",0) < 1){
            tvStok.setText("Produk Habis !");
            btnKeranjang.setVisibility(View.GONE);
            btnBeli.setVisibility(View.GONE);
        }else{
            tvStok.setText(String.valueOf(intentku.getIntExtra("STOK",0)) + " Item");
        }

        if(intentku.getIntExtra("ID_PENJUAL",0) == SharedPrefManager.getInstance(DetailActivity.this).getPengguna().getIdPengguna()){
            btnKeranjang.setVisibility(View.GONE);
            btnBeli.setVisibility(View.GONE);
            rlChat.setVisibility(View.GONE);
            rlTelp.setVisibility(View.GONE);
        }

        harga = intentku.getIntExtra("HARGA", 0);
        idPakan = intentku.getIntExtra("ID", 0);
        stok = intentku.getIntExtra("STOK",0);
        nama = intentku.getStringExtra("NAMA");
        penjualNama = intentku.getStringExtra("NAMA_PENJUAL");
        satuan = intentku.getStringExtra("SATUAN");
        tvBerat.setText(intentku.getIntExtra("BERAT", 0) + " gram");

        fotoBanner = new ArrayList<>();
        fotoBanner.add(intentku.getStringExtra("FOTO"));

        if(!intentku.getStringExtra("FOTO2").equalsIgnoreCase("kosong")){
            fotoBanner.add(intentku.getStringExtra("FOTO2"));
        }


        if(!intentku.getStringExtra("FOTO3").equalsIgnoreCase("kosong")){
            fotoBanner.add(intentku.getStringExtra("FOTO3"));
        }

        mPager.setAdapter(new GambarProdukAdapter(DetailActivity.this,fotoBanner));

        indicator.setViewPager(mPager);

        Picasso.get()
                .load("http://inkubator.sikubis.com/uploads/file/" + intentku.getStringExtra("FOTO_PENJUAL"))
                .into(ivFotoProdusen);

        tvNamaPakan.setText(intentku.getStringExtra("NAMA"));
        tvNamaProdusen.setText(intentku.getStringExtra("NAMA_PENJUAL"));
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(intentku.getIntExtra("HARGA",0));
        yourFormattedString = yourFormattedString.replace(",", ".");
        tvDetailHargaBerat.setText("Rp. " + yourFormattedString + " / " + satuan);
        tvDeskripsiPakan.setText(intentku.getStringExtra("DESKRIPSI"));
        tvTipePakan.setText(intentku.getStringExtra("KATEGORI"));
        tvStatus.setText(intentku.getStringExtra("STATUS"));

//        lat = intentku.getDoubleExtra("LAT",0);
//        lng = intentku.getDoubleExtra("LNG",0);

        StringBuilder sb = new StringBuilder(intentku.getStringExtra("NO_TELP"));
        sb = sb.deleteCharAt(0);

        final StringBuilder finalSb = sb;
        rlTelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+62" + finalSb)));
            }
        });

        rlChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+62" + finalSb + "&text=");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        btnKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SharedPrefManager.getInstance(DetailActivity.this).isLoggedIn()){
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.tambahKeranjang,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        Toast.makeText(DetailActivity.this, obj.getString("success"), Toast.LENGTH_LONG).show();

                                        if(obj.getString("success").equalsIgnoreCase("Berhasil Tambah Keranjang")){
                                            showKeranjang = true;
                                            slideUp.animateIn();
                                            daftarKeranjang.clear();
                                            ambilKeranjang();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(DetailActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(DetailActivity.this).getPengguna().getRememberToken());
                            params.put("Accept", "application/json");

                            return params;
                        }

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("id_produk", String.valueOf(idPakan));
                            params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(DetailActivity.this).getPengguna().getIdPengguna()));

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(DetailActivity.this);
                    requestQueue.add(stringRequest);
                }else{
                    Toast.makeText(DetailActivity.this, "Anda harus login terlebih dahulu !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        daftarKeranjang = new ArrayList<>();
        ambilKeranjang();

        btnBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intentku = getIntent();
                    if (SharedPrefManager.getInstance(DetailActivity.this).isLoggedIn()) {
                        if (intentku.getIntExtra("STOK",0) > 0) {
                            Intent intent = new Intent(DetailActivity.this, LokasiPengirimanActivity.class);
                            intent.putExtra("ASAL", "beli");
                            intent.putExtra("TOTAL_HARGA", harga);
                            intent.putExtra("NAMA", nama);
                            intent.putExtra("SATUAN", satuan);
                            intent.putExtra("ID_PAKAN", idPakan);
                            intent.putExtra("ID_PENJUAL", idPenjual);
                            intent.putExtra("ID_DAERAH", idDaerah);
                            intent.putExtra("BERAT", berat);
                            intent.putExtra("STOK",stok);

                            startActivity(intent);
                        } else {
                            Toast.makeText(DetailActivity.this, "Stok Habis, tunggu penjual menambahkan stok !", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(DetailActivity.this, "Anda harus login terlebih dahulu !", Toast.LENGTH_SHORT).show();
                    }
                }
        });

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
                    Toast.makeText(DetailActivity.this, "Harap Tambah Produk Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(DetailActivity.this, LokasiPengirimanActivity.class);
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
        StringRequest stringRequest = new StringRequest(BuildConfig.host + BuildConfig.daftarKeranjang + SharedPrefManager.getInstance(DetailActivity.this).getPengguna().getIdPengguna(),
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



                            ItemKeranjangAdapter adapter = new ItemKeranjangAdapter(daftarKeranjang, DetailActivity.this);
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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(DetailActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(DetailActivity.this);
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
    public void onBackPressed() {
        if(showKeranjang){
            slideUp.animateOut();
            showKeranjang = false;
        }else{
            finish();
        }
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
