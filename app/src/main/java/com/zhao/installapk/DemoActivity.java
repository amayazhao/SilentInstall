package com.zhao.installapk;

import java.io.File;
import java.util.HashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author zhao
 *         测试Activity
 */
public class DemoActivity extends Activity {

    private String filepath = Environment.getExternalStorageDirectory() + File.separator + "Download" + File.separator;

    private String filename = "AidDeviceTest.apk";

    private String packagename = "com.zhao.aiddevicetest";

    private boolean isOpen = true;

    private static final String RESULT_BROAD = "com.zhao.install.EXCUTOR_RESULT";

    View view;

    private myReceiver receiver;

    /**
     * 接收执行结果
     * 注册广播：com.zhao.install.EXCUTOR_RESULT
     */
    class myReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //1表示执行成功
            int result = intent.getIntExtra("result",0);
            if(result == 1){
                showResult("操作成功");
            }else {
                showResult("操作失败:" + result);
            }
        }
    }

    @Override
    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter(RESULT_BROAD);
        receiver = new myReceiver();
        registerReceiver(receiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.btn_install);
    }

    public void installOnClick(View view) {
        SendInstallBroadcast(filepath + filename, packagename, isOpen);
    }

    public void unInstallOnclick(View view) {
        SendUnInstallBroadcast(packagename);
    }

    public void showResult(String str){
        Toast.makeText(this, str ,Toast.LENGTH_SHORT).show();
    }
    /**
     * 发送静默安装请求
     * @Action：android.intent.action.zhao.MANAGE_INSTALL_SILENT
     * @param apkPath 安装包本地地址
     * @param packageName 包名
     * @param isOpen 安装成功后是否打开
     */
    private void SendInstallBroadcast(String apkPath, String packageName, boolean isOpen){
        Intent mIntent = new Intent("android.intent.action.zhao.MANAGE_INSTALL_SILENT");
        mIntent.putExtra("apkpath",apkPath);
        mIntent.putExtra("package",packageName);
        mIntent.putExtra("isopen", isOpen);
        sendBroadcast(mIntent);
    }

    /**
     * 发送静默卸载请求
     * @Action：android.intent.action.zhao.MANAGE_UNINSTALL_SILENT
     * @param packageName 卸载apk包名
     */
    private void SendUnInstallBroadcast(String packageName){
        Intent mIntent = new Intent("android.intent.action.zhao.MANAGE_UNINSTALL_SILENT");
        mIntent.putExtra("package",packageName);
        sendBroadcast(mIntent);
    }
}