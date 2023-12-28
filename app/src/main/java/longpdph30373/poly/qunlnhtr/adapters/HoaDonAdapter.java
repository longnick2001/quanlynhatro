package longpdph30373.poly.qunlnhtr.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import longpdph30373.poly.qunlnhtr.R;
import longpdph30373.poly.qunlnhtr.activities.ChiTietPhongActivity;
import longpdph30373.poly.qunlnhtr.activities.ThemHoaDonActivity;
import longpdph30373.poly.qunlnhtr.activities.ThemPhongActivity;
import longpdph30373.poly.qunlnhtr.models.HoaDon;
import longpdph30373.poly.qunlnhtr.models.Phong;

public class HoaDonAdapter extends RecyclerView.Adapter<HoaDonAdapter.ViewHolder> {
    Context context;
    List<HoaDon> list;

    public HoaDonAdapter(Context context, List<HoaDon> list) {
        this.context = context;
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HoaDonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hoa_don, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoaDonAdapter.ViewHolder holder, int position) {
        HoaDon hoaDon = list.get(position);
        holder.tienPhong.setText("Tiền phòng: " + hoaDon.getTienPhong() + " vnd");
        holder.tienNuoc.setText("Tiền nước: " + (hoaDon.getSoNuoc()*hoaDon.getGiaNuoc()) + " vnd");
        holder.tienDien.setText("Tiền điện: " + (hoaDon.getSoDien()*hoaDon.getGiaDien()) + " vnd");
        holder.tongTien.setText("Tổng: " + hoaDon.getTongTien()+" vnd");
        holder.ghiChu.setText("Ghi chú: " + hoaDon.getGhiChu());
        holder.ngayThang.setText("Ngày: " + hoaDon.getNgayThang());
        holder.tienVeSinh.setText("Tiền vệ sinh: " + hoaDon.getTienVeSinh() + " vnd");
        if (hoaDon.isTrangThaiThanhToan()) {
            holder.daThanhToan.setVisibility(View.VISIBLE);
            holder.xacNhanThanhToan.setVisibility(View.GONE);
        } else {
            holder.chuaThanhToan.setVisibility(View.VISIBLE);
            holder.xacNhanThanhToan.setVisibility(View.VISIBLE);
        }

        holder.getTenPhong(hoaDon.getRoomId(), (TextView) holder.tenPhong);

        holder.xacNhanThanhToan.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Xác nhậ đã thanh toán?");
            builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Xử lý xóa ở đây
                    updateBill(hoaDon.getId(), true, holder.getAdapterPosition(), hoaDon);
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
        });

        holder.cardView_hoa_don.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Chọn tác vụ")
                        .setItems(new CharSequence[]{"Sửa", "Xóa", "Chi tiết"}, (dialog, which) -> {
                            switch (which) {
                                case 0:
                                    // Xử lý khi chọn Sửa
                                    Dialog(0, hoaDon, holder.tenPhong, holder.getAdapterPosition());
                                    break;
                                case 1:
                                    // Xử lý khi chọn Xóa
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                    builder1.setMessage("Bạn có chắc chắn muốn xóa không?");
                                    builder1.setPositiveButton("Xóa", (dialog12, id) -> {
                                        // Xử lý xóa ở đây
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("bills")
                                                .document(hoaDon.getId())
                                                .delete()
                                                .addOnSuccessListener(aVoid -> {
                                                    list.remove(holder.getAdapterPosition());
                                                    notifyItemRemoved(holder.getAdapterPosition());
                                                })
                                                .addOnFailureListener(e -> {
                                                });
                                    });
                                    builder1.setNegativeButton("Hủy", (dialog13, id) -> {
                                        // Hủy xóa
                                        dialog13.dismiss();
                                    });
                                    AlertDialog dialog1 = builder1.create();
                                    dialog1.show();
                                    break;
                                case 2:
                                    // Xử lý khi chọn Chi tiết
                                    Dialog(1, hoaDon, holder.tenPhong, holder.getAdapterPosition());
                                    break;
                            }
                        });
                builder.create().show();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tienDien, tienNuoc, tienPhong, tenPhong, tienVeSinh, tongTien, ngayThang, ghiChu, daThanhToan, chuaThanhToan;
        Button xacNhanThanhToan;
        CardView cardView_hoa_don;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView_hoa_don = itemView.findViewById(R.id.cardview_hoa_don);
            tienDien = itemView.findViewById(R.id.soDienTextView);
            tienNuoc = itemView.findViewById(R.id.soNuocTextView);
            tenPhong = itemView.findViewById(R.id.roomNameTextView);
            tienPhong = itemView.findViewById(R.id.tienPhongTextView);
            tienVeSinh = itemView.findViewById(R.id.tienVeSinhTextView);
            tongTien = itemView.findViewById(R.id.tongTienTextView);
            ngayThang = itemView.findViewById(R.id.ngayThangTextView);
            ghiChu = itemView.findViewById(R.id.ghiChuTextView);
            daThanhToan = itemView.findViewById(R.id.daThanhToan);
            chuaThanhToan = itemView.findViewById(R.id.chuaThanhToan);
            xacNhanThanhToan = itemView.findViewById(R.id.xacNhanThanhToan);

        }

        void getTenPhong(String idRoom, TextView editTenPhong) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection("rooms")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                if (queryDocumentSnapshot.getId().equals(idRoom)) {
                                    editTenPhong.setText("Tên phòng: " + queryDocumentSnapshot.getString("tenphong"));
                                }
                            }

                        }
                    });
        }
    }

    private void updateBill(String id, boolean trangThaiThanhToan, int position, HoaDon hoaDon) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference billRef = database.collection("bills").document(id);

        Map<String, Object> updates = new HashMap<>();
        updates.put("trangThaiThanhToan", trangThaiThanhToan);

        billRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Xử lý khi sửa hóa đơn thành công
                    hoaDon.setTrangThaiThanhToan(true);
                    list.set(position, hoaDon);
                    notifyItemChanged(position);
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi sửa hóa đơn thất bại
                });
    }


    public void Dialog(int type, HoaDon hoaDon, TextView tenPhongStr, int position){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_sua_hoa_don);

        // Ánh xạ các phần tử trong layout vào các biến
        TextView tenPhong = dialog.findViewById(R.id.tenPhong);
        EditText soDienEditText = dialog.findViewById(R.id.soDienEditText);
        EditText giaDien = dialog.findViewById(R.id.giaDien);
        EditText soNuocEditText = dialog.findViewById(R.id.soNuocEditText);
        EditText giaNuoc = dialog.findViewById(R.id.giaNuoc);
        EditText tienVeSinhEditText = dialog.findViewById(R.id.tienVeSinhEditText);
        EditText tienPhongEditText = dialog.findViewById(R.id.tienPhongEditText);
        EditText ghiChuEditText = dialog.findViewById(R.id.ghiChuEditText);
        CheckBox trangThaiThanhToan = dialog.findViewById(R.id.trangThaiThanhToan);
        Button chinhSua = dialog.findViewById(R.id.chinh_sua);

        //fill dữ liệu lên các trường
        tenPhong.setText(tenPhongStr.getText().toString());
        soDienEditText.setText(""+ hoaDon.getSoDien());
        giaDien.setText(""+hoaDon.getGiaDien());
        soNuocEditText.setText(""+hoaDon.getSoNuoc());
        giaNuoc.setText(""+hoaDon.getGiaNuoc());
        tienVeSinhEditText.setText(""+hoaDon.getTienVeSinh());
        tienPhongEditText.setText(""+hoaDon.getTienPhong());
        ghiChuEditText.setText(""+hoaDon.getGhiChu());
        if(hoaDon.isTrangThaiThanhToan()){
            trangThaiThanhToan.setChecked(true);
        }else{
            trangThaiThanhToan.setChecked(false);
        }

        //check loại chỉnh sửa: 0 --- chi tiết: 1
        if(type == 0){

        } else if (type == 1) {
            soDienEditText.setEnabled(false);
            giaDien.setEnabled(false);
            soNuocEditText.setEnabled(false);
            giaNuoc.setEnabled(false);
            tienVeSinhEditText.setEnabled(false);
            tienPhongEditText.setEnabled(false);
            ghiChuEditText.setEnabled(false);
            chinhSua.setVisibility(View.GONE);
        }


        chinhSua.setOnClickListener(view -> {
            // check trống các trường dữ liệu
            if(soDienEditText.getText().toString().equals("")){
                Toast.makeText(context, "Bạn chưa nhập số điện!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(soNuocEditText.getText().toString().equals("")){
                Toast.makeText(context, "Bạn chưa nhập số nước!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(tienVeSinhEditText.getText().toString().equals("")){
                Toast.makeText(context, "Bạn chưa nhập tiền vệ sinh!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(tienPhongEditText.getText().toString().equals("")){
                Toast.makeText(context, "Bạn chưa nhập tiền phòng!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(ghiChuEditText.getText().toString().equals("")){
                Toast.makeText(context, "Bạn chưa nhập ghi chú!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(giaNuoc.getText().toString().equals("")){
                Toast.makeText(context, "Bạn chưa nhập giá nước!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(giaDien.getText().toString().equals("")){
                Toast.makeText(context, "Bạn chưa nhập giá điện!", Toast.LENGTH_SHORT).show();
                return;
            }
            // Thiết lập các sự kiện cho các phần tử trong dialog
            String id = hoaDon.getId();
            String roomId = hoaDon.getRoomId();
            List<String> thanhVien = hoaDon.getThanhVien();
            float soDien = Float.parseFloat(soDienEditText.getText().toString());
            float soNuoc = Float.parseFloat(soNuocEditText.getText().toString());
            float tienVeSinh = Float.parseFloat(tienVeSinhEditText.getText().toString());
            float tienPhong = Float.parseFloat(tienPhongEditText.getText().toString());
            float giaDienFlo = Float.parseFloat(giaDien.getText().toString());
            float giaNuocFlo = Float.parseFloat(giaNuoc.getText().toString());
            float tongTien = (soDien*giaDienFlo)+(soNuoc*giaNuocFlo)+tienVeSinh+tienPhong;
            boolean trangThaiTT;
            if(trangThaiThanhToan.isChecked()){
                trangThaiTT = true;
            }else{
                trangThaiTT = false;
            }
            Log.d("soDien", ""+soDien);
            Log.d("soNuoc", ""+soNuoc);
            Log.d("soDiened", ""+soDienEditText.getText().toString());
            Log.d("soDiened", ""+soNuocEditText.getText().toString());


            String ghiChuStr = ghiChuEditText.getText().toString();
            String ngayThang = hoaDon.getNgayThang();
            String managerId = hoaDon.getManagerId();
            Log.d("idmanager", ""+managerId);
            updateBill(id, roomId, thanhVien, soDien, soNuoc, tienVeSinh, tienPhong, tongTien, ngayThang, trangThaiTT, ghiChuStr, giaDienFlo, giaNuocFlo, position, managerId);
            dialog.cancel();
            // Thực hiển chỉnh sửa sau khi đã check trống
        });



            // Hiển thị dialog
        dialog.show();

    }

    public void updateBill(String id, String roomId, List<String> thanhVien, float soDien, float soNuoc, float tienVeSinh, float tienPhong, float tongTien, String ngayThang, boolean trangThaiThanhToan, String ghiChu, float giaDien, float giaNuoc, int position, String idMana) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference billRef = database.collection("bills").document(id);

        Map<String, Object> updates = new HashMap<>();
        updates.put("roomId", roomId);
        updates.put("thanhVien", thanhVien);
        updates.put("soDien", soDien);
        updates.put("soNuoc", soNuoc);
        updates.put("tienVeSinh", tienVeSinh);
        updates.put("tienPhong", tienPhong);
        updates.put("tongTien", tongTien);
        updates.put("ngayThang", ngayThang);
        updates.put("trangThaiThanhToan", trangThaiThanhToan);
        updates.put("ghiChu", ghiChu);
        updates.put("giaDien", giaDien);
        updates.put("giaNuoc", giaNuoc);
        updates.put("managerid", idMana);

        billRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Xử lý khi sửa hóa đơn thành công
                    HoaDon hoaDon = list.get(position);
                    hoaDon.setGhiChu(ghiChu);
                    hoaDon.setTrangThaiThanhToan(trangThaiThanhToan);
                    hoaDon.setThanhVien(thanhVien);
                    hoaDon.setManagerId(idMana);
                    hoaDon.setRoomId(roomId);
                    hoaDon.setGiaDien(giaDien);
                    hoaDon.setGiaNuoc(giaNuoc);
                    hoaDon.setNgayThang(ngayThang);
                    hoaDon.setTienPhong(tienPhong);
                    hoaDon.setTienVeSinh(tienVeSinh);
                    hoaDon.setTongTien(tongTien);
                    hoaDon.setSoDien(soDien);
                    hoaDon.setSoNuoc(soNuoc);
                    list.set(position, hoaDon);
                    notifyItemChanged(position);

                })
                .addOnFailureListener(e -> {
                    // Xử lý khi sửa hóa đơn thất bại
                });
    }

}
