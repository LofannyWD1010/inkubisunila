package com.unila.inkubis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.Model.Pengguna;
import com.unila.inkubis.Model.Pesanan;
import com.unila.inkubis.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadBuktiActivity extends AppCompatActivity {
    CircleImageView ivFoto;
    RelativeLayout rlAmbilFoto, rlPilihFoto;
    Button btnSimpan;
    ProgressDialog progressDialog;
    int harga,idPengguna,ongkir,totalBayar;
    String idPesanan,foto,status;

    private static final int CAMERA_REQUEST = 1888;
    Bitmap bitmapSaya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_bukti);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Bukti Pembayaran");

        ivFoto = (CircleImageView) findViewById(R.id.iv_foto);
        rlAmbilFoto = (RelativeLayout) findViewById(R.id.rl_ambil_foto);
        rlPilihFoto = (RelativeLayout) findViewById(R.id.rl_pilih_foto);
        btnSimpan = (Button) findViewById(R.id.btn_simpan);

        Pesanan pesanan = SharedPrefManager.getInstance(UploadBuktiActivity.this).getPesanan();

        final Intent intent = getIntent();
        harga = intent.getIntExtra("harga", 0);
        idPengguna = intent.getIntExtra("id_pengguna",0);
        ongkir = intent.getIntExtra("ongkir",0);
        totalBayar = intent.getIntExtra("total_bayar",0);
        idPesanan = intent.getStringExtra("kode_pesanan");
        foto = intent.getStringExtra("foto");



        Glide.with(this)
                .asBitmap()
                .load("http://inkubator.sikubis.com/uploads/file/" + pesanan.getFoto())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ivFoto.setImageBitmap(resource);
                        bitmapSaya = resource;
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

//        BitmapDrawable drawable = (BitmapDrawable) ivFoto.getDrawable();
//        bitmapSaya = drawable.getBitmap();

        rlAmbilFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        rlPilihFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmapSaya == null){
                    Toast.makeText(UploadBuktiActivity.this, "Harap tambah bukti pembayaran", Toast.LENGTH_SHORT).show();
                }else{
                    uploadBukti();
                }
            }
        });

    }

    public void uploadBukti(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Upload Bukti...");
        progressDialog.show();

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BuildConfig.host + BuildConfig.ubahStatusPesan,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));

                            if(obj.getString("success").equalsIgnoreCase("Berhasil Update")){
                                Toast.makeText(getApplicationContext(), obj.getString("success"), Toast.LENGTH_SHORT).show();
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_CANCELED, returnIntent);
                                finish();
                            }else if(obj.getString("error").equalsIgnoreCase("Gagal Upload")){
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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(UploadBuktiActivity.this).getPengguna().getRememberToken());
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
                params.put("ongkir", String.valueOf(ongkir));
                params.put("harga", String.valueOf(harga));
                params.put("total_bayar", String.valueOf(totalBayar));
                params.put("id_pengguna", String.valueOf(idPengguna));

                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("foto", new DataPart(SharedPrefManager.getInstance(UploadBuktiActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSaya)));
                return params;
            }


        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest).setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ivFoto.setImageBitmap(photo);
            bitmapSaya = photo;
        }

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                bitmapSaya = bitmap;

                //displaying selected image to imageview
                ivFoto.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
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
