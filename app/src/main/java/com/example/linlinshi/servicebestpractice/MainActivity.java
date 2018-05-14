package com.example.linlinshi.servicebestpractice;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button btnStartDownload;
    private Button btnPauseDownload;
    private Button btnCancelDownload;
    DownloadService.DownloadBinder downloadBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStartDownload = findViewById(R.id.btnStartDownload);
        btnPauseDownload = findViewById(R.id.btnPauseDownload);
        btnCancelDownload = findViewById(R.id.btnCancelDownload);
        Intent intent = new Intent(MainActivity.this,DownloadService.class);
        startService(intent);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
        List<String> permissionList = new ArrayList<>();
//        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission
//                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
//        }
//        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(android.Manifest.permission.READ_PHONE_STATE);
//        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MainActivity.this, "必须同意所有权限", Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "授权部分发生未知错误", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
        }
    }

    public void StartDownload(View view){
        String url ="https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe";
        downloadBinder.startDownload(url);
    }

    public void PauseDownload(View view){
        downloadBinder.pauseDownload();
    }

    public void CancelDownload(View view){
        downloadBinder.cancelDownload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

}
