package com.victoria.vshow.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试专用工具
 */
public class TestUtils {

    public static final String TAG = "TestUtils";

    public boolean isAppInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    // 获取所有带有已安装的APK
    public static List<String> getInstalledPackages(Context context) {
        List<String> installedPackageList = new ArrayList<>();
        List<PackageInfo> installedPackageInfoList = context.getPackageManager().getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);

        for (PackageInfo packageInfo: installedPackageInfoList) {
            installedPackageList.add(packageInfo.packageName);
        }
        return installedPackageList;
    }

    // 获取所有带有桌面属性的APK
    public static List<String> getAllLauncherIconPackages(Context context) {
        List<String> launcherIconPackageList = new ArrayList<>();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        //set MATCH_ALL to prevent any filtering of the results
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_ALL);
        for (ResolveInfo info: resolveInfos) {
            launcherIconPackageList.add(info.activityInfo.packageName);
        }

        return launcherIconPackageList;
    }

    public static void printPackages(List<String> packagesList, String packageTag){
        if (packagesList == null) {
            Log.d(TAG, packageTag + " packagesList is null" );
            return;
        }
        int packageSize = packagesList.size();
        Log.d(TAG, packageTag + " packageSize=" + packageSize);
        for (int index = 0; index < packageSize; index++) {
            Log.d(TAG, "The " + index + " package is: " + packagesList.get(index));
        }
    }

}
