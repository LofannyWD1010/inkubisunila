package com.unila.inkubis;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.Fragment.BantuanFragment;
import com.unila.inkubis.Fragment.BerandaFragment;
import com.unila.inkubis.Fragment.InformasiFragment;
import com.unila.inkubis.Fragment.PesananFragment;
import com.unila.inkubis.Fragment.ProfilFragment;
import com.unila.inkubis.R;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView ivPencarian, ivKonsultasi, ivKeranjang, ivNotifikasiAda;
    FrameLayout flNotifikasi;

    String token;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cekVersi();

//        Toast.makeText(this, InstanceId, Toast.LENGTH_SHORT).show();

        ivPencarian = (ImageView) findViewById(R.id.iv_pencarian);
//        ivKonsultasi = (ImageView) findViewById(R.id.iv_konsultasi);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        ivKeranjang = (ImageView) findViewById(R.id.iv_keranjang);
        flNotifikasi = (FrameLayout) findViewById(R.id.fl_notifikasi);
        ivNotifikasiAda = (ImageView) findViewById(R.id.iv_notifikasi_ada);

        jumlahNotifikasi();

        flNotifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListNotifikasiActivity.class));
            }
        });

        ivKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SharedPrefManager.getInstance(MainActivity.this).isLoggedIn()){
                    startActivity(new Intent(MainActivity.this, KeranjangActivity.class));
                }else{
                    Toast.makeText(MainActivity.this, "Harap Login Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (Build.VERSION.SDK_INT >= 23) {
            if(!SharedPrefManager.getInstance(MainActivity.this).isFirst()){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                View itemMenu = bottomNavigationView.getRootView();
                final BottomNavigationItemView menuBeranda = (BottomNavigationItemView) itemMenu.findViewById(R.id.navigation_home);
                final BottomNavigationItemView menuPesanan = (BottomNavigationItemView) itemMenu.findViewById(R.id.navigation_pesan);
//                final BottomNavigationItemView menuInformasi = (BottomNavigationItemView) itemMenu.findViewById(R.id.navigation_blog);
                final BottomNavigationItemView menuBantuan = (BottomNavigationItemView) itemMenu.findViewById(R.id.navigation_help);
                final BottomNavigationItemView menuProfil = (BottomNavigationItemView) itemMenu.findViewById(R.id.navigation_profil);


                GuideView.Builder gv1 =  new GuideView.Builder(this);



                gv1.setTitle("Pencarian")
                        .setContentText("Cari produk yang anda inginkan")
                        .setGravity(Gravity.auto)
                        .setDismissType(DismissType.anywhere)
                        .setGuideListener(new GuideListener() {
                            @Override
                            public void onDismiss(View view) {
                                new GuideView.Builder(MainActivity.this).setTitle("Keranjang")
                                        .setContentText("Anda dapat menambahkan barang yang ingin anda beli pada keranjang")
                                        .setGravity(Gravity.auto) //optional
                                        .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                                        .setGuideListener(new GuideListener() {
                                            @Override
                                            public void onDismiss(View view) {
                                                new GuideView.Builder(MainActivity.this)
                                                        .setTitle("Beranda")
                                                        .setContentText("Anda dapat melihat berbagai produk kami pada menu 'Beranda'")
                                                        .setGravity(Gravity.auto) //optional
                                                        .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                                                        .setGuideListener(new GuideListener() {
                                                            @Override
                                                            public void onDismiss(View view) {
                                                                new GuideView.Builder(MainActivity.this)
                                                                        .setTitle("Pesanan")
                                                                        .setContentText("Anda dapat melihat daftar pesanan pada menu 'Pesanan'")
                                                                        .setGravity(Gravity.auto) //optional
                                                                        .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                                                                        .setGuideListener(new GuideListener() {
                                                                            public void onDismiss(View view) {
                                                                                new GuideView.Builder(MainActivity.this)
                                                                                        .setTitle("Bantuan")
                                                                                        .setContentText("Untuk melihat berbagai panduan penggunaan")
                                                                                        .setGravity(Gravity.auto) //optional
                                                                                        .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                                                                                        .setTargetView(menuBantuan)
                                                                                        .setGuideListener(new GuideListener() {
                                                                                            @Override
                                                                                            public void onDismiss(View view) {
                                                                                                new GuideView.Builder(MainActivity.this)
                                                                                                        .setTitle("Profil")
                                                                                                        .setContentText("Anda dapat mengatur profil anda pada menu 'Profil'")
                                                                                                        .setGravity(Gravity.auto) //optional
                                                                                                        .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                                                                                                        .setTargetView(menuProfil)
                                                                                                        .setContentTextSize(12)//optional
                                                                                                        .setTitleTextSize(14)//optional
                                                                                                        .build()
                                                                                                        .show();
                                                                                            }
                                                                                        })
                                                                                        .setContentTextSize(12)//optional
                                                                                        .setTitleTextSize(14)//optional
                                                                                        .build()
                                                                                        .show();
                                                                            }
                                                                        })
                                                                        .setTargetView(menuPesanan)
                                                                        .setContentTextSize(12)//optional
                                                                        .setTitleTextSize(14)//optional
                                                                        .build()
                                                                        .show();
                                                            }
                                                        })
                                                        .setTargetView(menuBeranda)
                                                        .setContentTextSize(12)//optional
                                                        .setTitleTextSize(14)//optional
                                                        .build()
                                                        .show();
                                            }
                                        })
                                        .setTargetView(ivKeranjang)
                                        .setContentTextSize(12)//optional
                                        .setTitleTextSize(14)//optional
                                        .build()
                                        .show();
                            }
                        })
                        .setTargetView(ivPencarian)
                        .setContentTextSize(12)//optional
                        .setTitleTextSize(14)//optional
                        .build()
                        .show();

                SharedPrefManager.getInstance(MainActivity.this).setFirst();
            }
        }



        token = SharedPrefManager.getInstance(MainActivity.this).getDeviceToken();

        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        ivPencarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LainnyaActivity.class);

                startActivity(intent);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frame_container, new BerandaFragment(), "beranda").commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        bottomNavigationView.getMenu().getItem(0).setChecked(true);
                        if(fragmentManager.findFragmentByTag("beranda") != null) {
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("beranda")).commit();
                        } else {
                            fragmentManager.beginTransaction().add(R.id.frame_container, new BerandaFragment(), "beranda").commit();
                        }
                        if(fragmentManager.findFragmentByTag("pesanan") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("pesanan")).commit();
                        }
