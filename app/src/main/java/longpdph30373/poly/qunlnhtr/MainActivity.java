package longpdph30373.poly.qunlnhtr;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import longpdph30373.poly.qunlnhtr.activities.Login;
import longpdph30373.poly.qunlnhtr.fragments.DSHocSinhFragment;
import longpdph30373.poly.qunlnhtr.fragments.DichVuFragment;
import longpdph30373.poly.qunlnhtr.fragments.PhongFragment;
import longpdph30373.poly.qunlnhtr.fragments.ThongTinHoaDonFragment;
import longpdph30373.poly.qunlnhtr.fragments.TimKiemFragment;
import longpdph30373.poly.qunlnhtr.fragments.TrangChuFragment;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    NavigationView navigationView;
    Toolbar toolbar_frame4;
    Fragment selectedFragment;
    DrawerLayout drawerLayout;
    TextView name, phoneNumber, email;
    String managerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        navigationView = findViewById(R.id.navigationView_frame4);
        toolbar_frame4 = findViewById(R.id.toolbar_frame4);
        drawerLayout = findViewById(R.id.drawerLayout);

        View headerLayout = navigationView.getHeaderView(0);
        email = headerLayout.findViewById(R.id.email);
        name = headerLayout.findViewById(R.id.name);
        phoneNumber = headerLayout.findViewById(R.id.phone);
        Button logout = headerLayout.findViewById(R.id.logout);

        setSupportActionBar(toolbar_frame4);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.header_menu);
        Intent serviceIntent = new Intent(this, FirebaseNotificationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }

        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        mUser = mAuth.getCurrentUser();
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationView.setNavigationItemSelectedListener(
                item -> {
                    int menuId = item.getItemId();
                    if (menuId == R.id.dich_vu) {
                        selectedFragment  = new DichVuFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    toolbar_frame4.setTitle(item.getTitle());
                    return true;
                }
        );

        if(mUser == null){
            Intent intent = new Intent(getApplicationContext(), Long.class);
            startActivity(intent);
            finish();
        }else{
            getManagerName(mUser.getEmail());
            email.setText(mUser.getEmail());
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        selectedFragment = new TrangChuFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        int id = item.getItemId();
        if(id == R.id.navigation_item1){
            selectedFragment = new TrangChuFragment();
        }else if(id == R.id.navigation_item2){
            selectedFragment = new PhongFragment();
        }else if(id == R.id.navigation_item3){
            selectedFragment = new TimKiemFragment();
        }else if(id == R.id.navigation_item4){
            selectedFragment = new DSHocSinhFragment();
        }else if(id == R.id.navigation_item5){
            selectedFragment = new ThongTinHoaDonFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        toolbar_frame4.setTitle(item.getTitle());
        return true;
    };



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    public void getManagerName(String email) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("managers")
                .get()
                .addOnCompleteListener(task -> {


                    if (task.isSuccessful() && task.getResult() != null) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if(queryDocumentSnapshot.getString("email").equals(email)){
                                name.setText("Hi, "+queryDocumentSnapshot.getString("name"));
                                phoneNumber.setText(queryDocumentSnapshot.getString("phone"));
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("managerId", queryDocumentSnapshot.getId());
                                editor.apply();
                            }
                        }

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

    private void checkNotificationPermission() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Kiểm tra xem quyền thông báo đã được cấp chưa
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel("longpdph30373.poly.qunlnhtr.service");
            if (channel == null || channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                showNotificationPermissionDialog();
            }
        } else {
            // Cho các phiên bản Android thấp hơn Oreo
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                showNotificationPermissionDialog();
            }
        }
    }

    private void showNotificationPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Yêu cầu cấp quyền thông báo");
        builder.setMessage("Ứng dụng cần quyền thông báo để hiển thị thông báo. Hãy cấp quyền thông báo trong cài đặt.");
        builder.setPositiveButton("Điều chỉnh cài đặt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openNotificationSettings();
            }
        });
        builder.setNegativeButton("Để sau", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng ứng dụng hoặc thực hiện các hành động khác tùy thuộc vào yêu cầu của bạn
            }
        });

        try {
            builder.show();
        } catch (WindowManager.BadTokenException e) {
            // Xử lý ngoại lệ nếu có
            e.printStackTrace();
        }
    }

    private void openNotificationSettings() {
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isMobileDataEnabled(this)){
            requestMobileDataEnable(this);
        }
        checkNotificationPermission();
    }
}