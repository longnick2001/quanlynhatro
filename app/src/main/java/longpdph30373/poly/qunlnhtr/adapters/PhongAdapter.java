package longpdph30373.poly.qunlnhtr.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import longpdph30373.poly.qunlnhtr.R;
import longpdph30373.poly.qunlnhtr.activities.ChiTietPhongActivity;
import longpdph30373.poly.qunlnhtr.activities.ThemPhongActivity;
import longpdph30373.poly.qunlnhtr.models.Phong;

public class PhongAdapter extends RecyclerView.Adapter<PhongAdapter.ViewHolder> {
    Context context;
    List<Phong> list;

    public PhongAdapter(Context context, List<Phong> list) {
        this.context = context;
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phong, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhongAdapter.ViewHolder holder, int position) {
        holder.tenPhong.setText("Tên phòng: "+list.get(position).getTenPhong());
        holder.dienTich.setText("Diện tích: "+list.get(position).getDientich());
        holder.thanhVien.setText(list.get(position).getThanhVien().size()+"/"+list.get(position).getSoLuong());
        holder.mota.setText(list.get(holder.getAdapterPosition()).getMoTa());
        holder.giaPhong.setText(""+list.get(position).getGiaPhong());

        if(list.get(position).getThanhVien().size() >= list.get(position).getSoLuong()){
            holder.trangThai.setText("Hết chỗ");
        }else{
            holder.trangThai.setText("Còn chỗ");
        }

        holder.cardView_phong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Phong phong = new Phong();
                phong = list.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, ChiTietPhongActivity.class);
                intent.putExtra("room", phong);
                context.startActivity(intent);
            }
        });

        holder.cardView_phong.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Phong phong = new Phong();
                phong = list.get(holder.getAdapterPosition());
                Intent intent = new Intent(context, ThemPhongActivity.class);
                intent.putExtra("room", phong);
                context.startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tenPhong, dienTich, mota, trangThai, thanhVien, giaPhong;
        CardView cardView_phong;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenPhong = itemView.findViewById(R.id.tenPhong);
            dienTich = itemView.findViewById(R.id.dienTich);
            mota = itemView.findViewById(R.id.moTa);
            trangThai = itemView.findViewById(R.id.trangThai);
            thanhVien = itemView.findViewById(R.id.thanhVien);
            cardView_phong = itemView.findViewById(R.id.cardview_phong);
            giaPhong = itemView.findViewById(R.id.giaPhong);
        }
    }
}
