package com.unila.inkubis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import java.util.concurrent.TimeUnit;

public class TambahPencairanActivity extends AppCompatActivity {

    EditText etJumlah;
    Button btnMax, btnCair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pencairan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Permintaan Pencairan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etJumlah = (EditText) findViewById(R.id.et_jumlah);
        btnMax = (Button) findViewById(R.id.btn_max);
        btnCair = (Button) findViewById(R.id.btn_cair);


        btnMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etJumlah.setText(String.valueOf(SharedPrefManager.getInstance(TambahPencairanActivity.this).getPengguna().getSaldoPengguna()));
            }
        });

        btnCair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int saldo = 0;

                try{
                    saldo = Integer.parseInt(etJumlah.getText().toString());
                }catch (Exception exception){
                    Toast.makeText(TambahPencairanActivity.this, "Harap masukkan nominal", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Integer.parseInt(etJumlah.getText().toString()) < 10000){
                    Toast.makeText(TambahPencairanActivity.this, "Minimal pencairan Rp.10.000", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(saldo > SharedPrefManager.getInstance(TambahPencairanActivity.this).getPengguna().getSaldoPengguna()){
                    Toast.makeText(TambahPencairanActivity.this, "Saldo tidak mencukupi", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    final ProgressDialog progressDialog = new ProgressDialog(TambahPencairanActivity.this);
                    progressDialog.setMessage("Proses Pencairan...");
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.tambahPencairan,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        Toast.makeText(TambahPencairanActivity.this, obj.getString("success"), Toast.LENGTH_LONG).show();

                                        if(obj.getString("success").equalsIgnoreCase("Berhasil Tambah Request Pencairan")){
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
                                    progressDialog.dismiss();
                                    Toast.makeText(TambahPencairanActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(TambahPencairanActivity.this).getPengguna().getRememberToken());
                            params.put("Accept", "application/json");

                            return params;
                        }

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(TambahPencairanActivity.this).getPengguna().getIdPengguna()));
                            params.put("saldo", etJumlah.getText().toString());

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(TambahPencairanActivity.this);
                    requestQueue.add(stringRequest).setRetryPolicy(new DefaultRetryPolicy(
                            (int) TimeUnit.SECONDS.toMillis(5),
                            0,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                }

            }
        });

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
