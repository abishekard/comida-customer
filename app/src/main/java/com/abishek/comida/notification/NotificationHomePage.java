package com.abishek.comida.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.abishek.comida.R;
import com.abishek.comida.cart.cartRoom.ComidaDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotificationHomePage extends AppCompatActivity {

    private static final String TAG = "NotificationHomepage";
    private List<MyNotificationTable> notificationList;
    private RecyclerView notificationRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_home_page);
        notificationRecycler = findViewById(R.id.notification_recycler);
        notificationList = new ArrayList<>();

        new FetchNotificationsFromRoom(ComidaDatabase.getDatabase(NotificationHomePage.this)).execute();
    }

    class FetchNotificationsFromRoom extends AsyncTask<Void, Void, Void> {

        private NotificationDao notificationDao;

        public FetchNotificationsFromRoom(ComidaDatabase instance) {
            notificationDao = instance.getMyNotificationDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (notificationList != null)
                notificationList.clear();
            notificationList = notificationDao.getAllNotifications();
            Log.e(TAG, ".....size: " + notificationList.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startAdapter();
            Log.e(TAG, ".....done");
        }
    }

    private void startAdapter() {



       if(notificationList.size() == 0) {
           findViewById(R.id.no_notification).setVisibility(View.VISIBLE);
           findViewById(R.id.notification_recycler).setVisibility(View.GONE);
       }
        else {
        findViewById(R.id.notification_recycler).setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationHomePage.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        NotificationAdapter mAdapter = new NotificationAdapter(notificationList, NotificationHomePage.this);

        notificationRecycler.setLayoutManager(linearLayoutManager);
        notificationRecycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
          }

    }
}