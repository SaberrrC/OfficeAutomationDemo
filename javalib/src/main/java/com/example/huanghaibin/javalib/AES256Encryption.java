package com.example.huanghaibin.javalib;


import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class AES256Encryption {

    public static void main(String[] args) {
        String content = "���";
        try {
            System.out.println("�������� " + "\n" + "����ǰ: " + content);
            //��һ��
            BASE64Encoder encode = new BASE64Encoder();
            String base64 = encode.encode(content.getBytes());
            System.out.println("��һ����" + base64);

            //�ڶ���
            String string = AESCipher.aesEncryptString(base64, "0000sl_010122940");
            System.out.println("�ڶ�����" + string);

            //������
            String string2 = AESCipher.aesEncryptString(string, "_ease_oa_#$%^&*(");
            System.out.println("���������ռ����ı���" + string2);

            //����
            System.out.println("�������� " + "\n" + "����ǰ: " + string2);

            //��һ��
            String jm1 = AESCipher.aesDecryptString(string2, "_ease_oa_#$%^&*(");
            System.out.println("��һ����" + jm1);

            //�ڶ���
            String jm2 = AESCipher.aesDecryptString(jm1, "0000sl_010122940");
            System.out.println("�ڶ�����" + jm2);

            //������
            byte b[] = Base64.decode(jm2);
            String result = new String(b);
            System.out.println("��������" + result);
            System.out.println("-------------------------------" + "\n" + "\n");

            String jimihou = Encrypt("���", "0000sl_010122940");
            System.out.println("���ܺ�" + jimihou);
            System.out.println("���ܺ�" + Decrypt(jimihou, "0000sl_010122940"));
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
     * ����
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
     * ����
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
     * SHA����
     *
     * @param strSrc ����
     * @return ����֮�������
     */
    public static String shaEncrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");// ���˻���SHA-1��SHA-512��SHA-384�Ȳ���
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * byte����ת��Ϊ16�����ַ���
     *
     * @param bts ����Դ
     * @return 16�����ַ���
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