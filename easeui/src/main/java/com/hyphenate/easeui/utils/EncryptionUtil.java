package com.hyphenate.easeui.utils;

import android.util.Base64;
import android.util.Log;

public class EncryptionUtil {

    /***
     * 加密
     * @param content
     * @param
     * @return
     */
    public static String getEncryptionStr(String content, String key) {
        String result = "";
        try {
            key = AESCipherUtils.getkeyLength16(key);
            String base64 = Base64.encodeToString(content.getBytes(), Base64.NO_WRAP);

            String string = AESCipherUtils.aesEncryptString(base64, key);
            result = AESCipherUtils.aesEncryptString(string, "_ease_oa_#$%^&*(");
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

    /***
     * 解密
     * @param content
     * @param
     * @return
     */
    public static String getDecryptStr(String content, String key) {
        String result;
        try {
            key = AESCipherUtils.getkeyLength16(key);
            String jm1 = AESCipherUtils.aesDecryptString(content, "_ease_oa_#$%^&*(");
            String jm2 = AESCipherUtils.aesDecryptString(jm1, key);
            byte b[] = Base64.decode(jm2, Base64.NO_WRAP);
            result = new String(b);
        } catch (Exception e) {
            result = "";
        }
        return result;
    }
}