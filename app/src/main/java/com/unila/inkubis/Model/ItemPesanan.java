package com.unila.inkubis.Model;

public class ItemPesanan {
    int idDetail, idPenjual, idPembeli, idProduk;
    String idPesanan, tanggal, nama_penjual, telp_penjual, foto_penjual, nama_pembeli, telp_pembeli, foto_pembeli, total_keuntungan, status, produk;

    public ItemPesanan(int idDetail, int idPenjual, int idPembeli, String idPesanan, String tanggal, String nama_penjual, String telp_penjual, String foto_penjual, String nama_pembeli, String telp_pembeli, String foto_pembeli, String total_keuntungan, String status, String produk, int idProduk) {
        this.idDetail = idDetail;
        this.idPenjual = idPenjual;
        this.idPembeli = idPembeli;
        this.idPesanan = idPesanan;
        this.tanggal = tanggal;
        this.nama_penjual = nama_penjual;
        this.telp_penjual = telp_penjual;
        this.foto_penjual = foto_penjual;
        this.nama_pembeli = nama_pembeli;
        this.telp_pembeli = telp_pembeli;
        this.foto_pembeli = foto_pembeli;
        this.total_keuntungan = total_keuntungan;
        this.status = status;
        this.produk = produk;
        this.idProduk = idProduk;
    }

    public int getIdDetail() {
        return idDetail;
    }

    public int getIdPenjual() {
        return idPenjual;
    }

    public int getIdPembeli() {
        return idPembeli;
    }

    public String getIdPesanan() {
        return idPesanan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getNama_penjual() {
        return nama_penjual;
    }

    public String getTelp_penjual() {
        return telp_penjual;
    }

    public String getFoto_penjual() {
        return foto_penjual;
    }

    public String getNama_pembeli() {
        return nama_pembeli;
    }

    public String getTelp_pembeli() {
        return telp_pembeli;
    }

    public String getFoto_pembeli() {
        return foto_pembeli;
    }

    public String getTotal_keuntungan() {
        return total_keuntungan;
    }

    public String getStatus() {
        return status;
    }

    public String getProduk() {
        return produk;
    }

    public int getIdProduk() {
        return idProduk;
    }
}
