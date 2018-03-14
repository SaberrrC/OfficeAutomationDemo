package com.hyphenate.easeui.utils;

//import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    /**
     * 加密key与向量长度为16位
     */
    public static final int STR_LENGTH = 16;


    public static String get(String name) {
        return "7368616e6c2d6f61";
    }

    /**
     * 加密算法
     *
     * @param sSrc 加密内容
     * @param sKey 加密key
     * @param siv  加密向量
     * @return
     * @throws Exception
     */
    public static String Encrypt(String sSrc, String sKey, String siv)
            throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        // 判断加密向量16位
        if (siv == null) {
            System.out.print("加密向量iv为空null");
            return null;
        }

        byte[] dataBytes = sSrc.getBytes("utf-8");

        SecretKeySpec keyspec = new SecretKeySpec(sKey.getBytes("utf-8"), "AES");

        // "算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        int plaintextLength = dataBytes.length;
        int blockSize = cipher.getBlockSize();
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }

        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(siv.getBytes("utf-8"));
        cipher.init(Cipher.ENCRYPT_MODE, keyspec, iv);
        byte[] encrypted = cipher.doFinal(plaintext);
        MyBase64.Encoder encoder = MyBase64.getEncoder();
        return encoder.encodeToString(encrypted);
    }

    /**
     * 对字符串不满足指定长度的在右侧补空格
     *
     * @param targetStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String PadRight(String targetStr) throws UnsupportedEncodingException {
        int curLength = targetStr.getBytes().length;

        if (targetStr != null && curLength > STR_LENGTH) {
            targetStr = SubStringByte(targetStr);
        }
        String newString = "";
        int cutLength = STR_LENGTH - targetStr.getBytes().length;
        for (int i = 0; i < cutLength; i++) {
            newString += " ";
        }
        String preString = targetStr + newString;
        return preString;
    }

    public static String SubStringByte(String targetStr) {
        while (targetStr.getBytes().length > STR_LENGTH) {
            targetStr = targetStr.substring(0, targetStr.length() - 1);
        }
        return targetStr;
    }

    /**
     * 从普通字符串转换为适用于URL的Base64编码字符串
     *
     * @param normalString
     * @return
     */
    public static String Base64Replace(String normalString) {
        return normalString.replace('+', '*').replace('/', '-')
                .replace('=', '.');
    }

    public static String Encrypt(String str) throws Exception {
        String key = get("key");
        String iv = get("iv");
        String pkey;
        String piv;
        if (key == null || iv == null) {
            System.out.println("请配置好aesconfig.ini文件！");
            return null;
        } else {
            pkey = PadRight(get("key"));
            piv = PadRight(get("iv"));
        }
        String encryStr = Encrypt(str, pkey, piv);
        if (encryStr != null) {
            return Base64Replace(encryStr);
        }
        return null;
    }
}
