package com.unila.inkubis.Model;

public class Pesanan {
    int  ongkir, harga, totalBayar,idPengguna;
    String idPesanan,foto;

    public Pesanan(int ongkir, int harga, int totalBayar, int idPengguna, String idPesanan, String foto){
        this.ongkir = ongkir;
        this.harga = harga;
        this.totalBayar = totalBayar;
        this.idPengguna = idPengguna;
        this.idPesanan = idPesanan;
        this.foto = foto;
    }

    public int getOngkir() { return ongkir; }

    public int getHarga() { return harga; }

    public int getTotalBayar() { return totalBayar; }

    public int getIdPengguna() { return idPengguna; }

    public String getIdPesanan() { return idPesanan; }

    public String getFoto() { return foto; }




}
