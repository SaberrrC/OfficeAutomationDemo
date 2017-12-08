package com.example.huanghaibin.javalib;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class AES256Encryption {

    public static void main(String[] args) {
        String content = "你好";
        try {
            System.out.println("加密流程 " + "\n" + "加密前: " + content);
            //第一步
            BASE64Encoder encode = new BASE64Encoder();
            String base64 = encode.encode(content.getBytes());
            System.out.println("第一步：" + base64);

            //第二步
            String string = AESCipher.aesEncryptString(base64, "0000sl_010122940");
            System.out.println("第二步：" + string);

            //第三步
            String string2 = AESCipher.aesEncryptString(string, "_ease_oa_#$%^&*(");
            System.out.println("第三步最终加密文本：" + string2);

            //解密
            System.out.println("解密流程 " + "\n" + "解密前: " + string2);

            //第一步
            String jm1 = AESCipher.aesDecryptString(string2, "_ease_oa_#$%^&*(");
            System.out.println("第一步：" + jm1);

            //第二步
            String jm2 = AESCipher.aesDecryptString(jm1, "0000sl_010122940");
            System.out.println("第二步：" + jm2);

            //第三步
            byte b[] = Base64.decode(jm2);
            String result = new String(b);
            System.out.println("第三步：" + result);
            System.out.println("-------------------------------" + "\n" + "\n");

            String jimihou = Encrypt("你好", "0000sl_010122940");
            System.out.println("加密后：" + jimihou);
            System.out.println("解密后：" + Decrypt(jimihou, "0000sl_010122940"));
            System.out.println(AESCipher.getkeyLength16(""));
            System.out.println(AESCipher.getkeyLength16("1234"));
            System.out.println(AESCipher.getkeyLength16("1234567890"));
            System.out.println(AESCipher.getkeyLength16("1234567890123456"));
            System.out.println(AESCipher.getkeyLength16("1234567890123456oqewwqr"));



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 加密
     * @param content
     * @param key
     * @return
     */
    public static String Encrypt(String content, String key) {
        try {
            key = AESCipher.getkeyLength16(key);
            BASE64Encoder encode = new BASE64Encoder();
            String base64 = encode.encode(content.getBytes());
            String string = AESCipher.aesEncryptString(base64, key);
            string = AESCipher.aesEncryptString(string, "_ease_oa_#$%^&*(");
            return string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /***
     * 解密
     * @param content
     * @param key
     * @return
     */
    public static String Decrypt(String content, String key) {
        try {
            key = AESCipher.getkeyLength16(key);
            String jm1 = AESCipher.aesDecryptString(content, "_ease_oa_#$%^&*(");
            String jm2 = AESCipher.aesDecryptString(jm1, key);

            byte b[] = Base64.decode(jm2);
            String result = new String(b);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * SHA加密
     *
     * @param strSrc 明文
     * @return 加密之后的密文
     */
    public static String shaEncrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * byte数组转换为16进制字符串
     *
     * @param bts 数据源
     * @return 16进制字符串
     */
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}