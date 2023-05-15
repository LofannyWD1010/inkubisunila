package com.unila.inkubis.Model;

public class RequestPenjual {
    int idRequest, idPengguna,nomor,idFakultas,idJurusan,idCivitas, statusRequest;
    String  foto1, foto2, foto3;


    public RequestPenjual(int idPengguna, int idRequest, int nomor, int statusRequest, int idCivitas, int idFakultas, int idJurusan, String foto1, String foto2, String foto3) {
        this.idPengguna = idPengguna;
        this.statusRequest = statusRequest;
        this.nomor = nomor;
        this.idRequest = idRequest;
        this.idCivitas = idCivitas;
        this.idFakultas = idFakultas;
        this.idJurusan = idJurusan;
        this.foto1 = foto1;
        this.foto2 = foto2;
        this.foto3 = foto3;

    }
    public int getIdPengguna() {
        return idPengguna;
    }

    public int getIdRequest() { return idRequest; }

    public int getNomor() { return nomor;}

    public int getStatusRequest() {
        return statusRequest;
    }

    public int getIdCivitas() { return idCivitas; }

    public int getIdFakultas() { return idFakultas; }

    public int getIdJurusan() { return idJurusan; }

    public String getFoto1() { return foto1; }

    public String getFoto2() {return foto2; }

    public String getFoto3() {return foto3; }


}
