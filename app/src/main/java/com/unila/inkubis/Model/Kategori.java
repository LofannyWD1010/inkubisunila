package com.unila.inkubis.Model;

public class Kategori {

    int idKategori;
    String namakategori, fotoKategori;

    public Kategori(int idKategori, String namakategori, String fotoKategori){
        this.idKategori = idKategori;
        this.namakategori = namakategori;
        this.fotoKategori = fotoKategori;
    }

    public int getIdKategori() {
        return idKategori;
    }

    public String getNamakategori() {
        return namakategori;
    }

    public String getFotoKategori() {
        return fotoKategori;
    }
}
