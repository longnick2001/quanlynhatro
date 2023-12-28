package longpdph30373.poly.qunlnhtr.models;

import java.io.Serializable;

public class HocSinh implements Serializable {

    public String id;
    public String name;
    public String email;
    public String phone;
    public String roomId;
    public String schoolName;
    public String gender;
    public String dob;
    public String managerId;
    public String khoa;
    public String nganh;

    public String getKhoa() {
        return khoa;
    }

    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }

    public String getNganh() {
        return nganh;
    }

    public void setNganh(String nganh) {
        this.nganh = nganh;
    }

    public HocSinh(String id, String name, String email, String phone, String roomId, String schoolName, String gender, String dob, String managerId, String khoa, String nganh) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.roomId = roomId;
        this.schoolName = schoolName;
        this.gender = gender;
        this.dob = dob;
        this.managerId = managerId;
        this.khoa = khoa;
        this.nganh = nganh;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public HocSinh() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
