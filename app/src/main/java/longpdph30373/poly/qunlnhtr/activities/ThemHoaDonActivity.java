package longpdph30373.poly.qunlnhtr.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import longpdph30373.poly.qunlnhtr.R;
import longpdph30373.poly.qunlnhtr.models.Phong;

public class ThemHoaDonActivity extends AppCompatActivity {

    Button xuatHoaDon;
    EditText soDien, soNuoc, tienVeSinh, tienPhong, tongTien, ghiChu, giaDien, giaNuoc;
    Phong phong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_hoa_don);
        giaDien = findViewById(R.id.giaDien);
        giaNuoc = findViewById(R.id.giaNuoc);

        soDien = findViewById(R.id.soDienEditText);
        soNuoc = findViewById(R.id.soNuocEditText);
        tienPhong = findViewById(R.id.tienPhongEditText);
        tienVeSinh = findViewById(R.id.tienVeSinhEditText);
//        tongTien = findViewById(R.id.tongTienEditText);
        ghiChu = findViewById(R.id.ghiChuEditText);

        xuatHoaDon = findViewById(R.id.xuat_hoa_don);

        Intent intent = getIntent();
        if (intent != null) {
            phong = (Phong) intent.getSerializableExtra("phong");
            tienPhong.setEnabled(false);
            tienPhong.setText("" + phong.getGiaPhong());
        }

        xuatHoaDon.setOnClickListener(view -> {
            if(soDien.getText().toString().equals("")){
                Toast.makeText(ThemHoaDonActivity.this, "Bạn chưa nhập số điện!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(soNuoc.getText().toString().equals("")){
                Toast.makeText(ThemHoaDonActivity.this, "Bạn chưa nhập số nước!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(tienVeSinh.getText().toString().equals("")){
                Toast.makeText(ThemHoaDonActivity.this, "Bạn chưa nhập tiền vệ sinh!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(ghiChu.getText().toString().equals("")){
                Toast.makeText(ThemHoaDonActivity.this, "Bạn chưa nhập ghi chú!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(giaNuoc.getText().toString().equals("")){
                Toast.makeText(ThemHoaDonActivity.this, "Bạn chưa nhập giá nước!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(giaDien.getText().toString().equals("")){
                Toast.makeText(ThemHoaDonActivity.this, "Bạn chưa nhập giá điện!", Toast.LENGTH_SHORT).show();
                return;
            }


            float tienDien = Float.parseFloat(soDien.getText().toString()) * Float.parseFloat(giaDien.getText().toString()) ;
            float tienNuoc = Float.parseFloat(soNuoc.getText().toString()) * Float.parseFloat(giaNuoc.getText().toString()) ;
            float tienVeSinhStr = Float.parseFloat(tienVeSinh.getText().toString());
            float tienPhongStr = Float.parseFloat(tienPhong.getText().toString());
            float giaNuocStr = Float.parseFloat(giaNuoc.getText().toString());
            float giaDienStr = Float.parseFloat(giaDien.getText().toString());
            float tongTienStr = (Float.parseFloat(soDien.getText().toString()) * Float.parseFloat(giaDien.getText().toString())) + (Float.parseFloat(soNuoc.getText().toString()) * Float.parseFloat(giaNuoc.getText().toString())) + (Float.parseFloat(tienVeSinh.getText().toString())) + Float.parseFloat(tienPhong.getText().toString());
            String ghiChuStr = ghiChu.getText().toString();
            String ngayThangStr = getCurrentDateTime();
            String message = "Tiền điện: " + tienDien + "\n"
                    + "Tiền nước: " + tienNuoc + "\n"
                    + "Tiền vệ sinh: " + tienVeSinhStr + "\n"
                    +"Tiền phòng: "+tienPhongStr+"\n"
                    + "Tổng tiền: " + tongTienStr+"\n"
                    +"Ghi chú: "+ghiChuStr+"\n"
                    +"Ngày, giờ: "+ngayThangStr;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message)
                    .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Xử lý khi người dùng nhấn nút OK
                            float tienDien = Float.parseFloat(soDien.getText().toString()) * Float.parseFloat(giaDien.getText().toString());
                            float tienNuoc = Float.parseFloat(soNuoc.getText().toString()) * Float.parseFloat(giaNuoc.getText().toString());
                            float tienVeSinhStr = Float.parseFloat(tienVeSinh.getText().toString());
                            float tienPhongStr = Float.parseFloat(tienPhong.getText().toString());
                            float giaNuocStr = Float.parseFloat(giaNuoc.getText().toString());
                            float giaDienStr = Float.parseFloat(giaDien.getText().toString());
                            float tongTienStr = (Float.parseFloat(soDien.getText().toString()) * Float.parseFloat(giaDien.getText().toString())) + (Float.parseFloat(soNuoc.getText().toString()) * Float.parseFloat(giaNuoc.getText().toString())) + (Float.parseFloat(tienVeSinh.getText().toString()))+(Float.parseFloat(tienPhong.getText().toString()));
                            String roomId = phong.getId();
                            String ghiChuStr = ghiChu.getText().toString();
                            List<String> thanhVien = phong.getThanhVien();
                            boolean trangThaiThanhToan = false;
                            String ngayThang = getCurrentDateTime();
                            addBill(roomId, thanhVien, tienDien, tienNuoc, tienVeSinhStr, tienPhongStr, tongTienStr, ngayThang, trangThaiThanhToan, ghiChuStr, giaDienStr, giaNuocStr);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    public String getCurrentDateTime() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return formatter.format(date);
    }

    // Hàm thêm hóa đơn
    private void addBill(String roomId, List<String> thanhVien, float soDien, float soNuoc, float tienVeSinh, float tienPhong, float tongTien, String ngayThang, boolean trangThaiThanhToan, String ghiChu, float giaDien, float giaNuoc) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("managerId", "");


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        Map<String, Object> bill = new HashMap<>();
        bill.put("roomId", roomId);
        bill.put("thanhVien", thanhVien);
        bill.put("soDien", soDien);
        bill.put("soNuoc", soNuoc);
        bill.put("tienVeSinh", tienVeSinh);
        bill.put("tienPhong", tienPhong);
        bill.put("tongTien", tongTien);
        bill.put("ngayThang", ngayThang);
        bill.put("trangThaiThanhToan", trangThaiThanhToan);
        bill.put("ghiChu", ghiChu);
        bill.put("managerid", id);
        bill.put("giaDien", giaDien);
        bill.put("giaNuoc", giaNuoc);

        database.collection("bills")
                .add(bill)
                .addOnSuccessListener(documentReference -> {
                    // Xử lý khi thêm hóa đơn thành công
                    onBackPressed();
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi thêm hóa đơn thất bại
                });
    }

    // Hàm sửa hóa đơn


}