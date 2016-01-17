package cn.lyh.buckupapk;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Buckup {

    public static int LOGO = 0;
    public static int APP_NAME = 1;
    public static int PACKAGE_NAME = 2;
    public static int VERSION_CODE = 3;
    public static int VERSION_NAME = 4;

    private static Boolean isDebug = true;
    private final static String TAG = Buckup.class.getName();

    //查询手机内非系统应用
    public static List<Map<Integer, Object>> getAllApps(Activity activity) {
        List<Map<Integer, Object>> data = new ArrayList<>();
        PackageManager pManager = activity.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> pakList = pManager.getInstalledPackages(0);
        for (PackageInfo pak : pakList) {
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                Map<Integer, Object> map = new HashMap<>();
                map.put(LOGO, pak.applicationInfo.loadIcon(pManager));
                map.put(APP_NAME, pManager.getApplicationLabel(pak.applicationInfo).toString());
                map.put(PACKAGE_NAME, pak.applicationInfo.packageName);
                map.put(VERSION_CODE, pak.versionCode);
                map.put(VERSION_NAME, pak.versionName);
                data.add(map);
            }
        }
        return data;
    }

    /**
     * 备份APK android5.0+
     *
     * @param package_name 包名
     * @param path         备份APK路径
     * @param file_name    备份APK名字
     */
    public static void backupApp5(String package_name, String path, String file_name) {
        String toFilePath = path + File.separator + file_name;

        File fromFile = new File("/data/app/" + package_name + "-1/base.apk");
        if (!fromFile.exists()) {
            fromFile = new File("/data/app/" + package_name + "-2/base.apk");
            if (!fromFile.exists()) {
                if (isDebug)
                    Log.e(TAG, package_name + "不存在");
                return;
            }
        }
        backup(fromFile, toFilePath);


    }

    /**
     * 备份APK android5。0以下
     *
     * @param package_name 包名
     * @param path         备份APK路径
     * @param file_name    备份APK名字
     */
    public static void backupApp(String package_name, String path, String file_name) {
        String toFilePath = path + File.separator + file_name;
        if (TextUtils.isEmpty(package_name) || TextUtils.isEmpty(file_name)) {
            if (isDebug)
                Log.e(TAG, "参数不合法");
            return;
        }
        File fromFile = new File("/data/app/" + package_name + "-1.apk");
        if (!fromFile.exists()) {
            fromFile = new File("/data/app/" + package_name + "-2.apk");
            if (!fromFile.exists()) {
                if (isDebug)
                    Log.e(TAG, package_name + "不存在");
                return;
            }
        }
        backup(fromFile, toFilePath);
    }

    private static void backup(File fromFile, String toFilePath) {
        FileInputStream in;
        try {
            in = new FileInputStream(fromFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            if (isDebug)
                Log.e(TAG, e.getMessage());
            return;
        }
        int i = toFilePath.lastIndexOf('/');
        if (i != -1) {
            File dirs = new File(toFilePath.substring(0, i));
            dirs.mkdirs();
        }
        byte[] c = new byte[1024];
        int slen;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(toFilePath);
            while ((slen = in.read(c, 0, c.length)) != -1)
                out.write(c, 0, slen);
        } catch (IOException e) {
            e.printStackTrace();
            if (isDebug)
                Log.e(TAG, e.getMessage());
            return;
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    if (isDebug)
                        Log.e(TAG, e.getMessage());
                }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    if (isDebug)
                        Log.e(TAG, e.getMessage());
                }
            }
        }
        if (isDebug)
            Log.e(TAG, "成功");
    }

}
