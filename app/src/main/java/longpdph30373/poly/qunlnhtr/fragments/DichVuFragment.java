package longpdph30373.poly.qunlnhtr.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import longpdph30373.poly.qunlnhtr.R;

public class DichVuFragment extends Fragment {

    Button themDichVu, suaDichVu;
    EditText giaDien, giaNuoc, tienVeSinh, giaPhong;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dich_vu, container, false);
        themDichVu = view.findViewById(R.id.them_dich_vu);
        suaDichVu = view.findViewById(R.id.sua_dich_vu);
        giaDien = view.findViewById(R.id.giaDienEditText);
        giaNuoc = view.findViewById(R.id.giaNuocEditText);
        tienVeSinh = view.findViewById(R.id.giaVeSinhEditText);
        giaPhong = view.findViewById(R.id.tienPhongEditText);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        themDichVu.setOnClickListener(view1 -> {
            float giaDienStr = Float.parseFloat(giaDien.getText().toString());
            float giaNuocStr = Float.parseFloat(giaNuoc.getText().toString());
            float tienVeSinhStr = Float.parseFloat(tienVeSinh.getText().toString());
            float giaPhongStr = Float.parseFloat(giaPhong.getText().toString());
            addService(giaDienStr, giaNuocStr, tienVeSinhStr, giaPhongStr);
        });

        suaDichVu.setOnClickListener(view12 -> {

        });
    }

    // Hàm thêm dịch vụ
    private void addService(float electricityPrice, float waterPrice, float sanitationPrice, float roomPrice) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        Map<String, Object> service = new HashMap<>();
        service.put("giaDien", electricityPrice);
        service.put("giaNuoc", waterPrice);
        service.put("giaVeSinh", sanitationPrice);
        service.put("tienPhong", roomPrice);

        database.collection("services")
                .add(service)
                .addOnSuccessListener(documentReference -> {
                    // Xử lý khi thêm dịch vụ thành công
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi thêm dịch vụ thất bại
                });
    }

    // Hàm sửa dịch vụ
    private void updateService(String serviceId, float electricityPrice, float waterPrice, float sanitationPrice, float roomPrice) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference serviceRef = database.collection("services").document(serviceId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("giaDien", electricityPrice);
        updates.put("giaNuoc", waterPrice);
        updates.put("giaVeSinh", sanitationPrice);
        updates.put("tienPhong", roomPrice);

        serviceRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Xử lý khi cập nhật dịch vụ thành công
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi cập nhật dịch vụ thất bại
                });
    }

}