package com.shanlinjinrong.oa.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

import com.shanlinjinrong.oa.manager.AppManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cuieney
 * @time 31/08/2017
 */

public class EmojiFilter implements InputFilter {

    public static boolean isHasEmoji(CharSequence source) {
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher emojiMatcher = emoji.matcher(source);
        if (emojiMatcher.find()) {
            return true;
        }
        return false;
    }

    public EmojiFilter(int max) {
        mMax = max;
    }

    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        //判断是否含有emoji表情
        if (isHasEmoji(source)) {
            Toast.makeText(AppManager.sharedInstance().getAppComponent().getContext(), "不能输入表情", Toast.LENGTH_SHORT).show();
            return "";
        }
        int keep = mMax - (dest.length() - (dend - dstart));
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return null; // keep original
        } else {
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            return source.subSequence(start, keep);
        }
    }

    private int mMax;
}
