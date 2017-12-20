package com.hyphenate.easeui.utils;

public class EncryptionUtil {

    /***
     * 加密
     * @param content
     * @param
     * @return
     */
    public static String getEncryptionStr(String content, String key) {

      /*  StringBuffer sb = new StringBuffer();
        try {
            content = EncryptionStringUtils.encryptionStr(content);
            sb.append("<0-");
            sb.append(content);
            sb.append("->");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();*/
        return content;

    }

    /***
     * 解密
     * @param content
     * @param
     * @return
     */
    public static String getDecryptStr(String content, String key) {

     /*   try {
            if (content.startsWith("<0-") && content.endsWith("->")) {//<0-95s3->
                content = content.substring(3, content.length() - 2);
                content = EncryptionStringUtils.DecryptStr(content);
            } else {
                return content;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return content;
    }
}