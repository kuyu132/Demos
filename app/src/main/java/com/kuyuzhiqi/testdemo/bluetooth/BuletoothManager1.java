package com.kuyuzhiqi.testdemo.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;

//使用lescan的方式
public class BuletoothManager1 {
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;
    private boolean isRegistered = false;

    public BuletoothManager1(Context context) {
        this.context = context;
        final BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public void start() {
        bluetoothAdapter.startLeScan(leScanCallback);
    }

    public void stop() {
        bluetoothAdapter.stopLeScan(leScanCallback);
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.i("tencent", "蓝牙信息1：" + device.toString() + " rssi:" + rssi);
        }
    };
}
