package com.unila.inkubis.Model;

public class PakanKeranjang{
    int id, harga, jumlah, idPenjual, idKeranjang, idDaerah, berat;
    String nama, foto, satuan, jenis;

    public PakanKeranjang(){

    }

    public PakanKeranjang(int id, String nama, String satuan, int harga, String foto, int jumlah, String jenis, int idPenjual, int idKeranjang, int idDaerah, int berat){
        this.id = id;
        this.nama = nama;
        this.satuan = satuan;
        this.harga = harga;
        this.foto = foto;
        this.jumlah = jumlah;
        this.jenis = jenis;
        this.idPenjual = idPenjual;
        this.idKeranjang = idKeranjang;
        this.idDaerah = idDaerah;
        this.berat = berat;
    }

    public int getBerat() {
        return berat;
    }

    public int getIdDaerah() {
        return idDaerah;
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
}
