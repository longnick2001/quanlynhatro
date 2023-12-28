package longpdph30373.poly.qunlnhtr.models;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.List;

public class Phong implements Serializable {
    public String id;
    public String tenPhong;
    public float dientich;
    public String moTa;
    public int soLuong;
    public List<String> thanhVien;
    public int giaPhong;

    public String managerId;

    public Phong(String id, String tenPhong, float dientich, String moTa, int soLuong, List<String> thanhVien, int giaPhong, String managerId) {
        this.id = id;
        this.tenPhong = tenPhong;
        this.dientich = dientich;
        this.moTa = moTa;
        this.soLuong = soLuong;
        this.thanhVien = thanhVien;
        this.giaPhong = giaPhong;
        this.managerId = managerId;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public int getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(int giaPhong) {
        this.giaPhong = giaPhong;
    }

    public Phong() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public float getDientich() {
        return dientich;
    }

    public void setDientich(float dientich) {
        this.dientich = dientich;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public List<String> getThanhVien() {
        return thanhVien;
    }

    public void setThanhVien(List<String> thanhVien) {
        this.thanhVien = thanhVien;
    }
}
