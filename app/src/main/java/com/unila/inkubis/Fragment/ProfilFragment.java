package com.unila.inkubis.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.Model.Pengguna;
import com.unila.inkubis.PencairanActivity;
import com.unila.inkubis.PesananActivity;
import com.unila.inkubis.R;
import com.unila.inkubis.SharedPrefManager;
import com.unila.inkubis.UbahProfilActivity;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {

    TextView tvNama, tvTelp, tvSaldo, tvMitra;
    ProgressDialog progressDialog;
    ImageView ivProfil;
    RelativeLayout rlMasuk, rlDaftar;
    Button btnMasuk, btnDaftar, btnMasukDisini, btnDaftarDisini, btnCashOut;
    EditText etTelpDaftar, etNamaDaftar, etAlamatDaftar, etEmailDaftar, etPasswordDaftar, etEmailMasuk, etPasswordMasuk;
    Spinner spDaerah;
    TabLayout tabLayout;
    FrameLayout flBelum, flDiproses, flDikirim, flDiterima;
    SwipeRefreshLayout refresh;

    int daerahId = 21;

    String token, daerahPilih = "Bandar Lampung", noTelp;
    int time = 59;

    public ProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        ivProfil = (ImageView) view.findViewById(R.id.iv_foto_profil);
        rlMasuk = (RelativeLayout) view.findViewById(R.id.rl_masuk);
        rlDaftar = (RelativeLayout) view.findViewById(R.id.rl_daftar);
        btnMasuk = (Button) view.findViewById(R.id.btn_masuk);
        btnDaftarDisini = (Button) view.findViewById(R.id.btn_daftar_disini);
        btnDaftar = (Button) view.findViewById(R.id.btn_daftar);
        btnMasukDisini = (Button) view.findViewById(R.id.btn_masuk_disini);
        tvNama = (TextView) view.findViewById(R.id.tv_nama);
        tvTelp = (TextView) view.findViewById(R.id.tv_telp);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_profil);
        spDaerah = (Spinner) view.findViewById(R.id.sp_daerah);
        etEmailDaftar = view.findViewById(R.id.et_email_daftar);
        etPasswordDaftar = view.findViewById(R.id.et_password_daftar);
        etEmailMasuk = view.findViewById(R.id.et_email_masuk);
        etPasswordMasuk = view.findViewById(R.id.et_password_masuk);
        tvSaldo = (TextView) view.findViewById(R.id.tv_saldo);
        tvMitra = (TextView) view.findViewById(R.id.tv_mitra);
        btnCashOut = (Button) view.findViewById(R.id.btn_cash_out);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);

