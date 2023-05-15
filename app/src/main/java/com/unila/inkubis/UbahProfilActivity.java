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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.Model.Pengguna;
import com.unila.inkubis.R;

public class UbahProfilActivity extends AppCompatActivity {

    EditText etNama, etNoTelp, etAlamat, etEmail, etPassword;
    Spinner spDaerah;
    CircleImageView ivFoto;
    RelativeLayout rlAmbilFoto, rlPilihFoto;
    Button btnSimpan;
    ProgressDialog progressDialog;
    ArrayList<String> lokasi;

    private static final int CAMERA_REQUEST = 1888;
    int REQUEST_PLACE_PICKER = 2, daerahId;
    Bitmap bitmapSaya;
    String daerahPilih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profil);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ubah Profil");
        
        lokasi = new ArrayList<>();

        etNama = (EditText) findViewById(R.id.et_nama);
        etNoTelp = (EditText) findViewById(R.id.et_no_telp);
        etAlamat = (EditText) findViewById(R.id.et_alamat);
        ivFoto = (CircleImageView) findViewById(R.id.iv_foto);
        spDaerah = (Spinner) findViewById(R.id.sp_daerah);
        rlAmbilFoto = (RelativeLayout) findViewById(R.id.rl_ambil_foto);
        rlPilihFoto = (RelativeLayout) findViewById(R.id.rl_pilih_foto);
        btnSimpan = (Button) findViewById(R.id.btn_simpan);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        Pengguna pengguna = SharedPrefManager.getInstance(UbahProfilActivity.this).getPengguna();

        etNama.setText(pengguna.getNamaPengguna());
        etNoTelp.setText(pengguna.getNoTelpPengguna());
        etAlamat.setText(pengguna.getAlamatPengguna());
        etEmail.setText(pengguna.getEmailPengguna());
        etPassword.setText(pengguna.getPasswordPengguna());

        if(pengguna.getDaerahPengguna().equalsIgnoreCase("Bandar Lampung")){
            lokasi.add("Bandar Lampung");
            lokasi.add("Metro");
            lokasi.add("Lampung Barat");
            lokasi.add("Lampung Selatan");
            lokasi.add("Lampung Tengah");
            lokasi.add("Lampung Timur");
            lokasi.add("Lampung Utara");
            lokasi.add("Mesuji");
            lokasi.add("Pesawaran");
            lokasi.add("Pringsewu");
            lokasi.add("Tanggamus");
            lokasi.add("Tulang Bawang");
            lokasi.add("Tulang Bawang Barat");
            lokasi.add("Way Kanan");
            lokasi.add("Pesisir Barat");
        }else if(pengguna.getDaerahPengguna().equalsIgnoreCase("Metro")){
            lokasi.add("Metro");
            lokasi.add("Bandar Lampung");
            lokasi.add("Lampung Barat");
            lokasi.add("Lampung Selatan");
            lokasi.add("Lampung Tengah");
            lokasi.add("Lampung Timur");
            lokasi.add("Lampung Utara");
            lokasi.add("Mesuji");
            lokasi.add("Pesawaran");
            lokasi.add("Pringsewu");
            lokasi.add("Tanggamus");
            lokasi.add("Tulang Bawang");
            lokasi.add("Tulang Bawang Barat");
            lokasi.add("Way Kanan");
            lokasi.add("Pesisir Barat");
        }else if(pengguna.getDaerahPengguna().equalsIgnoreCase("Lampung Barat")){
            lokasi.add("Lampung Barat");
            lokasi.add("Metro");
            lokasi.add("Bandar Lampung");
            lokasi.add("Lampung Selatan");
            lokasi.add("Lampung Tengah");
            lokasi.add("Lampung Timur");
            lokasi.add("Lampung Utara");
            lokasi.add("Mesuji");
            lokasi.add("Pesawaran");
            lokasi.add("Pringsewu");
            lokasi.add("Tanggamus");
            lokasi.add("Tulang Bawang");
            lokasi.add("Tulang Bawang Barat");
            lokasi.add("Way Kanan");
            lokasi.add("Pesisir Barat");
        }else if(pengguna.getDaerahPengguna().equalsIgnoreCase("Lampung Selatan")){
            lokasi.add("Lampung Selatan");
            lokasi.add("Metro");
            lokasi.add("Bandar Lampung");
            lokasi.add("Lampung Barat");
            lokasi.add("Lampung Tengah");
            lokasi.add("Lampung Timur");
            lokasi.add("Lampung Utara");
            lokasi.add("Mesuji");
            lokasi.add("Pesawaran");
            lokasi.add("Pringsewu");
            lokasi.add("Tanggamus");
            lokasi.add("Tulang Bawang");
            lokasi.add("Tulang Bawang Barat");
            lokasi.add("Way Kanan");
            lokasi.add("Pesisir Barat");
        }else if(pengguna.getDaerahPengguna().equalsIgnoreCase("Lampung Tengah")){
            lokasi.add("Lampung Tengah");
            lokasi.add("Metro");
            lokasi.add("Bandar Lampung");
            lokasi.add("Lampung Barat");
            lokasi.add("Lampung Selatan");
            lokasi.add("Lampung Timur");
            lokasi.add("Lampung Utara");
            lokasi.add("Mesuji");
            lokasi.add("Pesawaran");
            lokasi.add("Pringsewu");
            lokasi.add("Tanggamus");
            lokasi.add("Tulang Bawang");
            lokasi.add("Tulang Bawang Barat");
            lokasi.add("Way Kanan");
            lokasi.add("Pesisir Barat");
        }else if(pengguna.getDaerahPengguna().equalsIgnoreCase("Lampung Timur")){
            lokasi.add("Lampung Timur");
            lokasi.add("Metro");
            lokasi.add("Bandar Lampung");
            lokasi.add("Lampung Barat");
            lokasi.add("Lampung Selatan");
            lokasi.add("Lampung Tengah");
            lokasi.add("Lampung Utara");
            lokasi.add("Mesuji");
            lokasi.add("Pesawaran");
            lokasi.add("Pringsewu");
            lokasi.add("Tanggamus");
            lokasi.add("Tulang Bawang");
            lokasi.add("Tulang Bawang Barat");
            lokasi.add("Way Kanan");
            lokasi.add("Pesisir Barat");
        }else if(pengguna.getDaerahPengguna().equalsIgnoreCase("Lampung Utara")){
            lokasi.add("Lampung Utara");
            lokasi.add("Metro");
            lokasi.add("Bandar Lampung");
            lokasi.add("Lampung Barat");
            lokasi.add("Lampung Selatan");
            lokasi.add("Lampung Tengah");
            lokasi.add("Lampung Timur");
            lokasi.add("Mesuji");
            lokasi.add("Pesawaran");
            lokasi.add("Pringsewu");
            lokasi.add("Tanggamus");
            lokasi.add("Tulang Bawang");
            lokasi.add("Tulang Bawang Barat");
            lokasi.add("Way Kanan");
            lokasi.add("Pesisir Barat");
        }else if(pengguna.getDaerahPengguna().equalsIgnoreCase("Mesuji")){
            lokasi.add("Mesuji");
            lokasi.add("Metro");
            lokasi.add("Bandar Lampung");
            lokasi.add("Lampung Barat");
            lokasi.add("Lampung Selatan");
            lokasi.add("Lampung Tengah");
            lokasi.add("Lampung Timur");
            lokasi.add("Lampung Utara");
            lokasi.add("Pesawaran");
            lokasi.add("Pringsewu");
            lokasi.add("Tanggamus");
            lokasi.add("Tulang Bawang");
            lokasi.add("Tulang Bawang Barat");
            lokasi.add("Way Kanan");
            lokasi.add("Pesisir Barat");
        }else if(pengguna.getDaerahPengguna().equalsIgnoreCase("Pesawaran")){
            lokasi.add("Pesawaran");
            lokasi.add("Metro");
            lokasi.add("Bandar Lampung");
            lokasi.add("Lampung Barat");
            lokasi.add("Lampung Selatan");
            lokasi.add("Lampung Tengah");
            lokasi.add("Lampung Timur");
            lokasi.add("Lampung Utara");
            lokasi.add("Mesuji");
            lokasi.add("Pringsewu");
            lokasi.add("Tanggamus");
            lokasi.add("Tulang Bawang");
            lokasi.add("Tulang Bawang Barat");
            lokasi.add("Way Kanan");
            lokasi.add("Pesisir Barat");
        }else if(pengguna.getDaerahPengguna().equalsIgnoreCase("Pringsewu")){
            lokasi.add("Pringsewu");
            lokasi.add("Metro");
            lokasi.add("Bandar Lampung");
            lokasi.add("Lampung Barat");
            lokasi.add("Lampung Selatan");
            lokasi.add("Lampung Tengah");
            lokasi.add("Lampung Timur");
            lokasi.add("Lampung Utara");
            lokasi.add("Mesuji");
            lokasi.add("Pesawaran");
            lokasi.add("Tanggamus");
            lokasi.add("Tulang Bawang");
            lokasi.add("Tulang Bawang Barat");
            lokasi.add("Way Kanan");
            lokasi.add("Pesisir Barat");
        }else if(pengguna.getDaerahPengguna().equalsIgnoreCase("Tanggamus")){
            lokasi.add("Tanggamus");
            lokasi.add("Metro");
            lokasi.add("Bandar Lampung");
            lokasi.add("Lampung Barat");
            lokasi.add("Lampung Selatan");
            lokasi.add("Lampung Tengah");
            lokasi.add("Lampung Timur");
            lokasi.add("Lampung Utara");
            lokasi.add("Mesuji");
            lokasi.add("Pesawaran");
            lokasi.add("Pringsewu");
            lokasi.add("Tulang Bawang");
            lokasi.add("Tulang Bawang Barat");
            lokasi.add("Way Kanan");
            lokasi.add("Pesisir Barat");
        }else if(pengguna.getDaerahPengguna().equalsIgnoreCase("Tulang Bawang")){
            lokasi.add("Tulang Bawang");
            lokasi.add("Metro");
            lokasi.add("Bandar Lampung");
            lokasi.add("Lampung Barat");
            lokasi.add("Lampung Selatan");
            lokasi.add("Lampung Tengah");
            lokasi.add("Lampung Timur");
            lokasi.add("Lampung Utara");
            lokasi.add("Mesuji");
            lokasi.add("Pesawaran");
            lokasi.add("Pringsewu");
            lokasi.add("Tanggamus");
            lokasi.add("Tulang Bawang Barat");
            lokasi.add("Way Kanan");
            lokasi.add("Pesisir Barat");
        }else if(pengguna.getDaerahPengguna().equalsIgnoreCase("Tulang Bawang Barat")){
            lokasi.add("Tulang Bawang Barat");
            lokasi.add("Metro");
            lokasi.add("Bandar Lampung");
            lokasi.add("Lampung Barat");
            lokasi.add("Lampung Selatan");
            lokasi.add("Lampung Tengah");
            lokasi.add("Lampung Timur");
            lokasi.add("Lampung Utara");
            lokasi.add("Mesuji");
            lokasi.add("Pesawaran");
            lokasi.add("Pringsewu");
            lokasi.add("Tanggamus");
            lokasi.add("Tulang Bawang");
            lokasi.add("Way Kanan");
            lokasi.add("Pesisir Barat");
        }else if(pengguna.getDaerahPengguna().equalsIgnoreCase("Way Kanan")){
            lokasi.add("Way Kanan");
            lokasi.add("Metro");
            lokasi.add("Bandar Lampung");
            lokasi.add("Lampung Barat");
            lokasi.add("Lampung Selatan");
            lokasi.add("Lampung Tengah");
            lokasi.add("Lampung Timur");
            lokasi.add("Lampung Utara");
            lokasi.add("Mesuji");
            lokasi.add("Pesawaran");
            lokasi.add("Pringsewu");
            lokasi.add("Tanggamus");
            lokasi.add("Tulang Bawang");
            lokasi.add("Tulang Bawang Barat");
            lokasi.add("Pesisir Barat");
        }else if(pengguna.getDaerahPengguna().equalsIgnoreCase("Pesisir Barat")){
            lokasi.add("Pesisir Barat");
            lokasi.add("Metro");
            lokasi.add("Bandar Lampung");
            lokasi.add("Lampung Barat");
            lokasi.add("Lampung Selatan");
            lokasi.add("Lampung Tengah");
            lokasi.add("Lampung Timur");
            lokasi.add("Lampung Utara");
            lokasi.add("Mesuji");
            lokasi.add("Pesawaran");
            lokasi.add("Pringsewu");
            lokasi.add("Tanggamus");
            lokasi.add("Tulang Bawang");
            lokasi.add("Tulang Bawang Barat");
            lokasi.add("Way Kanan");
        }


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lokasi);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDaerah.setAdapter(adapter1);
        

        spDaerah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                daerahPilih = lokasi.get(i);
                if(daerahPilih.equalsIgnoreCase("Bandar Lampung")){
                    daerahId = 21;
                }else if(daerahPilih.equalsIgnoreCase("Metro")){
                    daerahId = 283;
                }else if(daerahPilih.equalsIgnoreCase("Lampung Barat")){
                    daerahId = 223;
                }else if(daerahPilih.equalsIgnoreCase("Lampung Selatan")){
                    daerahId = 224;
                }else if(daerahPilih.equalsIgnoreCase("Lampung Tengah")){
                    daerahId = 225;
                }else if(daerahPilih.equalsIgnoreCase("Lampung Timur")){
                    daerahId = 226;
                }else if(daerahPilih.equalsIgnoreCase("Lampung Utara")){
                    daerahId = 227;
                }else if(daerahPilih.equalsIgnoreCase("Mesuji")){
                    daerahId = 282;
                }else if(daerahPilih.equalsIgnoreCase("Pesawaran")){
                    daerahId = 355;
                }else if(daerahPilih.equalsIgnoreCase("Pringsewu")){
                    daerahId = 368;
                }else if(daerahPilih.equalsIgnoreCase("Tanggamus")){
                    daerahId = 458;
                }else if(daerahPilih.equalsIgnoreCase("Tulang Bawang")){
                    daerahId = 490;
                }else if(daerahPilih.equalsIgnoreCase("Tulang Bawang Barat")){
                    daerahId = 491;
                }else if(daerahPilih.equalsIgnoreCase("Way Kanan")){
                    daerahId = 496;
                }else if(daerahPilih.equalsIgnoreCase("Pesisir Barat")){
                    daerahId = 356;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        Picasso.get()
//                .load("http://inkubator.sikubis.com/uploads/file/" + pengguna.getFotoPengguna())
//                .into(ivFoto);

        Glide.with(this)
                .asBitmap()
                .load("http://inkubator.sikubis.com/uploads/file/" + pengguna.getFotoPengguna())
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
                    Toast.makeText(UbahProfilActivity.this, "Harap tambah foto profil", Toast.LENGTH_SHORT).show();
                }else{
                    simpanProfil(bitmapSaya);
                }
            }
        });

    }

    public void simpanProfil(final Bitmap bitmapSaya){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Simpan Profil...");
        progressDialog.show();

        final String nama = etNama.getText().toString();
        final String noTelp = etNoTelp.getText().toString();
        final String alamat = etAlamat.getText().toString();

        Log.d("UBAH",  nama + " , "+ noTelp + " , " + alamat + " , " + daerahPilih + " , " + daerahId + " , "  + etEmail + " , " + etPassword);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BuildConfig.host + BuildConfig.updateProfil,
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
                            }else if(obj.getString("error").equalsIgnoreCase("Gagal Update")){
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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(UbahProfilActivity.this).getPengguna().getRememberToken());
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

                params.put("id", String.valueOf(SharedPrefManager.getInstance(UbahProfilActivity.this).getPengguna().getIdPengguna()));
                params.put("nama", nama);
                params.put("no_telp", noTelp);
                params.put("alamat", alamat);
                params.put("daerah", daerahPilih);
                params.put("daerah_id", String.valueOf(daerahId));
                params.put("email", etEmail.getText().toString());
                params.put("password", etPassword.getText().toString());

                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("foto", new DataPart(SharedPrefManager.getInstance(UbahProfilActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSaya)));
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
