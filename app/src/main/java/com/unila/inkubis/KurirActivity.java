package com.unila.inkubis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
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
import com.unila.inkubis.R;

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

public class KurirActivity extends AppCompatActivity {
    Switch swJNE, swTIKI, swPOS;
    int status_jne, status_tiki, status_pos, status, idKurir, kurir;
    Button btnSimpan;
    ProgressDialog progressDialog;
    ArrayList<SettingKurir> settingKurirs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kurir);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pengaturan Kurir");
        swJNE = (Switch) findViewById(R.id.sw_jne);
        swTIKI = (Switch) findViewById(R.id.sw_tiki);
        swPOS = (Switch) findViewById(R.id.sw_pos);
        btnSimpan = (Button) findViewById(R.id.btn_simpan);

        settingKurirs = new ArrayList<>();
        ambilSettingKurir();


        swJNE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    status_jne = 1;
                } else {
                    status_jne = 0;
                }
            }
        });
        swTIKI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    status_tiki = 1;
                } else {
                    status_tiki = 0;
                }
            }
        });
        swPOS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    status_pos = 1;
                } else {
                    status_pos = 0;
                }
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpanPengaturan();

            }
        });
    }
    public void ambilSettingKurir(){
    StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.getKurir,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.d("HERE", String.valueOf(obj));
                        JSONObject objStatus = new JSONObject(obj.getString("success"));

                        status_jne = Integer.parseInt(objStatus.getString("jne"));
                        status_tiki = Integer.parseInt(objStatus.getString("tiki"));
                        status_pos = Integer.parseInt(objStatus.getString("pos"));

                        if (status_jne == 1) {
                            swJNE.setChecked(true);
                        } else {
                            swJNE.setChecked(false);
                        }

                        if (status_tiki == 1) {
                            swTIKI.setChecked(true);
                        } else {
                            swTIKI.setChecked(false);
                        }

                        if (status_pos == 1) {
                            swPOS.setChecked(true);
                        } else {
                            swPOS.setChecked(false);
                        }


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
            params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(KurirActivity.this).getPengguna().getRememberToken());
            params.put("Accept", "application/json");

            return params;
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(KurirActivity.this).getPengguna().getIdPengguna()));
            return params;
        }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(KurirActivity.this);
    requestQueue.add(stringRequest);
    }
    public void simpanPengaturan(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Simpan Pengaturan Kurir...");
        progressDialog.show();

        Log.d("UBAH",  status_jne + " , "+ status_pos + " , " + status_tiki);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, BuildConfig.host + BuildConfig.updateKurir,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));

                            if(obj.getString("success").equalsIgnoreCase("Berhasil Update Kurir")){
                                Toast.makeText(getApplicationContext(), obj.getString("success"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(KurirActivity.this).getPengguna().getRememberToken());
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

                params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(KurirActivity.this).getPengguna().getIdPengguna()));
                params.put("status_jne", String.valueOf(status_jne));
                params.put("status_tiki", String.valueOf(status_tiki));
                params.put("status_pos", String.valueOf(status_pos));


                return params;
            }

        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest).setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public class SettingKurir {
        int  idKurir,kurir,status;

        public SettingKurir( int idKurir, int kurir, int status) {

            this.idKurir = idKurir;
            this.kurir = kurir;
            this.status = status;
        }

        public int getIdKurir() {

            return idKurir;
        }

        public int getKurir() {

            return kurir;
        }

        public int getStatus() {

            return status;
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
