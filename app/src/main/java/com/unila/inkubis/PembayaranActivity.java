package com.unila.inkubis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.os.Environment.getExternalStoragePublicDirectory;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.Model.Pesanan;
import com.unila.inkubis.R;


import org.json.JSONException;
import org.json.JSONObject;

public class PembayaranActivity extends AppCompatActivity {

    Button btnUploadBukti, btnAmbilUlang, btnUpload;
    LinearLayout llBukti;
    ImageView ivBukti;
    TextView tvStatusProses, tvTotalBayar;
    ProgressDialog mProgressDialog;

    String pathToFile, name, nameFinal;
    Bitmap bitmapSaya;
    String kodePesanan;
    private static final int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Pembayaran");

        btnUploadBukti = (Button) findViewById(R.id.btn_upload_bukti);
        ivBukti = (ImageView) findViewById(R.id.iv_bukti);
        llBukti = (LinearLayout) findViewById(R.id.ll_bukti);
        btnAmbilUlang = (Button) findViewById(R.id.btn_ambil_ulang);
        btnUpload = (Button) findViewById(R.id.btn_konfirmasi_bukti);
        tvStatusProses = (TextView) findViewById(R.id.tv_status_proses);
        tvTotalBayar = (TextView) findViewById(R.id.tv_total_bayar);

        name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        Intent intent = getIntent();
        kodePesanan = intent.getStringExtra("KODE_PESANAN");
        DecimalFormat formatter1 = new DecimalFormat("#,###,###");
        String yourFormattedString1 = formatter1.format(intent.getIntExtra("BAYAR", 0));
        yourFormattedString1 = yourFormattedString1.replace(",", ".");
        tvTotalBayar.setText("Rp. " + yourFormattedString1);

        llBukti.setVisibility(View.GONE);
        ivBukti.setVisibility(View.GONE);

        btnUploadBukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                dispatchPictureTakerAction();
            }
        });

        btnAmbilUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                dispatchPictureTakerAction();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmapSaya == null){
                    Toast.makeText(PembayaranActivity.this, "Harap Tambah Foto Bukti", Toast.LENGTH_SHORT).show();
                }else {
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
        mProgressDialog = new ProgressDialog(PembayaranActivity.this);
        mProgressDialog.setMessage("Mengupload Bukti Bayar(Sekitar 5 Detik) ...");
        mProgressDialog.show();

        Log.d("TAMBAH",  kodePesanan);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BuildConfig.host + BuildConfig.ubahStatusPesan,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        try {

                            JSONObject obj = new JSONObject(new String(response.data));

                            if(obj.getString("success").equalsIgnoreCase("Berhasil Ubah Status Pesanan")){
                                Toast.makeText(getApplicationContext(), obj.getString("success"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(PembayaranActivity.this).getPengguna().getRememberToken());
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

                params.put("id_pesanan", kodePesanan);
                params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(PembayaranActivity.this).getPengguna().getIdPengguna()));
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            public Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("foto", new DataPart(SharedPrefManager.getInstance(PembayaranActivity.this).getPesanan().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSaya)));System.out.println("Here 6");
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PembayaranActivity.this);
        builder1.setMessage("Anda harus menyelesaikan pembayaran pesanan ini, detail pembayaran anda terdapat pada menu pesanan");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Oke",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                AlertDialog.Builder builder1 = new AlertDialog.Builder(PembayaranActivity.this);
                builder1.setMessage("Anda harus menyelesaikan pembayaran pesanan ini, detail pembayaran anda terdapat pada menu pesanan");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Oke",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                                finish();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
