package com.abishek.comida.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abishek.comida.R;

import java.util.List;

import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.getFormattedDate;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.getFormattedTime;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final String TAG = "NotificationAdapter";

    private List<MyNotificationTable> notificationTableList;
    private Context context;

    public NotificationAdapter(List<MyNotificationTable> notificationTableList, Context context) {
        this.notificationTableList = notificationTableList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_design,parent,false);

        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        holder.titleView.setText(notificationTableList.get(position).getTitle());
        holder.timeView.setText(getFormattedDate(TAG,notificationTableList.get(position).getUnixTime())
                +", "+ getFormattedTime(TAG,notificationTableList.get(position).getUnixTime()));
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationTableList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{

        private TextView titleView;
        private TextView timeView;
        private LinearLayout card;

        public NotificationViewHolder(@NonNull View v) {
            super(v);

            titleView = v.findViewById(R.id.title);
            timeView = v.findViewById(R.id.time);
            card = v.findViewById(R.id.card);
        }
    }

}
