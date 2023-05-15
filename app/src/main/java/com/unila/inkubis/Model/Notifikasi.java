package com.unila.inkubis.Model;

public class Notifikasi {
    int idProduk;
    String kodePesanan, tanggal, isi;

    public Notifikasi(String kodePesanan, int idProduk, String isi, String tanggal) {
        this.idProduk = idProduk;
        this.kodePesanan = kodePesanan;
        this.tanggal = tanggal;
        this.isi = isi;
    }

    public String getIsi() {
        return isi;
    }

    public String getTanggal() {
        return tanggal;
    }


    public int getIdProduk() {
        return idProduk;
    }

    public String getKodePesanan() {
        return kodePesanan;
    }
}
