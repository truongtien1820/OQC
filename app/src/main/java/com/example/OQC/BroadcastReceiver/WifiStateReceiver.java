package com.example.OQC.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.ImageView;

import com.example.OQC.R;

public class WifiStateReceiver extends BroadcastReceiver {

    private final ImageView wifiStatusIcon;

    public WifiStateReceiver(ImageView wifiStatusIcon) {
        this.wifiStatusIcon = wifiStatusIcon;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int signalLevel = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 4);

            // Cập nhật biểu tượng Wi-Fi
            switch (signalLevel) {
                case 3:
                    wifiStatusIcon.setImageResource(R.drawable.wifi_full); // Tín hiệu mạnh
                    break;
                case 2:
                    wifiStatusIcon.setImageResource(R.drawable.wifi_medium); // Tín hiệu trung bình
                    break;
                case 1:
                    wifiStatusIcon.setImageResource(R.drawable.wifi_weak); // Tín hiệu yếu
                    break;
                default:
                    wifiStatusIcon.setImageResource(R.drawable.wifi_off); // Không kết nối
                    break;
            }
        } else {
            // Wi-Fi bị tắt
            wifiStatusIcon.setImageResource(R.drawable.wifi_off);
        }
    }
}
