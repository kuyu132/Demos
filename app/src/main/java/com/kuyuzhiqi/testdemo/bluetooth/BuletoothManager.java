package com.kuyuzhiqi.testdemo.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import java.text.DecimalFormat;

public class BuletoothManager {
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private boolean isRegistered = false;
    private StringBuilder sb;
    private Callback callback;

    public BuletoothManager(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public void start() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(receiver, intentFilter);
        isRegistered = true;
        bluetoothAdapter.startDiscovery();
        sb = new StringBuilder();
    }

    public void stop() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        if (isRegistered) {
            context.unregisterReceiver(receiver);
        }
        if (callback != null) {
            callback.onCallback(sb.toString());
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                // 将蓝牙信号强度换算为距离
                double d = Math.pow(10, ((Math.abs(rssi) - 59) / (10 * 2.0)));
                DecimalFormat df = new DecimalFormat("######0.00");
                String mm = df.format(d);

                if (device != null) {
                    sb.append("蓝牙信息：" + device.toString() + " 绑定：" + device.getBondState() + " 强度：" + rssi + " 距离：" + mm).append("\n");
                    Log.i("tencent", "蓝牙信息：" + device.toString() + " 绑定：" + device.getBondState() + " 强度：" + rssi + " 距离：" + mm);
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())) {

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())) {
                Toast.makeText(context, "扫描完成了", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public interface Callback {
        void onCallback(String result);
    }
}
