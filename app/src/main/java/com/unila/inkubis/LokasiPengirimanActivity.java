package com.unila.inkubis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.unila.inkubis.Adapter.DaftarLokasiPenjualAdapter;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.Customable.RepeatListener;
import com.unila.inkubis.Model.PakanKeranjang;
import com.unila.inkubis.Model.PakanLokasi;
import com.unila.inkubis.Model.tempOngkir;
import com.unila.inkubis.R;
import com.unila.inkubis.Adapter.DaftarLokasiPenjualAdapter;

public class LokasiPengirimanActivity extends AppCompatActivity {

    TextView tvTotalHarga, tvRincianHarga, tvRincianOngkos, tvTotalPembayaran, tvNama, tvHarga, tvJumlah, tvAmbil, tvKeteranganKirim;
    LinearLayout llInfoPembayaran, llBeli, llPengiriman,llKota, llKurir, llLayanan;
    Switch swAmbil;
    Button btnKonfirmasi, btnLanjutBayar, btnKurang, btnTambah;
    View separatorPengiriman, separatorKota, separatorKurir, separatorLayanan;
    EditText etDetailAlamat;
    ScrollView scrollView;
    ProgressDialog mProgressDialog;
    ExpandableHeightListView lvKeranjang, lvPenjual;
    Spinner spWilayah, spWilayahKota, spKurir, spLayanan;
    CardView cvLokasiBeli;

    ArrayAdapter<CharSequence> adapter;
    int ongkir = 0, harga = 0, jumlah = 1, idPenjual, idPakan, ambil = 0, idDaerah, berat, totalBerat, idWilayahKota,stok,idKurir;
    String nama, satuan, kurir,namaKurir;

    ArrayList<PakanKeranjang> daftarBeli;
    ArrayList<PakanLokasi> pakanLokasi;
    ArrayList<tempOngkir> daftarTempOngkir;
    ArrayList<Integer> daftarJumlah, daftarPenjualArray, daftarDaerahPenjual, daftarBerat;
    ArrayAdapter<String> adapterSpinner;
    ArrayList<Ongkir> daftarOngkir;
    ArrayList<Layanan> daftarLayanan;
    String alamatAntar;
    DaftarLokasiPenjualAdapter adapterLokasiPenjual;

    ArrayList<Provinsi> daftarProvinsi;
    ArrayList<Kota> daftarKota;
    ArrayList<Kurir> daftarKurir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi_pengiriman);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Pembelian");

        daftarOngkir = new ArrayList<>();
        daftarKurir = new ArrayList<>();

        tvTotalHarga = (TextView) findViewById(R.id.tv_total_harga);

        tvRincianHarga = (TextView) findViewById(R.id.tv_rincian_harga);
        tvRincianOngkos = (TextView) findViewById(R.id.tv_rincian_ongkos);
        tvTotalPembayaran = (TextView) findViewById(R.id.tv_total_pembayaran);
        lvKeranjang = (ExpandableHeightListView) findViewById(R.id.lv_daftar_beli);
        btnKonfirmasi = (Button) findViewById(R.id.btn_konfirmasi);
        llInfoPembayaran = (LinearLayout) findViewById(R.id.ll_info_pembayaran);
        scrollView = (ScrollView) findViewById(R.id.scrollview);
        btnLanjutBayar = (Button) findViewById(R.id.btn_lanjut_bayar);
        spWilayah = (Spinner) findViewById(R.id.sp_wilayah);
        llBeli = (LinearLayout) findViewById(R.id.ll_beli);
        tvNama = (TextView) findViewById(R.id.tv_nama);
        tvHarga = (TextView) findViewById(R.id.tv_harga);
        tvJumlah = (TextView) findViewById(R.id.tv_jumlah);
        btnTambah = (Button) findViewById(R.id.btn_tambah);
        btnKurang = (Button) findViewById(R.id.btn_kurang);
        etDetailAlamat = (EditText) findViewById(R.id.et_detail_alamat);
        llPengiriman = (LinearLayout) findViewById(R.id.ll_pengiriman);
        swAmbil = (Switch) findViewById(R.id.sw_ambil);
        tvAmbil = (TextView) findViewById(R.id.tv_ambil);
        separatorPengiriman = (View) findViewById(R.id.separator_pengiriman);
        tvKeteranganKirim = (TextView) findViewById(R.id.tv_keterangan_kirim);
        cvLokasiBeli = (CardView) findViewById(R.id.cv_lokasi_beli);
        lvPenjual = (ExpandableHeightListView) findViewById(R.id.lv_lokasi_penjual);
        spWilayahKota = findViewById(R.id.sp_kota);
        spKurir = findViewById(R.id.sp_kurir);
        separatorKota = findViewById(R.id.separator_kota);
        separatorKurir = findViewById(R.id.separator_kurir);
        llKota = findViewById(R.id.ll_kota);
        llKurir = findViewById(R.id.ll_kurir);
        llLayanan = findViewById(R.id.ll_layanan);
        separatorLayanan = findViewById(R.id.separator_layanan);
        spLayanan = findViewById(R.id.sp_layanan);

