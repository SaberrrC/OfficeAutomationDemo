package com.shanlin.oa.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * <h3>Description: 操作安装包中的“assets”目录下的文件 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/10/11.<br />
 */
public class AssetsUtils {

    /**
     * read file content
     *
     * @param context   the context
     * @param assetPath the asset path
     * @return String string
     */
    public static String readText(Context context, String assetPath) {
        try {
            StringBuilder sb = new StringBuilder();
            InputStream is = context.getAssets().open(assetPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            while (br.ready()) {
                String line = br.readLine();
                if (line != null) {
                    // 读出来文件末尾多了“null”?
                    sb.append(line).append("\n");
                }
            }
            br.close();
            is.close();
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
