package longpdph30373.poly.qunlnhtr.models;

import java.io.Serializable;
import java.util.List;

public class HoaDon implements Serializable {
    public String id;
    public String roomId;
    public List<String> thanhVien;
    public float giaDien;
    public float giaNuoc;
    public float soDien;
    public float soNuoc;
    public float tienVeSinh;
    public float tienPhong;
    public float tongTien;
    public String ngayThang;
    public boolean trangThaiThanhToan;
    public String ghiChu;
    public String managerId;

    public HoaDon(String id, String roomId, List<String> thanhVien, float giaDien, float giaNuoc, float soDien, float soNuoc, float tienVeSinh, float tienPhong, float tongTien, String ngayThang, boolean trangThaiThanhToan, String ghiChu, String managerId) {
        this.id = id;
        this.roomId = roomId;
        this.thanhVien = thanhVien;
        this.giaDien = giaDien;
        this.giaNuoc = giaNuoc;
        this.soDien = soDien;
        this.soNuoc = soNuoc;
        this.tienVeSinh = tienVeSinh;
        this.tienPhong = tienPhong;
        this.tongTien = tongTien;
        this.ngayThang = ngayThang;
        this.trangThaiThanhToan = trangThaiThanhToan;
        this.ghiChu = ghiChu;
        this.managerId = managerId;
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

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public HoaDon() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public List<String> getThanhVien() {
        return thanhVien;
    }

    public void setThanhVien(List<String> thanhVien) {
        this.thanhVien = thanhVien;
    }

    public float getSoDien() {
        return soDien;
    }

    public void setSoDien(float tienDien) {
        this.soDien = soDien;
    }

    public float getSoNuoc() {
        return soNuoc;
    }

    public void setSoNuoc(float tienNuoc) {
        this.soNuoc = soNuoc;
    }

    public float getTienVeSinh() {
        return tienVeSinh;
    }

    public void setTienVeSinh(float tienVeSinh) {
        this.tienVeSinh = tienVeSinh;
    }

    public float getTienPhong() {
        return tienPhong;
    }

    public void setTienPhong(float tienPhong) {
        this.tienPhong = tienPhong;
    }

    public float getTongTien() {
        return tongTien;
    }

    public void setTongTien(float tongTien) {
        this.tongTien = tongTien;
    }

    public String getNgayThang() {
        return ngayThang;
    }

    public void setNgayThang(String ngayThang) {
        this.ngayThang = ngayThang;
    }

    public boolean isTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(boolean trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }
}
