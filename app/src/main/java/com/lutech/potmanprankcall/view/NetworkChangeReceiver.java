package com.lutech.potmanprankcall.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;

import androidx.appcompat.app.AlertDialog;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            boolean isConnected = NetworkUtils.isNetworkAvailable(context);
            if (!isConnected) {
                showDialog(context);
            }
        }
    }

    private void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Không có kết nối mạng");
        builder.setMessage("Vui lòng kiểm tra kết nối mạng của bạn và thử lại.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                context.startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}