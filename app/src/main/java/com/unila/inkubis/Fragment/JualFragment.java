package com.unila.inkubis.Fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unila.inkubis.AturLokasiTokoActivity;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.KurirActivity;
import com.unila.inkubis.MainActivity;
import com.unila.inkubis.Model.Pengguna;
import com.unila.inkubis.Model.RequestPenjual;
import com.unila.inkubis.OngkirActivity;
import com.unila.inkubis.ProdukSayaActivity;
import com.unila.inkubis.R;
import com.unila.inkubis.RequestMitraActivity;
import com.unila.inkubis.RequestPenjualActivity;
import com.unila.inkubis.SharedPrefManager;
import com.unila.inkubis.TambahProdukActivity;
import com.unila.inkubis.UbahProfilActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class JualFragment extends Fragment {

    LinearLayout llTambahProduk, llProduk, llPusatBantuan, llRequestPenjual;

    public JualFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jual, container, false);
        llTambahProduk = (LinearLayout) view.findViewById(R.id.ll_tambah_produk);
        llProduk = (LinearLayout) view.findViewById(R.id.ll_produk);
        llPusatBantuan = (LinearLayout) view.findViewById(R.id.ll_pusat_bantuan);
        llRequestPenjual = (LinearLayout) view.findViewById(R.id.ll_request_penjual);




        llProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SharedPrefManager.getInstance(getContext()).getPengguna().getStatusPengguna() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Anda Harus Request Penjual Terlebih Dahulu").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getContext(), "batal", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }else{
                    startActivity(new Intent(getContext(), ProdukSayaActivity.class));
                }
            }
        });

        llTambahProduk.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view){
                    if (SharedPrefManager.getInstance(getContext()).getPengguna().getStatusPengguna() == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Anda Harus Request Penjual Terlebih Dahulu").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getContext(), "batal", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                    }else{
                        startActivity(new Intent(getContext(), TambahProdukActivity.class));
                    }
                }
        });
        llRequestPenjual.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (SharedPrefManager.getInstance(getContext()).getPengguna().getStatusPengguna() == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Anda Sudah Menjadi Penjual").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getContext(), "batal", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
//                    }
//                    else if(SharedPrefManager.getInstance(getContext()).getRequestPenjual().getStatusRequest() == "belum") {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                    builder.setMessage("Anda Sudah Request Penjual").setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            Toast.makeText(getContext(), "batal", Toast.LENGTH_SHORT).show();
//                        }
//                    }).setNegativeButton("batal", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            Toast.makeText(getContext(), "batal", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Apakah anda ingin menjadi Penjual ?")
                            .setPositiveButton("ya", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                    progressDialog.setMessage("Proses Request...");
                                    progressDialog.show();

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + "daftarRequestMitra",
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    progressDialog.dismiss();
                                                    try {

                                                        JSONObject obj = new JSONObject(response);

                                                        try{
                                                            if(obj.getString("error").equalsIgnoreCase("Mendaftar Menjadi Penjual")){
                                                                Toast.makeText(getContext(), "Mendaftar", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(getContext(), RequestPenjualActivity.class));

                                                            }
                                                        }catch (Exception e){
                                                            e.printStackTrace();
                                                        }

                                                        Toast.makeText(getContext(), obj.getString("success"), Toast.LENGTH_SHORT).show();

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
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            Map<String, String>  params = new HashMap<String, String>();
                                            params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(getContext()).getPengguna().getRememberToken());

                                            return params;
                                        }

                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("id_pengguna", String.valueOf(SharedPrefManager.getInstance(getContext()).getPengguna().getIdPengguna()));

                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                    requestQueue.add(stringRequest);
                                }
                            })
                            .setNegativeButton("batal", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(getContext(), "batal", Toast.LENGTH_SHORT).show();
                                }
                            });
                    builder.show();
                }
            }
        });


        llPusatBantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPrefManager.getInstance(getContext()).getPengguna().getStatusPengguna() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Anda Harus Request Penjual Terlebih Dahulu").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getContext(), "batal", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }else{
                    startActivity(new Intent(getContext(), KurirActivity.class));
                }
            }
        });

        return view;
    }

}
