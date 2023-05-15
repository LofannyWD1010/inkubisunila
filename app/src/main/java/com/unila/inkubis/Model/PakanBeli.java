package com.unila.inkubis.Model;

public class PakanBeli {
    String nama, produsen, berat;

    public PakanBeli(String nama, String produsen, String berat){
        this.nama = nama;
        this.produsen = produsen;
        this.berat = berat;
    }

    public String getNama(){
        return nama;
    }

    public String getProdusen(){
        return produsen;
    }

    public String getBerat(){
        return berat;
    }
}
