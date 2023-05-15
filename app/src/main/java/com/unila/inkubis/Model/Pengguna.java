package com.unila.inkubis.Model;

public class Pengguna {

    int idPengguna, saldoPengguna, statusPengguna, daerahId;
    String namaPengguna, noTelpPengguna, tokenPengguna, alamatPengguna, daerahPengguna, fotoPengguna, emailPengguna, passwordPengguna, rememberToken;

    public Pengguna(int idPengguna, int saldoPengguna, int statusPengguna, String namaPengguna, String noTelpPengguna, String tokenPengguna, String alamatPengguna, String daerahPengguna, String fotoPengguna, String emailPengguna, String passwordPengguna, String rememberToken, int daerahId) {
        this.idPengguna = idPengguna;
        this.saldoPengguna = saldoPengguna;
        this.statusPengguna = statusPengguna;
        this.namaPengguna = namaPengguna;
        this.noTelpPengguna = noTelpPengguna;
        this.tokenPengguna = tokenPengguna;
        this.alamatPengguna = alamatPengguna;
        this.daerahPengguna = daerahPengguna;
        this.fotoPengguna = fotoPengguna;
        this.emailPengguna = emailPengguna;
        this.passwordPengguna = passwordPengguna;
        this.rememberToken = rememberToken;
        this.daerahId = daerahId;
    }

    public int getDaerahId() {
        return daerahId;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public int getIdPengguna() {
        return idPengguna;
    }

    public int getSaldoPengguna() {
        return saldoPengguna;
    }

    public int getStatusPengguna() {
        return statusPengguna;
    }

    public String getNamaPengguna() {
        return namaPengguna;
    }

    public String getNoTelpPengguna() {
        return noTelpPengguna;
    }

    public String getTokenPengguna() {
        return tokenPengguna;
    }

    public String getAlamatPengguna() {
        return alamatPengguna;
    }

    public String getDaerahPengguna() {
        return daerahPengguna;
    }

    public String getFotoPengguna() {
        return fotoPengguna;
    }

    public String getEmailPengguna() {
        return emailPengguna;
    }

    public String getPasswordPengguna() {
        return passwordPengguna;
    }
}
