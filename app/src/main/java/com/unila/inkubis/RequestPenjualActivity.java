package com.unila.inkubis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.unila.inkubis.Model.RequestPenjual;
import com.unila.inkubis.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestPenjualActivity extends AppCompatActivity {

        ProgressDialog progressDialog;
        EditText etNomor;
        ImageView ivFoto1, ivFoto2, ivFoto3;
        FrameLayout fl1, fl2, fl3;
        LinearLayout llIdentitas_civitas, llIdentitas, llJurusan;
        View separatorJurusan, separatorIdentitas;
        Spinner spStatusCivitas, spStatusFakultas, spStatusJurusan;
        Button btnTambah, btnTambahFoto, ganti1, ganti2, ganti3;
        HorizontalScrollView hsv;
        ListView lvJurusan;

        ArrayList<Civitas> daftarCivitas;
        ArrayList<Fakultas> daftarFakultas;
        ArrayList<Jurusan> daftarJurusan;

        ArrayList<String> daftarNama = new ArrayList<String>();
        int id;

        ArrayList<String> civitas, fakultas, jurusan;
        int statusTerpilihCivitas = 0;
        int statusTerpilihFakultas = 0;
        int statusTerpilihJurusan = 0;
        private static final int CAMERA_REQUEST = 1888;
        int jumlah = 1, idJurusan, idCivitas;
        double lat, lng;
        Bitmap bitmapSaya, bitmapSaya2, bitmapSaya3;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_request_penjual);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Form Request Penjual");

            spStatusCivitas = (Spinner) findViewById(R.id.sp_civitas);
            spStatusFakultas = (Spinner) findViewById(R.id.sp_fakultas);
            spStatusJurusan = (Spinner) findViewById(R.id.sp_jurusan);
            etNomor = (EditText) findViewById(R.id.et_nomor);
            btnTambah = (Button) findViewById(R.id.btn_tambah);
            btnTambahFoto = (Button) findViewById(R.id.btn_tambah_foto);
            ivFoto1 = (ImageView) findViewById(R.id.iv_foto1);
            ivFoto2 = (ImageView) findViewById(R.id.iv_foto2);
            ivFoto3 = (ImageView) findViewById(R.id.iv_foto3);
            fl1 = (FrameLayout) findViewById(R.id.fl_1);
            fl2 = (FrameLayout) findViewById(R.id.fl_2);
            fl3 = (FrameLayout) findViewById(R.id.fl_3);