//        adapter = ArrayAdapter.createFromResource(this,
//                R.array.wilayahs, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spWilayah.setAdapter(adapter);

        final Intent intent = getIntent();
        swAmbil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(intent.getStringExtra("ASAL").equalsIgnoreCase("beli")){
                    if(isChecked){
                        llPengiriman.setVisibility(View.GONE);
                        etDetailAlamat.setVisibility(View.GONE);
                        tvAmbil.setVisibility(View.VISIBLE);
                        separatorPengiriman.setVisibility(View.GONE);

                        ambil = 1;

                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(0);
                        yourFormattedString = yourFormattedString.replace(",", ".");
                        tvRincianOngkos.setText("Rp. " + yourFormattedString);

                        DecimalFormat formatter1 = new DecimalFormat("#,###,###");
                        String yourFormattedString1 = formatter1.format((harga*jumlah) + 0);
                        yourFormattedString1 = yourFormattedString1.replace(",", ".");
                        tvTotalPembayaran.setText("Rp. " + yourFormattedString1);
                    }else if(!isChecked){
                        ambil = 0;
                        llPengiriman.setVisibility(View.VISIBLE);
                        etDetailAlamat.setVisibility(View.VISIBLE);
                        tvAmbil.setVisibility(View.GONE);
                        separatorPengiriman.setVisibility(View.VISIBLE);

                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(ongkir*jumlah);
                        yourFormattedString = yourFormattedString.replace(",", ".");
                        tvRincianOngkos.setText("Rp. " + yourFormattedString);

                        DecimalFormat formatter1 = new DecimalFormat("#,###,###");
                        String yourFormattedString1 = formatter1.format((harga*jumlah) + (ongkir*jumlah));
                        yourFormattedString1 = yourFormattedString1.replace(",", ".");
                        tvTotalPembayaran.setText("Rp. " + yourFormattedString1);
                    }
                }else if(intent.getStringExtra("ASAL").equalsIgnoreCase("keranjang")){
                    if(isChecked){
                        ambil = 1;
                        lvPenjual.setVisibility(View.GONE);
                        tvAmbil.setVisibility(View.VISIBLE);
                        cvLokasiBeli.setVisibility(View.VISIBLE);
                        llPengiriman.setVisibility(View.GONE);
                        separatorPengiriman.setVisibility(View.GONE);
                        etDetailAlamat.setVisibility(View.GONE);

                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(0);
                        yourFormattedString = yourFormattedString.replace(",", ".");
                        tvRincianOngkos.setText("Rp. " + yourFormattedString);

                        DecimalFormat formatter1 = new DecimalFormat("#,###,###");
                        String yourFormattedString1 = formatter1.format(harga + 0);
                        yourFormattedString1 = yourFormattedString1.replace(",", ".");
                        tvTotalPembayaran.setText("Rp. " + yourFormattedString1);
                    }else if(!isChecked){
                        ambil = 0;
                        lvPenjual.setVisibility(View.VISIBLE);
                        tvAmbil.setVisibility(View.GONE);
                        cvLokasiBeli.setVisibility(View.GONE);

                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(ongkir*jumlah);
                        yourFormattedString = yourFormattedString.replace(",", ".");
                        tvRincianOngkos.setText("Rp. " + yourFormattedString);

                        DecimalFormat formatter1 = new DecimalFormat("#,###,###");
                        String yourFormattedString1 = formatter1.format(harga + (ongkir*jumlah));
                        yourFormattedString1 = yourFormattedString1.replace(",", ".");
                        tvTotalPembayaran.setText("Rp. " + yourFormattedString1);
                    }
                }
            }
        });

        llBeli.setVisibility(View.GONE);

        llInfoPembayaran.setVisibility(View.GONE);
        lvKeranjang.setExpanded(true);
        lvKeranjang.setVisibility(View.GONE);


        harga = intent.getIntExtra("TOTAL_HARGA", 0);
        stok = intent.getIntExtra("STOK",0);
        idPenjual = intent.getIntExtra("ID_PENJUAL", 0);
        idPakan = intent.getIntExtra("ID_PAKAN", 0);

        if(intent.getStringExtra("ASAL").equalsIgnoreCase("beli")){
//            daftarOngkir();
            daftarProvinsi();
            idDaerah = getIntent().getIntExtra("ID_DAERAH",0);
            berat = getIntent().getIntExtra("BERAT", 0);
            totalBerat = berat;
            lvPenjual.setVisibility(View.GONE);
            spWilayah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i>0){
//                        Toast.makeText(LokasiPengirimanActivity.this, daftarProvinsi.get(i-1).getId()+"", Toast.LENGTH_SHORT).show();
                        daftarKota(daftarProvinsi.get(i-1).getId());
                        alamatAntar = daftarProvinsi.get(i-1).getNama();
                    }

                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    String yourFormattedString = formatter.format(ongkir*jumlah);
                    yourFormattedString = yourFormattedString.replace(",", ".");
                    if(ongkir*jumlah == 0){
                        tvRincianOngkos.setText("Gratis !");
                    }else{
                        tvRincianOngkos.setText("Rp. " + yourFormattedString);
                    }

                    DecimalFormat formatter1 = new DecimalFormat("#,###,###");
                    String yourFormattedString1 = formatter1.format((harga*jumlah) + (ongkir*jumlah));
                    yourFormattedString1 = yourFormattedString1.replace(",", ".");
                    tvTotalPembayaran.setText("Rp. " + yourFormattedString1);
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
                        if(idKurir == 1){
                            kurir = "jne";
                            Log.d("cost", idDaerah+"");
                            Log.d("cost", idWilayahKota+"");
                            Log.d("cost", totalBerat+"");
                            Log.d("cost", kurir);
                            daftarLayanan(kurir);
                        }else if(idKurir == 2){
                            kurir = "tiki";
                            Log.d("cost", idDaerah+"");
                            Log.d("cost", idWilayahKota+"");
                            Log.d("cost", totalBerat+"");
                            Log.d("cost", kurir);
                            daftarLayanan(kurir);
                        }else if(idKurir == 3){
                            kurir = "pos";
                            Log.d("cost", idDaerah+"");
                            Log.d("cost", idWilayahKota+"");
                            Log.d("cost", totalBerat+"");
                            Log.d("cost", kurir);
                            daftarLayanan(kurir);
                        }
                        else{
                            Log.d("HERE", "lalalala");
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

            llBeli.setVisibility(View.VISIBLE);
            tvJumlah.setText(String.valueOf(jumlah));
            nama = intent.getStringExtra("NAMA");
            satuan = intent.getStringExtra("SATUAN");
            tvNama.setText(nama);

            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String yourFormattedString = formatter.format(harga);
            yourFormattedString = yourFormattedString.replace(",", ".");

            tvHarga.setText("Rp. " + yourFormattedString + " / " + satuan);

            DecimalFormat formatter1 = new DecimalFormat("#,###,###");
            String yourFormattedString1 = formatter1.format(harga);
            yourFormattedString1 = yourFormattedString1.replace(",", ".");
            tvTotalHarga.setText("Rp. " + yourFormattedString1);

            btnTambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (jumlah >= stok) {
                        jumlah = stok;
                        totalBerat = jumlah * berat;
                    } else {
                        jumlah++;
                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(harga * jumlah);
                        yourFormattedString = yourFormattedString.replace(",", ".");
                        tvTotalHarga.setText("Rp. " + yourFormattedString);
                        tvJumlah.setText(String.valueOf(jumlah));

                        tvRincianHarga.setText("Rp. " + yourFormattedString);

                        DecimalFormat formatter2 = new DecimalFormat("#,###,###");
                        String yourFormattedString2 = formatter2.format(ongkir*jumlah);
                        yourFormattedString2 = yourFormattedString2.replace(",", ".");
                        tvRincianOngkos.setText("Rp. " + yourFormattedString2);

                        DecimalFormat formatter1 = new DecimalFormat("#,###,###");
                        String yourFormattedString1 = formatter1.format((harga * jumlah) + (ongkir*jumlah));
                        yourFormattedString1 = yourFormattedString1.replace(",", ".");
                        tvTotalPembayaran.setText("Rp. " + yourFormattedString1);
                        totalBerat = jumlah * berat;
                    }
                }
            });

            btnTambah.setOnTouchListener(new RepeatListener(800, 200, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (jumlah >= stok) {
                        jumlah = stok;
                        totalBerat = jumlah * berat;
                    } else {
                        jumlah++;
                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(harga * jumlah);
                        yourFormattedString = yourFormattedString.replace(",", ".");
                        tvTotalHarga.setText("Rp. " + yourFormattedString);
                        tvJumlah.setText(String.valueOf(jumlah));

                        tvRincianHarga.setText("Rp. " + yourFormattedString);

                        DecimalFormat formatter2 = new DecimalFormat("#,###,###");
                        String yourFormattedString2 = formatter2.format(ongkir*jumlah);
                        yourFormattedString2 = yourFormattedString2.replace(",", ".");
                        tvRincianOngkos.setText("Rp. " + yourFormattedString2);

                        DecimalFormat formatter1 = new DecimalFormat("#,###,###");
                        String yourFormattedString1 = formatter1.format((harga * jumlah) + (ongkir*jumlah));
                        yourFormattedString1 = yourFormattedString1.replace(",", ".");
                        tvTotalPembayaran.setText("Rp. " + yourFormattedString1);
                        totalBerat = jumlah * berat;
                    }
                }
            }));

            btnKurang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(jumlah <= 1){
                        jumlah = 1;
                        totalBerat = jumlah*berat;
                    }else{
                        jumlah--;
                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(harga*jumlah);
                        yourFormattedString = yourFormattedString.replace(",", ".");
                        tvTotalHarga.setText("Rp. " + yourFormattedString);
                        tvJumlah.setText(String.valueOf(jumlah));

                        tvRincianHarga.setText("Rp. " + yourFormattedString);

                        DecimalFormat formatter2 = new DecimalFormat("#,###,###");
                        String yourFormattedString2 = formatter2.format(ongkir*jumlah);
                        yourFormattedString2 = yourFormattedString2.replace(",", ".");
                        tvRincianOngkos.setText("Rp. " + yourFormattedString2);

                        DecimalFormat formatter1 = new DecimalFormat("#,###,###");
                        String yourFormattedString1 = formatter1.format((harga*jumlah) + (ongkir*jumlah));
                        yourFormattedString1 = yourFormattedString1.replace(",", ".");
                        tvTotalPembayaran.setText("Rp. " + yourFormattedString1);
                        totalBerat = jumlah*berat;
                    }
                }
            });

            btnKurang.setOnTouchListener(new RepeatListener(800, 200, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(jumlah <= 1){
                        jumlah = 1;
                        totalBerat = jumlah*berat;
                    }else{
                        jumlah--;
                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(harga*jumlah);
                        yourFormattedString = yourFormattedString.replace(",", ".");
                        tvTotalHarga.setText("Rp. " + yourFormattedString);
                        tvJumlah.setText(String.valueOf(jumlah));

                        tvRincianHarga.setText("Rp. " + yourFormattedString);

                        DecimalFormat formatter2 = new DecimalFormat("#,###,###");
                        String yourFormattedString2 = formatter2.format(ongkir*jumlah);
                        yourFormattedString2 = yourFormattedString2.replace(",", ".");
                        tvRincianOngkos.setText("Rp. " + yourFormattedString2);

                        DecimalFormat formatter1 = new DecimalFormat("#,###,###");
                        String yourFormattedString1 = formatter1.format((harga*jumlah) + (ongkir*jumlah));
                        yourFormattedString1 = yourFormattedString1.replace(",", ".");
                        tvTotalPembayaran.setText("Rp. " + yourFormattedString1);
                        totalBerat = jumlah*berat;
                    }
                }
            }));


            btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(spWilayah.getSelectedItemPosition() == 0 || TextUtils.isEmpty(etDetailAlamat.getText().toString())){

                        if(ambil == 1){
                            btnKonfirmasi.setVisibility(View.GONE);
                            DecimalFormat formatter = new DecimalFormat("#,###,###");
                            String yourFormattedString = formatter.format(0);
                            yourFormattedString = yourFormattedString.replace(",", ".");
                            tvRincianOngkos.setText("Rp. " + yourFormattedString);

                            DecimalFormat formatter1 = new DecimalFormat("#,###,###");
                            String yourFormattedString1 = formatter1.format((harga*jumlah) + 0);
                            yourFormattedString1 = yourFormattedString1.replace(",", ".");
                            tvTotalPembayaran.setText("Rp. " + yourFormattedString1);

                            llInfoPembayaran.setVisibility(View.VISIBLE);
                            scrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    tvRincianHarga.setText(tvTotalHarga.getText());
                                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                        }else{
                            Toast.makeText(LokasiPengirimanActivity.this, "Harap Pilih Lokasi Pengiriman dan isi Detail Lokasi", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        btnKonfirmasi.setVisibility(View.GONE);

                        if(ambil == 1) {
                            DecimalFormat formatter = new DecimalFormat("#,###,###");
                            String yourFormattedString = formatter.format(0);
                            yourFormattedString = yourFormattedString.replace(",", ".");
                            tvRincianOngkos.setText("Rp. " + yourFormattedString);

                            DecimalFormat formatter1 = new DecimalFormat("#,###,###");
                            String yourFormattedString1 = formatter1.format((harga*jumlah) + 0);
                            yourFormattedString1 = yourFormattedString1.replace(",", ".");
                            tvTotalPembayaran.setText("Rp. " + yourFormattedString1);
                        }else{
                            DecimalFormat formatter = new DecimalFormat("#,###,###");
                            String yourFormattedString = formatter.format(ongkir*jumlah);
                            yourFormattedString = yourFormattedString.replace(",", ".");
                            tvRincianOngkos.setText("Rp. " + yourFormattedString);

                            DecimalFormat formatter1 = new DecimalFormat("#,###,###");
                            String yourFormattedString1 = formatter1.format((harga*jumlah) + (ongkir*jumlah));
                            yourFormattedString1 = yourFormattedString1.replace(",", ".");
                            tvTotalPembayaran.setText("Rp. " + yourFormattedString1);
                        }

                        llInfoPembayaran.setVisibility(View.VISIBLE);
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                tvRincianHarga.setText(tvTotalHarga.getText());
                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                    }
                }
            });

        }else if(intent.getStringExtra("ASAL").equalsIgnoreCase("keranjang")){
            lvPenjual.setVisibility(View.VISIBLE);
            tvKeteranganKirim.setText("Daftar Lokasi Pengiriman Produk");

            cvLokasiBeli.setVisibility(View.GONE);

//            spWilayah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    if(i == 0){
//                        ongkir = 10000;
//                        alamatAntar = "Bandar Lampung";
//                    }else if(i == 1){
//                        ongkir = 20000;
//                        alamatAntar = "Metro";
//                    }else if(i == 2){
//                        ongkir = 30000;
//                        alamatAntar = "Lampung Barat";
//                    }else if(i == 3){
//                        ongkir = 40000;
//                        alamatAntar = "Lampung Selatan";
//                    }else if(i == 4){
//                        ongkir = 50000;
//                        alamatAntar = "Lampung Tengah";
//                    }else if(i == 5){
//                        ongkir = 60000;
//                        alamatAntar = "Lampung Timur";
//                    }else if(i == 6){
//                        ongkir = 70000;
//                        alamatAntar = "Lampung Utara";
//                    }else if(i == 7){
//                        ongkir = 80000;
//                        alamatAntar = "Mesuji";
//                    }else if(i == 8){
//                        ongkir = 90000;
//                        alamatAntar = "Pesawaran";
//                    }else if(i == 9){
//                        ongkir = 100000;
//                        alamatAntar = "Pringsewu";
//                    }else if(i == 10){
//                        ongkir = 110000;
//                        alamatAntar = "Tanggamus";
//                    }else if(i == 11){
//                        ongkir = 120000;
//                        alamatAntar = "Tulang Bawang";
//                    }else if(i == 12){
//                        ongkir = 130000;
//                        alamatAntar = "Tulang Bawang Barat";
//                    }else if(i == 13){
//                        ongkir = 140000;
//                        alamatAntar = "Way Kanan";
//                    }else if(i == 14){
//                        ongkir = 150000;
//                        alamatAntar = "Pesisir Barat";
//                    }
//
//                    DecimalFormat formatter = new DecimalFormat("#,###,###");
//                    String yourFormattedString = formatter.format(ongkir);
//                    yourFormattedString = yourFormattedString.replace(",", ".");
//                    tvRincianOngkos.setText("Rp. " + yourFormattedString);
//
//                    DecimalFormat formatter1 = new DecimalFormat("#,###,###");
//                    String yourFormattedString1 = formatter1.format(harga + ongkir);
//                    yourFormattedString1 = yourFormattedString1.replace(",", ".");
//                    tvTotalPembayaran.setText("Rp. " + yourFormattedString1);
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });


            lvKeranjang.setVisibility(View.VISIBLE);
            daftarBeli = new ArrayList<>();
            daftarJumlah = new ArrayList<>();
            daftarTempOngkir = new ArrayList<>();
            pakanLokasi = new ArrayList<>();

            daftarJumlah = intent.getIntegerArrayListExtra("JUMLAH");
            daftarPenjualArray = intent.getIntegerArrayListExtra("ID_PENJUAL_ARRAY");
            daftarDaerahPenjual = intent.getIntegerArrayListExtra("DAERAH_ID");
            daftarBerat = intent.getIntegerArrayListExtra("BERAT");

            for(int i = 0; i < daftarPenjualArray.size(); i++){
                daftarTempOngkir.add(new tempOngkir(daftarPenjualArray.get(i), -1, "", daftarDaerahPenjual.get(i), daftarBerat.get(i), daftarJumlah.get(i),0));
            }

            adapterLokasiPenjual = new DaftarLokasiPenjualAdapter(daftarTempOngkir, LokasiPengirimanActivity.this);
            lvPenjual.setAdapter(adapterLokasiPenjual);
            lvPenjual.setExpanded(true);

            ambilKeranjang();

            harga = intent.getIntExtra("TOTAL_HARGA", 0);
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String yourFormattedString = formatter.format(harga);
            yourFormattedString = yourFormattedString.replace(",", ".");
            tvTotalHarga.setText("Rp. " + yourFormattedString);

            btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tambahPakanLokasi();
                    ArrayList<Integer> intList = new ArrayList<>();
                    for(int i = 0 ; i < pakanLokasi.size() ; i++){
                        intList.add(pakanLokasi.get(i).getOngkir());
                    }
                    if(ambil == 1){
                        btnKonfirmasi.setVisibility(View.GONE);
                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(0);
                        yourFormattedString = yourFormattedString.replace(",", ".");
                        tvRincianOngkos.setText("Rp. " + yourFormattedString);

                        DecimalFormat formatter1 = new DecimalFormat("#,###,###");
                        String yourFormattedString1 = formatter1.format(harga + 0);
                        yourFormattedString1 = yourFormattedString1.replace(",", ".");
                        tvTotalPembayaran.setText("Rp. " + yourFormattedString1);

                        llInfoPembayaran.setVisibility(View.VISIBLE);
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                tvRincianHarga.setText(tvTotalHarga.getText());
                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                    }else{
                        if(intList.contains(-1)){
                            Toast.makeText(LokasiPengirimanActivity.this, "Harap atur semua lokasi pengiriman", Toast.LENGTH_SHORT).show();
                        }else{
                            btnKonfirmasi.setVisibility(View.GONE);

                            for(int i = 0 ; i < daftarPenjualArray.size() ; i++){
                                Log.d("PENJUAL_ARRAY", String.valueOf(daftarPenjualArray.get(i)));
                            }

                            DecimalFormat formatter = new DecimalFormat("#,###,###");
                            String yourFormattedString = formatter.format(ongkir*jumlah);
                            yourFormattedString = yourFormattedString.replace(",", ".");
                            tvRincianOngkos.setText("Rp. " + yourFormattedString);

                            DecimalFormat formatter1 = new DecimalFormat("#,###,###");
                            String yourFormattedString1 = formatter1.format(harga + (ongkir*jumlah));
                            yourFormattedString1 = yourFormattedString1.replace(",", ".");
                            tvTotalPembayaran.setText("Rp. " + yourFormattedString1);

                            llInfoPembayaran.setVisibility(View.VISIBLE);
                            scrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    tvRincianHarga.setText(tvTotalHarga.getText());
                                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });

                            hitungOngkirBanyak();
                        }
                    }
                }
            });
        }





        btnLanjutBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = getIntent();
                if(intent.getStringExtra("ASAL").equalsIgnoreCase("keranjang")){
                    ArrayList<Integer> intList = new ArrayList<>();
                    for(int i = 0 ; i < pakanLokasi.size() ; i++){
                        intList.add(pakanLokasi.get(i).getOngkir());
                    }
                    if(ambil == 1){
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(LokasiPengirimanActivity.this);
                        builder1.setMessage("Apakah anda yakin ingin membuat pesanan ini ?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yakin",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mProgressDialog = new ProgressDialog(LokasiPengirimanActivity.this);
                                        mProgressDialog.setMessage("Membuat Pesanan...");
                                        mProgressDialog.show();

                                        final String ALLOWED_CHARACTERS = "QWERTYUIOPASDFGHJKKLZXCVBNMM";
                                        final Random random = new Random();
                                        final StringBuilder sb = new StringBuilder(2);
                                        for(int i=0;i<2;++i){
                                            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
                                        }
                                        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
                                        final String kodePesanan = sb + formatter.format(new Date(System.currentTimeMillis()));
                                        for(int i = 0 ; i < daftarBeli.size() ; i++){
                                            Log.d("KERANJANG" + i, kodePesanan + " , " + String.valueOf(daftarBeli.get(i).getIdPenjual()) + " , " + SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getIdPengguna() + " , " + String.valueOf(daftarBeli.get(i).getId()) + " , " + String.valueOf(daftarBeli.get(i).getHarga()) + " , " + ongkir + " , " + harga + " , " + daftarJumlah.get(i) + " , " );
                                        }

                                        StringRequest stringRequestPesan = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.pesan,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject obj = new JSONObject(response);
//                                                        if(obj.getString("success").equalsIgnoreCase("Berhasil Tambah Pesanan")){
//
//                                                        }

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
                                                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getRememberToken());
                                                params.put("Accept", "application/json");

                                                return params;
                                            }

                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("kode_pesanan", kodePesanan);
                                                if(ambil == 1){
                                                    params.put("ongkir", String.valueOf(0));
                                                    params.put("total_bayar", String.valueOf(harga+0));
                                                }else{
                                                    params.put("ongkir", String.valueOf(ongkir));
                                                    params.put("total_bayar", String.valueOf(harga+(ongkir*jumlah)));
                                                }
                                                params.put("harga", String.valueOf(harga));

                                                params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getIdPengguna()));

                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(LokasiPengirimanActivity.this);
                                        requestQueue.add(stringRequestPesan).setRetryPolicy(new DefaultRetryPolicy(
                                                (int) TimeUnit.SECONDS.toMillis(5),
                                                0,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                        final ArrayList<Integer> daftarPenjual = new ArrayList<>();


                                        for(int i = 0 ; i < pakanLokasi.size() ; i++){
                                            final int finalI = i;
                                            StringRequest stringRequestDetailPesan = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.tambahDetailPesan,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject obj = new JSONObject(response);
                                                                hapusKeranjang(daftarBeli.get(finalI).getIdKeranjang());
//                                                            if(obj.getString("success").equalsIgnoreCase("Berhasil Tambah Pesanan")){
//
//                                                            }

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
                                                    params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getRememberToken());
                                                    params.put("Accept", "application/json");

                                                    return params;
                                                }

                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("kode_pesanan", kodePesanan);
                                                    params.put("id_penjual", String.valueOf(pakanLokasi.get(finalI).getIdPenjual()));
                                                    params.put("id_pembeli", String.valueOf(SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getIdPengguna()));
                                                    params.put("id_produk", String.valueOf(pakanLokasi.get(finalI).getId()));
                                                    params.put("harga", String.valueOf(pakanLokasi.get(finalI).getHarga()));
                                                    params.put("ambil", String.valueOf(ambil));

                                                    if(ambil == 1){
//                                                    if(daftarPenjual.size() > 0){
//                                                        for(int j = 0 ; j < daftarPenjual.size() ; j++){
//                                                            if(daftarBeli.get(finalI).getIdPenjual() == daftarPenjual.get(j)){
//                                                                params.put("ongkir", String.valueOf(0));
//                                                                params.put("total_keuntungan", String.valueOf(daftarBeli.get(finalI).getHarga() * daftarJumlah.get(finalI)));
//                                                            }else{
//                                                                params.put("ongkir", String.valueOf(0));
//                                                                params.put("total_keuntungan", String.valueOf((daftarBeli.get(finalI).getHarga() * daftarJumlah.get(finalI)) + 0));
//                                                            }
//                                                        }
//                                                    }else{
//                                                        daftarPenjual.add(daftarBeli.get(finalI).getIdPenjual());
//                                                        params.put("ongkir", String.valueOf(0));
//                                                        params.put("total_keuntungan", String.valueOf((daftarBeli.get(finalI).getHarga() * daftarJumlah.get(finalI)) + 0));
//                                                    }
                                                        params.put("ongkir", String.valueOf(0));
                                                        params.put("total_keuntungan", String.valueOf((pakanLokasi.get(finalI).getHarga() * pakanLokasi.get(finalI).getJumlah()) + 0));
                                                        params.put("alamat_antar", "Pembeli Mengambil Sendiri Pesanannya");
                                                        params.put("id_kurir", String.valueOf(4));
                                                    }else{
//                                                    if(daftarPenjual.size() > 0){
//                                                        for(int j = 0 ; j < daftarPenjual.size() ; j++){
//                                                            if(daftarBeli.get(finalI).getIdPenjual() == daftarPenjual.get(j)){
//                                                                params.put("ongkir", String.valueOf(0));
//                                                                params.put("total_keuntungan", String.valueOf(daftarBeli.get(finalI).getHarga() * daftarJumlah.get(finalI)));
//                                                            }else{
//                                                                params.put("ongkir", String.valueOf(ongkir));
//                                                                params.put("total_keuntungan", String.valueOf((daftarBeli.get(finalI).getHarga() * daftarJumlah.get(finalI)) + ongkir));
//                                                            }
//                                                        }
//                                                    }else{
//                                                        daftarPenjual.add(daftarBeli.get(finalI).getIdPenjual());
//                                                        params.put("ongkir", String.valueOf(ongkir));
//                                                        params.put("total_keuntungan", String.valueOf((daftarBeli.get(finalI).getHarga() * daftarJumlah.get(finalI)) + ongkir));
//                                                    }
                                                        params.put("ongkir", String.valueOf(pakanLokasi.get(finalI).getOngkir()));
                                                        params.put("total_keuntungan", String.valueOf((pakanLokasi.get(finalI).getHarga() * pakanLokasi.get(finalI).getJumlah()) + pakanLokasi.get(finalI).getOngkir()));
                                                        params.put("alamat_antar", pakanLokasi.get(finalI).getLokasiAntar());
                                                        params.put("id_kurir", String.valueOf(pakanLokasi.get(finalI).getIdKurir()));
                                                    }

                                                    params.put("jumlah", String.valueOf(pakanLokasi.get(finalI).getJumlah()));



                                                    return params;
                                                }
                                            };
                                            requestQueue.add(stringRequestDetailPesan).setRetryPolicy(new DefaultRetryPolicy(
                                                    (int) TimeUnit.SECONDS.toMillis(5),
                                                    0,
                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                            kirimListNotifikasi(kodePesanan, String.valueOf(pakanLokasi.get(finalI).getIdPenjual()), String.valueOf(pakanLokasi.get(finalI).getId()));
                                        }
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgressDialog.dismiss();
                                                Intent intent = new Intent(LokasiPengirimanActivity.this, PembayaranActivity.class);
                                                if(ambil == 1){
                                                    intent.putExtra("BAYAR", harga+0);
                                                    intent.putExtra("KODE_PESANAN", kodePesanan);
                                                }else {
                                                    intent.putExtra("BAYAR", harga+(ongkir*jumlah));
                                                    intent.putExtra("KODE_PESANAN", kodePesanan);
                                                }
                                                startActivity(intent);
                                            }
                                        }, 3000);

                                    }
                                });
                        builder1.setNegativeButton(
                                "Batal",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                    }else {
                        if (intList.contains(-1)) {
                            Toast.makeText(LokasiPengirimanActivity.this, "Harap atur semua lokasi pengiriman", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LokasiPengirimanActivity.this);
                            builder1.setMessage("Apakah anda yakin ingin membuat pesanan ini ?");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Yakin",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            mProgressDialog = new ProgressDialog(LokasiPengirimanActivity.this);
                                            mProgressDialog.setMessage("Membuat Pesanan...");
                                            mProgressDialog.show();

                                            final String ALLOWED_CHARACTERS = "QWERTYUIOPASDFGHJKKLZXCVBNMM";
                                            final Random random = new Random();
                                            final StringBuilder sb = new StringBuilder(2);
                                            for(int i=0;i<2;++i){
                                                sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
                                            }
                                            SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
                                            final String kodePesanan = sb + formatter.format(new Date(System.currentTimeMillis()));
                                            for(int i = 0 ; i < daftarBeli.size() ; i++){
                                                Log.d("KERANJANG" + i, kodePesanan + " , " + String.valueOf(daftarBeli.get(i).getIdPenjual()) + " , " + SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getIdPengguna() + " , " + String.valueOf(daftarBeli.get(i).getId()) + " , " + String.valueOf(daftarBeli.get(i).getHarga()) + " , " + ongkir + " , " + harga + " , " + daftarJumlah.get(i) + " , " );
                                            }

                                            StringRequest stringRequestPesan = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.pesan,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject obj = new JSONObject(response);
//                                                        if(obj.getString("success").equalsIgnoreCase("Berhasil Tambah Pesanan")){
//
//                                                        }

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
                                                    params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getRememberToken());
                                                    params.put("Accept", "application/json");

                                                    return params;
                                                }

                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("kode_pesanan", kodePesanan);
                                                    if(ambil == 1){
                                                        params.put("ongkir", String.valueOf(0));
                                                        params.put("total_bayar", String.valueOf(harga+0));
                                                    }else{
                                                        params.put("ongkir", String.valueOf(ongkir));
                                                        params.put("total_bayar", String.valueOf(harga+(ongkir*jumlah)));
                                                    }
                                                    params.put("harga", String.valueOf(harga));

                                                    params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getIdPengguna()));

                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(LokasiPengirimanActivity.this);
                                            requestQueue.add(stringRequestPesan).setRetryPolicy(new DefaultRetryPolicy(
                                                    (int) TimeUnit.SECONDS.toMillis(5),
                                                    0,
                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                            final ArrayList<Integer> daftarPenjual = new ArrayList<>();


                                            for(int i = 0 ; i < pakanLokasi.size() ; i++){
                                                final int finalI = i;
                                                StringRequest stringRequestDetailPesan = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.tambahDetailPesan,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                try {
                                                                    JSONObject obj = new JSONObject(response);
                                                                    hapusKeranjang(daftarBeli.get(finalI).getIdKeranjang());
//                                                            if(obj.getString("success").equalsIgnoreCase("Berhasil Tambah Pesanan")){
//
//                                                            }

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
                                                        params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getRememberToken());
                                                        params.put("Accept", "application/json");

                                                        return params;
                                                    }

                                                    @Override
                                                    protected Map<String, String> getParams() throws AuthFailureError {
                                                        Map<String, String> params = new HashMap<>();
                                                        params.put("kode_pesanan", kodePesanan);
                                                        params.put("id_penjual", String.valueOf(pakanLokasi.get(finalI).getIdPenjual()));
                                                        params.put("id_pembeli", String.valueOf(SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getIdPengguna()));
                                                        params.put("id_produk", String.valueOf(pakanLokasi.get(finalI).getId()));
                                                        params.put("harga", String.valueOf(pakanLokasi.get(finalI).getHarga()));
                                                        params.put("ambil", String.valueOf(ambil));

                                                        if(ambil == 1){
//                                                    if(daftarPenjual.size() > 0){
//                                                        for(int j = 0 ; j < daftarPenjual.size() ; j++){
//                                                            if(daftarBeli.get(finalI).getIdPenjual() == daftarPenjual.get(j)){
//                                                                params.put("ongkir", String.valueOf(0));
//                                                                params.put("total_keuntungan", String.valueOf(daftarBeli.get(finalI).getHarga() * daftarJumlah.get(finalI)));
//                                                            }else{
//                                                                params.put("ongkir", String.valueOf(0));
//                                                                params.put("total_keuntungan", String.valueOf((daftarBeli.get(finalI).getHarga() * daftarJumlah.get(finalI)) + 0));
//                                                            }
//                                                        }
//                                                    }else{
//                                                        daftarPenjual.add(daftarBeli.get(finalI).getIdPenjual());
//                                                        params.put("ongkir", String.valueOf(0));
//                                                        params.put("total_keuntungan", String.valueOf((daftarBeli.get(finalI).getHarga() * daftarJumlah.get(finalI)) + 0));
//                                                    }
                                                            params.put("ongkir", String.valueOf(0));
                                                            params.put("total_keuntungan", String.valueOf((pakanLokasi.get(finalI).getHarga() * pakanLokasi.get(finalI).getJumlah()) + 0));
                                                            params.put("alamat_antar", "Pembeli Mengambil Sendiri Pesanannya");
                                                            params.put("id_kurir", String.valueOf(4));
                                                        }else{
//                                                    if(daftarPenjual.size() > 0){
//                                                        for(int j = 0 ; j < daftarPenjual.size() ; j++){
//                                                            if(daftarBeli.get(finalI).getIdPenjual() == daftarPenjual.get(j)){
//                                                                params.put("ongkir", String.valueOf(0));
//                                                                params.put("total_keuntungan", String.valueOf(daftarBeli.get(finalI).getHarga() * daftarJumlah.get(finalI)));
//                                                            }else{
//                                                                params.put("ongkir", String.valueOf(ongkir));
//                                                                params.put("total_keuntungan", String.valueOf((daftarBeli.get(finalI).getHarga() * daftarJumlah.get(finalI)) + ongkir));
//                                                            }
//                                                        }
//                                                    }else{
//                                                        daftarPenjual.add(daftarBeli.get(finalI).getIdPenjual());
//                                                        params.put("ongkir", String.valueOf(ongkir));
//                                                        params.put("total_keuntungan", String.valueOf((daftarBeli.get(finalI).getHarga() * daftarJumlah.get(finalI)) + ongkir));
//                                                    }
                                                            params.put("ongkir", String.valueOf(pakanLokasi.get(finalI).getOngkir()));
                                                            params.put("total_keuntungan", String.valueOf((pakanLokasi.get(finalI).getHarga() * pakanLokasi.get(finalI).getJumlah()) + pakanLokasi.get(finalI).getOngkir()));
                                                            params.put("alamat_antar", pakanLokasi.get(finalI).getLokasiAntar());
                                                            params.put("id_kurir", String.valueOf(pakanLokasi.get(finalI).getIdKurir()));
                                                        }

                                                        params.put("jumlah", String.valueOf(pakanLokasi.get(finalI).getJumlah()));



                                                        return params;
                                                    }
                                                };
                                                requestQueue.add(stringRequestDetailPesan).setRetryPolicy(new DefaultRetryPolicy(
                                                        (int) TimeUnit.SECONDS.toMillis(5),
                                                        0,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                kirimListNotifikasi(kodePesanan, String.valueOf(pakanLokasi.get(finalI).getIdPenjual()), String.valueOf(pakanLokasi.get(finalI).getId()));
                                            }
                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mProgressDialog.dismiss();
                                                    Intent intent = new Intent(LokasiPengirimanActivity.this, PembayaranActivity.class);
                                                    if(ambil == 1){
                                                        intent.putExtra("BAYAR", harga+0);
                                                        intent.putExtra("KODE_PESANAN", kodePesanan);
                                                    }else {
                                                        intent.putExtra("BAYAR", harga+(ongkir*jumlah));
                                                        intent.putExtra("KODE_PESANAN", kodePesanan);
                                                    }
                                                    startActivity(intent);
                                                }
                                            }, 3000);

                                        }
                                    });
                            builder1.setNegativeButton(
                                    "Batal",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                        }
                    }


                }else if(intent.getStringExtra("ASAL").equalsIgnoreCase("beli")){

                    if(spWilayah.getSelectedItemPosition() == 0){
                        if(ambil == 1){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LokasiPengirimanActivity.this);
                            builder1.setMessage("Apakah anda yakin ingin membuat pesanan ini ?");
                            builder1.setCancelable(true);

                            final String ALLOWED_CHARACTERS = "QWERTYUIOPASDFGHJKKLZXCVBNMM";
                            final Random random = new Random();
                            final StringBuilder sb = new StringBuilder(2);
                            for(int i=0;i<2;++i){
                                sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
                            }
                            SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
                            final String kodePesanan = sb + formatter.format(new Date(System.currentTimeMillis()));

                            builder1.setPositiveButton(
                                    "Yakin",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            mProgressDialog = new ProgressDialog(LokasiPengirimanActivity.this);
                                            mProgressDialog.setMessage("Membuat Pesanan...");
                                            mProgressDialog.show();

                                            StringRequest stringRequestPesan = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.pesan,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject obj = new JSONObject(response);
//                                                        if(obj.getString("success").equalsIgnoreCase("Berhasil Tambah Pesanan")){
//
//                                                        }

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
                                                    params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getRememberToken());
                                                    params.put("Accept", "application/json");

                                                    return params;
                                                }

                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("kode_pesanan", kodePesanan);
                                                    if(ambil == 1){
                                                        params.put("ongkir", String.valueOf(0));
                                                        params.put("total_bayar", String.valueOf((harga*jumlah)+0));
                                                    }else{
                                                        params.put("ongkir", String.valueOf(ongkir));
                                                        params.put("total_bayar", String.valueOf((harga*jumlah)+(ongkir*jumlah)));
                                                    }
                                                    params.put("harga", String.valueOf(harga));

                                                    params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getIdPengguna()));

                                                    return params;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(LokasiPengirimanActivity.this);
                                            requestQueue.add(stringRequestPesan).setRetryPolicy(new DefaultRetryPolicy(
                                                    (int) TimeUnit.SECONDS.toMillis(5),
                                                    0,
                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                            StringRequest stringRequestDetailPesan = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.tambahDetailPesan,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject obj = new JSONObject(response);
//                                                            if(obj.getString("success").equalsIgnoreCase("Berhasil Tambah Pesanan")){
//
//                                                            }

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
                                                    params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getRememberToken());
                                                    params.put("Accept", "application/json");

                                                    return params;
                                                }

                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<>();
                                                    params.put("kode_pesanan", kodePesanan);
                                                    params.put("id_penjual", String.valueOf(idPenjual));
                                                    params.put("id_pembeli", String.valueOf(SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getIdPengguna()));
                                                    params.put("id_produk", String.valueOf(idPakan));
                                                    params.put("harga", String.valueOf(harga));
                                                    params.put("ambil", String.valueOf(ambil));
                                                    if(ambil == 1){
                                                        params.put("ongkir", String.valueOf(0));
                                                        params.put("total_keuntungan", String.valueOf((harga*jumlah) + 0));
                                                        params.put("alamat_antar", "Pembeli Mengambil Sendiri Pesanannya");
                                                        params.put("id_kurir", String.valueOf(4));
                                                    }else{
                                                        params.put("ongkir", String.valueOf(ongkir));
                                                        params.put("total_keuntungan", String.valueOf((harga*jumlah) + (ongkir*jumlah)));
                                                        params.put("alamat_antar", etDetailAlamat.getText().toString() + " ," + alamatAntar);
                                                        params.put("id_kurir", String.valueOf(idKurir));
                                                    }

                                                    params.put("jumlah", String.valueOf(jumlah));



                                                    return params;
                                                }
                                            };
                                            requestQueue.add(stringRequestDetailPesan).setRetryPolicy(new DefaultRetryPolicy(
                                                    (int) TimeUnit.SECONDS.toMillis(5),
                                                    0,
                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                            kirimListNotifikasi(kodePesanan, String.valueOf(idPenjual), String.valueOf(idPakan));

                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mProgressDialog.dismiss();
                                                    Intent intent = new Intent(LokasiPengirimanActivity.this, PembayaranActivity.class);
                                                    if(ambil == 1){
                                                        intent.putExtra("BAYAR", (harga*jumlah) + 0);
                                                        intent.putExtra("KODE_PESANAN", kodePesanan);
                                                    }else{
                                                        intent.putExtra("BAYAR", (harga*jumlah) + (ongkir*jumlah));
                                                        intent.putExtra("KODE_PESANAN", kodePesanan);
                                                    }
                                                    startActivity(intent);
                                                }
                                            }, 3000);
                                        }
                                    });

                            builder1.setNegativeButton(
                                    "Batal",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();


                        }else{
                            Toast.makeText(LokasiPengirimanActivity.this, "Harap Pilih Lokasi Pengiriman", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(LokasiPengirimanActivity.this);
                        builder1.setMessage("Apakah anda yakin ingin membuat pesanan ini ?");
                        builder1.setCancelable(true);

                        final String ALLOWED_CHARACTERS = "QWERTYUIOPASDFGHJKKLZXCVBNMM";
                        final Random random = new Random();
                        final StringBuilder sb = new StringBuilder(2);
                        for(int i=0;i<2;++i){
                            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
                        }
                        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
                        final String kodePesanan = sb + formatter.format(new Date(System.currentTimeMillis()));

                        builder1.setPositiveButton(
                                "Yakin",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mProgressDialog = new ProgressDialog(LokasiPengirimanActivity.this);
                                        mProgressDialog.setMessage("Membuat Pesanan...");
                                        mProgressDialog.show();

                                        StringRequest stringRequestPesan = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.pesan,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject obj = new JSONObject(response);
//                                                        if(obj.getString("success").equalsIgnoreCase("Berhasil Tambah Pesanan")){
//
//                                                        }

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
                                                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getRememberToken());
                                                params.put("Accept", "application/json");

                                                return params;
                                            }

                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("kode_pesanan", kodePesanan);
                                                if(ambil == 1){
                                                    params.put("ongkir", String.valueOf(0));
                                                    params.put("total_bayar", String.valueOf((harga*jumlah)+0));
                                                }else{
                                                    params.put("ongkir", String.valueOf(ongkir));
                                                    params.put("total_bayar", String.valueOf((harga*jumlah)+(ongkir*jumlah)));
                                                }
                                                params.put("harga", String.valueOf(harga));

                                                params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getIdPengguna()));

                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(LokasiPengirimanActivity.this);
                                        requestQueue.add(stringRequestPesan).setRetryPolicy(new DefaultRetryPolicy(
                                                (int) TimeUnit.SECONDS.toMillis(5),
                                                0,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                        StringRequest stringRequestDetailPesan = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.tambahDetailPesan,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject obj = new JSONObject(response);
//                                                            if(obj.getString("success").equalsIgnoreCase("Berhasil Tambah Pesanan")){
//
//                                                            }

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
                                                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getRememberToken());
                                                params.put("Accept", "application/json");

                                                return params;
                                            }

                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("kode_pesanan", kodePesanan);
                                                params.put("id_penjual", String.valueOf(idPenjual));
                                                params.put("id_pembeli", String.valueOf(SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getIdPengguna()));
                                                params.put("id_produk", String.valueOf(idPakan));
                                                params.put("harga", String.valueOf(harga));
                                                params.put("ambil", String.valueOf(ambil));
                                                if(ambil == 1){
                                                    params.put("ongkir", String.valueOf(0));
                                                    params.put("total_keuntungan", String.valueOf((harga*jumlah) + 0));
                                                    params.put("alamat_antar", "Pembeli Mengambil Sendiri Pesanannya");
                                                    params.put("id_kurir", String.valueOf(4));
                                                }else{
                                                    params.put("ongkir", String.valueOf(ongkir));
                                                    params.put("total_keuntungan", String.valueOf((harga*jumlah) + (ongkir*jumlah)));
                                                    params.put("alamat_antar", etDetailAlamat.getText().toString() + " ," + alamatAntar);
                                                    params.put("id_kurir", String.valueOf(idKurir));
                                                }

                                                params.put("jumlah", String.valueOf(jumlah));


                                                return params;
                                            }
                                        };
                                        requestQueue.add(stringRequestDetailPesan).setRetryPolicy(new DefaultRetryPolicy(
                                                (int) TimeUnit.SECONDS.toMillis(5),
                                                0,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                        kirimListNotifikasi(kodePesanan, String.valueOf(idPenjual), String.valueOf(idPakan));

                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgressDialog.dismiss();
                                                Intent intent = new Intent(LokasiPengirimanActivity.this, PembayaranActivity.class);
                                                if(ambil == 1){
                                                    intent.putExtra("BAYAR", (harga*jumlah) + 0);
                                                    intent.putExtra("KODE_PESANAN", kodePesanan);
                                                }else{
                                                    intent.putExtra("BAYAR", (harga*jumlah) + (ongkir*jumlah));
                                                    intent.putExtra("KODE_PESANAN", kodePesanan);
                                                }
                                                startActivity(intent);
                                            }
                                        }, 3000);
                                    }
                                });

                        builder1.setNegativeButton(
                                "Batal",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();


                    }

                }
            }
        });
    }

    public void daftarOngkir(){
        StringRequest stringRequest = new StringRequest(BuildConfig.host + BuildConfig.daftarOngkir + idPenjual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            JSONArray kecamatan = obj.getJSONArray("success");



                            for(int i = 0 ; i < kecamatan.length() ; i ++){
                                JSONObject detail = kecamatan.getJSONObject(i);
                                daftarOngkir.add(new Ongkir(
                                        detail.getString("nama_kabupaten"),
                                        detail.getString("nama_kecamatan"),
                                        Integer.parseInt(detail.getString("ongkir"))));

                            }

                            ArrayList<String> daftarNama = new ArrayList<>();

                            daftarNama.add("Pilih Lokasi Pengiriman");

                            for(int i = 0 ; i < daftarOngkir.size() ; i++){
                                daftarNama.add(daftarOngkir.get(i).getNamaKabupaten() + " (" + daftarOngkir.get(i).getNamaKecamatan() + ")");
                            }

                            adapterSpinner = new ArrayAdapter<String>(LokasiPengirimanActivity.this,
                                    android.R.layout.simple_spinner_item, daftarNama);

                            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spWilayah.setAdapter(adapterSpinner);
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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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
                            ArrayAdapter adapterKurir = new ArrayAdapter<String>(LokasiPengirimanActivity.this,
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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getRememberToken());
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

    public void ambilKeranjang(){
        StringRequest stringRequest = new StringRequest("http://inkubator.sikubis.com/mobile/daftarKeranjang/" + SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getIdPengguna(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            JSONArray barang = obj.getJSONArray("success");

//                            Toast.makeText(getContext(), events.getString(0), Toast.LENGTH_SHORT).show();

                            for(int i = 0 ; i < barang.length() ; i ++){
                                JSONObject product = barang.getJSONObject(i);
                                daftarBeli.add(new PakanKeranjang(
                                        Integer.parseInt(product.getString("id_produk")),
                                        product.getString("nama"),
                                        product.getString("satuan"),
                                        Integer.valueOf(product.getString("harga")),
                                        product.getString("foto"),
                                        daftarJumlah.get(i),
                                        product.getString("jenis"),
                                        Integer.parseInt(product.getString("id_penjual")),
                                        product.getInt("id"),
                                        Integer.parseInt(product.getString("daerah_id")),
                                        Integer.parseInt(product.getString("berat"))
                                ));
                            }

                            ItemPakanBeliAdapter adapter = new ItemPakanBeliAdapter(daftarBeli, LokasiPengirimanActivity.this);
                            lvKeranjang.setAdapter(adapter);
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

        RequestQueue requestQueue = Volley.newRequestQueue(LokasiPengirimanActivity.this);
        requestQueue.add(stringRequest);
    }

    public class Ongkir{
        String namaKabupaten, namaKecamatan;
        int ongkir;
        public Ongkir(String namaKabupaten, String namaKecamatan, int ongkir){
            this.namaKabupaten = namaKabupaten;
            this.namaKecamatan = namaKecamatan;
            this.ongkir = ongkir;
        }

        public int getOngkir() {
            return ongkir;
        }

        public String getNamaKabupaten() {
            return namaKabupaten;
        }

        public String getNamaKecamatan() {
            return namaKecamatan;
        }
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
                                daftarProvinsi.add(new Provinsi(Integer.parseInt(detail.getString("province_id")), detail.getString("province")));
                            }

                            ArrayList<String> daftarNama = new ArrayList<>();

                            daftarNama.add("Pilih Provinsi");

                            for(int i = 0 ; i < daftarProvinsi.size() ; i++){
                                daftarNama.add(daftarProvinsi.get(i).getNama());
                            }

                            ArrayAdapter adapterSpinnerProvinsi = new ArrayAdapter<String>(LokasiPengirimanActivity.this,
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
                                daftarKota.add(new Kota(Integer.parseInt(detail.getString("city_id")), detail.getString("city_name")));
                            }

                            Log.d("KOTA_SIZE",daftarKota.size()+"");
                            ArrayList<String> daftarNamaKota = new ArrayList<>();

                            daftarNamaKota.add("Pilih Kota");

                            for(int i = 0 ; i < daftarKota.size() ; i++){
                                daftarNamaKota.add(daftarKota.get(i).getNama());
                            }

                            ArrayAdapter adapterSpinnerKota = new ArrayAdapter<String>(LokasiPengirimanActivity.this,
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

                                daftarLayanan.add(new Layanan(service,ongkirnya,estimasi));

                                Log.d("MyLog", "Service : " + service + ", Estimasi :" + estimasi + ", Ongkir : " + ongkirnya);
                            }

                            ArrayList<String> daftarNamaLayanan = new ArrayList<>();

                            daftarNamaLayanan.add("Pilih Layanan");

                            for(int i = 0 ; i < daftarLayanan.size() ; i++){
                                daftarNamaLayanan.add(daftarLayanan.get(i).getService() + ", " + daftarLayanan.get(i).getEtd() + " hari, Ongkir : Rp." + daftarLayanan.get(i).getValue());
                            }

                            ArrayAdapter adapterSpinnerLayanan = new ArrayAdapter<String>(LokasiPengirimanActivity.this,
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
                        Toast.makeText(LokasiPengirimanActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put("weight", String.valueOf(totalBerat));
                params.put("courier", kurir);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LokasiPengirimanActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
//                String result=data.getStringExtra("result");
                int id = data.getIntExtra("ID",0);
                int ongkir = data.getIntExtra("ONGKIR",0);
                String lokasi = data.getStringExtra("LOKASI");
                int idKurir = data.getIntExtra("KURIR",0);
                daftarTempOngkir.get(id).setLokasiAntar(lokasi);
                daftarTempOngkir.get(id).setOngkir(ongkir);
                daftarTempOngkir.get(id).setIdKurir(idKurir);

                adapterLokasiPenjual.notifyDataSetChanged();
                for(int i = 0 ; i < daftarTempOngkir.size() ; i++){
                    Log.d("MANTAPU", String.valueOf(daftarTempOngkir.get(i).getOngkir() + " , " + daftarTempOngkir.get(i).getLokasiAntar()+ " , " + daftarTempOngkir.get(i).getIdKurir()));
                }
                tambahPakanLokasi();
                hitungOngkirBanyak();

                for (int i = 0; i < pakanLokasi.size() ; i++){
                    Log.d("HAHAHUHU", pakanLokasi.get(i).getIdPenjual() + " , " + pakanLokasi.get(i).getHarga()+ " , " +pakanLokasi.get(i).getOngkir()+ " , " +pakanLokasi.get(i).getJumlah()+ " , " +pakanLokasi.get(i).getLokasiAntar()+ " , " +pakanLokasi.get(i).getIdKurir());
                }

//                Toast.makeText(this, id + " , " + ongkir + " , " + lokasi, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void tambahPakanLokasi(){
//        pakanLokasi.clear();
//        ArrayList<Integer> intList = new ArrayList<>();
//        for(int i = 0 ; i < daftarBeli.size() ; i++){
//            if(pakanLokasi.size() > 0){
//
//                //CEK ADA ID UNIK
//                for(int j = 0 ; j < pakanLokasi.size() ; j++){
//                    intList.add(pakanLokasi.get(j).getIdPenjual());
//                }
//
//                for(int j = 0 ; j < daftarTempOngkir.size() ; j++) {
//                    if(intList.contains(daftarTempOngkir.get(j).getIdUnik())){
//                        if(!(pakanLokasi.get(i-1).getIdPenjual() == daftarTempOngkir.get(j).getIdUnik())){
//                            String haha = "";
//                            for(int x = 0 ; x < daftarBeli.size() ; x++){
//                                if(daftarBeli.get(x).getIdPenjual() == daftarTempOngkir.get(j).getIdUnik()){
//                                    haha = daftarTempOngkir.get(j).getLokasiAntar();
//                                }
//                            }
//                            pakanLokasi.add(new PakanLokasi(daftarBeli.get(i).getId(), daftarBeli.get(i).getNama(), daftarBeli.get(i).getSatuan(), daftarBeli.get(i).getHarga(), daftarBeli.get(i).getFoto(), daftarBeli.get(i).getJumlah(), daftarBeli.get(i).getJenis(), daftarBeli.get(i).getIdPenjual(), daftarBeli.get(i).getIdKeranjang(), 0, haha));
//                            Log.d("FIX", "jalan1");
//                        }
//                    }else{
//                        if(!(pakanLokasi.get(i-1).getIdPenjual() == daftarTempOngkir.get(j).getIdUnik())) {
//                            String haha = "";
//                            for(int x = 0 ; x < daftarBeli.size() ; x++){
//                                if(daftarBeli.get(x).getIdPenjual() == daftarTempOngkir.get(j).getIdUnik()){
//                                    haha = daftarTempOngkir.get(j).getLokasiAntar();
//                                }
//                            }
//                            pakanLokasi.add(new PakanLokasi(daftarBeli.get(i).getId(), daftarBeli.get(i).getNama(), daftarBeli.get(i).getSatuan(), daftarBeli.get(i).getHarga(), daftarBeli.get(i).getFoto(), daftarBeli.get(i).getJumlah(), daftarBeli.get(i).getJenis(), daftarBeli.get(i).getIdPenjual(), daftarBeli.get(i).getIdKeranjang(), daftarTempOngkir.get(j).getOngkir(), haha));
//                            Log.d("FIX", "jalan2");
//                        }
//                    }
//                }
//
//            }else{
//
//                for(int j = 0 ; j < daftarTempOngkir.size() ; j++){
//                    if(daftarBeli.get(i).getIdPenjual() == daftarTempOngkir.get(j).getIdUnik()){
//                        String haha = "";
//                        for(int x = 0 ; x < daftarTempOngkir.size() ; x++){
//                            haha = haha + daftarTempOngkir.get(x).getLokasiAntar();
//                        }
//                        pakanLokasi.add(new PakanLokasi(daftarBeli.get(i).getId(), daftarBeli.get(i).getNama(), daftarBeli.get(i).getSatuan(), daftarBeli.get(i).getHarga(), daftarBeli.get(i).getFoto(), daftarBeli.get(i).getJumlah(), daftarBeli.get(i).getJenis(), daftarBeli.get(i).getIdPenjual(), daftarBeli.get(i).getIdKeranjang(), daftarTempOngkir.get(j).getOngkir(), daftarTempOngkir.get(j).getLokasiAntar()));
//                        Log.d("FIX", "jalan3");
//                    }
//                }
//
//            }
//        }
//

        for(int i = 0 ; i < daftarTempOngkir.size() ; i++){
                Log.d("TEMPONGKIR", daftarTempOngkir.get(i).getIdUnik() + " | " + daftarTempOngkir.get(i).getOngkir() + " | " + daftarTempOngkir.get(i).getLokasiAntar()+ " | " + daftarTempOngkir.get(i).getIdKurir());
        }

        for(int i = 0 ; i < daftarBeli.size() ; i++){
            Log.d("TEMPBELI", daftarBeli.get(i).getIdPenjual() + " | " + daftarBeli.get(i).getId() + " | " + daftarBeli.get(i).getHarga() + " | " + daftarBeli.get(i).getJumlah());
        }


        //kosongin dulu
        pakanLokasi.clear();

        //daftar id penjual unik
        ArrayList<Integer> intList = new ArrayList<>();

        //duplicate dari daftarbeli
        for(int i = 0 ; i < daftarBeli.size() ; i++){
            //cek kalau kosong
            if(pakanLokasi.size() > 0){

                for(int j = 0 ; j < pakanLokasi.size() ; j++){
                    intList.add(pakanLokasi.get(j).getIdPenjual());
                }

                if(intList.contains(daftarBeli.get(i).getIdPenjual())){
                    for(int j = 0; j < daftarTempOngkir.size() ; j++){
                        if(daftarBeli.get(i).getIdPenjual() == daftarTempOngkir.get(j).getIdUnik()){
                            pakanLokasi.add(new PakanLokasi(daftarBeli.get(i).getId(), daftarBeli.get(i).getNama(), daftarBeli.get(i).getSatuan(), daftarBeli.get(i).getHarga(), daftarBeli.get(i).getFoto(), daftarBeli.get(i).getJumlah(), daftarBeli.get(i).getJenis(), daftarBeli.get(i).getIdPenjual(), daftarBeli.get(i).getIdKeranjang(), 0, daftarTempOngkir.get(j).getLokasiAntar(), daftarTempOngkir.get(j).getIdKurir()));
                            Log.d("FIX","haha");
                        }
                    }
                }else{
                    for(int j = 0; j < daftarTempOngkir.size() ; j++){
                        if(daftarBeli.get(i).getIdPenjual() == daftarTempOngkir.get(j).getIdUnik()){
                            pakanLokasi.add(new PakanLokasi(daftarBeli.get(i).getId(), daftarBeli.get(i).getNama(), daftarBeli.get(i).getSatuan(), daftarBeli.get(i).getHarga(), daftarBeli.get(i).getFoto(), daftarBeli.get(i).getJumlah(), daftarBeli.get(i).getJenis(), daftarBeli.get(i).getIdPenjual(), daftarBeli.get(i).getIdKeranjang(), daftarTempOngkir.get(j).getOngkir(), daftarTempOngkir.get(j).getLokasiAntar(), daftarTempOngkir.get(j).getIdKurir()));
                            Log.d("FIX","hehe");
                        }
                    }
                }

            }else{
                //ambil tarif ongkir
                for(int j = 0 ; j < daftarTempOngkir.size() ; j++){
                    if(daftarBeli.get(i).getIdPenjual() == daftarTempOngkir.get(j).getIdUnik()){
                        pakanLokasi.add(new PakanLokasi(daftarBeli.get(i).getId(), daftarBeli.get(i).getNama(), daftarBeli.get(i).getSatuan(), daftarBeli.get(i).getHarga(), daftarBeli.get(i).getFoto(), daftarBeli.get(i).getJumlah(), daftarBeli.get(i).getJenis(), daftarBeli.get(i).getIdPenjual(), daftarBeli.get(i).getIdKeranjang(), daftarTempOngkir.get(j).getOngkir(), daftarTempOngkir.get(j).getLokasiAntar(), daftarTempOngkir.get(j).getIdKurir()));
                        Log.d("FIX","huhu");
                    }
                }
            }
        }

        for (int i = 0 ; i < pakanLokasi.size() ; i++){
            Log.d("FIX", pakanLokasi.get(i).getIdPenjual() + " , " + pakanLokasi.get(i).getOngkir() + " , " + pakanLokasi.get(i).getLokasiAntar()+ " , " + pakanLokasi.get(i).getIdKurir());
        }
    }

    public void hitungOngkirBanyak(){
        ongkir = 0;
        for(int i = 0 ; i < pakanLokasi.size() ; i++){
            ongkir += pakanLokasi.get(i).getOngkir();
        }

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(ongkir*jumlah);
        yourFormattedString = yourFormattedString.replace(",", ".");
        tvRincianOngkos.setText("Rp. " + yourFormattedString);

        DecimalFormat formatter1 = new DecimalFormat("#,###,###");
        String yourFormattedString1 = formatter1.format((harga*jumlah) + (ongkir*jumlah));
        yourFormattedString1 = yourFormattedString1.replace(",", ".");
        tvTotalPembayaran.setText("Rp. " + yourFormattedString1);
    }

    public void hapusKeranjang(final int idKeranjang){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.hapusKeranjang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);
                            Log.d("HAPUS KERANJANG", String.valueOf(obj));

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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id_keranjang", String.valueOf(idKeranjang));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void kirimListNotifikasi(final String kodePesanannya, final String idPenjualnya, final String idProduknya){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.setListNotifikasi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);


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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(LokasiPengirimanActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_pengguna", idPenjualnya);
                params.put("id_pesanan", kodePesanannya);
                params.put("id_produk", idProduknya);
                params.put("isi","Ada Pesanan Masuk");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LokasiPengirimanActivity.this);
        requestQueue.add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(5),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
