package com.unila.inkubis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AturOngkirActivity extends AppCompatActivity {

    TextView tvKecamatan, tvKabupaten;
    EditText etOngkir;
    Button btnSimpan;
    ProgressDialog mProgressDialog;

    int idKecamatan;
    String URLnya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atur_ongkir);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Atur Tarif Ongkir");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvKecamatan = (TextView) findViewById(R.id.tv_kecamatan);
        tvKabupaten = (TextView) findViewById(R.id.tv_kabupaten);
        etOngkir = (EditText) findViewById(R.id.et_ongkir);
        btnSimpan = (Button) findViewById(R.id.btn_simpan);

        Intent intent = getIntent();

        tvKecamatan.setText(intent.getStringExtra("NAMA_KECAMATAN"));
        tvKabupaten.setText(intent.getStringExtra("NAMA_KABUPATEN"));

        idKecamatan = intent.getIntExtra("ID_KECAMATAN",0);

        if(intent.getIntExtra("ONGKIR",0) < 0){
            etOngkir.setHint("Tarif Ongkir (Belum DiAtur)");
            URLnya = BuildConfig.host + BuildConfig.tambahOngkir;
        }else if(intent.getIntExtra("ONGKIR", 0) == 0){
            URLnya = BuildConfig.host + BuildConfig.ubahOngkir;
            etOngkir.setText(String.valueOf(intent.getIntExtra("ONGKIR",0)));
        }else{
            URLnya = BuildConfig.host + BuildConfig.ubahOngkir;
            etOngkir.setText(String.valueOf(intent.getIntExtra("ONGKIR",0)));
        }

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(etOngkir.getText().toString())){
                    Toast.makeText(AturOngkirActivity.this, "Harap Isi Nominal Ongkir !", Toast.LENGTH_SHORT).show();
                }else{
                    aturOngkir();
                }
            }
        });
    }

    public void aturOngkir(){
        mProgressDialog = new ProgressDialog(AturOngkirActivity.this);
        mProgressDialog.setMessage("Menyimpan Tarif Ongkir ...");
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLnya,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);

                            Toast.makeText(AturOngkirActivity.this, obj.getString("success"), Toast.LENGTH_SHORT).show();

                            if(obj.getString("success").equalsIgnoreCase("Berhasil Ubah Ongkir") || obj.getString("success").equalsIgnoreCase("Berhasil Tambah Ongkir")){
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_CANCELED, returnIntent);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AturOngkirActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(AturOngkirActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_penjual", String.valueOf(SharedPrefManager.getInstance(AturOngkirActivity.this).getPengguna().getIdPengguna()));
                params.put("id_kecamatan", String.valueOf(idKecamatan));
                params.put("ongkir", String.valueOf(etOngkir.getText().toString()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AturOngkirActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
