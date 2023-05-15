package com.unila.inkubis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TambahProdukActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    EditText etNama, etHarga, etDeskripsi, etSatuan, etMinimum, etStok, etBerat, etKategori;
    ImageView ivFoto1, ivFoto2, ivFoto3;
    FrameLayout fl1, fl2, fl3;
    Spinner spStatus;
    Button btnTambah, btnTambahFoto, ganti1, ganti2, ganti3;
    HorizontalScrollView hsv;

    ArrayList<String> jenis, lokasi, status;
    String jenisTerpilih, lokasiTerpilih, statusTerpilih;
    private static final int CAMERA_REQUEST = 1888;
    int jumlah = 1;
    double lat, lng;
    Bitmap bitmapSaya, bitmapSaya2, bitmapSaya3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Form Jual Produk");

        spStatus = (Spinner) findViewById(R.id.sp_status);
        etNama = (EditText) findViewById(R.id.et_nama);
        etHarga = (EditText) findViewById(R.id.et_harga);
        etDeskripsi = (EditText) findViewById(R.id.et_deskripsi);
        btnTambah = (Button) findViewById(R.id.btn_tambah);
        etSatuan = (EditText) findViewById(R.id.et_satuan);
        etMinimum = (EditText) findViewById(R.id.et_minimum);
        etStok = (EditText) findViewById(R.id.et_stok);
        btnTambahFoto = (Button) findViewById(R.id.btn_tambah_foto);
        ivFoto1 = (ImageView) findViewById(R.id.iv_foto1);
        ivFoto2 = (ImageView) findViewById(R.id.iv_foto2);
        ivFoto3 = (ImageView) findViewById(R.id.iv_foto3);
        fl1 = (FrameLayout) findViewById(R.id.fl_1);
        fl2 = (FrameLayout) findViewById(R.id.fl_2);
        fl3 = (FrameLayout) findViewById(R.id.fl_3);
        ganti1 = (Button) findViewById(R.id.ganti_1);
        ganti2 = (Button) findViewById(R.id.ganti_2);
        ganti3 = (Button) findViewById(R.id.ganti_3);
        hsv = (HorizontalScrollView) findViewById(R.id.scrollView);
        etBerat = findViewById(R.id.et_berat);
        etKategori = findViewById(R.id.et_kategori);

        btnTambahFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(jumlah <= 3){
                    Intent intent = CropImage.activity()
                            .setAspectRatio(3,2)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .getIntent(TambahProdukActivity.this);
                    startActivityForResult(intent,jumlah);
                }else{
                    Toast.makeText(TambahProdukActivity.this, "Maksimal 3 foto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ganti1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CropImage.activity()
                        .setAspectRatio(3,2)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .getIntent(TambahProdukActivity.this);
                startActivityForResult(intent,4);
            }
        });

        ganti2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CropImage.activity()
                        .setAspectRatio(3,2)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .getIntent(TambahProdukActivity.this);
                startActivityForResult(intent,5);
            }
        });

        ganti3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CropImage.activity()
                        .setAspectRatio(3,2)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .getIntent(TambahProdukActivity.this);
                startActivityForResult(intent,6);
            }
        });

        status = new ArrayList<>();
        status.add("pre sale");
        status.add("siap antar");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(adapter2);


        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                statusTerpilih = status.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmapSaya == null){
                    Toast.makeText(TambahProdukActivity.this, "Harap tambah foto produk", Toast.LENGTH_SHORT).show();
                }else{
                    prosesJual();
                }
            }
        });

    }



    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        if (reqCode == 1) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                ivFoto1.setImageURI(resultUri);
                fl1.setVisibility(View.VISIBLE);
                jumlah++;
                bitmapSaya = ((BitmapDrawable) ivFoto1.getDrawable()).getBitmap();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }

        if (reqCode == 2) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                ivFoto2.setImageURI(resultUri);
                fl2.setVisibility(View.VISIBLE);
                hsv.postDelayed(new Runnable() {
                    public void run() {
                        hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    }
                }, 100L);
                jumlah++;
                bitmapSaya2 = ((BitmapDrawable) ivFoto2.getDrawable()).getBitmap();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (reqCode == 3) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                ivFoto3.setImageURI(resultUri);
                fl3.setVisibility(View.VISIBLE);
                hsv.postDelayed(new Runnable() {
                    public void run() {
                        hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    }
                }, 100L);
                jumlah++;
                bitmapSaya3 = ((BitmapDrawable) ivFoto3.getDrawable()).getBitmap();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (reqCode == 4) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                ivFoto1.setImageURI(resultUri);
                bitmapSaya = ((BitmapDrawable) ivFoto1.getDrawable()).getBitmap();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (reqCode == 5) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                ivFoto2.setImageURI(resultUri);
                bitmapSaya2 = ((BitmapDrawable) ivFoto2.getDrawable()).getBitmap();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (reqCode == 6) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                ivFoto3.setImageURI(resultUri);
                bitmapSaya3 = ((BitmapDrawable) ivFoto3.getDrawable()).getBitmap();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    public void prosesJual(){


        final String nama = etNama.getText().toString();
        final int harga = Integer.parseInt(etHarga.getText().toString());
        final String satuan = etSatuan.getText().toString();
        final String deskripsi = etDeskripsi.getText().toString();
        final int minimum = Integer.parseInt(etMinimum.getText().toString());
        final int stok = Integer.parseInt(etStok.getText().toString());
        final int berat = Integer.parseInt(etBerat.getText().toString());
        final String kategori = etKategori.getText().toString();

        if(TextUtils.isEmpty(etNama.getText().toString()) || TextUtils.isEmpty(etSatuan.getText().toString()) || TextUtils.isEmpty(etDeskripsi.getText().toString()) || TextUtils.isEmpty(etKategori.getText().toString())){
            Toast.makeText(this, "Harap Isi Semua Borang", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Jual Produk...");
        progressDialog.show();

        Log.d("TAMBAH", nama + " , " + harga + " , " + satuan + " , " + deskripsi + " , " + statusTerpilih + " , " + jenisTerpilih + " , " + lokasiTerpilih);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BuildConfig.host + BuildConfig.tambahProduk,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));

                            if(obj.getString("success").equalsIgnoreCase("Berhasil Tambah Produk")){
                                Toast.makeText(getApplicationContext(), obj.getString("success"), Toast.LENGTH_SHORT).show();
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_CANCELED, returnIntent);
                                finish();
                            }else if(obj.getString("success").equalsIgnoreCase("Gagal Tambah Produk")){
                                Toast.makeText(getApplicationContext(), obj.getString("success"), Toast.LENGTH_SHORT).show();
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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(TambahProdukActivity.this).getPengguna().getRememberToken());
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

                params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(TambahProdukActivity.this).getPengguna().getIdPengguna()));
                params.put("nama", nama);
                params.put("harga", String.valueOf(harga));
                params.put("satuan", satuan);
                params.put("status", statusTerpilih);
                params.put("lokasi", "");
                params.put("deskripsi", deskripsi);
                params.put("minimum", String.valueOf(minimum));
                params.put("stok", String.valueOf(stok));
                params.put("kategori", kategori);
                params.put("berat", String.valueOf(berat));

                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if((jumlah-1) == 1){
                    params.put("foto", new DataPart(SharedPrefManager.getInstance(TambahProdukActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSaya)));
                }else if((jumlah-1) == 2){
                    params.put("foto", new DataPart(SharedPrefManager.getInstance(TambahProdukActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSaya)));
                    params.put("foto2", new DataPart(SharedPrefManager.getInstance(TambahProdukActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSaya2)));
                }else if((jumlah-1) == 3){
                    params.put("foto", new DataPart(SharedPrefManager.getInstance(TambahProdukActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSaya)));
                    params.put("foto2", new DataPart(SharedPrefManager.getInstance(TambahProdukActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSaya2)));
                    params.put("foto3", new DataPart(SharedPrefManager.getInstance(TambahProdukActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSaya3)));
                }


                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest).setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
