package com.unila.inkubis.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.unila.inkubis.Adapter.ItemInformasiAdapter;
import com.unila.inkubis.Model.Informasi;
import com.unila.inkubis.R;
import com.unila.inkubis.WebInformasiActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformasiFragment extends Fragment {

    ListView lvInformasi;
    ArrayList<Informasi> daftarInformasi;
    ProgressBar loadingBar;
    ItemInformasiAdapter adapter;

    int halaman = 1;
    
    public InformasiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informasi, container, false);
        lvInformasi = (ListView) view.findViewById(R.id.lv_informasi);
        loadingBar = (ProgressBar) view.findViewById(R.id.loading_bar);

        daftarInformasi = new ArrayList<>();

        adapter = new ItemInformasiAdapter(daftarInformasi, getContext());

        prosesAmbil();


        return view;
    }

    public void prosesAmbil(){

        loadingBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest("http://blog.epakan.id/wp-json/wp/v2/posts?per_page=4&_embed&page="+halaman,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            loadingBar.setVisibility(View.GONE);
                            JSONArray json = new JSONArray(response);

                            for(int i = 0 ; i < json.length() ; i ++){
                                JSONObject post = json.getJSONObject(i);
                                daftarInformasi.add(new Informasi(post.getJSONObject("title").getString("rendered"),  post.getJSONObject("_embedded").getJSONArray("wp:featuredmedia").getJSONObject(0).getString("source_url"), post.getString("link"),post.getJSONObject("content").getString("rendered").substring(4)));
                            }


                            lvInformasi.setAdapter(adapter);
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

        lvInformasi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), WebInformasiActivity.class);
                intent.putExtra("TITLE", daftarInformasi.get(i).getJudul());
                intent.putExtra("LINK", daftarInformasi.get(i).getLink());
                startActivity(intent);
            }
        });

        Button btnLoadMore = new Button(getContext());
        btnLoadMore.setText("Tekan Untuk Lihat Lainnya");
        btnLoadMore.setGravity(Gravity.CENTER);
        btnLoadMore.setTextColor(getResources().getColor(android.R.color.white));
        btnLoadMore.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        lvInformasi.addFooterView(btnLoadMore);
        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                halaman+=1;
                prosesAmbilKategoriPagination();
            }
        });
    }

    public void prosesAmbilKategoriPagination(){
        loadingBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest("http://blog.epakan.id/wp-json/wp/v2/posts?per_page=4&_embed&page="+halaman,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            loadingBar.setVisibility(View.GONE);
                            JSONArray json = new JSONArray(response);

                            for(int i = 0 ; i < json.length() ; i ++){
                                JSONObject post = json.getJSONObject(i);
                                daftarInformasi.add(new Informasi(post.getJSONObject("title").getString("rendered"),  post.getJSONObject("_embedded").getJSONArray("wp:featuredmedia").getJSONObject(0).getString("source_url"), post.getString("link"),post.getJSONObject("content").getString("rendered").substring(4)));
                            }


                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingBar.setVisibility(View.GONE);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    
}
