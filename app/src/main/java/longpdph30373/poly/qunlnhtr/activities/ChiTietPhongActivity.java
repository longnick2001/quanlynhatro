package longpdph30373.poly.qunlnhtr.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import longpdph30373.poly.qunlnhtr.R;
import longpdph30373.poly.qunlnhtr.adapters.HocSinhAdapter;
import longpdph30373.poly.qunlnhtr.models.HocSinh;
import longpdph30373.poly.qunlnhtr.models.Phong;

public class ChiTietPhongActivity extends AppCompatActivity {

    FloatingActionButton themnguoi;
    String roomId;
    String roomName;
    List<String> roomMember;
    RecyclerView recycler_student;
    List<String> listCheck;
    int memberCheck;
    boolean check;
    String gender;
    Phong phong;
    Button xuatHoaDon;
    TextView tenPhong, soLuong, dienTich, moTa, trangThai, thongBao, giaPhong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_phong);
        themnguoi = findViewById(R.id.them_nguoi);
        recycler_student = findViewById(R.id.recycle_student);
        tenPhong = findViewById(R.id.tenPhong);
        soLuong = findViewById(R.id.soLuong);
        moTa = findViewById(R.id.moTa);
        trangThai = findViewById(R.id.trangthai);
        dienTich = findViewById(R.id.dienTich);
        thongBao = findViewById(R.id.thongBao);
        xuatHoaDon = findViewById(R.id.xuat_hoa_don);
        giaPhong = findViewById(R.id.giaPhong);

        check = true;

        Intent intent = getIntent();
        if (intent != null) {
            phong = (Phong) intent.getSerializableExtra("room");
            roomId = phong.getId();
            roomName = phong.getTenPhong();
            tenPhong.setText(roomName);
            soLuong.setText(""+phong.getSoLuong());
            moTa.setText(""+phong.getMoTa());
            dienTich.setText(""+phong.getDientich());
            giaPhong.setText(""+phong.getGiaPhong());
            if(phong.getThanhVien().size() >= phong.getSoLuong()){
                trangThai.setText("Đã đầy");
            }else{
                trangThai.setText("Còn chỗ");
            }
            if(phong.getThanhVien().size() == 0){
                thongBao.setVisibility(View.VISIBLE);
            }

            if(phong.getThanhVien().size() == 0){
                xuatHoaDon.setVisibility(View.GONE);
            }else if(phong.getThanhVien().size() > 0){
                xuatHoaDon.setVisibility(View.VISIBLE);
            }
        }


        xuatHoaDon.setOnClickListener(view -> {
            Intent intent1 = new Intent(ChiTietPhongActivity.this, ThemHoaDonActivity.class);
            intent1.putExtra("phong", phong);
            startActivity(intent1);
        });

        themnguoi.setOnClickListener(view -> {
            Check();
            if (check) {
                themnguoi.setVisibility(View.VISIBLE);
                Dialog();
            } else {
                themnguoi.setVisibility(View.GONE);
                Toast.makeText(ChiTietPhongActivity.this, "Phòng đã đủ người !", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Check();
        getListStudent();
    }

    public void Dialog() {
        Dialog builder = new Dialog(this);
        builder.setTitle("Thêm người");

        builder.setContentView(R.layout.dialog_them_nguoi);
        EditText editTenPhong = builder.findViewById(R.id.editTenPhong);
        editTenPhong.setText(roomName);

        Spinner spinnerKhoa = builder.findViewById(R.id.spinnerKhoa);
        Spinner spinnerNganh = builder.findViewById(R.id.spinnerNganh);
        EditText editTextName = builder.findViewById(R.id.editTextName);
        EditText editTextEmail = builder.findViewById(R.id.editTextEmail);
        EditText editTextPhone = builder.findViewById(R.id.editTextPhone);
        EditText editTenTruong = builder.findViewById(R.id.editTenTruong);
        RadioGroup radioGroup = builder.findViewById(R.id.radioGroup);
        EditText editTextNgaySinh = builder.findViewById(R.id.editTextNgaySinh);
        Button them = builder.findViewById(R.id.add);
        Button cancle = builder.findViewById(R.id.cancle);

        //xử lý spinner
        ArrayAdapter<String> khoaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"K16", "K17"});
        khoaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKhoa.setAdapter(khoaAdapter);

        ArrayAdapter<String> nganhAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Công nghệ ô tô", "Cơ khí", "Xây dựng", "CNTT", "VHM", "Điện CN"});
        nganhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNganh.setAdapter(nganhAdapter);


        //xử lý click vào ngày sinh
        editTextNgaySinh.setOnClickListener(v -> {
            Toast.makeText(ChiTietPhongActivity.this, "click", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(ChiTietPhongActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        // Xử lý khi người dùng chọn ngày tháng năm
                        String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                        editTextNgaySinh.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });

        //xử lý radiogroup
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton nam = builder.findViewById(R.id.radioNam);
            RadioButton nu = builder.findViewById(R.id.radioNu);
            if (nam.isChecked()) {
                // Xử lý khi chọn Nam
                gender = "Nam";
                Toast.makeText(ChiTietPhongActivity.this, "Nam", Toast.LENGTH_SHORT).show();
            } else if (nu.isChecked()) {
                // Xử lý khi chọn Nữ
                gender = "Nữ";
                Toast.makeText(ChiTietPhongActivity.this, "Nữ", Toast.LENGTH_SHORT).show();
            }
        });

        them.setOnClickListener(view -> {
            // Xử lý logic thêm user vào Firestore ở đây
            String name = editTextName.getText().toString();
            String email = editTextEmail.getText().toString();
            String phone = editTextPhone.getText().toString();
            String schoolName = editTenTruong.getText().toString();
            String dob = editTextNgaySinh.getText().toString();
            String khoa = spinnerKhoa.getSelectedItem().toString();
            String nganh = spinnerNganh.getSelectedItem().toString();

            if(TextUtils.isEmpty(name)){
                Toast.makeText(ChiTietPhongActivity.this, "Bạn chưa nhập họ và tên!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(email)){
                Toast.makeText(ChiTietPhongActivity.this, "Bạn chưa nhập email!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(phone)){
                Toast.makeText(ChiTietPhongActivity.this, "Bạn chưa nhập số điện thoại!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(schoolName)){
                Toast.makeText(ChiTietPhongActivity.this, "Bạn chưa nhập lớp!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(dob)){
                Toast.makeText(ChiTietPhongActivity.this, "Bạn chưa nhập ngày sinh!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(gender)){
                Toast.makeText(ChiTietPhongActivity.this, "Bạn chưa chọn giới tính!", Toast.LENGTH_SHORT).show();
                return;
            }

            AddUser(name, email, phone, roomId, schoolName, gender, dob, khoa, nganh);
            builder.cancel();
        });

        cancle.setOnClickListener(view -> {
            builder.cancel();
        });


        builder.show();
    }


    private void AddUser(String name, String email, String phone, String roomId, String schoolName, String gender, String dob, String khoa, String nganh) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("managerId", "");

        HashMap<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("phone", phone);
        user.put("roomid", roomId);
        user.put("schoolname", schoolName);
        user.put("dob", dob);
        user.put("gender", gender);
        user.put("managerid", id);
        user.put("khoa", khoa);
        user.put("nganh", nganh);


        // Write a message to the database
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    addMemberToFirestore(documentReference.getId());
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
                });
    }

    public void addMemberToFirestore(String studentId) {
        if (roomId == null) {
            return;
        }
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference docRef = database.collection("rooms").document(roomId);
        roomMember = new ArrayList<>();

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                roomMember = (List<String>) task.getResult().get("thanhvien");
                roomMember.add(studentId);

                HashMap<String, Object> user = new HashMap<>();
                user.put("thanhvien", roomMember);

                docRef.set(user, SetOptions.merge());
                getListStudent();
            }
        });
    }

    public void getListStudent() {
        if (roomId == null) {
            return;
        }
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference docRef = database.collection("rooms").document(roomId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                List<String> list = (List<String>) task.getResult().get("thanhvien");

                Toast.makeText(ChiTietPhongActivity.this, "completed" + list.size(), Toast.LENGTH_SHORT).show();
                getUser(list);
            }
        });

    }

    public void getUser(List<String> listId) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("users")
                .get()
                .addOnCompleteListener(task -> {


                    if (task.isSuccessful() && task.getResult() != null) {
                        List<HocSinh> listUser = new ArrayList<>();
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        String idManager = sharedPreferences.getString("managerId", "");

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            for(String id: listId){
                                if (id.equals(queryDocumentSnapshot.getId()) && idManager.equals(queryDocumentSnapshot.getString("managerid"))) {
                                    HocSinh user = new HocSinh();
                                    user.name = queryDocumentSnapshot.getString("name");
                                    user.email = queryDocumentSnapshot.getString("email");
                                    user.phone = queryDocumentSnapshot.getString("phone");
                                    user.roomId = queryDocumentSnapshot.getString("roomid");
                                    user.schoolName = queryDocumentSnapshot.getString("schoolname");
                                    user.gender = queryDocumentSnapshot.getString("gender");
                                    user.dob = queryDocumentSnapshot.getString("dob");
                                    user.id = queryDocumentSnapshot.getId();
                                    user.managerId = queryDocumentSnapshot.getString("managerid");
                                    user.khoa = queryDocumentSnapshot.getString("khoa");
                                    user.nganh = queryDocumentSnapshot.getString("nganh");
                                    listUser.add(user);
                                }
                            }
                        }
                        recycler_student.setAdapter(new HocSinhAdapter(ChiTietPhongActivity.this, listUser, roomId, roomName));
                        if(listUser.size() == 0){
                            thongBao.setVisibility(View.VISIBLE);
                        }else{
                            thongBao.setVisibility(View.GONE);
                        }
                    }


//                    for (String id : listId) {
//                        for (HocSinh hs : listUser) {
//
//                        }
//                    }
                });
    }

    public void Check() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference docRef = database.collection("rooms").document(roomId);
        listCheck = new ArrayList<>();

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                listCheck = (List<String>) task.getResult().get("thanhvien");
                memberCheck = Math.toIntExact(task.getResult().getLong("soluong"));
                if (listCheck.size() >= memberCheck) {
                    check = false;
                    themnguoi.setVisibility(View.GONE);
                } else {
                    check = true;
                    themnguoi.setVisibility(View.VISIBLE);
                }
                if(listCheck.size()<=0){
                    thongBao.setVisibility(View.VISIBLE);
                }else {
                    thongBao.setVisibility(View.GONE);
                }
            }
        });
    }


}