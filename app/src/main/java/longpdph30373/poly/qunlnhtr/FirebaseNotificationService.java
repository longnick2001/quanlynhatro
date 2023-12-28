package longpdph30373.poly.qunlnhtr;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import longpdph30373.poly.qunlnhtr.models.NotificationData;

public class FirebaseNotificationService extends Service {

    private ChildEventListener childEventListener;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate() {
        super.onCreate();
        // Check if notification permission is enabled
        if (!isNotificationPermissionEnabled()) {
            // If not, show notification permission dialog
            showNotificationPermissionDialog();
        }

        startMyOwnForeground();
        listenForNotifications();
    }

    private boolean isNotificationPermissionEnabled() {
        // Check if notification permission is enabled
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = getSystemService(NotificationManager.class);
            return manager != null && manager.getImportance() != NotificationManager.IMPORTANCE_NONE;
        } else {
            // For versions lower than Oreo
            return NotificationManagerCompat.from(this).areNotificationsEnabled();
        }
    }

    private void showNotificationPermissionDialog() {
        // Show notification permission dialog
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
                // Close the app or perform other actions based on your requirements
            }
        });

        try {
            builder.show();
        } catch (WindowManager.BadTokenException e) {
            // Handle exception if needed
            e.printStackTrace();
        }
    }

    private void openNotificationSettings() {
        // Open app notification settings
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        startActivity(intent);
    }

    @SuppressLint("ForegroundServiceType")
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "longpdph30373.poly.qunlnhtr.service";
        String channelName = "Background Service";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setContentTitle("Service is running in the background")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();

            startForeground(1, notification);
        }
    }

    private void listenForNotifications() {
        // No need to re-declare databaseReference here
        databaseReference = FirebaseDatabase.getInstance().getReference("notifications");

        databaseReference.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    addChildEventListener(); // No need to pass databaseReference as it's an instance variable
                }
                // No data, no need to register the ChildEventListener
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }

    private void addChildEventListener() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    NotificationData notificationData = dataSnapshot.getValue(NotificationData.class);
                    if (notificationData != null) {
                        showNotification(notificationData);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle child changed if needed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Handle child removed if needed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle child moved if needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle cancellation if needed
            }
        };

        databaseReference.addChildEventListener(childEventListener);
    }

    private void showNotification(NotificationData notificationData) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "face_recognition_notifications";
        String channelName = "Face Recognition Notifications";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_NOTIFICATION).build());
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(notificationData.getStatus())
                .setContentText("Name: " + notificationData.getName() + ", Similarity: " + notificationData.getSimilarity())
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify(new Random().nextInt(), builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not used for bound service
    }
}
