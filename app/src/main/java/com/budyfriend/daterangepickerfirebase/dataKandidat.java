package com.budyfriend.daterangepickerfirebase;

public class dataKandidat {
    String key;
    String ketua;
    String wakil;
    String group;
    String year;
    long count;


    public dataKandidat() {
    }

    public dataKandidat(String ketua, String wakil, String group, String year,long count) {
        this.ketua = ketua;
        this.wakil = wakil;
        this.group = group;
        this.year = year;
        this.count = count;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getKetua() {
        return ketua;
    }

    public String getWakil() {
        return wakil;
    }

    public String getGroup() {
        return group;
    }

    public String getYear() {
        return year;
    }

    public long getCount() {
        return count;
    }
}
