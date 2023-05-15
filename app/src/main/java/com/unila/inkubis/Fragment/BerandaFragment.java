package com.unila.inkubis.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.unila.inkubis.Adapter.GridBerandaAdapter;
import com.unila.inkubis.Adapter.ItemPakanAdapter;
import com.unila.inkubis.Adapter.PagerBanner;
import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.Customable.ExpandableHeightGridView;
import com.unila.inkubis.LainnyaActivity;
import com.unila.inkubis.Model.Kategori;
import com.unila.inkubis.Model.Pakan;
import com.unila.inkubis.R;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class BerandaFragment extends Fragment {

    private static ViewPager mPager;
    private CircleIndicator indicator;
    private ExpandableHeightGridView gridView;
    private ImageView ivKelebihan;
    private RecyclerView rvFavorit;
    private TextView tvLihatLainnya;

    private ArrayList<String> fotoBanner;
    private ArrayList<Kategori> daftarKategori;
    private ArrayList<Pakan> daftarFavorit;
    private static int currentPage = 0;

    public BerandaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        indicator = (CircleIndicator) view.findViewById(R.id.indicator);
//        gridView = (ExpandableHeightGridView) view.findViewById(R.id.gv_kategori);
//        ivKelebihan = (ImageView) view.findViewById(R.id.iv_kelebihna);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        rvFavorit = (RecyclerView) view.findViewById(R.id.rv_favorit);
        tvLihatLainnya = (TextView) view.findViewById(R.id.tv_lihat_lainnya_favorit);
        rvFavorit.setLayoutManager(layoutManager);

        tvLihatLainnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LainnyaActivity.class);

                startActivity(intent);
            }
        });

        fotoBanner = new ArrayList<>();
        init();

//        daftarKategori = new ArrayList<>();
//        daftarKategori.add(new Kategori(1, "Pakan Sapi", "ic_sapi.png"));
//        daftarKategori.add(new Kategori(1, "Pakan Kuda", "ic_kuda.png"));
//        daftarKategori.add(new Kategori(1, "Pakan Domba & Kambing", "ic_doka.png"));
//        daftarKategori.add(new Kategori(1, "Pakan Ayam", "ic_ayam.png"));
//        daftarKategori.add(new Kategori(1, "Pakan Kerbau", "ic_kerbau.png"));
//        daftarKategori.add(new Kategori(1, "Supplement", "ic_suplement.png"));
//        daftarKategori.add(new Kategori(1, "Hijauan", "ic_hijauan.png"));
//        daftarKategori.add(new Kategori(1, "Bahan Mentah Pakan", "ic_bahanmentah.png"));
//        daftarKategori.add(new Kategori(1, "Produk Peternak Binaan", "ic_produkbinaan.png"));
//        daftarKategori.add(new Kategori(1, "Lainnya", "ic_lainnya.png"));
//
//        GridBerandaAdapter adapter = new GridBerandaAdapter(getContext(), R.layout.grid_kategori, daftarKategori);
//        gridView.setAdapter(adapter);
//        gridView.setExpanded(true);
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getContext(), LainnyaActivity.class);
//                intent.putExtra("TITLE", daftarKategori.get(i).getNamakategori());
//                startActivity(intent);
//            }
//        });

//        Picasso.get()
//                .load("http://sigerdev.com/dipangan/api/upload/kelebihan.jpg")
//                .into(ivKelebihan);

        daftarFavorit = new ArrayList<>();
        ambilFavorit();

        return view;
    }

    public void ambilFavorit(){
        StringRequest stringRequest = new StringRequest(BuildConfig.host + BuildConfig.daftarProdukHome,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            JSONArray barang = obj.getJSONArray("success");

//                            Toast.makeText(getContext(), events.getString(0), Toast.LENGTH_SHORT).show();

                            for(int i = 0 ; i < barang.length() ; i ++){
                                JSONObject product = barang.getJSONObject(i);

                                String foto2, foto3;

                                if(product.isNull("foto2")){
                                    foto2 = "kosong";
                                }else{
                                    foto2 = product.getString("foto2");
                                }

                                if(product.isNull("foto3")){
                                    foto3 = "kosong";
                                }else{
                                    foto3 = product.getString("foto3");
                                }

                                daftarFavorit.add(new Pakan(
                                        product.getInt("id"),
                                        Integer.parseInt(product.getString("id_pengguna")),
                                        Integer.parseInt(product.getString("harga")),
                                        product.getString("nama"),
                                        product.getString("satuan"),
                                        product.getString("status"),
                                        product.getString("kategori"),
                                        product.getString("lokasi"),
                                        product.getString("foto"),
                                        foto2,
                                        foto3,
                                        product.getString("deskripsi"),
                                        product.getString("nama_penjual"),
                                        product.getString("foto_penjual"),
                                        product.getString("no_telp"),
                                        Integer.parseInt(product.getString("iklan")),
                                        Integer.parseInt(product.getString("minimum")),
                                        Integer.parseInt(product.getString("stok")),
                                        Integer.parseInt(product.getString("berat")),
                                        Integer.parseInt(product.getString("daerah_id"))
                                ));
                            }

                            ItemPakanAdapter adapterFavorit = new ItemPakanAdapter(getContext(), daftarFavorit);
                            rvFavorit.setAdapter(adapterFavorit);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void init() {

        fotoBanner.add("banner_1.png");
        fotoBanner.add("banner_2.png");



        mPager.setAdapter(new PagerBanner(getContext(),fotoBanner));

        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == fotoBanner.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 5500);
    }

}
