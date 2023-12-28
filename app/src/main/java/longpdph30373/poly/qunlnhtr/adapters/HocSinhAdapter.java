package longpdph30373.poly.qunlnhtr.adapters;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import longpdph30373.poly.qunlnhtr.R;
import longpdph30373.poly.qunlnhtr.activities.ChiTietPhongActivity;
import longpdph30373.poly.qunlnhtr.models.HocSinh;
import longpdph30373.poly.qunlnhtr.models.Phong;

public class HocSinhAdapter extends RecyclerView.Adapter<HocSinhAdapter.ViewHolder> {

    Context context;
    List<HocSinh> list;
    String roomId;
    String roomName;

    public HocSinhAdapter(Context context, List<HocSinh> list, String roomId, String roomName) {
        this.context = context;
        this.list = list;
        this.roomId = roomId;
        this.roomName = roomName;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HocSinhAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hoc_sinh, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HocSinhAdapter.ViewHolder holder, int position) {
        HocSinh hocSinh = list.get(position);
        holder.name.setText(list.get(position).getName());
        holder.truongHoc.setText("Lớp: "+list.get(position).getSchoolName());
        holder.email.setText("Email: "+list.get(position).getEmail());
        holder.phone.setText("SDT: "+list.get(position).getPhone());
        holder.khoa.setText("Khóa: "+list.get(position).getKhoa());
        holder.nganh.setText("Ngành: "+list.get(position).getNganh());

        if (roomId.equals("")) {
            holder.xoa_khoi_phong.setVisibility(View.GONE);
            holder.xoa_khoi_users.setVisibility(View.VISIBLE);
            holder.sua.setVisibility(View.VISIBLE);
        } else {
            holder.xoa_khoi_users.setVisibility(View.GONE);
            holder.sua.setVisibility(View.GONE);
        }

        holder.getTenPhong(hocSinh.getRoomId(), holder.phong);

        holder.sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(1,
                        holder.getAdapterPosition(),
                        list.get(holder.getAdapterPosition()).getId(),
                        list.get(holder.getAdapterPosition()).getName(),
                        list.get(holder.getAdapterPosition()).getEmail(),
                        list.get(holder.getAdapterPosition()).getPhone(),
                        list.get(holder.getAdapterPosition()).getSchoolName(),
                        list.get(holder.getAdapterPosition()).getDob(),
                        list.get(holder.getAdapterPosition()).getGender(),
                        list.get(holder.getAdapterPosition()).getRoomId(),
                        hocSinh,
                        list.get(holder.getAdapterPosition()).getKhoa(),
                        list.get(holder.getAdapterPosition()).getNganh());
            }
        });


        if (!roomId.equals("")) {
            holder.xoa_khoi_phong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Bạn có chắc chắn muốn xóa không?");
                    builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Xử lý xóa ở đây
                            xoaKhoiPhong(holder.getAdapterPosition());
//                    xoaKhoiUsers(holder.getAdapterPosition());
                            list.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                        }
                    });
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Hủy xóa
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }

        holder.xoa_khoi_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có chắc chắn muốn xóa không?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Xử lý xóa ở đây
                        xoaKhoiPhong(holder.getAdapterPosition());
                        xoaKhoiUsers(holder.getAdapterPosition());
                        list.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Hủy xóa
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog(0,
                        holder.getAdapterPosition(),
                        list.get(holder.getAdapterPosition()).getId(),
                        list.get(holder.getAdapterPosition()).getName(),
                        list.get(holder.getAdapterPosition()).getEmail(),
                        list.get(holder.getAdapterPosition()).getPhone(),
                        list.get(holder.getAdapterPosition()).getSchoolName(),
                        list.get(holder.getAdapterPosition()).getDob(),
                        list.get(holder.getAdapterPosition()).getGender(),
                        list.get(holder.getAdapterPosition()).getRoomId(),
                        hocSinh,
                        list.get(holder.getAdapterPosition()).getKhoa(),
                        list.get(holder.getAdapterPosition()).getNganh());
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, phone, phong, truongHoc, khoa, nganh;
        Button xoa_khoi_phong, xoa_khoi_users, sua;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            khoa = itemView.findViewById(R.id.khoa);
            nganh = itemView.findViewById(R.id.nganh);
            phong = itemView.findViewById(R.id.phong);
            name = itemView.findViewById(R.id.name);
            truongHoc = itemView.findViewById(R.id.truongHoc);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            xoa_khoi_phong = itemView.findViewById(R.id.xoa_khoi_phong);
            xoa_khoi_users = itemView.findViewById(R.id.xoa_khoi_users);
            cardView = itemView.findViewById(R.id.cardView_student);
            sua = itemView.findViewById(R.id.sua_user);
        }

        void getTenPhong(String idRoom, TextView phong) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection("rooms")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                if (queryDocumentSnapshot.getId().equals(idRoom)) {
                                    phong.setText("Phòng: " + queryDocumentSnapshot.getString("tenphong"));
                                }
                            }

                        }
                    });
        }
    }

    public void xoaKhoiPhong(int pos) {
        String roomid = list.get(pos).getRoomId();
        String userIdToRemove = list.get(pos).getId();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("rooms").document(roomid);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> roommember = (List<String>) document.get("thanhvien");

                        // Xóa id khỏi roommember
                        if (roommember.contains(userIdToRemove)) {
                            // Id được tìm thấy trong danh sách
                            roommember.remove(userIdToRemove);
                        }

                        // Cập nhật lại trường "roommember" trong Firestore
                        docRef.update("thanhvien", roommember)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Document successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void xoaKhoiUsers(int id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String documentId = list.get(id).getId();

        // Xóa user từ collection "users"
        db.collection("users").document(documentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document successfully deleted!");

                        // Sau khi xóa user thành công, gọi hàm xoaKhoiPhong() để cập nhật phòng

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void showDialog(int type, int position, String userId, String name, String email, String phone, String schoolName, String dob,String gender, String idRoom, HocSinh hocSinh, String khoa, String nganh) {
        Dialog builder = new Dialog(context);
        builder.setTitle("Chi tiết");

        builder.setContentView(R.layout.dialog_them_nguoi);

        Spinner spinnerKhoa = builder.findViewById(R.id.spinnerKhoa);
        Spinner spinnerNganh = builder.findViewById(R.id.spinnerNganh);
        EditText editTextName = builder.findViewById(R.id.editTextName);
        EditText editTextEmail = builder.findViewById(R.id.editTextEmail);
        EditText editTextPhone = builder.findViewById(R.id.editTextPhone);
        EditText editTenTruong = builder.findViewById(R.id.editTenTruong);
        EditText editTenPhong = builder.findViewById(R.id.editTenPhong);
        EditText editTextNgaySinh = builder.findViewById(R.id.editTextNgaySinh);
        RadioButton nam = builder.findViewById(R.id.radioNam);
        RadioButton nu = builder.findViewById(R.id.radioNu);
        RadioGroup radioGroup = builder.findViewById(R.id.radioGroup);
        Button add = builder.findViewById(R.id.add);
        Button cancle = builder.findViewById(R.id.cancle);

        //xử lý spinner
        ArrayAdapter<String> khoaAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new String[]{"K16", "K17"});
        khoaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKhoa.setAdapter(khoaAdapter);
        ArrayAdapter<String> adapter1 = (ArrayAdapter<String>) spinnerKhoa.getAdapter();
        int vitri1 = adapter1.getPosition(khoa);
        spinnerKhoa.setSelection(vitri1);

        ArrayAdapter<String> nganhAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new String[]{"Công nghệ ô tô", "Cơ khí", "Xây dựng", "CNTT", "VHM", "Điện CN"});
        nganhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNganh.setAdapter(nganhAdapter);
        ArrayAdapter<String> adapter2 = (ArrayAdapter<String>) spinnerNganh.getAdapter();
        int vitri2 = adapter2.getPosition(nganh);
        spinnerNganh.setSelection(vitri2);

        cancle.setVisibility(View.VISIBLE);
        editTextName.setText(name);
        editTextEmail.setText(email);
        editTextPhone.setText(phone);
        editTenTruong.setText(schoolName);
        editTextNgaySinh.setText(dob);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("rooms")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if(queryDocumentSnapshot.getId().equals(idRoom)){
                                editTenPhong.setText(queryDocumentSnapshot.getString("tenphong"));
                            }
                        }

                    }
                });

        //xử lý click vào ngày sinh
        editTextNgaySinh.setOnClickListener(v -> {
            Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (view, year1, month1, dayOfMonth) -> {
                        // Xử lý khi người dùng chọn ngày tháng năm
                        String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                        editTextNgaySinh.setText(selectedDate);
                    }, year, month, day);
            datePickerDialog.show();
        });


        //xử lý radiogroup
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (nam.isChecked()) {
                // Xử lý khi chọn Nam
                Toast.makeText(context, "Nam", Toast.LENGTH_SHORT).show();
            } else if (nu.isChecked()) {
                // Xử lý khi chọn Nữ
                Toast.makeText(context, "Nữ", Toast.LENGTH_SHORT).show();
            }
        });

        if(gender.equals("Nam")){
            nam.setChecked(true);
        }else{
            nu.setChecked(true);
        }


        if (type == 0) {
            //xem chi tiet sinh vien
            add.setVisibility(View.GONE);
            editTenPhong.setEnabled(false);
            editTextName.setEnabled(false);
            editTextEmail.setEnabled(false);
            editTextPhone.setEnabled(false);
            editTenTruong.setEnabled(false);
            editTextNgaySinh.setEnabled(false);
        }

        //xử lý cho chức năng chỉnh sửa
        if (type != 0) {
            add.setText("Sửa");
            add.setVisibility(View.VISIBLE);
            editTenPhong.setEnabled(false);
            add.setOnClickListener(view -> {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Bạn có chắc chắn muốn sửa không?");
                builder1.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(editTextName.getText().toString().equals("")){
                            Toast.makeText(context, "Bạn chưa nhập họ và tên!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(editTextEmail.getText().toString().equals("")){
                            Toast.makeText(context, "Bạn chưa nhập email!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(editTextPhone.getText().toString().equals("")){
                            Toast.makeText(context, "Bạn chưa nhập số điện thoại!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(editTenTruong.getText().toString().equals("")){
                            Toast.makeText(context, "Bạn chưa nhập trường học!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(editTextNgaySinh.getText().toString().equals("")){
                            Toast.makeText(context, "Bạn chưa nhập ngày sinh!", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        String khoaStr = spinnerKhoa.getSelectedItem().toString();
                        String nganhStr = spinnerNganh.getSelectedItem().toString();
                        String name = editTextName.getText().toString();
                        String email = editTextEmail.getText().toString();
                        String phone = editTextPhone.getText().toString();
                        String schoolName = editTenTruong.getText().toString();
                        String dob = editTextNgaySinh.getText().toString();
                        String gioiTinh;
                        String roomid = list.get(position).getRoomId();
                        hocSinh.setName(name);
                        hocSinh.setEmail(email);
                        hocSinh.setPhone(phone);
                        hocSinh.setDob(dob);
                        hocSinh.setSchoolName(schoolName);
                        if(nam.isChecked()){
                            gioiTinh = "Nam";
                            hocSinh.setGender("Nam");
                        }else{
                            gioiTinh = "Nữ";
                            hocSinh.setGender("Nữ");
                        }
                        hocSinh.setKhoa(khoaStr);
                        hocSinh.setNganh(nganhStr);

                        updateUser(userId ,name, email, phone, roomid, schoolName, gioiTinh, dob, khoaStr, nganhStr);
                        list.set(position, hocSinh);
                        notifyItemChanged(position);
                        Toast.makeText(context, "Đã sửa", Toast.LENGTH_SHORT).show();
                        builder.cancel();
                    }
                });
                builder1.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Hủy xóa
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder1.create();
                dialog.show();
            });
        }


        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.cancel();
            }
        });

        builder.show();
    }

    public void updateUser(String userId, String name, String email, String phone, String roomId, String schoolName, String gender, String dob, String khoa, String nganh) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference userRef = database.collection("users").document(userId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("email", email);
        updates.put("phone", phone);
        updates.put("roomid", roomId);
        updates.put("schoolname", schoolName);
        updates.put("dob", dob);
        updates.put("gender", gender);
        updates.put("khoa", khoa);
        updates.put("nganh", nganh);

        userRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Cập nhật thành công
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi cập nhật thất bại
                });
    }


}
