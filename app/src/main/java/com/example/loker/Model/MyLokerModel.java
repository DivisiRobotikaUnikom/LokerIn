package com.example.loker.Model;

public class MyLokerModel {
    private String loker;
    private String tanggal;
    private String jam;

    public MyLokerModel(String loker, String tanggal, String jam) {
        this.loker = loker;
        this.tanggal = tanggal;
        this.jam = jam;
    }

    public MyLokerModel() {
    }

    public String getLoker() {
        return loker;
    }

    public void setLoker(String loker) {
        this.loker = loker;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }
}
