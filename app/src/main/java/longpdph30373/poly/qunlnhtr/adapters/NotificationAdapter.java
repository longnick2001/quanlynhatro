package longpdph30373.poly.qunlnhtr.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import longpdph30373.poly.qunlnhtr.R;
import longpdph30373.poly.qunlnhtr.models.NotificationData;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationData> notificationList;

    // Constructor to initialize the list
    public NotificationAdapter(List<NotificationData> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the current notification data
        NotificationData notification = notificationList.get(position);

        String status = notification.getStatus();
        if (status.equals("Nhận diện thành công")){
            holder.notificationTitle.setTextColor(R.color.black);
        }
        else {
            holder.notificationTitle.setTextColor(R.color.red);
        }

        // Set data to the views in the CardView
        holder.notificationTitle.setText(notification.getStatus());
        holder.notificationDetails.setText("Name: " + notification.getName() + ", Similarity: " + notification.getSimilarity());

        // Convert timestamp to a human-readable date and time
        // Check if the timestamp is not null before using it
        if (notification.getTimestamp() != null) {
            // Convert timestamp to a human-readable date and time
            String formattedDateTime = getFormattedDateTime(notification.getTimestamp());
            holder.notificationTime.setText(formattedDateTime);
        } else {
            holder.notificationTime.setText("N/A");
        }   }
    private String getFormattedDateTime(long timestamp) {
        try {
            // Create a DateFormatter object for displaying date and time in specified format
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());

            // Create a Date object using the timestamp
            Date date = new Date(timestamp);

            // Format the date and time
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "N/A";
        }

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificationTitle;
        TextView notificationDetails;
        TextView notificationTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views from the layout
            notificationTitle = itemView.findViewById(R.id.notificationTitle);
            notificationDetails = itemView.findViewById(R.id.notificationDetails);
            notificationTime= itemView.findViewById(R.id.notification_time);
        }
    }
}

