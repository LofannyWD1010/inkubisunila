package com.unila.inkubis.Model;

public class PakanLokasi {
    int id, harga, jumlah, idPenjual, idKeranjang, ongkir, idKurir;
    String nama, foto, satuan, jenis, lokasiAntar;

    public PakanLokasi(){

    }

    public PakanLokasi(int id, String nama, String satuan, int harga, String foto, int jumlah, String jenis, int idPenjual, int idKeranjang, int ongkir, String lokasiAntar, int idKurir){
        this.id = id;
        this.nama = nama;
        this.satuan = satuan;
        this.harga = harga;
        this.foto = foto;
        this.jumlah = jumlah;
        this.jenis = jenis;
        this.idPenjual = idPenjual;
        this.idKeranjang = idKeranjang;
        this.ongkir = ongkir;
        this.lokasiAntar = lokasiAntar;
        this.idKurir = idKurir;
    }

    public String getSatuan(){
        return satuan;
    }

    public int getId() {
        return id;
    }

    public int getHarga() {
        return harga;
    }

    public String getNama() {
        return nama;
    }

    public String getFoto() {
        return foto;
    }

    public int getJumlah() {
        return jumlah;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getIdPenjual() {
        return idPenjual;
    }

    public int getIdKeranjang() {
        return idKeranjang;
    }

    public void setOngkir(int ongkir) {
        this.ongkir = ongkir;
    }

    public int getOngkir() {
        return ongkir;
    }

    public String getLokasiAntar() {
        return lokasiAntar;
    }

    public void setLokasiAntar(String lokasiAntar) {
        this.lokasiAntar = lokasiAntar;
    }

    public int getIdKurir() {
        return idKurir;
    }
}
