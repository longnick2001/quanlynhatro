package longpdph30373.poly.qunlnhtr.models;

import java.io.Serializable;

public class DichVu implements Serializable {
    public String id;
    public float giaDien;
    public float giaNuoc;
    public float giaVeSinh;
    public float tienPhong;

    public DichVu() {
    }

    public DichVu(String id, float giaDien, float giaNuoc, float giaVeSinh, float tienPhong) {
        this.id = id;
        this.giaDien = giaDien;
        this.giaNuoc = giaNuoc;
        this.giaVeSinh = giaVeSinh;
        this.tienPhong = tienPhong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getGiaDien() {
        return giaDien;
    }

    public void setGiaDien(float giaDien) {
        this.giaDien = giaDien;
    }

    public float getGiaNuoc() {
        return giaNuoc;
    }

    public void setGiaNuoc(float giaNuoc) {
        this.giaNuoc = giaNuoc;
    }

    public float getGiaVeSinh() {
        return giaVeSinh;
    }

    public void setGiaVeSinh(float giaVeSinh) {
        this.giaVeSinh = giaVeSinh;
    }

    public float getTienPhong() {
        return tienPhong;
    }

    public void setTienPhong(float tienPhong) {
        this.tienPhong = tienPhong;
    }
}
