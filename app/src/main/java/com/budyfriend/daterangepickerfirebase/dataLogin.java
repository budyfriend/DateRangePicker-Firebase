package com.budyfriend.daterangepickerfirebase;

public class dataLogin {
    String npm;
    String nama;
    String password;
    String role;

    public dataLogin() {
    }

    public dataLogin(String npm, String nama, String password,String role) {
        this.npm = npm;
        this.nama = nama;
        this.password = password;
        this.role = role;
    }

    public String getNpm() {
        return npm;
    }

    public String getNama() {
        return nama;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
