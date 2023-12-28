package longpdph30373.poly.qunlnhtr.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import longpdph30373.poly.qunlnhtr.R;
import longpdph30373.poly.qunlnhtr.fragments.PhongFragment;
import longpdph30373.poly.qunlnhtr.models.Phong;

public class ThemPhongActivity extends AppCompatActivity {
    EditText tenPhong;
    EditText dienTich;
    EditText soLuong;
    EditText moTa;
    EditText giaPhong;
    Button suaPhong, themPhong;
    Phong phong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_phong);

        giaPhong = findViewById(R.id.giaPhong);
        themPhong = findViewById(R.id.themPhong);
        suaPhong = findViewById(R.id.suaPhong);
        tenPhong = findViewById(R.id.tenPhong);
        dienTich = findViewById(R.id.dienTich);
        moTa = findViewById(R.id.moTa);
        soLuong = findViewById(R.id.soLuong);
        Intent intent = getIntent();
        if(intent != null){
            suaPhong.setVisibility(View.VISIBLE);
            themPhong.setVisibility(View.GONE);

            phong = (Phong) intent.getSerializableExtra("room");
            tenPhong.setText(phong.getTenPhong());
            if(phong.getDientich() > 0){
                dienTich.setText(""+phong.getDientich());
            }
            moTa.setText(phong.getMoTa());
            if(phong.getSoLuong()>0){
                soLuong.setText(""+phong.getSoLuong());
            }
            if (phong.getGiaPhong() > 0){
                giaPhong.setText(""+phong.getGiaPhong());
            }

        }
        if(phong.getId().equals("")){
            suaPhong.setVisibility(View.GONE);
            themPhong.setVisibility(View.VISIBLE);
        }

        suaPhong.setOnClickListener(view -> {
            onUpdateButtonClick();
        });
        themPhong.setOnClickListener(view -> {
            onSaveButtonClick();
        });

    }

    public void onSaveButtonClick() {
        if(tenPhong.getText().toString().equals("")){
            Toast.makeText(ThemPhongActivity.this, "Bạn chưa nhập tên phòng!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(dienTich.getText().toString().equals("")){
            Toast.makeText(ThemPhongActivity.this, "Bạn chưa nhập diện tích phòng!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(giaPhong.getText().toString().equals("")){
            Toast.makeText(ThemPhongActivity.this, "Bạn chưa nhập giá phòng!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(soLuong.getText().toString().equals("")){
            Toast.makeText(ThemPhongActivity.this, "Bạn chưa nhập số lượng tối đa!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(moTa.getText().toString().equals("")){
            Toast.makeText(ThemPhongActivity.this, "Bạn chưa nhập mô tả!", Toast.LENGTH_SHORT).show();
            return;
        }

        String tenPhongStr = tenPhong.getText().toString();
        float dienTichStr = Float.parseFloat(dienTich.getText().toString());
        String moTaStr = moTa.getText().toString();
        int soLuongStr = Integer.parseInt(soLuong.getText().toString());
        List<String> roomMember = new ArrayList<>();
        int giaPhongStr = Integer.parseInt(giaPhong.getText().toString());


        // Add your logic to save the room data to database
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("managerId", "");

        HashMap<String, Object> user = new HashMap<>();
        user.put("tenphong", tenPhongStr);
        user.put("dientich", dienTichStr);
        user.put("mota", moTaStr);
        user.put("soluong", soLuongStr);
        user.put("thanhvien", roomMember);
        user.put("giaphong", giaPhongStr);
        user.put("managerid", id);

        // Write a message to the database
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("rooms")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
                });
    }

    public void onUpdateButtonClick() {
        if(tenPhong.getText().toString().equals("")){
            Toast.makeText(ThemPhongActivity.this, "Bạn chưa nhập tên phòng!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(dienTich.getText().toString().equals("")){
            Toast.makeText(ThemPhongActivity.this, "Bạn chưa nhập diện tích phòng!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(giaPhong.getText().toString().equals("")){
            Toast.makeText(ThemPhongActivity.this, "Bạn chưa nhập giá phòng!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(soLuong.getText().toString().equals("")){
            Toast.makeText(ThemPhongActivity.this, "Bạn chưa nhập số lượng tối đa!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(moTa.getText().toString().equals("")){
            Toast.makeText(ThemPhongActivity.this, "Bạn chưa nhập mô tả!", Toast.LENGTH_SHORT).show();
            return;
        }

        String tenPhongStr = tenPhong.getText().toString();
        float dienTichStr = Float.parseFloat(dienTich.getText().toString());
        String moTaStr = moTa.getText().toString();
        int soLuongStr = Integer.parseInt(soLuong.getText().toString());
        List<String> roomMember = new ArrayList<>();
        int giaPhongStr = Integer.parseInt(giaPhong.getText().toString());
        // Add your logic to update the room data in the database
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("managerId", "");


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference roomRef = database.collection("rooms").document(phong.getId());

        Map<String, Object> updates = new HashMap<>();
        updates.put("tenphong", tenPhongStr);
        updates.put("dientich", dienTichStr);
        updates.put("mota", moTaStr);
        updates.put("soluong", soLuongStr);
        updates.put("thanhvien", phong.getThanhVien());
        updates.put("giaPhong", giaPhongStr);
        updates.put("managerid", id);

        roomRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
                });
    }

}