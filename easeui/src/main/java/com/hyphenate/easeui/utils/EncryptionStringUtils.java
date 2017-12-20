package com.hyphenate.easeui.utils;

import java.util.ArrayList;

public class EncryptionStringUtils {
    /**
     * 单个字符转成 Unicode 码
     */
    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        char c = string.charAt(0);
        String code16 = Integer.toHexString(c);
        int code10 = Integer.parseInt(code16, 16);
        unicode.append("" + code10);
        return unicode.toString();//unicode.toString()为字符的unicode码
    }

    /**
     * unicode码   转单个字符
     */
    public static String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        unicode = Integer.toHexString(Integer.parseInt(unicode));
        int data = Integer.parseInt(unicode, 16);
        string.append((char) data);
        return string.toString();

    }


    /***
     * 加密字符串
     * @param str
     * @return
     */
    public static String encryptionStr(String str) {
        StringBuffer sb = new StringBuffer();
        try {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                String unicode2Aee = Unicode2Array(string2Unicode(c + ""));
                sb.append(unicode2Aee);
                if (i != str.length() - 1) {
                    sb.append("4");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 加密unicode码 转成数组string
     */
    private static String Unicode2Array(String unidcode) {
        if (unidcode.length() < 4) {
            return toArry3(unidcode);
        } else {
            return toArry4(unidcode);
        }
    }

    /***
     * unidcode小于3  转成数组
     * @param unidcode
     */

    private static String toArry3(String unidcode) {

        ArrayList<Integer> intArray = new ArrayList<>();
        String intArrayRever = "";
        int arraySum = 0;
        int arrayStart;
        int arrayEnd;

        for (int i = 0; i < unidcode.length(); i++) {
            String s = String.valueOf(unidcode.charAt(i));
            arraySum = arraySum + Integer.parseInt(s);
        }

        if (arraySum == 10) {
            arrayStart = 1;
            arrayEnd = 0;
        } else if (arraySum > 10) {
            arrayStart = arraySum / 10;
            arrayEnd = arraySum % 10;
        } else {
            arrayStart = 0;
            arrayEnd = arraySum;
        }
        intArray.add(arrayStart);
        for (int i = 0; i < unidcode.length(); i++) {
            String s = String.valueOf(unidcode.charAt(i));
            intArray.add(Integer.parseInt(s));
        }
        intArray.add(arrayEnd);

        for (int i = intArray.size() - 1; i >= 0; i--) {
            intArrayRever = intArrayRever + intArray.get(i);
        }


        return replacenEryptionCharStr(intArrayRever);
    }


    /***
     * unidcode小于4  转成数组
     * @param unidcode
     */
    private static String toArry4(String unidcode) {
        ArrayList<Integer> intArray = new ArrayList<>();
        String intArrayRever = "";
        int arraySum = 0;
        int arrayStart;
        int arrayEnd;

        for (int i = 0; i < unidcode.length(); i++) {
            String s = String.valueOf(unidcode.charAt(i));
            arraySum = arraySum + Integer.parseInt(s);
        }
        if (arraySum == 10) {
            arrayStart = 1;
            arrayEnd = 0;
        } else if (arraySum > 10) {
            arrayStart = arraySum / 10;
            arrayEnd = arraySum % 10;
        } else {
            arrayStart = 0;
            arrayEnd = arraySum;
        }
        intArray.add(arrayStart);
        for (int i = 3; i < unidcode.length(); i++) {
            String s = String.valueOf(unidcode.charAt(i));
            intArray.add(Integer.parseInt(s));
        }
        intArray.add(Integer.parseInt(String.valueOf(unidcode.charAt(1))));
        intArray.add(Integer.parseInt(String.valueOf(unidcode.charAt(2))));
        intArray.add(Integer.parseInt(String.valueOf(unidcode.charAt(0))));
        intArray.add(arrayEnd);

        for (int i = intArray.size() - 1; i >= 0; i--) {
            intArrayRever = intArrayRever + intArray.get(i);
        }

        return replacenEryptionCharStr(intArrayRever);
    }

    /***
     * 加密转换
     * 对每一个数字按照下表进行替换    得到字符串
     * @param arrayStr
     * @return
     */
    public static String replacenEryptionCharStr(String arrayStr) {
        String replaceStr = "";
        for (int i = 0; i < arrayStr.length(); i++) {

            String s = String.valueOf(arrayStr.charAt(i));
            switch (s) {
                case "0":
                    s = "l";
                    break;

                case "1":
                    s = "3";
                    break;
                case "2":
                    s = "%";
                    break;
                case "3":
                    s = "9";
                    break;
                case "4":
                    s = "s";
                    break;
                case "5":
                    s = "$";
                    break;
                case "6":
                    s = "@";
                    break;
                case "7":
                    s = "8";
                    break;
                case "8":
                    s = "^";
                    break;
                case "9":
                    s = "5";
                    break;
                default:
                    break;

            }
            replaceStr = replaceStr + s;
        }

        return replaceStr;
    }


    /***
     * 解密字符串
     * @param str
     * @return
     */
    public static String DecryptStr(String str) {
        StringBuffer sb = new StringBuffer();
        try {
            String charUnicode = "";
            if (str.contains("4")) {
                String[] ary = str.split("4");//调用API方法按照逗号分隔字符串
                for (String item : ary) {
                    charUnicode = DecryptArray(replacenDecryptCharStr(item));
                    sb.append(unicode2String(charUnicode));
                }
            } else {
                charUnicode = DecryptArray(replacenDecryptCharStr(str));
                sb.append(unicode2String(charUnicode));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 解密数组string
     * 转换成unidcode
     */

    private static String DecryptArray(String unidcode) {
        if (unidcode.length() < 6) {
            return toUnidcode3(unidcode);
        } else {
            return toUnidcode4(unidcode);
        }
    }

    /***
     * unidcode小于4  转成数组
     * @param unidcode
     */
    private static String toUnidcode4(String unidcode) {
        ArrayList<Integer> intArray = new ArrayList<>();
        String intArrayRever = "";
        unidcode = unidcode.substring(1, unidcode.length() - 1);
        unidcode = new StringBuffer(unidcode).reverse().toString();
        intArray.add(Integer.parseInt(String.valueOf(unidcode.charAt(unidcode.length() - 1))));
        intArray.add(Integer.parseInt(String.valueOf(unidcode.charAt(unidcode.length() - 3))));
        intArray.add(Integer.parseInt(String.valueOf(unidcode.charAt(unidcode.length() - 2))));

        for (int i = 0; i < unidcode.length() - 3; i++) {
            String s = String.valueOf(unidcode.charAt(i));
            intArray.add(Integer.parseInt(s));
        }
        for (int i = 0; i < intArray.size(); i++) {
            intArrayRever = intArrayRever + intArray.get(i);
        }
        return intArrayRever;
    }

    private static String toUnidcode3(String unidcode) {
        unidcode = unidcode.substring(1, unidcode.length() - 1);
        unidcode = new StringBuffer(unidcode).reverse().toString();
        return unidcode;
    }


    /***
     * 解密转换
     * 对每一个数字按照下表进行替换    得到字符串
     * @param arrayStr
     * @return
     */
    public static String replacenDecryptCharStr(String arrayStr) {
        String replaceStr = "";
        for (int i = 0; i < arrayStr.length(); i++) {

            String s = String.valueOf(arrayStr.charAt(i));
            switch (s) {
                case "l":
                    s = "0";
                    break;

                case "3":
                    s = "1";
                    break;
                case "%":
                    s = "2";
                    break;
                case "9":
                    s = "3";
                    break;
                case "s":
                    s = "4";
                    break;
                case "$":
                    s = "5";
                    break;
                case "@":
                    s = "6";
                    break;
                case "8":
                    s = "7";
                    break;
                case "^":
                    s = "8";
                    break;
                case "5":
                    s = "9";
                    break;
                default:
                    break;

            }
            replaceStr = replaceStr + s;
        }
        return replaceStr;
    }

}


