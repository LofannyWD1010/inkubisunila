package com.unila.inkubis.Model;

public class Produk {
    int id, idPengguna, harga, iklan, minimum, stok, berat;
    String nama, satuan, status, kategori, lokasi, foto, foto2, foto3, deskripsi;

    public Produk(int id, int idPengguna, int harga, String nama, String satuan, String status, String kategori, String lokasi, String foto, String foto2, String foto3, String deskripsi, int iklan, int minimum, int stok, int berat) {
        this.id = id;
        this.idPengguna = idPengguna;
        this.harga = harga;
        this.nama = nama;
        this.satuan = satuan;
        this.status = status;
        this.kategori = kategori;
        this.lokasi = lokasi;
        this.foto = foto;
        this.foto2 = foto2;
        this.foto3 = foto3;
        this.deskripsi = deskripsi;
        this.iklan = iklan;
        this.minimum = minimum;
        this.stok = stok;
        this.berat = berat;
    }


    public int getBerat() {
        return berat;
    }

    public int getId() {
        return id;
    }

    public int getIdPengguna() {
        return idPengguna;
    }

    public int getHarga() {
        return harga;
    }

    public String getNama() {
        return nama;
    }

    public String getSatuan() {
        return satuan;
    }

    public String getStatus() {
        return status;
    }

    public String getKategori() {
        return kategori;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getFoto() {
        return foto;
    }

    public String getFoto2() {
        return foto2;
    }

    public String getFoto3() {
        return foto3;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public int getIklan() {
        return iklan;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getStok() {
        return stok;
    }
}
