package com.unila.inkubis.Model;

public class Pencairan {
    int id;
    String tanggal, idPengguna, saldo, status;

    public Pencairan(int id, String tanggal, String idPengguna, String saldo, String status) {
        this.id = id;
        this.tanggal = tanggal;
        this.idPengguna = idPengguna;
        this.saldo = saldo;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getIdPengguna() {
        return idPengguna;
    }

    public String getSaldo() {
        return saldo;
    }

    public String getStatus() {
        return status;
    }
}
