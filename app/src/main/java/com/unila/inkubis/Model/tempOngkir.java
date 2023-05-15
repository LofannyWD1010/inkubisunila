package com.unila.inkubis.Model;

public class tempOngkir {
    int idUnik, ongkir, idDaerah, berat, jumlah, idKurir;
    String lokasiAntar;

    public tempOngkir(int idUnik, int ongkir, String lokasiAntar, int idDaerah, int berat, int jumlah, int idKurir) {
        this.idUnik = idUnik;
        this.ongkir = ongkir;
        this.lokasiAntar = lokasiAntar;
        this.idDaerah = idDaerah;
        this.berat = berat;
        this.jumlah = jumlah;
        this.idKurir = idKurir;
    }

    public int getJumlah() {
        return jumlah;
    }

    public int getBerat() {
        return berat;
    }

    public int getIdDaerah() {
        return idDaerah;
    }

    public int getIdUnik() {
        return idUnik;
    }

    public int getOngkir() {
        return ongkir;
    }

    public String getLokasiAntar() {
        return lokasiAntar;
    }

    public void setIdUnik(int idUnik) {
        this.idUnik = idUnik;
    }

    public void setOngkir(int ongkir) {
        this.ongkir = ongkir;
    }

    public void setLokasiAntar(String lokasiAntar) {
        this.lokasiAntar = lokasiAntar;
    }

    public int getIdKurir() {
        return idKurir;
    }

    public void setIdKurir(int idKurir) {
        this.idKurir = idKurir;
    }
}
