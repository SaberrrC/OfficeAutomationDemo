package com.shanlinjinrong.oa.utils;



import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.Arrays;

public class VersionManagementUtil {

    private static final String TAG = "VersionManagementUtil";

    private static Context mContext;

    private static final VersionManagementUtil INSTANCE = new VersionManagementUtil();

    public static VersionManagementUtil getInstance(Context mContext) {

        VersionManagementUtil.mContext = mContext;

        return VersionManagementUtil.INSTANCE;
    }

    /**
     * 获取版本号 
     *
     * @return 当前应用的版本号，默认是1.0.0 
     */
    public static String getVersion(Context mContext) {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }

    /**
     *
     * @param
     * @param
     * @return if version1 > version2, return 1, if equal, return 0, else return
     *         -1
     */
    public static int VersionComparison(String versionServer, String versionLocal) {
        String version1 = versionServer;
        String version2 = versionLocal;
        if (version1 == null || version1.length() == 0 || version2 == null || version2.length() == 0)
            throw new IllegalArgumentException("Invalid parameter!");

        int index1 = 0;
        int index2 = 0;
        while (index1 < version1.length() && index2 < version2.length()) {
            int[] number1 = getValue(version1, index1);
            Log.i(TAG," ===== number1 ====" + Arrays.toString(number1));
            int[] number2 = getValue(version2, index2);
            Log.i(TAG," ===== number2 ====" + Arrays.toString(number2));

            if (number1[0] < number2[0]){
                Log.i(TAG," ===== number1[0] ====" + number1[0]);
                Log.i(TAG," ===== number2[0] ====" + number2[0]);
                return -1;
            }
            else if (number1[0] > number2[0]){
                Log.i(TAG," ===== number1[0] ====" + number1[0]);
                Log.i(TAG," ===== number2[0] ====" + number2[0]);
                return 1;
            }
            else {
                index1 = number1[1] + 1;
                index2 = number2[1] + 1;
            }
        }
        if (index1 == version1.length() && index2 == version2.length())
            return 0;
        if (index1 < version1.length())
            return 1;
        else
            return -1;
    }

    /**
     *
     * @param version
     * @param index
     *            the starting point
     * @return the number between two dots, and the index of the dot
     */
    public static int[] getValue(String version, int index) {
        int[] value_index = new int[2];
        StringBuilder sb = new StringBuilder();
        while (index < version.length() && version.charAt(index) != '.') {
            sb.append(version.charAt(index));
            index++;
        }
        value_index[0] = Integer.parseInt(sb.toString());
        value_index[1] = index;

        return value_index;
    }

    /**
     * 判断版本更新
     * @param version1 服务器应用版本
     * @param version2 应用本身版本
     * @return 0=>版本相同，1=>版本需要更新，-1版本不需要更新
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int minLen = Math.min(version1Array.length, version2Array.length);
        int index = 0;
        int diff = 0;
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }
}  