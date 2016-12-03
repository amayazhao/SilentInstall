# SilentInstall

## 简介

**静默安装**用于解决android自动嵌入式智能设备远程自动升级问题，安装过程无需人为操作。本项目通过获取系统platform签名，并利用隐藏Api--PackageManage实现Apk管理服务，设备内其他程序可通过广播接口完成应用程序静默安装与卸载功能。

## 使用(Eclipse与android studio通用)

静默安装条件比较苛刻（只讨论无法root的情况），需使用与android系统相同的签名获取SignatureOrSystem权限，但由于android系统签名每个厂家Rom各有不同，故缺点也很明显改程序需专门制作匹配的签名。下面介绍如何使用该项目

1. 生成使用系统签名的本项目Apk（比较麻烦的一点）
    - 前提条件拿到系统的签名文件：platform.pk8 platform.x509.pem（可从码源获取，一般做嵌入式的公司应该不是问题）
    - 签名，如何制作签名，<请点击>()

2. 安装后开启自动启动服务，（PS：本想做一个纯Service程序，还得继续研究一下）

3. 在需要的地方，实现广播接口，OK
    - 需按照接口发送广播请求
    
``` java
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
```

    - 还可以实现接收结果广播，获得执行结果

``` java
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
                showResult("操作失败");
            }
        }
    }
```

    - 示例代码见DemoActivity.java



