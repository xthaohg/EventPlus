package com.poly.eventplus.model;

public class Event {

    private String Tens;
    private String Thoigian;
    private String Hinhs;
    private String Danhmuc;
    private String Khuvuc;
    private String Sokhach;
    private String Donvi;
    private String Mota;
    private String Diadiem;
    private String Username;
    private String Email;
    private String MatKhau;
    private String Status;
    private int Sdt;
    private int Id;
    private String IdUsername;
    private int countOrder;
    private Double Rate;

    public Event() {

    }

    public int getCountOrder() {
        return countOrder;
    }

    public void setCountOrder(int countOrder) {
        this.countOrder = countOrder;
    }

    public Event(int countOrder) {
        this.countOrder = countOrder;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String matKhau) {
        MatKhau = matKhau;
    }

    public Event(String idUsername) {
        IdUsername = idUsername;
    }

    public String getIdUsername() {
        return IdUsername;
    }

    public void setIdUsername(String idUsername) {
        IdUsername = idUsername;
    }

    public Event(String tens, String thoigian, String hinhs, String danhmuc, String khuvuc, String sokhach, String donvi, String mota, String diadiem, String username, String email, String status, int sdt, int id, Double rate) {
        Tens = tens;
        Thoigian = thoigian;
        Hinhs = hinhs;
        Danhmuc = danhmuc;
        Khuvuc = khuvuc;
        Sokhach = sokhach;
        Donvi = donvi;
        Mota = mota;
        Diadiem = diadiem;
        Username = username;
        Email = email;
        Status = status;
        Sdt = sdt;
        Id = id;
        Rate = rate;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getSdt() {
        return Sdt;
    }

    public void setSdt(int sdt) {
        Sdt = sdt;
    }

    public String getTens() {
        return Tens;
    }

    public void setTens(String tens) {
        Tens = tens;
    }

    public String getThoigian() {
        return Thoigian;
    }

    public void setThoigian(String thoigian) {
        Thoigian = thoigian;
    }

    public String getHinhs() {
        return Hinhs;
    }

    public void setHinhs(String hinhs) {
        Hinhs = hinhs;
    }

    public String getDanhmuc() {
        return Danhmuc;
    }

    public void setDanhmuc(String danhmuc) {
        Danhmuc = danhmuc;
    }

    public String getKhuvuc() {
        return Khuvuc;
    }

    public void setKhuvuc(String khuvuc) {
        Khuvuc = khuvuc;
    }

    public String getSokhach() {
        return Sokhach;
    }

    public void setSokhach(String sokhach) {
        Sokhach = sokhach;
    }

    public String getDonvi() {
        return Donvi;
    }

    public void setDonvi(String donvi) {
        Donvi = donvi;
    }

    public String getMota() {
        return Mota;
    }

    public void setMota(String mota) {
        Mota = mota;
    }

    public String getDiadiem() {
        return Diadiem;
    }

    public void setDiadiem(String diadiem) {
        Diadiem = diadiem;
    }

}
