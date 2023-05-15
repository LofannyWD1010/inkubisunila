package com.unila.inkubis.Model;

public class Kecamatan {
    int idKecamatan, idKabupaten, ongkir;
    String namaKecamatan, namaKabupaten;

    public Kecamatan(int idKecamatan, int idKabupaten, String namaKecamatan, String namaKabupaten, int ongkir) {
        this.idKecamatan = idKecamatan;
        this.idKabupaten = idKabupaten;
        this.namaKecamatan = namaKecamatan;
        this.namaKabupaten = namaKabupaten;
        this.ongkir = ongkir;
    }

    public int getIdKecamatan() {
        return idKecamatan;
    }

    public int getIdKabupaten() {
        return idKabupaten;
    }

    public String getNamaKecamatan() {
        return namaKecamatan;
    }

    public String getNamaKabupaten() {
        return namaKabupaten;
    }

    public void setOngkir(int ongkir) {
        this.ongkir = ongkir;
    }

    public int getOngkir(){
        return ongkir;
    }
}
