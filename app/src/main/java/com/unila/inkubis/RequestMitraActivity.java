package com.unila.inkubis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class RequestMitraActivity extends AppCompatActivity {
    
    RadioButton rbPeternak, rbProdusen, rbPetani, rbSupplier;
    TextView tvDeskripsi;
    EditText etFotoKtp, etFotoKandang, etCppb, etSertifikat, etNama, etNik;
    CheckBox cbSyarat;
    Button btnDaftar;
    ImageView ivKtp, ivKandang, ivSertifikat, ivCppb;
    LinearLayout llPeternak, llProdusen, llPetani, llSupplier;
    ProgressDialog progressDialog;

    String tipe = "peternak";
    Bitmap bitmapKtp, bitmapKandang, bitmapCppb, bitmapSertifikat;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_mitra);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        
        rbPeternak = (RadioButton) findViewById(R.id.rb_peternak);
        rbProdusen = (RadioButton) findViewById(R.id.rb_produsen);
        rbPetani = (RadioButton) findViewById(R.id.rb_petani);
        rbSupplier = (RadioButton) findViewById(R.id.rb_supplier);
        tvDeskripsi = (TextView) findViewById(R.id.tv_deskripsi);
        etFotoKtp = (EditText) findViewById(R.id.et_foto_ktp);
        etFotoKandang = (EditText) findViewById(R.id.et_foto_kandang);
        etCppb = (EditText) findViewById(R.id.et_foto_cppb);
        etSertifikat =(EditText) findViewById(R.id.et_sertifikat);
        ivKtp = (ImageView) findViewById(R.id.iv_ktp);
        ivKandang = (ImageView) findViewById(R.id.iv_kandang);
        llPeternak = (LinearLayout) findViewById(R.id.ll_peternakan);
        llProdusen = (LinearLayout) findViewById(R.id.ll_cppb);
        llSupplier = (LinearLayout) findViewById(R.id.ll_supplier);
        ivSertifikat = (ImageView) findViewById(R.id.iv_sertifikat);
        ivCppb = (ImageView) findViewById(R.id.iv_cppb);
        etSertifikat = (EditText) findViewById(R.id.et_sertifikat);
        etCppb = (EditText) findViewById(R.id.et_foto_cppb);
        etNama = (EditText) findViewById(R.id.et_nama);
        etNik = (EditText) findViewById(R.id.et_nik);
        cbSyarat = (CheckBox) findViewById(R.id.cb_syarat);
        btnDaftar = (Button) findViewById(R.id.btn_daftar);

        etFotoKtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
            }
        });

        etFotoKandang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 2);
            }
        });

        etCppb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 3);
            }
        });

        etSertifikat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 4);
            }
        });

        rbPeternak.setChecked(true);
        rbPeternak.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));

        rbPeternak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDeskripsi.setText("Peternak menjual produk seperti sapi, kambing, kuda, domba, dll");
                rbPeternak.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                rbProdusen.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                rbPetani.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                rbSupplier.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));

                llPeternak.setVisibility(View.VISIBLE);
                llProdusen.setVisibility(View.GONE);
                llSupplier.setVisibility(View.GONE);

                ivCppb.setVisibility(View.GONE);
                ivSertifikat.setVisibility(View.GONE);
                ivKandang.setVisibility(View.VISIBLE);

                tipe = "peternak";
            }
        });

        rbProdusen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDeskripsi.setText("Produsen menjual produk seperti pakan ternak konsentrat");
                rbProdusen.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                rbPeternak.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                rbPetani.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                rbSupplier.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));

                llProdusen.setVisibility(View.VISIBLE);
                llPeternak.setVisibility(View.GONE);
                llSupplier.setVisibility(View.GONE);

                ivKandang.setVisibility(View.GONE);
                ivSertifikat.setVisibility(View.GONE);
                ivCppb.setVisibility(View.VISIBLE);

                tipe = "produsen";
            }
        });

        rbPetani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDeskripsi.setText("Petani dapat menjual produk hijauan");
                rbPetani.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                rbProdusen.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                rbPeternak.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                rbSupplier.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));

                llProdusen.setVisibility(View.GONE);
                llPeternak.setVisibility(View.GONE);
                llSupplier.setVisibility(View.GONE);

                ivCppb.setVisibility(View.GONE);
                ivKandang.setVisibility(View.GONE);
                ivSertifikat.setVisibility(View.GONE);

                tipe = "petani";
            }
        });

        rbSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvDeskripsi.setText("Supplier dapat menjual produk berupa supplement");
                rbSupplier.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                rbProdusen.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                rbPetani.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                rbPeternak.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));

                llSupplier.setVisibility(View.VISIBLE);
                llPeternak.setVisibility(View.GONE);
                llProdusen.setVisibility(View.GONE);

                ivKandang.setVisibility(View.GONE);
                ivCppb.setVisibility(View.GONE);
                ivSertifikat.setVisibility(View.VISIBLE);

                tipe = "supplier";
            }
        });

        etNama.setText(SharedPrefManager.getInstance(RequestMitraActivity.this).getPengguna().getNamaPengguna());

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cbSyarat.isChecked()){
                    requestMitra();
                }else{
                    Toast.makeText(RequestMitraActivity.this, "Harap Menyetujui Syarat dan Ketentuan", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            bitmapKtp = photo;
            ivKtp.setImageBitmap(photo);
            ivKtp.setVisibility(View.VISIBLE);
        }

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            bitmapKandang = photo;
            ivKandang.setImageBitmap(photo);
            ivKandang.setVisibility(View.VISIBLE);
        }

        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            bitmapCppb = photo;
            ivCppb.setImageBitmap(photo);
            ivCppb.setVisibility(View.VISIBLE);
        }

        if (requestCode == 4 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            bitmapSertifikat = photo;
            ivSertifikat.setImageBitmap(photo);
            ivSertifikat.setVisibility(View.VISIBLE);
        }
    }

    public void requestMitra(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengirim Permintaan...");
        progressDialog.show();

        if(TextUtils.isEmpty(etNik.getText().toString())){
            Toast.makeText(this, "Harap Isi NIK", Toast.LENGTH_SHORT).show();
            return;
        }


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BuildConfig.host + BuildConfig.requestMitra,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));

                            Toast.makeText(getApplicationContext(), obj.getString("success"), Toast.LENGTH_SHORT).show();
                            if(obj.getString("success").equalsIgnoreCase("Berhasil Request Mitra")){
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(RequestMitraActivity.this);
                                builder1.setMessage("Permintaan anda telah dikirim dan akan di proses oleh admin");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Oke",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                finish();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(RequestMitraActivity.this).getPengguna().getRememberToken());
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

                params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(RequestMitraActivity.this).getPengguna().getIdPengguna()));
                params.put("nama", etNama.getText().toString());
                params.put("nik", etNik.getText().toString());
                params.put("tipe", tipe);

                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if(tipe.equalsIgnoreCase("peternak")){
                    params.put("foto_ktp", new DataPart(SharedPrefManager.getInstance(RequestMitraActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapKtp)));
                    params.put("foto_peternakan", new DataPart(SharedPrefManager.getInstance(RequestMitraActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapKandang)));
                }else if(tipe.equalsIgnoreCase("produsen")){
                    params.put("foto_ktp", new DataPart(SharedPrefManager.getInstance(RequestMitraActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapKtp)));
                    params.put("foto_cppb", new DataPart(SharedPrefManager.getInstance(RequestMitraActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapCppb)));
                }else if(tipe.equalsIgnoreCase("petani")){
                    params.put("foto_ktp", new DataPart(SharedPrefManager.getInstance(RequestMitraActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapKtp)));
                }else if(tipe.equalsIgnoreCase("supplier")){
                    params.put("foto_ktp", new DataPart(SharedPrefManager.getInstance(RequestMitraActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapKtp)));
                    params.put("foto_sertifikat", new DataPart(SharedPrefManager.getInstance(RequestMitraActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSertifikat)));
                }
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest).setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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
