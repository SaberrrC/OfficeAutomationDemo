package com.hyphenate.easeui.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;

public class EncryptionUtil {

    /***
     * 加密
     * @param content
     * @param
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getEncryptionStr(String content) {
        String key = "0000sl_010122940";
        content="你好";
        try {
            key = AESCipherUtils.getkeyLength16(key);
            String base64 = Base64.encodeToString(content.getBytes(), Base64.DEFAULT);
            String string = AESCipherUtils.aesEncryptString(base64, key);
            string = AESCipherUtils.aesEncryptString(string, "_ease_oa_#$%^&*(");
            return string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /***
     * 解密
     * @param content
     * @param
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDecryptStr(String content) {
        String key = "0000sl_010122940";
        try {
            key = AESCipherUtils.getkeyLength16(key);
            String jm1 = AESCipherUtils.aesDecryptString(content, "_ease_oa_#$%^&*(");
            String jm2 = AESCipherUtils.aesDecryptString(jm1, key);
            byte b[] = Base64.decode(jm2, Base64.DEFAULT);
            String result = new String(b);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}