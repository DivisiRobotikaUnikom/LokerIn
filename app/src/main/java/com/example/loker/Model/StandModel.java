package com.example.loker.Model;

public class StandModel {
    private String harga;
    private String jumlah;
    private String lokasi;
    private String nama;

    public StandModel(String harga, String jumlah, String lokasi, String nama) {
        this.harga = harga;
        this.jumlah = jumlah;
        this.lokasi = lokasi;
        this.nama = nama;
    }

    public StandModel() {
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
