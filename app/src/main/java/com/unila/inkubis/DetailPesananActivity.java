package com.unila.inkubis;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.Model.ItemPesanan;
import com.unila.inkubis.Model.Pesanan;
import com.unila.inkubis.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class DetailPesananActivity extends AppCompatActivity {

    TextView tvStatus, tvTotalBayar, tvIdentitas, tvStatusTransaksi, tvSebesar, tvTotalHarga, tvTotalTarif, tvProduk, tvAlamat, tvNamaIdentitas, tvKeuntungan, tvNamaProduk, tvKategoriProduk, tvPengiriman, tvNamaKurir, tvNoResi;
    EditText etNoResi;
    LinearLayout llBelum, llBukti, llAmbil, llLoading, llPembayaran,llNoResi,llAmbilResi;
    CircleImageView ivIdentitas;
    ImageView ivBukti, ivFotoProduk, ivCopy;
    Button btnUploadBukti, btnAmbilUlang, btnKonfirmasiBukti, btnAction, btnAmbil, btnTanya, btnBatal;
    RelativeLayout rlTelepon, rlSms;
    ProgressDialog mProgressDialog;
    CardView cvPengiriman, cvNoResi;
    private ArrayList<Pesanan> daftarPesanan;

    double latToko = 0, lngToko = 0;
    int totalkeuntungan, ongkir, idPembeli, idPenjual, idProduk, ambil, totalBayar, harga;

    String idPesanan, status, statusBayar, foto, alamat,namaKurir, fotoPenjual, namaPenjual, telpPenjual, fotoPembeli, namaPembeli, telpPembeli, pathToFile, nameFinal, noResi;

    Bitmap bitmapSaya;
    private static final int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Pesanan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvStatus = (TextView) findViewById(R.id.tv_status);
        tvTotalBayar = (TextView) findViewById(R.id.tv_total_bayar);
        tvIdentitas = (TextView) findViewById(R.id.tv_identitas);
        llBelum = (LinearLayout) findViewById(R.id.ll_belum);
        ivBukti = (ImageView) findViewById(R.id.iv_bukti);
        btnUploadBukti = (Button) findViewById(R.id.btn_upload_bukti);
        btnAmbilUlang = (Button) findViewById(R.id.btn_ambil_ulang);
        btnKonfirmasiBukti = (Button) findViewById(R.id.btn_konfirmasi_bukti);
        btnAction = (Button) findViewById(R.id.btn_action);
        tvStatusTransaksi = (TextView) findViewById(R.id.tv_status_transaksi);
        tvSebesar = (TextView) findViewById(R.id.tv_sebesar);
        llBukti = (LinearLayout) findViewById(R.id.ll_bukti);
        tvTotalHarga = (TextView) findViewById(R.id.tv_total_harga);
        tvTotalTarif = (TextView) findViewById(R.id.tv_total_tarif);
        tvProduk = (TextView) findViewById(R.id.tv_produk);
        tvAlamat = (TextView) findViewById(R.id.tv_alamat);
        tvNamaKurir = (TextView) findViewById(R.id.tv_nama_kurir);
        ivIdentitas = (CircleImageView) findViewById(R.id.iv_foto_identitas);
        tvNamaIdentitas = (TextView) findViewById(R.id.tv_nama_identitas);
        tvKeuntungan = (TextView) findViewById(R.id.tv_keuntungan);
        rlTelepon = (RelativeLayout) findViewById(R.id.rl_telepon);
        rlSms = (RelativeLayout) findViewById(R.id.rl_sms);
        tvNamaProduk = (TextView) findViewById(R.id.tv_nama_produk);
        tvKategoriProduk = (TextView) findViewById(R.id.tv_kategori);
        ivFotoProduk = (ImageView) findViewById(R.id.iv_foto_produk);
        llAmbil = (LinearLayout) findViewById(R.id.ll_ambil);
        btnAmbil = (Button) findViewById(R.id.btn_ambil);
        btnTanya = (Button) findViewById(R.id.btn_tanya);
        tvPengiriman = (TextView) findViewById(R.id.tv_pengiriman);
        cvPengiriman = (CardView) findViewById(R.id.cv_pengiriman);
        cvNoResi = (CardView) findViewById(R.id.cv_no_resi);
        btnBatal = (Button) findViewById(R.id.btn_batal);
        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        ivCopy = (ImageView) findViewById(R.id.iv_copy);
        llPembayaran = (LinearLayout) findViewById(R.id.ll_pembayaran);
        tvNoResi = (TextView) findViewById(R.id.tv_no_resi);
        llAmbilResi = (LinearLayout) findViewById(R.id.ll_ambil_resi);
        etNoResi = (EditText) findViewById(R.id.et_no_resi);
        llNoResi = (LinearLayout) findViewById(R.id.ll_no_resi);


        llAmbil.setVisibility(View.GONE);

        Intent intent = getIntent();

        daftarPesanan = new ArrayList<>();

        idPesanan = intent.getStringExtra("ID_PESANAN");
        idProduk = intent.getIntExtra("ID_PRODUK",0);

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getIdPengguna() == idPenjual){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailPesananActivity.this);
                    builder1.setMessage("Untuk membatalkan pesanan, anda harus mendapatkan izin pembeli terlebih dahulu, Silahkan hubungi pembeli terlebih dahulu !");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Sudah",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mProgressDialog = new ProgressDialog(DetailPesananActivity.this);
                                    mProgressDialog.setMessage("Membatalkan Pesanan ...");
                                    mProgressDialog.show();
                                    mProgressDialog.setCancelable(false);
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.ubahStatusBatal,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    mProgressDialog.dismiss();
                                                    try {
                                                        JSONObject obj = new JSONObject(response);

                                                        Toast.makeText(DetailPesananActivity.this, obj.getString("success"), Toast.LENGTH_SHORT).show();
                                                        if(obj.getString("success").equalsIgnoreCase("Pesanan Dibatalkan")){
                                                            ambilDetailPesanan();
                                                            kirimListNotifikasi(idPesanan, String.valueOf(idPembeli), String.valueOf(idProduk), "Pesanan Dibatalkan");
                                                            kirimListNotifikasi(idPesanan, String.valueOf(idPenjual), String.valueOf(idProduk), "Pesanan Dibatalkan");
                                                        }

                                                        ambilDetailPesanan();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(DetailPesananActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }) {

                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            Map<String, String>  params = new HashMap<String, String>();
                                            params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getRememberToken());
                                            params.put("Accept", "application/json");

                                            return params;
                                        }

                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("id_penjual", String.valueOf(idPenjual));
                                            params.put("kode_pesanan", idPesanan);
                                            params.put("id_pembeli", String.valueOf(idPembeli));
                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(DetailPesananActivity.this);
                                    requestQueue.add(stringRequest);

                                }
                            });

                    builder1.setNegativeButton(
                            "Hubungi Pembeli",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    StringBuilder sb = new StringBuilder(telpPembeli);
                                    sb = sb.deleteCharAt(0);
                                    final StringBuilder finalSb = sb;
                                    Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+62" + finalSb + "&text=Saya ingin membatalkan pesanan dengan kode pesan  " + idPesanan + ", dikarenakan....., terima kasih");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailPesananActivity.this);
                    builder1.setMessage("Untuk membatalkan pesanan, anda harus mendapatkan izin penjual terlebih dahulu, Silahkan hubungi penjual terlebih dahulu !");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Sudah",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mProgressDialog = new ProgressDialog(DetailPesananActivity.this);
                                    mProgressDialog.setMessage("Membatalkan Pesanan ...");
                                    mProgressDialog.show();
                                    mProgressDialog.setCancelable(false);
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.ubahStatusBatal,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    mProgressDialog.dismiss();
                                                    try {
                                                        JSONObject obj = new JSONObject(response);

                                                        Toast.makeText(DetailPesananActivity.this, obj.getString("success"), Toast.LENGTH_SHORT).show();
                                                        if(obj.getString("success").equalsIgnoreCase("Pesanan Dibatalkan")){
                                                            ambilDetailPesanan();
                                                            kirimListNotifikasi(idPesanan, String.valueOf(idPembeli), String.valueOf(idProduk), "Pesanan Dibatalkan");
                                                            kirimListNotifikasi(idPesanan, String.valueOf(idPenjual), String.valueOf(idProduk), "Pesanan Dibatalkan");
                                                        }

                                                        ambilDetailPesanan();
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(DetailPesananActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }) {

                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            Map<String, String>  params = new HashMap<String, String>();
                                            params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getRememberToken());
                                            params.put("Accept", "application/json");

                                            return params;
                                        }

                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("id_penjual", String.valueOf(idPenjual));
                                            params.put("kode_pesanan", idPesanan);
                                            params.put("id_pembeli", String.valueOf(idPembeli));
                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(DetailPesananActivity.this);
                                    requestQueue.add(stringRequest);
                                }
                            });

                    builder1.setNegativeButton(
                            "Hubungi Penjual",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    StringBuilder sb = new StringBuilder(telpPenjual);
                                    sb = sb.deleteCharAt(0);
                                    final StringBuilder finalSb = sb;
                                    Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+62" + finalSb + "&text=Saya ingin membatalkan pesanan dengan kode pesan  " + idPesanan + ", dikarenakan....., terima kasih");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });

        ambilProduk();

        tvStatusTransaksi.setText("Status Transaksi (" + intent.getStringExtra("ID_PESANAN") + ")");
        ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("KODE_PESANAN", idPesanan);
                clipboard.setPrimaryClip(clip);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Kode pesanan berhasil disalin", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        ambilDetailPesanan();

        btnUploadBukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchPictureTakerAction();
            }
        });

        btnAmbilUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchPictureTakerAction();
            }
        });

        btnKonfirmasiBukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmapSaya == null){
                    Toast.makeText(DetailPesananActivity.this, "Harap Ambil Gambar Bukti Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }else{
                    simpanBukti();
                }
            }
        });

    }

    public void dispatchPictureTakerAction(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 100);
    }

    public File createPhotoFile(){
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try{
            image = File.createTempFile(name, ".jpeg", storageDir);
        }catch (IOException e){

        }
        return image;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            bitmapSaya = photo;
//            ivBukti.setImageBitmap(photo);
//            ivBukti.setVisibility(View.VISIBLE);
//            btnUploadBukti.setVisibility(View.GONE);
//            llBukti.setVisibility(View.VISIBLE);
//        }
//        if(requestCode == 1){
//            if(resultCode == RESULT_OK){
//                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
//                ivBukti.setImageBitmap(bitmap);
//                ivBukti.setVisibility(View.VISIBLE);
//                bitmapSaya = bitmap;
//                btnUploadBukti.setVisibility(View.GONE);
//                llBukti.setVisibility(View.VISIBLE);
//            }
//        }
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                bitmapSaya = bitmap;

                //displaying selected image to imageview
                ivBukti.setImageBitmap(bitmap);
                ivBukti.setVisibility(View.VISIBLE);
                btnUploadBukti.setVisibility(View.GONE);
                llBukti.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public void simpanBukti(){
        mProgressDialog = new ProgressDialog(DetailPesananActivity.this);
        mProgressDialog.setMessage("Mengupload Bukti Bayar(Sekitar 5 Detik) ...");
        mProgressDialog.show();

        Log.d("TAMBAH",  idPesanan);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BuildConfig.host + BuildConfig.ubahStatusPesan,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        try {

                            JSONObject obj = new JSONObject(new String(response.data));

                            if(obj.getString("success").equalsIgnoreCase("Berhasil Ubah Status Pesanan")){
                                Toast.makeText(getApplicationContext(), obj.getString("success"), Toast.LENGTH_SHORT).show();
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_CANCELED, returnIntent);
                                finish();
                            }else if(obj.getString("error").equalsIgnoreCase("Gagal Ubah Status Pesanan")){
                                Toast.makeText(getApplicationContext(), obj.getString("error"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");
                return params;
            }

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id_pesanan", idPesanan);
                params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getIdPengguna()));
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            public Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("foto", new DataPart(SharedPrefManager.getInstance(DetailPesananActivity.this).getPesanan().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSaya)));System.out.println("Here 6");
                return params;
            }


        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest).setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5000);
    }

    public void ambilProduk(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.ambilDetailProduk,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject obj = new JSONObject(response);
                            JSONObject objProduk = new JSONObject(obj.getString("success"));


                            Picasso.get()
                                    .load("http://inkubator.sikubis.com/uploads/file/" + objProduk.getString("foto"))
                                    .into(ivFotoProduk);

                            tvNamaProduk.setText(objProduk.getString("nama_produk"));
                            tvKategoriProduk.setText(objProduk.getString("kategori"));

                            fotoPenjual = objProduk.getString("foto_penjual");
                            namaPenjual = objProduk.getString("nama_penjual");
                            telpPenjual = objProduk.getString("telp_penjual");
                            idPenjual = objProduk.getInt("id_penjual");
                            latToko = Double.parseDouble(objProduk.getString("lat_toko"));
                            lngToko = Double.parseDouble(objProduk.getString("lng_toko"));

                            Picasso.get()
                                    .load("http://inkubator.sikubis.com/uploads/file//" + fotoPenjual)
                                    .into(ivIdentitas);

                            tvNamaIdentitas.setText(namaPenjual);

                            rlTelepon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (ActivityCompat.checkSelfPermission(DetailPesananActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return;
                                    }
                                    StringBuilder sb = new StringBuilder(telpPenjual);
                                    sb = sb.deleteCharAt(0);

                                    final StringBuilder finalSb = sb;
                                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+62" + finalSb)));
                                }
                            });

                            rlSms.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    StringBuilder sb = new StringBuilder(telpPenjual);
                                    sb = sb.deleteCharAt(0);
                                    final StringBuilder finalSb = sb;
                                    Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+62" + finalSb + "&text=");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
                            });

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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_produk", String.valueOf(idProduk));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailPesananActivity.this);
        requestQueue.add(stringRequest);
    }


    //Mengambil Detail Pesanan
    public void ambilDetailPesanan(){
        final ProgressDialog progressDialog = new ProgressDialog(DetailPesananActivity.this);
        progressDialog.setMessage("Mengambil Pesanan...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.ambilDetailPesanan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject objPesanan = new JSONObject(obj.getString("success"));

                            totalkeuntungan = Integer.parseInt(objPesanan.getString("total_keuntungan"));
                            status = objPesanan.getString("status");
                            idPembeli = objPesanan.getInt("id_pembeli");
                            ambil = Integer.parseInt(objPesanan.getString("ambil"));
                            idPenjual = objPesanan.getInt("id_penjual");


                            if(ambil == 1){
                                tvPengiriman.setVisibility(View.GONE);
                                cvPengiriman.setVisibility(View.GONE);
                            }

                            statusBayar = objPesanan.getString("status_bayar");
                            ongkir = Integer.parseInt(objPesanan.getString("ongkir"));



                            if(idPenjual == SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getIdPengguna()){
                                tvKeuntungan.setVisibility(View.VISIBLE);
                                DecimalFormat formatter = new DecimalFormat("#,###,###");
                                String yourFormattedString1 = formatter.format(totalkeuntungan);
                                String yourFormattedString2 = formatter.format(totalkeuntungan - (totalkeuntungan * 0.05));
                                yourFormattedString1 = yourFormattedString1.replace("," , ".");
                                yourFormattedString2 = yourFormattedString2.replace("," , ".");
                                tvKeuntungan.setText("Rp. " + yourFormattedString1 + " - 5% (pendapatan transaksi)");
                                btnAction.setText("Kirim Pesanan");
                                tvIdentitas.setText("Identitas Pembeli");
                                llBelum.setVisibility(View.GONE);
                                tvSebesar.setText("Pendapatan :");
                                llBukti.setVisibility(View.GONE);
                                btnUploadBukti.setVisibility(View.GONE);
                                llNoResi.setVisibility(View.GONE);

                                fotoPembeli = objPesanan.getString("foto_pembeli");
                                namaPembeli = objPesanan.getString("nama_pembeli");
                                telpPembeli = objPesanan.getString("telp_pembeli");

                                Picasso.get()
                                        .load("http://inkubator.sikubis.com/uploads/file//" + fotoPembeli)
                                        .into(ivIdentitas);

                                tvNamaIdentitas.setText(namaPembeli);
                                String yourFormattedString3 = formatter.format(ongkir);
                                yourFormattedString3 = yourFormattedString3.replace("," , ".");
                                tvTotalBayar.setText("Rp. " + yourFormattedString2);
                                tvTotalTarif.setText("Rp. " + yourFormattedString3);

                                rlTelepon.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (ActivityCompat.checkSelfPermission(DetailPesananActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            // TODO: Consider calling
                                            //    ActivityCompat#requestPermissions
                                            // here to request the missing permissions, and then overriding
                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                            //                                          int[] grantResults)
                                            // to handle the case where the user grants the permission. See the documentation
                                            // for ActivityCompat#requestPermissions for more details.
                                            return;
                                        }
                                        StringBuilder sb = new StringBuilder(telpPembeli);
                                        sb = sb.deleteCharAt(0);

                                        final StringBuilder finalSb = sb;
                                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+62" + finalSb)));
                                    }
                                });

                                rlSms.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        StringBuilder sb = new StringBuilder(telpPembeli);
                                        sb = sb.deleteCharAt(0);
                                        final StringBuilder finalSb = sb;
                                        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+62" + finalSb + "&text=");
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }
                                });

                            }else{


                                btnAction.setText("Konfirmasi Pesanan Diterima");
                                tvIdentitas.setText("Identitas Penjual");
                                tvSebesar.setText("Total Bayar :");
                                llNoResi.setVisibility(View.GONE);





                                DecimalFormat formatter = new DecimalFormat("#,###,###");
                                String yourFormattedString1 = formatter.format(totalkeuntungan);
                                String yourFormattedString2 = formatter.format(ongkir);
                                yourFormattedString1 = yourFormattedString1.replace("," , ".");
                                yourFormattedString2 = yourFormattedString2.replace("," , ".");
                                tvTotalBayar.setText("Rp. " + yourFormattedString1);
                                tvTotalTarif.setText("Rp. " + yourFormattedString2);



                            }



                            if(objPesanan.isNull("foto")){
                                llBelum.setVisibility(View.VISIBLE);
                                ivBukti.setVisibility(View.GONE);
                                llBukti.setVisibility(View.GONE);
                                llLoading.setVisibility(View.GONE);
                                btnUploadBukti.setVisibility(View.VISIBLE);
                                llNoResi.setVisibility(View.GONE);
                                llAmbilResi.setVisibility(View.GONE);

                                if(idPenjual == SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getIdPengguna()){
                                    tvStatus.setText("Menunggu Pembeli Membayar Pesanan");
                                    llBelum.setVisibility(View.GONE);
                                    btnUploadBukti.setVisibility(View.GONE);
                                    btnBatal.setVisibility(View.VISIBLE);
                                    llNoResi.setVisibility(View.GONE);
                                    llAmbilResi.setVisibility(View.GONE);
                                }else{
                                    tvStatus.setText("Harap Melakukan Pembayaran dan Upload Bukti Transfer");
                                    btnBatal.setVisibility(View.VISIBLE);
                                    llNoResi.setVisibility(View.GONE);
                                    llAmbilResi.setVisibility(View.GONE);
                                }

                                btnAction.setVisibility(View.GONE);
                            }else{
                                llLoading.setVisibility(View.VISIBLE);
                                if(statusBayar.equalsIgnoreCase("belum")){
                                    tvStatus.setText("Menunggu Konfirmasi Pembayaran oleh Admin");
                                    ivBukti.setVisibility(View.VISIBLE);
                                    btnAction.setVisibility(View.GONE);
                                    llNoResi.setVisibility(View.GONE);
                                    llAmbilResi.setVisibility(View.GONE);
                                }else{
                                    if(idPenjual == SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getIdPengguna()){
                                        if(status.equalsIgnoreCase("belum bayar")){
                                            tvStatus.setText("Menunggu Konfirmasi Pembayaran oleh Admin");
                                            btnAction.setVisibility(View.GONE);
                                            btnBatal.setVisibility(View.VISIBLE);
                                            llNoResi.setVisibility(View.GONE);
                                            llAmbilResi.setVisibility(View.GONE);
                                        }else if(status.equalsIgnoreCase("diproses")){
                                            btnBatal.setVisibility(View.GONE);
                                            if(ambil == 0){
                                                tvStatus.setText("Harap Kirim Pesanan Ke Konsumen");
                                                btnAction.setVisibility(View.VISIBLE);
                                                llNoResi.setVisibility(View.VISIBLE);
                                                llAmbilResi.setVisibility(View.GONE);
                                            }else{
                                                tvStatus.setText("Menunggu Pembeli Mengambil Produk Di Lokasi Anda");
                                                btnAction.setVisibility(View.GONE);
                                                llNoResi.setVisibility(View.GONE);

                                            }
                                            btnAction.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    final String noResi = etNoResi.getText().toString();

                                                    if(TextUtils.isEmpty(etNoResi.getText().toString())){
                                                        Toast.makeText(DetailPesananActivity.this, "Harap isi No Resi Pengiriman", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                    mProgressDialog = new ProgressDialog(DetailPesananActivity.this);
                                                    mProgressDialog.setMessage("Mengirim Pesanan ...");
                                                    mProgressDialog.show();
                                                    mProgressDialog.setCancelable(false);
                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.ubahStatusDikirim,
                                                            new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    mProgressDialog.dismiss();
                                                                    try {
                                                                        JSONObject obj = new JSONObject(response);

                                                                        Toast.makeText(DetailPesananActivity.this, obj.getString("success"), Toast.LENGTH_SHORT).show();
                                                                        if(obj.getString("success").equalsIgnoreCase("Berhasil Dikirim")){
                                                                            ambilDetailPesanan();
                                                                            kirimListNotifikasi(idPesanan, String.valueOf(idPembeli), String.valueOf(idProduk), "Pesanan Anda Sedang Dikirim");
                                                                        }

                                                                        ambilDetailPesanan();
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            },
                                                            new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    Toast.makeText(DetailPesananActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            }) {

                                                        @Override
                                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                                            Map<String, String>  params = new HashMap<String, String>();
                                                            params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getRememberToken());
                                                            params.put("Accept", "application/json");

                                                            return params;
                                                        }

                                                        @Override
                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                            Map<String, String> params = new HashMap<>();
                                                            params.put("id_penjual", String.valueOf(SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getIdPengguna()));
                                                            params.put("kode_pesanan", idPesanan);
                                                            params.put("id_pembeli", String.valueOf(idPembeli));
                                                            params.put("no_resi", noResi);
                                                            return params;
                                                        }
                                                    };
                                                    RequestQueue requestQueue = Volley.newRequestQueue(DetailPesananActivity.this);
                                                    requestQueue.add(stringRequest);

                                                }
                                            });
                                        }else if(status.equalsIgnoreCase("dikirim")){
                                            btnBatal.setVisibility(View.GONE);
                                            tvStatus.setText("Pesanan Sedang Dikirim");
                                            btnAction.setText("Konfirmasi Barang Telah Dikirim");
                                            noResi = objPesanan.getString("no_resi");
                                            llAmbilResi.setVisibility(View.VISIBLE);
                                            tvNoResi.setText(noResi);
                                            btnAction.setVisibility(View.GONE);
                                            llNoResi.setVisibility(View.GONE);
                                            btnAction.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    mProgressDialog = new ProgressDialog(DetailPesananActivity.this);
                                                    mProgressDialog.setMessage("Konfirmasi Barang Telah Sampai ...");
                                                    mProgressDialog.show();
                                                    mProgressDialog.setCancelable(false);
                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.ubahStatusDiterima,
                                                            new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    mProgressDialog.dismiss();
                                                                    try {
                                                                        JSONObject obj = new JSONObject(response);

                                                                        Toast.makeText(DetailPesananActivity.this, obj.getString("success"), Toast.LENGTH_SHORT).show();

                                                                        if(obj.getString("success").equalsIgnoreCase("Berhasil Diterima dan Saldo diteruskan ke penjual")){
                                                                            ambilDetailPesanan();
                                                                            kirimListNotifikasi(idPesanan, String.valueOf(idPembeli), String.valueOf(idProduk), "Pesanan Telah Diterima");
                                                                            kirimListNotifikasi(idPesanan, String.valueOf(idPenjual), String.valueOf(idProduk), "Pesanan Telah Diterima Oleh Pembeli");
                                                                        }

                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            },
                                                            new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    Toast.makeText(DetailPesananActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            }) {

                                                        @Override
                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                            Map<String, String> params = new HashMap<>();
                                                            params.put("id_penjual", String.valueOf(SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getIdPengguna()));
                                                            params.put("kode_pesanan", idPesanan);
                                                            params.put("id_pembeli", String.valueOf(idPembeli));
                                                            return params;
                                                        }
                                                    };
                                                    RequestQueue requestQueue = Volley.newRequestQueue(DetailPesananActivity.this);
                                                    requestQueue.add(stringRequest);


                                                }
                                            });
                                        }else if(status.equalsIgnoreCase("diterima") || status.equalsIgnoreCase("diambil")){
                                            btnBatal.setVisibility(View.GONE);
                                            tvStatus.setText("Pesanan Selesai");
                                            btnAction.setVisibility(View.GONE);
                                            llAmbil.setVisibility(View.GONE);
                                            llNoResi.setVisibility(View.GONE);
                                            noResi = objPesanan.getString("no_resi");
                                            llAmbilResi.setVisibility(View.VISIBLE);
                                            tvNoResi.setText(noResi);
                                        }
                                    }else{
                                        if(status.equalsIgnoreCase("belum bayar")){
                                            tvStatus.setText("Menunggu Konfirmasi Pembayaran oleh Admin");
                                            btnAction.setVisibility(View.GONE);
                                            llBukti.setVisibility(View.VISIBLE);
                                            btnBatal.setVisibility(View.VISIBLE);
                                            llNoResi.setVisibility(View.GONE);
                                            llAmbilResi.setVisibility(View.GONE);
                                        }else if(status.equalsIgnoreCase("diproses")){
                                            btnBatal.setVisibility(View.GONE);
                                            if(ambil == 0){
                                                tvStatus.setText("Pembayaran Sudah Diterima Oleh Admin, Menunggu Penjual Mengirim Pesanan");
                                                btnAction.setVisibility(View.GONE);
                                                llBukti.setVisibility(View.GONE);
                                                llNoResi.setVisibility(View.GONE);
                                                llAmbilResi.setVisibility(View.GONE);
                                            }else{
                                                tvStatus.setText("Harap Ambil Pesanan Anda Di Lokasi Penjual");
                                                btnAction.setVisibility(View.GONE);
                                                llBukti.setVisibility(View.GONE);
                                                llAmbil.setVisibility(View.VISIBLE);
                                                llNoResi.setVisibility(View.GONE);
                                                llAmbilResi.setVisibility(View.GONE);

                                                btnTanya.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        if(latToko == 0 && lngToko == 0){
                                                            Toast.makeText(DetailPesananActivity.this, "Penjual belum mengatur lokasi toko miliknya", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                                    Uri.parse("http://maps.google.com/maps?daddr=" + latToko + "," + lngToko));
                                                            startActivity(intent);
                                                        }
                                                    }
                                                });

                                                btnAmbil.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(DetailPesananActivity.this);
                                                        builder1.setMessage("Apakah anda sudah mengambil produk pada pesanan ini ?");
                                                        builder1.setCancelable(true);

                                                        builder1.setPositiveButton(
                                                                "Sudah Diambil",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                        mProgressDialog = new ProgressDialog(DetailPesananActivity.this);
                                                                        mProgressDialog.setMessage("Konfirmasi Barang Telah Diambil ...");
                                                                        mProgressDialog.show();
                                                                        mProgressDialog.setCancelable(false);
                                                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.ubahStatusDiambil,
                                                                                new Response.Listener<String>() {
                                                                                    @Override
                                                                                    public void onResponse(String response) {
                                                                                        mProgressDialog.dismiss();
                                                                                        try {
                                                                                            JSONObject obj = new JSONObject(response);

                                                                                            Toast.makeText(DetailPesananActivity.this, obj.getString("success"), Toast.LENGTH_SHORT).show();

                                                                                            if(obj.getString("success").equalsIgnoreCase("Berhasil Diterima dan Saldo diteruskan ke penjual")){
                                                                                                ambilDetailPesanan();
                                                                                                kirimListNotifikasi(idPesanan, String.valueOf(idPembeli), String.valueOf(idProduk), "Pesanan Telah Diambil");
                                                                                                kirimListNotifikasi(idPesanan, String.valueOf(idPenjual), String.valueOf(idProduk), "Pesanan Telah Diambil Oleh Pembeli");
                                                                                            }

                                                                                        } catch (JSONException e) {
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                    }
                                                                                },
                                                                                new Response.ErrorListener() {
                                                                                    @Override
                                                                                    public void onErrorResponse(VolleyError error) {
                                                                                        Toast.makeText(DetailPesananActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                }) {

                                                                            @Override
                                                                            public Map<String, String> getHeaders() throws AuthFailureError {
                                                                                Map<String, String>  params = new HashMap<String, String>();
                                                                                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getRememberToken());
                                                                                params.put("Accept", "application/json");

                                                                                return params;
                                                                            }

                                                                            @Override
                                                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                                                Map<String, String> params = new HashMap<>();
                                                                                params.put("id_pembeli", String.valueOf(SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getIdPengguna()));
                                                                                params.put("kode_pesanan", idPesanan);
                                                                                params.put("id_penjual", String.valueOf(idPenjual));
                                                                                return params;
                                                                            }
                                                                        };
                                                                        RequestQueue requestQueue = Volley.newRequestQueue(DetailPesananActivity.this);
                                                                        requestQueue.add(stringRequest);

                                                                    }
                                                                });

                                                        builder1.setNegativeButton(
                                                                "Belum",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {

                                                                    }
                                                                });

                                                        AlertDialog alert11 = builder1.create();
                                                        alert11.show();
                                                    }
                                                });
                                            }
                                        }else if(status.equalsIgnoreCase("dikirim")){
                                            btnBatal.setVisibility(View.GONE);
                                            tvStatus.setText("Pesanan Sedang Dikirim, Harap Konfirmasi Terima Barang Ketika Barang Telah Sampai");
                                            btnAction.setVisibility(View.VISIBLE);
                                            noResi = objPesanan.getString("no_resi");
                                            llAmbilResi.setVisibility(View.VISIBLE);
                                            tvNoResi.setText(noResi);
                                            btnAction.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    mProgressDialog = new ProgressDialog(DetailPesananActivity.this);
                                                    mProgressDialog.setMessage("Konfirmasi Barang Telah Sampai ...");
                                                    mProgressDialog.show();
                                                    mProgressDialog.setCancelable(false);
                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.ubahStatusDiterima,
                                                            new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    mProgressDialog.dismiss();
                                                                    try {
                                                                        JSONObject obj = new JSONObject(response);

                                                                        Toast.makeText(DetailPesananActivity.this, obj.getString("success"), Toast.LENGTH_SHORT).show();

                                                                        if(obj.getString("success").equalsIgnoreCase("Berhasil Diterima dan Saldo diteruskan ke penjual")){
                                                                            ambilDetailPesanan();
                                                                            kirimListNotifikasi(idPesanan, String.valueOf(idPembeli), String.valueOf(idProduk), "Pesanan Telah Diterima");
                                                                            kirimListNotifikasi(idPesanan, String.valueOf(idPenjual), String.valueOf(idProduk), "Pesanan Telah Diterima Oleh Pembeli");
                                                                        }

                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            },
                                                            new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                    Toast.makeText(DetailPesananActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                                                }
                                                            }) {

                                                        @Override
                                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                                            Map<String, String>  params = new HashMap<String, String>();
                                                            params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getRememberToken());
                                                            params.put("Accept", "application/json");

                                                            return params;
                                                        }

                                                        @Override
                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                            Map<String, String> params = new HashMap<>();
                                                            params.put("id_pembeli", String.valueOf(SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getIdPengguna()));
                                                            params.put("kode_pesanan", idPesanan);
                                                            params.put("id_penjual", String.valueOf(idPenjual));
                                                            return params;
                                                        }
                                                    };
                                                    RequestQueue requestQueue = Volley.newRequestQueue(DetailPesananActivity.this);
                                                    requestQueue.add(stringRequest);
                                                }
                                            });
                                            llBukti.setVisibility(View.GONE);
                                        }else if(status.equalsIgnoreCase("diterima")){
                                            btnBatal.setVisibility(View.GONE);
                                            tvStatus.setText("Pesanan Selesai");
                                            btnAction.setVisibility(View.GONE);
                                            llBukti.setVisibility(View.GONE);
                                            llAmbil.setVisibility(View.GONE);
                                            llNoResi.setVisibility(View.GONE);
                                            noResi = objPesanan.getString("no_resi");
                                            llAmbilResi.setVisibility(View.VISIBLE);
                                            tvNoResi.setText(noResi);
                                        }else if(status.equalsIgnoreCase("diambil")){
                                            btnBatal.setVisibility(View.GONE);
                                            tvStatus.setText("Pesanan Selesai");
                                            btnAction.setVisibility(View.GONE);
                                            llBukti.setVisibility(View.GONE);
                                            llAmbil.setVisibility(View.GONE);
                                            llNoResi.setVisibility(View.GONE);
                                            llAmbilResi.setVisibility(View.GONE);

                                        }
                                    }
                                }
                                Picasso.get()
                                        .load("http://inkubator.sikubis.com/uploads/file/" + objPesanan.getString("foto"))
                                        .into(ivBukti, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                llLoading.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onError(Exception e) {

                                            }
                                        });
                                llBelum.setVisibility(View.GONE);
                                btnUploadBukti.setVisibility(View.GONE);
                            }

                            if (status.equalsIgnoreCase("batal")) {
                                tvStatus.setText("Pesanan DiBatalkan");
                                btnBatal.setVisibility(View.GONE);
                                btnAction.setVisibility(View.GONE);
                                llBukti.setVisibility(View.GONE);
                                llPembayaran.setVisibility(View.GONE);
                                llNoResi.setVisibility(View.GONE);
                                llAmbilResi.setVisibility(View.GONE);
                            }

                            JSONArray produks = objPesanan.getJSONArray("produk");

                            String produk = "";
                            int harga = 0;

                            for(int j = 0 ; j < produks.length() ; j++){
                                JSONObject produkku = produks.getJSONObject(j);
                                if(j == 0){
                                    produk = produk + "\t - " + produkku.getString("nama_produk") + " (" + produkku.getString("jumlah") + " " + produkku.getString("satuan") + ")";
                                }else{
                                    produk = produk + "\n\t - " + produkku.getString("nama_produk") + " (" + produkku.getString("jumlah") + " " + produkku.getString("satuan") + ")";
                                }
                                harga += Integer.parseInt(produkku.getString("harga")) * Integer.parseInt(produkku.getString("jumlah"));
                            }
                            DecimalFormat formatter = new DecimalFormat("#,###,###");
                            String yourFormattedString3 = formatter.format(harga);
                            yourFormattedString3 = yourFormattedString3.replace("," , ".");

                            tvProduk.setText(produk);
                            tvTotalHarga.setText("Rp. " + yourFormattedString3);
                            alamat = objPesanan.getString("alamat");
                            namaKurir = objPesanan.getString("nama_kurir");
                            tvAlamat.setText(alamat);
                            tvNamaKurir.setText(namaKurir);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailPesananActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getIdPengguna()));
                params.put("id_pesanan", idPesanan);
                params.put("id_produk", String.valueOf(idProduk));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailPesananActivity.this);
        requestQueue.add(stringRequest);
    }

    public void kirimListNotifikasi(final String kodePesanannya, final String idPenjualnya, final String idProduknya, final String isi){
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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(DetailPesananActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_pengguna", idPenjualnya);
                params.put("id_pesanan", kodePesanannya);
                params.put("id_produk", idProduknya);
                params.put("isi",isi);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DetailPesananActivity.this);
        requestQueue.add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(5),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                return true;
            case R.id.refresh :
                ambilDetailPesanan();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