//            llIdentitas_civitas = (LinearLayout) findViewById(R.id.ll_identitas_civitas);
//            llIdentitas = (LinearLayout) findViewById(R.id.ll_identitas);
//            llJurusan = (LinearLayout) findViewById(R.id.ll_jurusan);
//            separatorIdentitas = findViewById(R.id.separator_identitas);
//            separatorJurusan = findViewById(R.id.separator_jurusan);
            ganti1 = (Button) findViewById(R.id.ganti_1);
            ganti2 = (Button) findViewById(R.id.ganti_2);
            ganti3 = (Button) findViewById(R.id.ganti_3);
            hsv = (HorizontalScrollView) findViewById(R.id.scrollView);

            daftarFakultas = new ArrayList<>();
            daftarJurusan = new ArrayList<>();
            daftarCivitas = new ArrayList<>();


            btnTambahFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(jumlah <= 3){
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, jumlah);
                    }else{
                        Toast.makeText(RequestPenjualActivity.this, "Maksimal 3 foto", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ganti1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 4);
                }
            });

            ganti2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 5);
                }
            });

            ganti3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 6);
                }
            });

            daftarCivitas();
            spStatusCivitas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String itemTerpilih = adapterView.getItemAtPosition(i).toString();
                    if(!itemTerpilih.equalsIgnoreCase("Pilih Civitas")){
                        statusTerpilihCivitas = daftarCivitas.get(i-1).getId();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            daftarFakultas();
            spStatusFakultas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String itemTerpilih = adapterView.getItemAtPosition(i).toString();
                    if(!itemTerpilih.equalsIgnoreCase("Pilih Fakultas")){
                        daftarJurusan(daftarFakultas.get(i-1).getId());
                        statusTerpilihFakultas = daftarFakultas.get(i-1).getId();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            spStatusJurusan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    String itemTerpilih = adapterView.getItemAtPosition(i).toString();
                    if(!itemTerpilih.equalsIgnoreCase("Pilih Jurusan")){
                        statusTerpilihJurusan = daftarJurusan.get(i-1).getIdJurusan();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            btnTambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(bitmapSaya == null ){
                        Toast.makeText(RequestPenjualActivity.this, "Harap tambah foto kartu identitas", Toast.LENGTH_SHORT).show();
                    }else{
                        prosesRequest();
                    }
                }
            });

        }



        protected void onActivityResult(int reqCode, int resultCode, Intent data) {
            if (reqCode == 1 && resultCode == RESULT_OK && data != null) {
                Uri imageUri = data.getData();
                try {
                    //getting bitmap object from uri
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    bitmapSaya = bitmap;

                    //displaying selected image to imageview
                    ivFoto1.setImageBitmap(bitmap);
                    fl1.setVisibility(View.VISIBLE);
                    jumlah++;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (reqCode == 2 && resultCode == RESULT_OK && data != null) {
                Uri imageUri = data.getData();
                try {
                    //getting bitmap object from uri
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    bitmapSaya2 = bitmap;

                    //displaying selected image to imageview
                    ivFoto2.setImageBitmap(bitmap);
                    fl2.setVisibility(View.VISIBLE);
                    hsv.postDelayed(new Runnable() {
                        public void run() {
                            hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        }
                    }, 100L);
                    jumlah++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (reqCode == 3 && resultCode == RESULT_OK && data != null) {
                Uri imageUri = data.getData();
                try {
                    //getting bitmap object from uri
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    bitmapSaya3 = bitmap;

                    //displaying selected image to imageview
                    ivFoto3.setImageBitmap(bitmap);
                    fl3.setVisibility(View.VISIBLE);
                    hsv.postDelayed(new Runnable() {
                        public void run() {
                            hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        }
                    }, 100L);
                    jumlah++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (reqCode == 4 && resultCode == RESULT_OK && data != null) {
                Uri imageUri = data.getData();
                try {
                    //getting bitmap object from uri
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    bitmapSaya = bitmap;

                    //displaying selected image to imageview
                    ivFoto1.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (reqCode == 5 && resultCode == RESULT_OK && data != null) {
                Uri imageUri = data.getData();
                try {
                    //getting bitmap object from uri
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    bitmapSaya2 = bitmap;

                    //displaying selected image to imageview
                    ivFoto2.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (reqCode == 6 && resultCode == RESULT_OK && data != null) {
                Uri imageUri = data.getData();
                try {
                    //getting bitmap object from uri
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    bitmapSaya3 = bitmap;

                    //displaying selected image to imageview
                    ivFoto3.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        public void prosesRequest(){



            final String nomor = etNomor.getText().toString();

            if(TextUtils.isEmpty(etNomor.getText().toString()) || statusTerpilihJurusan == 0 || statusTerpilihFakultas == 0 || statusTerpilihCivitas == 0){
                Toast.makeText(this, "Harap Isi Semua Data", Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Request Penjual...");
            progressDialog.show();

            Log.d("TAMBAH",  nomor + " , "+ statusTerpilihCivitas + " , " + statusTerpilihFakultas + " , " + statusTerpilihJurusan);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BuildConfig.host + BuildConfig.requestPenjual,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            progressDialog.dismiss();
                            try {
                                JSONObject obj = new JSONObject(new String(response.data));

                                if(obj.getString("success").equalsIgnoreCase("Berhasil Request Penjual")){
                                    Toast.makeText(getApplicationContext(), obj.getString("success"), Toast.LENGTH_SHORT).show();
                                    Intent returnIntent = new Intent();
                                    setResult(Activity.RESULT_CANCELED, returnIntent);
                                    finish();
                                }else if(obj.getString("success").equalsIgnoreCase("Gagal Request Penjual")){
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
                    params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(RequestPenjualActivity.this).getPengguna().getRememberToken());
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

                    params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(RequestPenjualActivity.this).getPengguna().getIdPengguna()));
                    params.put("nomor", nomor);
                    params.put("status", "0");
                    params.put("id_civitas_akademika", String.valueOf(statusTerpilihCivitas));
                    params.put("id_fakultas", String.valueOf(statusTerpilihFakultas));
                    params.put("id_jurusan", String.valueOf(statusTerpilihJurusan));


                    return params;
                }

                /*
                 * Here we are passing image by renaming it with a unique name
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    if((jumlah-1) == 1){
                        params.put("foto", new DataPart(SharedPrefManager.getInstance(RequestPenjualActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSaya)));
                    }else if((jumlah-1) == 2){
                        params.put("foto", new DataPart(SharedPrefManager.getInstance(RequestPenjualActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSaya)));
                        params.put("foto2", new DataPart(SharedPrefManager.getInstance(RequestPenjualActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSaya2)));
                    }else if((jumlah-1) == 3){
                        params.put("foto", new DataPart(SharedPrefManager.getInstance(RequestPenjualActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSaya)));
                        params.put("foto2", new DataPart(SharedPrefManager.getInstance(RequestPenjualActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSaya2)));
                        params.put("foto3", new DataPart(SharedPrefManager.getInstance(RequestPenjualActivity.this).getPengguna().getIdPengguna() + ".png", getFileDataFromDrawable(bitmapSaya3)));
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


    public class Civitas{
        int id;
        String nama;

        public Civitas(int id, String nama){
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

    public void daftarCivitas(){
        StringRequest stringRequest = new StringRequest(BuildConfig.host + BuildConfig.daftarCivitas,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            JSONArray fakultas = obj.getJSONArray("success");

//                            Toast.makeText(getContext(), events.getString(0), Toast.LENGTH_SHORT).show();

                            for(int i = 0 ; i < fakultas.length() ; i ++){
                                JSONObject detail = fakultas.getJSONObject(i);
                                daftarCivitas.add(new Civitas(detail.getInt("id"), detail.getString("nama")));
                            }

                            ArrayList<String> daftarNama = new ArrayList<>();
                            daftarNama.add("Pilih Civitas");
                            for(int i = 0 ; i < daftarCivitas.size() ; i++){
                                daftarNama.add(daftarCivitas.get(i).getNama());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RequestPenjualActivity.this,
                                    android.R.layout.simple_spinner_item, daftarNama);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spStatusCivitas.setAdapter(adapter);
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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(RequestPenjualActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public class Fakultas{
        int id;
        String nama;

        public Fakultas(int id, String nama){
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

    public void daftarFakultas(){
        StringRequest stringRequest = new StringRequest(BuildConfig.host + BuildConfig.daftarFakultas,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            JSONArray fakultas = obj.getJSONArray("success");

//                            Toast.makeText(getContext(), events.getString(0), Toast.LENGTH_SHORT).show();

                            for(int i = 0 ; i < fakultas.length() ; i ++){
                                JSONObject detail = fakultas.getJSONObject(i);
                                daftarFakultas.add(new Fakultas(detail.getInt("id"), detail.getString("nama")));
                            }

                            ArrayList<String> daftarNama = new ArrayList<>();
                            daftarNama.add("Pilih Fakultas");
                            for(int i = 0 ; i < daftarFakultas.size() ; i++){
                                daftarNama.add(daftarFakultas.get(i).getNama());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RequestPenjualActivity.this,
                                    android.R.layout.simple_spinner_item, daftarNama);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spStatusFakultas.setAdapter(adapter);
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
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(RequestPenjualActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public class Jurusan {
        int idJurusan, idFakultas;
        String namaJurusan;

        public Jurusan(int idJurusan, int idFakultas, String namaJurusan) {
            this.idJurusan = idJurusan;
            this.idFakultas = idFakultas;
            this.namaJurusan = namaJurusan;
        }

        public int getIdFakultas() {

            return idFakultas;
        }

        public int getIdJurusan() {

            return idJurusan;
        }

        public String getNamaJurusan() {

            return namaJurusan;
        }

    }

    public void daftarJurusan(int idFakultas) {

            StringRequest stringRequest = new StringRequest(BuildConfig.host + BuildConfig.daftarJurusan + idFakultas,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject obj = new JSONObject(response);

                                JSONArray jurusan = obj.getJSONArray("success");

                                for (int i = 0; i < jurusan.length(); i++) {
                                    JSONObject detail = jurusan.getJSONObject(i);
                                    daftarJurusan.add(new Jurusan(detail.getInt("id"),detail.getInt("id_fakultas"), detail.getString("nama")));
                                }

                                ArrayList<String> daftarNama = new ArrayList<>();
                                daftarNama.add("Pilih Jurusan");
                                for(int i = 0 ; i < daftarJurusan.size() ; i++){
                                    daftarNama.add(daftarJurusan.get(i).getNamaJurusan());
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RequestPenjualActivity.this,
                                        android.R.layout.simple_spinner_item, daftarNama);

                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                spStatusJurusan.setAdapter(adapter);

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
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(RequestPenjualActivity.this).getPengguna().getRememberToken());
                    params.put("Accept", "application/json");

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
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