//                        if(fragmentManager.findFragmentByTag("informasi") != null){
//                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("informasi")).commit();
//                        }
                        if(fragmentManager.findFragmentByTag("bantuan") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("bantuan")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("profil") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("profil")).commit();
                        }
                        break;
                    case R.id.navigation_pesan:
                        bottomNavigationView.getMenu().getItem(1).setChecked(true);
                        if(fragmentManager.findFragmentByTag("pesanan") != null) {
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("pesanan")).commit();
                        } else {
                            fragmentManager.beginTransaction().add(R.id.frame_container, new PesananFragment(), "pesanan").commit();
                        }
                        if(fragmentManager.findFragmentByTag("beranda") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("beranda")).commit();
                        }
//                        if(fragmentManager.findFragmentByTag("informasi") != null){
//                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("informasi")).commit();
//                        }
                        if(fragmentManager.findFragmentByTag("bantuan") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("bantuan")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("profil") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("profil")).commit();
                        }
                        break;
//                    case R.id.navigation_blog:
//                        bottomNavigationView.getMenu().getItem(2).setChecked(true);
//                        if(fragmentManager.findFragmentByTag("informasi") != null) {
//                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("informasi")).commit();
//                        } else {
//                            fragmentManager.beginTransaction().add(R.id.frame_container, new InformasiFragment(), "informasi").commit();
//                        }
//                        if(fragmentManager.findFragmentByTag("pesanan") != null){
//                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("pesanan")).commit();
//                        }
//                        if(fragmentManager.findFragmentByTag("profil") != null){
//                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("profil")).commit();
//                        }
//                        if(fragmentManager.findFragmentByTag("bantuan") != null){
//                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("bantuan")).commit();
//                        }
//                        if(fragmentManager.findFragmentByTag("beranda") != null){
//                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("beranda")).commit();
//                        }
//                        break;
                    case R.id.navigation_help:
                        bottomNavigationView.getMenu().getItem(2).setChecked(true);
                        if(fragmentManager.findFragmentByTag("bantuan") != null) {
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("bantuan")).commit();
                        } else {
                            fragmentManager.beginTransaction().add(R.id.frame_container, new BantuanFragment(), "bantuan").commit();
                        }
                        if(fragmentManager.findFragmentByTag("pesanan") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("pesanan")).commit();
                        }