//        Log.d("REMEMBER_TOKEN", SharedPrefManager.getInstance(getContext()).getPengguna().getRememberToken());

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.ambilProfil,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {

                                    refresh.setRefreshing(false);
                                    Pengguna pengguna = SharedPrefManager.getInstance(getContext()).getPengguna();

                                    JSONObject obj = new JSONObject(response);
                                    JSONObject objLogin = new JSONObject(obj.getString("success"));

                                    SharedPrefManager.getInstance(getContext()).userLogin(new Pengguna(
                                            objLogin.getInt("id"),
                                            Integer.parseInt(objLogin.getString("saldo")),
                                            Integer.parseInt(objLogin.getString("status")),
                                            objLogin.getString("nama"),
                                            objLogin.getString("no_telp"),
                                            pengguna.getTokenPengguna(),
                                            objLogin.getString("alamat"),
                                            objLogin.getString("daerah"),
                                            objLogin.getString("foto"),
                                            objLogin.getString("email"),
                                            pengguna.getPasswordPengguna(),
                                            pengguna.getRememberToken(),
                                            objLogin.getInt("daerah_id")
                                    ));

                                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                                    String yourFormattedString = formatter.format(Integer.parseInt(objLogin.getString("saldo")));
                                    yourFormattedString = yourFormattedString.replace(",", ".");
                                    tvSaldo.setText("Rp. " + yourFormattedString);

                                    tvNama.setText(objLogin.getString("nama"));
                                    tvTelp.setText(objLogin.getString("no_telp"));
                                    Picasso.get()
                                            .load("http://inkubator.sikubis.com/uploads/file/" + objLogin.getString("foto"))
                                            .into(ivProfil);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(getContext()).getPengguna().getRememberToken());
                        params.put("Accept", "application/json");

                        return params;
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", String.valueOf(SharedPrefManager.getInstance(getContext()).getPengguna().getIdPengguna()));

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);

            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.wilayahs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDaerah.setAdapter(adapter);

        if(SharedPrefManager.getInstance(getContext()).getPengguna().getStatusPengguna() == 1){
            tvMitra.setVisibility(View.VISIBLE);
        }else{
            tvMitra.setVisibility(View.GONE);
        }

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(SharedPrefManager.getInstance(getContext()).getPengguna().getSaldoPengguna());
        yourFormattedString = yourFormattedString.replace(",", ".");
        tvSaldo.setText("Rp. " + yourFormattedString);

        spDaerah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] size_values = getResources().getStringArray(R.array.wilayahs);
                daerahPilih = size_values[i];
                if(i==0){
                    daerahId = 21;
                }else if(i==1){
                    daerahId = 283;
                }else if(i==2){
                    daerahId = 223;
                }else if(i==3){
                    daerahId = 224;
                }else if(i==4){
                    daerahId = 225;
                }else if(i==5){
                    daerahId = 226;
                }else if(i==6){
                    daerahId = 227;
                }else if(i==7){
                    daerahId = 282;
                }else if(i==8){
                    daerahId = 355;
                }else if(i==9){
                    daerahId = 368;
                }else if(i==10){
                    daerahId = 458;
                }else if(i==11){
                    daerahId = 490;
                }else if(i==12){
                    daerahId = 491;
                }else if(i==13){
                    daerahId = 496;
                }else if(i==14){
                    daerahId = 356;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etTelpDaftar = (EditText) view.findViewById(R.id.et_telp_daftar);
        etNamaDaftar = (EditText) view.findViewById(R.id.et_nama_daftar);
        etAlamatDaftar = (EditText) view.findViewById(R.id.et_alamat);
        flBelum = (FrameLayout) view.findViewById(R.id.fl_belum_dibayar);
        flDiproses = (FrameLayout) view.findViewById(R.id.fl_diproses);
        flDikirim = (FrameLayout) view.findViewById(R.id.fl_dikirim);
        flDiterima = (FrameLayout) view.findViewById(R.id.fl_diterima);

        btnCashOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPrefManager.getInstance(getContext()).getPengguna().getStatusPengguna() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Anda Harus Menjadi Penjual Terlebih Dahulu").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getContext(), "batal", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                } else {
                    startActivity(new Intent(getContext(), PencairanActivity.class));
                }
            }
        });

        flBelum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PesananActivity.class);
                intent.putExtra("JENIS","Belum Dibayar");
                startActivity(intent);
            }
        });

        flDiproses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PesananActivity.class);
                intent.putExtra("JENIS","Diproses");
                startActivity(intent);
            }
        });

        flDikirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PesananActivity.class);
                intent.putExtra("JENIS","Dikirim");
                startActivity(intent);
            }
        });

        flDiterima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PesananActivity.class);
                intent.putExtra("JENIS","Diterima");
                startActivity(intent);
            }
        });

        token = SharedPrefManager.getInstance(getContext()).getDeviceToken();

        tabLayout.addTab(tabLayout.newTab().setText("Beli"));
        tabLayout.addTab(tabLayout.newTab().setText("Jual"));

        BeliFragment beliFragment = new BeliFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_profil,beliFragment, "BeliFragment");
        transaction.commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tabLayout.getSelectedTabPosition() == 0){
                    BeliFragment beliFragment = new BeliFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_profil,beliFragment, "BeliFragment");
                    transaction.commit();
                }else if(tabLayout.getSelectedTabPosition() == 1){
                    JualFragment jualFragment = new JualFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_profil,jualFragment, "JualFragment");
                    transaction.commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if(SharedPrefManager.getInstance(getContext()).isLoggedIn()){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.ambilProfil,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                Pengguna pengguna = SharedPrefManager.getInstance(getContext()).getPengguna();

                                JSONObject obj = new JSONObject(response);
                                JSONObject objLogin = new JSONObject(obj.getString("success"));

                                SharedPrefManager.getInstance(getContext()).userLogin(new Pengguna(
                                        objLogin.getInt("id"),
                                        Integer.parseInt(objLogin.getString("saldo")),
                                        Integer.parseInt(objLogin.getString("status")),
                                        objLogin.getString("nama"),
                                        objLogin.getString("no_telp"),
                                        pengguna.getTokenPengguna(),
                                        objLogin.getString("alamat"),
                                        objLogin.getString("daerah"),
                                        objLogin.getString("foto"),
                                        objLogin.getString("email"),
                                        pengguna.getPasswordPengguna(),
                                        pengguna.getRememberToken(),
                                        pengguna.getDaerahId()
                                ));

                                DecimalFormat formatter = new DecimalFormat("#,###,###");
                                String yourFormattedString = formatter.format(Integer.parseInt(objLogin.getString("saldo")));
                                yourFormattedString = yourFormattedString.replace(",", ".");
                                tvSaldo.setText("Rp. " + yourFormattedString);

                                tvNama.setText(objLogin.getString("nama"));
                                tvTelp.setText(objLogin.getString("no_telp"));
                                Picasso.get()
                                        .load("http://inkubator.sikubis.com/uploads/file/" + objLogin.getString("foto"))
                                        .into(ivProfil);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(getContext()).getPengguna().getRememberToken());
                    params.put("Accept", "application/json");

                    return params;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(SharedPrefManager.getInstance(getContext()).getPengguna().getIdPengguna()));

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
            rlMasuk.setVisibility(View.GONE);
        }else{
            rlMasuk.setVisibility(View.VISIBLE);
        }

        rlDaftar.setVisibility(View.GONE);

        btnDaftarDisini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlDaftar.setVisibility(View.VISIBLE);
                rlMasuk.setVisibility(View.GONE);
            }
        });

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prosesMasuk();
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prosesDaftar();

            }
        });

        btnMasukDisini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlDaftar.setVisibility(View.GONE);
                rlMasuk.setVisibility(View.VISIBLE);
            }
        });

        Picasso.get()
                .load("http://inkubator.sikubis.com/uploads/file/" + SharedPrefManager.getInstance(getContext()).getPengguna().getFotoPengguna())
                .into(ivProfil);

        tvNama.setText(SharedPrefManager.getInstance(getContext()).getPengguna().getNamaPengguna());
        tvTelp.setText(SharedPrefManager.getInstance(getContext()).getPengguna().getNoTelpPengguna());

        return view;
    }

    public String randomKode(){
        final String AB = "0123456789";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder( 4 );
        for( int i = 0; i < 4; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Picasso.get()
                        .load("http://inkubator.sikubis.com/uploads/file/" + SharedPrefManager.getInstance(getContext()).getPengguna().getFotoPengguna())
                        .into(ivProfil);

                tvNama.setText(SharedPrefManager.getInstance(getContext()).getPengguna().getNamaPengguna());
                tvTelp.setText(SharedPrefManager.getInstance(getContext()).getPengguna().getNoTelpPengguna());
            }
        }
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public void prosesDaftar(){
        final String nama = etNamaDaftar.getText().toString();
        final String email = etEmailDaftar.getText().toString();
        final String password = etPasswordDaftar.getText().toString();
        final String telp = etTelpDaftar.getText().toString();
        final String alamat = etAlamatDaftar.getText().toString();
        noTelp = telp;

        //Validasi
        if(TextUtils.isEmpty(nama)){
            etNamaDaftar.setError("Harap isi nama anda");
            etNamaDaftar.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(email)){
            etEmailDaftar.setError("Harap isi email anda");
            etNamaDaftar.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password)){
            etPasswordDaftar.setError("Harap isi password anda");
            etPasswordDaftar.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(telp)){
            etTelpDaftar.setError("Harap isi nomor telp");
            etTelpDaftar.requestFocus();
            return;
        }

        if(telp.length()<11 || telp.length()>13){
            etTelpDaftar.setError("Format nomor telp tidak sesuai");
            etTelpDaftar.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(alamat)){
            etAlamatDaftar.setError("Harap isi alamat anda");
            etAlamatDaftar.requestFocus();
            return;
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Proses Daftar...");
        progressDialog.show();

        Log.d("MASUK",  email + "" + password + "" + token + "" + nama + "" + telp + "" + alamat + "" + daerahPilih + "" + daerahId + "" + token);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.register,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getContext(), obj.getString("success"), Toast.LENGTH_LONG).show();

                            if(obj.getString("success").equalsIgnoreCase("Berhasil Register")){
                                rlDaftar.setVisibility(View.GONE);
                                rlMasuk.setVisibility(View.VISIBLE);
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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("email", email);
                params.put("password", password);
                params.put("no_telp", telp);
                params.put("alamat", alamat);
                params.put("daerah", daerahPilih);
                params.put("daerah_id", String.valueOf(daerahId));
                params.put("token", token);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void kirimOTP(final String kode){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.kirimUlangOTP,
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
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("no_telp", noTelp);
                params.put("kode_verifikasi", kode);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void prosesMasuk(){



        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Proses Masuk...");
        progressDialog.show();

        final String email = etEmailMasuk.getText().toString();
        final String password = etPasswordMasuk.getText().toString();

        //Validasi
        if(TextUtils.isEmpty(email)){
            etEmailMasuk.setError("Harap isi email anda");
            etEmailMasuk.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password)){
            etPasswordMasuk.setError("Harap isi password anda");
            etPasswordMasuk.requestFocus();
            return;
        }

        Log.d("MASUK",  email + "" + password + "" + token);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {

                            JSONObject obj = new JSONObject(response);

                            try{
                                if(obj.getString("error").equalsIgnoreCase("Gagal Login")){
                                    Toast.makeText(getContext(), "Email Atau Password Salah", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            JSONObject objLogin = new JSONObject(obj.getString("success"));

                            Toast.makeText(getContext(), objLogin.getString("keterangan"), Toast.LENGTH_SHORT).show();

                            if(objLogin.getString("keterangan").equalsIgnoreCase("Berhasil Login")){
                                Log.d("TAMPIL", Integer.parseInt(objLogin.getString("saldo")) + " , " + Integer.parseInt(objLogin.getString("status")));
                                rlMasuk.setVisibility(View.GONE);

                                SharedPrefManager.getInstance(getContext()).userLogin(new Pengguna(
                                        objLogin.getInt("id"),
                                        Integer.parseInt(objLogin.getString("saldo")),
                                        Integer.parseInt(objLogin.getString("status")),
                                        objLogin.getString("nama"),
                                        objLogin.getString("no_telp"),
                                        token,
                                        objLogin.getString("alamat"),
                                        objLogin.getString("daerah"),
                                        objLogin.getString("foto"),
                                        objLogin.getString("email"),
                                        etPasswordMasuk.getText().toString(),
                                        objLogin.getString("remember_token"),
                                        objLogin.getInt("daerah_id")
                                ));


                                DecimalFormat formatter = new DecimalFormat("#,###,###");
                                String yourFormattedString = formatter.format(Integer.parseInt(objLogin.getString("saldo")));
                                yourFormattedString = yourFormattedString.replace(",", ".");
                                tvSaldo.setText("Rp. " + yourFormattedString);

                                tvNama.setText(objLogin.getString("nama"));
                                tvTelp.setText(objLogin.getString("no_telp"));
                                Picasso.get()
                                        .load("http://inkubator.sikubis.com/uploads/file/" + objLogin.getString("foto"))
                                        .into(ivProfil);

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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                params.put("token", token);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            if(SharedPrefManager.getInstance(getContext()).isLoggedIn()){
                rlMasuk.setVisibility(View.GONE);
                rlDaftar.setVisibility(View.GONE);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.ambilProfil,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {

                                    refresh.setRefreshing(false);
                                    Pengguna pengguna = SharedPrefManager.getInstance(getContext()).getPengguna();

                                    JSONObject obj = new JSONObject(response);
                                    JSONObject objLogin = new JSONObject(obj.getString("success"));

                                    SharedPrefManager.getInstance(getContext()).userLogin(new Pengguna(
                                            objLogin.getInt("id"),
                                            Integer.parseInt(objLogin.getString("saldo")),
                                            Integer.parseInt(objLogin.getString("status")),
                                            objLogin.getString("nama"),
                                            objLogin.getString("no_telp"),
                                            pengguna.getTokenPengguna(),
                                            objLogin.getString("alamat"),
                                            objLogin.getString("daerah"),
                                            objLogin.getString("foto"),
                                            objLogin.getString("email"),
                                            pengguna.getPasswordPengguna(),
                                            pengguna.getRememberToken(),
                                            0
                                    ));

                                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                                    String yourFormattedString = formatter.format(Integer.parseInt(objLogin.getString("saldo")));
                                    yourFormattedString = yourFormattedString.replace(",", ".");
                                    tvSaldo.setText("Rp. " + yourFormattedString);

                                    tvNama.setText(objLogin.getString("nama"));
                                    tvTelp.setText(objLogin.getString("no_telp"));
                                    Picasso.get()
                                            .load("http://inkubator.sikubis.com/uploads/file/" + objLogin.getString("foto"))
                                            .into(ivProfil);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(getContext()).getPengguna().getRememberToken());
                        params.put("Accept", "application/json");

                        return params;
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", String.valueOf(SharedPrefManager.getInstance(getContext()).getPengguna().getIdPengguna()));

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);
            }
        }
    }

}
