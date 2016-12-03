package com.zhao.installapk;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;


public class ApkOperateManager {
    public static String TAG = "ApkOperateManager";
    public static final String EXCUTOR_RESULT = "com.zhao.install.EXCUTOR_RESULT";

    //安装apk
    public static void installApk(Context context, String fileName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + fileName),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    //卸载apk
    public static void uninstallApk(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }

    //静默安装
    public static void installApkDefaul(Context context, String fileName, String pakcageName, boolean isopen) {
        Log.d(TAG, "jing mo an zhuang");
        File file = new File(fileName);
        int installFlags = 0;
        if (!file.exists())
            return;
        Log.d(TAG, "jing mo an zhuang  out");
        installFlags |= PackageManager.INSTALL_REPLACE_EXISTING;
        PackageManager pm = context.getPackageManager();
        IPackageInstallObserver observer = new MyPakcageInstallObserver(context, pakcageName, isopen);
        pm.installPackage(Uri.fromFile(file), observer, installFlags, pakcageName);
    }

    //静默卸载
    public static void uninstallApkDefaul(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        IPackageDeleteObserver observer = new MyPackageDeleteObserver(context);
        pm.deletePackage(packageName, observer, 0);
    }

    private static void sendResultCode(Context context, int returnCode){
        Intent mResultIntent = new Intent(EXCUTOR_RESULT);
        mResultIntent.putExtra("result",returnCode);
        context.sendBroadcast(mResultIntent);
    }

    //静默安装回调
    private static class MyPakcageInstallObserver extends IPackageInstallObserver.Stub {
        private static final PackageManager Intent = null;
        Context context;
        String packageName;
        boolean isOpen;

        MyPakcageInstallObserver(Context conext, String packageName, boolean isopen) {
            this.context = conext;
            this.packageName = packageName;
            this.isOpen = isopen;
        }

        @Override
        public void packageInstalled(String packageName, int returnCode)
                throws RemoteException {
            Log.i(TAG, "returnCode = " + returnCode);//返回1代表安装成功
            sendResultCode(context,returnCode);
            if (isOpen) {
                // 启动目标应用
                PackageManager packManager = context.getPackageManager();
                // 这里的packageName就是从上面得到的目标apk的包名
                Intent resolveIntent = packManager.getLaunchIntentForPackage(packageName);
                context.startActivity(resolveIntent);
            }
        }
    }

    //静默卸载回调
    private static class MyPackageDeleteObserver extends IPackageDeleteObserver.Stub {
        Context context;
        MyPackageDeleteObserver(Context context){
            this.context = context;
        }
        @Override
        public void packageDeleted(String packageName, int returnCode) {
            Log.d(TAG, "returnCode = " + returnCode);//返回1代表卸载成功
            sendResultCode(context,returnCode);
        }

    }
}