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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import longpdph30373.poly.qunlnhtr.R;
import longpdph30373.poly.qunlnhtr.activities.ThemPhongActivity;
import longpdph30373.poly.qunlnhtr.adapters.HocSinhAdapter;
import longpdph30373.poly.qunlnhtr.adapters.PhongAdapter;
import longpdph30373.poly.qunlnhtr.models.HocSinh;
import longpdph30373.poly.qunlnhtr.models.Phong;

public class PhongFragment extends Fragment {
    FloatingActionButton themPhong;
    RecyclerView recycler_phong;
    PhongAdapter phongAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phong, container, false);
        themPhong = view.findViewById(R.id.them_phong);
        recycler_phong = view.findViewById(R.id.recycle_phong);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        getPhong();

        themPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ThemPhongActivity.class);
                List<String> list = new ArrayList<>();
                Phong phong = new Phong("", "", 0, "", 0, list, 0, "");
                intent.putExtra("room", phong);
                startActivity(intent);
            }
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


    private void getPhong() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("rooms")
                .get()
                .addOnCompleteListener(task -> {
                    List<Phong> listPhong = new ArrayList<>();

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    String id = sharedPreferences.getString("managerId", "");

                    if (task.isSuccessful() && task.getResult() != null) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (queryDocumentSnapshot.getString("managerid").equals(id)) {
                                Phong phong = queryDocumentSnapshot.toObject(Phong.class);
                                phong.tenPhong = queryDocumentSnapshot.getString("tenphong");
                                phong.dientich = queryDocumentSnapshot.getLong("dientich");
                                phong.moTa = queryDocumentSnapshot.getString("mota");
                                phong.soLuong = Math.toIntExact(queryDocumentSnapshot.getLong("soluong"));
                                phong.giaPhong = Math.toIntExact(queryDocumentSnapshot.getLong("giaphong"));
                                List<String> tags = new ArrayList<>();
                                tags = (List<String>) queryDocumentSnapshot.get("thanhvien");
                                phong.thanhVien = tags;
                                phong.managerId = queryDocumentSnapshot.getString("managerid");
                                phong.id = queryDocumentSnapshot.getId();
                                listPhong.add(phong);
                            }
                        }
                    }
                    phongAdapter = new PhongAdapter(getContext(), listPhong);
                    recycler_phong.setAdapter(phongAdapter);
                });
    }

}