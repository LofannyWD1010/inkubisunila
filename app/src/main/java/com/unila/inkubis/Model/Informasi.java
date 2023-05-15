package com.unila.inkubis.Model;

public class Informasi {
    String judul, foto, link, isi;

    public Informasi(String judul, String foto, String link, String isi){
        this.judul = judul;
        this.foto = foto;
        this.link = link;
        this.isi = isi;
    }

    public String getJudul(){
        return judul;
    }

    public String getFoto(){
        return foto;
    }

    public String getLink(){
        return link;
    }

    public String getIsi(){
        return isi;
    }
}
