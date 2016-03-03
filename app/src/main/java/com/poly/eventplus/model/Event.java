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
    private int Sdt;

    public int getSdt() {
        return Sdt;
    }

    public String getSokhach() {
        return Sokhach;
    }

    public String getDonvi() {
        return Donvi;
    }

    public String getMota() {
        return Mota;
    }

    public String getDiadiem() {
        return Diadiem;
    }

    public String getKhuvuc() {
        return Khuvuc;
    }

    public String getTens() {
        return Tens;
    }

    public String getThoigian() {
        return Thoigian;
    }

    public String getHinhs() {
        return Hinhs;
    }

    public String getDanhmuc() {
        return Danhmuc;
    }

    public Event(String tens, String thoigian, String hinhs, String danhmuc, String khuvuc, String sokhach, String donvi, String mota, String diadiem, int sdt) {
        Tens = tens;
        Thoigian = thoigian;
        Hinhs = hinhs;
        Danhmuc = danhmuc;
        Khuvuc = khuvuc;
        Sokhach = sokhach;
        Donvi = donvi;
        Mota = mota;
        Sdt = sdt;
        Diadiem = diadiem;
    }
}
