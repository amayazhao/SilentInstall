package com.zhao.installapk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ApkManageReceiver extends BroadcastReceiver {
    //开机广播
    public static final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    //安装广播
    public static final String MANAGE_INSTALL_SILENT = "android.intent.action.zhao.MANAGE_INSTALL_SILENT";

    public static final String MANAGE_UN_INSTALL_SILENT = "android.intent.action.zhao.MANAGE_UNINSTALL_SILENT";


    public static final String START_SERVICE = "com.zhao.installapk.RUN";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BOOT_COMPLETED)) {
            Intent mintent = new Intent(context, ApkManageService.class);
            mintent.setAction(START_SERVICE);
            context.startService(mintent);
        } else if (intent.getAction().equals(MANAGE_INSTALL_SILENT)) {
            //判断所需字段是否完整
            if (!intent.hasExtra("package") || !intent.hasExtra("apkpath")) {
                return;
            }
            Intent mintent = new Intent(context, ApkManageService.class);
            mintent.setAction(ApkManageService.ACTION_INSTALL_SILENT);
            mintent.putExtra("package", intent.getStringExtra("package"));
            mintent.putExtra("apkpath", intent.getStringExtra("apkpath"));
            mintent.putExtra("isopen", intent.getBooleanExtra("isopen",false));
            context.startService(mintent);
        } else if (intent.getAction().equals(MANAGE_UN_INSTALL_SILENT)) {
            Intent mintent = new Intent(context, ApkManageService.class);
            mintent.setAction(ApkManageService.ACTION_UN_INSTALL_SILENT);
            mintent.putExtra("package", intent.getStringExtra("package"));
            context.startService(mintent);
        }
    }
}
