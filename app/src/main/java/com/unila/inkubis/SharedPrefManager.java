package com.unila.inkubis;

import android.content.Context;
import android.content.SharedPreferences;

import com.unila.inkubis.Model.ItemPesanan;
import com.unila.inkubis.Model.Pakan;
import com.unila.inkubis.Model.Pengguna;
import com.unila.inkubis.Model.Pesanan;
import com.unila.inkubis.Model.Produk;
import com.unila.inkubis.Model.RequestPenjual;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";
    private static final String KEY_ID = "key_id";
    private static final String KEY_NAMA = "key_nama";
    private static final String KEY_TELP = "key_telp";
    private static final String KEY_TOKEN = "key_token";
    private static final String KEY_FOTO = "key_foto";
    private static final String KEY_FIRST = "key_first";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }

    public void userLogin(Pengguna pengguna) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID,pengguna.getIdPengguna());
        editor.putInt("key_saldo",pengguna.getSaldoPengguna());
        editor.putInt("key_status",pengguna.getStatusPengguna());
        editor.putString(KEY_NAMA, pengguna.getNamaPengguna());
        editor.putString(KEY_TELP, pengguna.getNoTelpPengguna());
        editor.putString(KEY_TOKEN, pengguna.getTokenPengguna());
        editor.putString("key_alamat", pengguna.getAlamatPengguna());
        editor.putString("key_daerah", pengguna.getDaerahPengguna());
        editor.putString(KEY_FOTO, pengguna.getFotoPengguna());
        editor.putString("key_email", pengguna.getEmailPengguna());
        editor.putString("key_password", pengguna.getPasswordPengguna());
        editor.putString("key_remember_token", pengguna.getRememberToken());
        editor.putInt("key_daerah_id",pengguna.getDaerahId());
        editor.apply();
    }

//    public void ubahStatusPesanan(Pesanan pesanan) {
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("ongkir",pesanan.getOngkir());
//        editor.putInt("harga",pesanan.getHarga());
//        editor.putInt("total_bayar",pesanan.getTotalBayar());
//        editor.putInt("id_pengguna",pesanan.getIdPengguna());
//        editor.putString("kode_pesanan",pesanan.getIdPesanan());
//        editor.putString(KEY_FOTO,pesanan.getFoto());
//        editor.apply();
//    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAMA, null) != null;
    }

    public void setFirst(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FIRST, "FIRST");
        editor.apply();
    }

    public boolean isFirst() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FIRST, null) != null;
    }

    public Pengguna getPengguna() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Pengguna(
                sharedPreferences.getInt(KEY_ID, 0),
                sharedPreferences.getInt("key_saldo", 0),
                sharedPreferences.getInt("key_status", 0),
                sharedPreferences.getString(KEY_NAMA,null),
                sharedPreferences.getString(KEY_TELP,null),
                sharedPreferences.getString(KEY_TOKEN,null),
                sharedPreferences.getString("key_alamat",null),
                sharedPreferences.getString("key_daerah",null),
                sharedPreferences.getString(KEY_FOTO,null),
                sharedPreferences.getString("key_email",null),
                sharedPreferences.getString("key_password",null),
                sharedPreferences.getString("key_remember_token", null),
                sharedPreferences.getInt("key_daerah_id",0)
        );
    }
//    public RequestPenjual getRequestPenjual() {
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        return new RequestPenjual(
//                sharedPreferences.getInt(KEY_ID, 0),
//                sharedPreferences.getInt("key_id_request",0),
//                sharedPreferences.getInt("key_nomor",0),
//                sharedPreferences.getInt("key_status",0),
//                sharedPreferences.getInt("key_civitas",0),
//                sharedPreferences.getInt("key_fakultas",0),
//                sharedPreferences.getInt("key_jurusan",0),
//                sharedPreferences.getString("key_foto1",null),
//                sharedPreferences.getString("key_foto2",null),
//                sharedPreferences.getString("key_foto3",null)
//        );
//    }

    public void logout(){
        mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit().clear().commit();
    }

    public void clearLoggedInUser(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_ID);
        editor.remove("key_saldo");
        editor.remove("key_status");
        editor.remove(KEY_NAMA);
        editor.remove(KEY_TELP);
        editor.remove(KEY_TOKEN);
        editor.remove("key_alamat");
        editor.remove("key_daerah");
        editor.remove(KEY_FOTO);
        editor.remove("key_email");
        editor.remove("key_password");
        editor.remove("key_remember_token");
        editor.remove("key_status");
        editor.remove("key_daerah_id");
        editor.apply();
    }

    public Pesanan getPesanan() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Pesanan(
                sharedPreferences.getInt("ongkir",0),
                sharedPreferences.getInt("harga",0),
                sharedPreferences.getInt("total_bayar",0),
                sharedPreferences.getInt("id_pengguna",0),
                sharedPreferences.getString("id_pesanan", null ),
                sharedPreferences.getString("foto",null)
        );
    }

    public Produk getProduk(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Produk(
                sharedPreferences.getInt(KEY_ID,0),
                sharedPreferences.getInt("id_pengguna",0),
                sharedPreferences.getInt("harga",0),
                sharedPreferences.getString("nama",null),
                sharedPreferences.getString("satuan",null),
                sharedPreferences.getString("status",null),
                sharedPreferences.getString("kategori",null),
                sharedPreferences.getString("lokasi",null),
                sharedPreferences.getString("foto",null),
                sharedPreferences.getString("foto2",null),
                sharedPreferences.getString("foto3",null),
                sharedPreferences.getString("deskripsi",null),
                sharedPreferences.getInt("iklan",0),
                sharedPreferences.getInt("minimum",0),
                sharedPreferences.getInt("stok",0),
                sharedPreferences.getInt("berat",0)
        );
    }
}

