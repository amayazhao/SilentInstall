package com.zhao.installapk;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ApkManageService extends Service {

    public static final String TAG = ApkManageService.class.getName();

    public static final String ACTION_INSTALL_SILENT = "com.zhao.service.INSTALL_SILENCE";

    public static final String ACTION_UN_INSTALL_SILENT = "com.zhao.service.UN_INSTALL_SILENCE";
    //服务kill掉后，重启不保留intent
    private static final int returnValue = START_STICKY;

    /**
     * id不可设置为0,否则不能设置为前台service
     */
    private static final int NOTIFICATION_DOWNLOAD_PROGRESS_ID = 0x0001;

    @Override
    public void onCreate() {
        createForeground();
        super.onCreate();
    }

    public void createForeground() {
        //使用兼容版本
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //禁止用户点击删除按钮删除
        builder.setAutoCancel(false);
        //禁止滑动删除
        builder.setOngoing(true);
        //设置通知栏的标题内容
        builder.setContentTitle("zhao Install Service");
        //创建通知
        Notification notification = builder.build();
        //创建前台服务
        this.startForeground(NOTIFICATION_DOWNLOAD_PROGRESS_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if (intent == null) {
            return returnValue;
        }
        if (ACTION_INSTALL_SILENT.equals(intent.getAction())) {
            String filepath = intent.getStringExtra("apkpath");
            String pakcage = intent.getStringExtra("package");
            boolean isopen = intent.getBooleanExtra("isopen", false);
            ApkOperateManager.installApkDefaul(this, filepath, pakcage, isopen);
        } else if (ACTION_UN_INSTALL_SILENT.equals(intent.getAction())) {
            String pakcage = intent.getStringExtra("package");
            ApkOperateManager.uninstallApkDefaul(this, pakcage);
        }
        return returnValue;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
