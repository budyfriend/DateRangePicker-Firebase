package com.budyfriend.daterangepickerfirebase;

public class dataUser {
    private String id;

    private String npm;
    private String nama;
    private String jk;
    private String jurusan;
    private long tgl_pendaftaran;
    boolean voting;

    public dataUser() {
    }

    public dataUser(String npm,String nama, String jk, String jurusan, long tgl_pendaftaran,boolean voting) {
        this.npm = npm;
        this.nama = nama;
        this.jk = jk;
        this.jurusan = jurusan;
        this.tgl_pendaftaran = tgl_pendaftaran;
        this.voting = voting;
    }

    public String getNpm() {
        return npm;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getJk() {
        return jk;
    }

    public String getJurusan() {
        return jurusan;
    }

    public long getTgl_pendaftaran() {
        return tgl_pendaftaran;
    }

    public boolean isVoting() {
        return voting;
    }
}