//                        if(fragmentManager.findFragmentByTag("informasi") != null){
//                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("informasi")).commit();
//                        }
                        if(fragmentManager.findFragmentByTag("beranda") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("beranda")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("profil") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("profil")).commit();
                        }
                        break;
                    case R.id.navigation_profil:
                        bottomNavigationView.getMenu().getItem(3).setChecked(true);
                        if(fragmentManager.findFragmentByTag("profil") != null) {
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("profil")).commit();
                        } else {
                            fragmentManager.beginTransaction().add(R.id.frame_container, new ProfilFragment(), "profil").commit();
                        }
                        if(fragmentManager.findFragmentByTag("pesanan") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("pesanan")).commit();
                        }
//                        if(fragmentManager.findFragmentByTag("informasi") != null){
//                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("informasi")).commit();
//                        }
                        if(fragmentManager.findFragmentByTag("bantuan") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("bantuan")).commit();
                        }
                        if(fragmentManager.findFragmentByTag("beranda") != null){
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("beranda")).commit();
                        }
                        break;
                }
                return false;
            }
        });

//        ivKonsultasi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setMessage("Anda dapat berkonsultasi dengan kami via Whatsapp, Gratis ...")
//                        .setPositiveButton("Konsultasi", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+6285780938970&text=");
//                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                                startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("Nanti", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.dismiss();
//                            }
//                        });
//                // Create the AlertDialog object and return it
//                AlertDialog dialog = builder.create();
//                dialog.show();
//
//            }
//        });
    }

    //Menghitung jumlah notifikasi
    public void jumlahNotifikasi(){
        StringRequest stringRequest = new StringRequest(BuildConfig.host + BuildConfig.jumlahListNotifikasi + SharedPrefManager.getInstance(MainActivity.this).getPengguna().getIdPengguna(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            int baris = obj.getInt("success");
                            if(baris > 0){
                                ivNotifikasiAda.setVisibility(View.VISIBLE);
                            }else{
                                ivNotifikasiAda.setVisibility(View.GONE);
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
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + SharedPrefManager.getInstance(MainActivity.this).getPengguna().getRememberToken());
                params.put("Accept", "application/json");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void cekVersi(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.host + BuildConfig.cekVersi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject obj = new JSONObject(response);
                            JSONObject objVersi = new JSONObject(obj.getString("success"));

                            PackageManager manager = MainActivity.this.getPackageManager();
                            PackageInfo info = null;
                            try {
                                info = manager.getPackageInfo(MainActivity.this.getPackageName(), PackageManager.GET_ACTIVITIES);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                            if(info.versionCode != Integer.parseInt(objVersi.getString("version_code")) && (!info.versionName.equalsIgnoreCase(objVersi.getString("version_name")))){
                                if(objVersi.getString("force_update").equalsIgnoreCase("iya")){
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                    builder1.setMessage("Versi terbaru aplikasi sudah tersedia, diharuskan untuk perbarui aplikasi terlebih dahulu !");
                                    builder1.setCancelable(false);

                                    builder1.setPositiveButton(
                                            "Perbarui",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                    try {
                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                    } catch (android.content.ActivityNotFoundException anfe) {
                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                    }
                                                    finish();
                                                }
                                            });


                                    builder1.setNegativeButton(
                                            "Nanti",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    finish();
                                                }
                                            });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();
                                }else{
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                    builder1.setMessage("Versi terbaru aplikasi sudah tersedia (lebih stabil)");
                                    builder1.setCancelable(true);

                                    builder1.setPositiveButton(
                                            "Perbarui",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                    try {
                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                    } catch (android.content.ActivityNotFoundException anfe) {
                                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                    }
                                                    finish();
                                                }
                                            });


                                    builder1.setNegativeButton(
                                            "Nanti",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                }
                                            });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_versi", "1");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void bukaBantuan(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        bottomNavigationView.getMenu().getItem(2).setChecked(true);
        if(fragmentManager.findFragmentByTag("bantuan") != null) {
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("bantuan")).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.frame_container, new BantuanFragment(), "bantuan").commit();
        }
        if(fragmentManager.findFragmentByTag("pesanan") != null){
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("pesanan")).commit();
        }
        if(fragmentManager.findFragmentByTag("beranda") != null){
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("beranda")).commit();
        }
        if(fragmentManager.findFragmentByTag("profil") != null){
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("profil")).commit();
        }
    }

    static class BottomNavigationViewHelper {

        static void removeShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);

                }
            } catch (NoSuchFieldException e) {
                Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
            } catch (IllegalAccessException e) {
                Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
            }
        }
    }
}
