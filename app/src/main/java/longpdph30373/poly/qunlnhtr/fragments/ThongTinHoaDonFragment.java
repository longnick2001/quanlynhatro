package longpdph30373.poly.qunlnhtr.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import longpdph30373.poly.qunlnhtr.R;
import longpdph30373.poly.qunlnhtr.adapters.HoaDonAdapter;
import longpdph30373.poly.qunlnhtr.adapters.PhongAdapter;
import longpdph30373.poly.qunlnhtr.models.HoaDon;
import longpdph30373.poly.qunlnhtr.models.Phong;

public class ThongTinHoaDonFragment extends Fragment {
    List<HoaDon> hoaDonList;
    RecyclerView recycler_hoadon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thong_tin_hoa_don, container, false);
        hoaDonList = new ArrayList<>();
        recycler_hoadon = view.findViewById(R.id.recycle_hoadon);
        return view;
    }


    private void getPhong(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("bills")
                .get()
                .addOnCompleteListener(task -> {
                    List<HoaDon> listPhong = new ArrayList<>();
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    String id = sharedPreferences.getString("managerId", "");
                    Log.d("managerId",id);
                    if (task.isSuccessful() && task.getResult() != null) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if(id.equals(queryDocumentSnapshot.getString("managerid"))){
                                HoaDon phong = queryDocumentSnapshot.toObject(HoaDon.class);
                                phong.id = queryDocumentSnapshot.getId();
                                phong.roomId = queryDocumentSnapshot.getString("roomId");
                                phong.giaDien = queryDocumentSnapshot.getLong("giaDien");
                                phong.giaNuoc = queryDocumentSnapshot.getLong("giaNuoc");
                                phong.soNuoc = queryDocumentSnapshot.getLong("soNuoc");
                                phong.soDien = queryDocumentSnapshot.getLong("soDien");
                                phong.managerId = queryDocumentSnapshot.getString("managerid");
                                phong.thanhVien = (List<String>) queryDocumentSnapshot.get("thanhVien");
                                listPhong.add(phong);
                            }
                        }
                    }
                    recycler_hoadon.setAdapter(new HoaDonAdapter(getContext(), listPhong));
                });
    }

    public boolean isMobileDataEnabled(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public void requestMobileDataEnable(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Mạng di động đang tắt. Bạn có muốn bật mạng di động không?")
                .setCancelable(false)
                .setPositiveButton("Bật", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                        activity.startActivity(intent);
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isMobileDataEnabled(getContext())){
            requestMobileDataEnable(getActivity());
        }else {
            getPhong();
        }
    }

}