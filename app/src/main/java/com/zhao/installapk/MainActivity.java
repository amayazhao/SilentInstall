package com.zhao.installapk;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;

/**
 * @author zhao
 *         测试Activity
 */
public class MainActivity extends Activity {
    private EditText ed_file, ed_package;
    private static final String baseBath = Environment.getExternalStorageDirectory() + File.separator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        ed_file = (EditText) findViewById(R.id.ed_file);
        ed_package = (EditText) findViewById(R.id.ed_package);
    }

    private void initData() {
        ed_file.setText("Download/AidDeviceTest.apk");
        ed_package.setText("com.prafly.aiddevicetest");
        Intent mIntent = new Intent(ApkManageReceiver.START_SERVICE);
        sendBroadcast(mIntent);
    }

    public void installOnClick(View view) {
        Intent mIntent = new Intent(ApkManageReceiver.MANAGE_INSTALL_SILENT);
        mIntent.putExtra("apkpath", baseBath + ed_file.getText().toString());
        mIntent.putExtra("package", ed_package.getText().toString());
        mIntent.putExtra("isopen", true);
        sendBroadcast(mIntent);
    }

    public void unInstallOnclick(View view) {
        Intent mIntent = new Intent(ApkManageReceiver.MANAGE_UN_INSTALL_SILENT);
        mIntent.putExtra("package", ed_package.getText().toString());
        sendBroadcast(mIntent);
    }

}