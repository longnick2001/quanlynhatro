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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import longpdph30373.poly.qunlnhtr.R;
import longpdph30373.poly.qunlnhtr.adapters.HocSinhAdapter;
import longpdph30373.poly.qunlnhtr.models.HocSinh;

public class DSHocSinhFragment extends Fragment {

    RecyclerView recycler_hocsinh;
    List<HocSinh> listHocSinh;
    String roomId = "";
    String roomName = "";
    public DSHocSinhFragment() {
        // Required empty public constructor
    }

    public static DSHocSinhFragment newInstance(String param1, String param2) {
        DSHocSinhFragment fragment = new DSHocSinhFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_d_s_hoc_sinh, container, false);
        recycler_hocsinh = view.findViewById(R.id.recycle_hocsinh);
        listHocSinh = new ArrayList<>();
        getListHocSinh();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getListHocSinh(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("users")
                .get()
                .addOnCompleteListener(task -> {

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    String id = sharedPreferences.getString("managerId", "");

                    if (task.isSuccessful() && task.getResult() != null) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if(id.equals(queryDocumentSnapshot.getString("managerid"))){
                                HocSinh user = new HocSinh();
                                user.name = queryDocumentSnapshot.getString("name");
                                user.email = queryDocumentSnapshot.getString("email");
                                user.phone = queryDocumentSnapshot.getString("phone");
                                user.roomId = queryDocumentSnapshot.getString("roomid");
                                user.schoolName = queryDocumentSnapshot.getString("schoolname");
                                user.gender = queryDocumentSnapshot.getString("gender");
                                user.dob = queryDocumentSnapshot.getString("dob");
                                user.managerId = queryDocumentSnapshot.getString("managerid");
                                user.khoa = queryDocumentSnapshot.getString("khoa");
                                user.nganh = queryDocumentSnapshot.getString("nganh");
                                user.id = queryDocumentSnapshot.getId();
                                listHocSinh.add(user);
                            }
                        }

                        recycler_hocsinh.setAdapter(new HocSinhAdapter(getContext(), listHocSinh, roomId, roomName));
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
        }
    }
}