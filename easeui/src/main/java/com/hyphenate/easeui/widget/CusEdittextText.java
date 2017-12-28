package com.hyphenate.easeui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import com.hyphenate.easeui.utils.EaseSmileUtils;

/**
 * Created by SaberrrC on 2017-12-28.
 */

@SuppressLint("AppCompatCustomView")
public class CusEdittextText extends EditText {
    public CusEdittextText(Context context) {
        super(context);
    }

    public CusEdittextText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CusEdittextText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CusEdittextText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onTextContextMenuItem(int id) {
        if (id == android.R.id.paste) {
            try {
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                    ClipboardManager clip = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    if (TextUtils.isEmpty(clip.getText().toString())) {
                        return true;
                    }
                    Editable edit = getEditableText();
                    Spannable span = EaseSmileUtils.getSmiledText(getContext(), clip.getText().toString());
                    int index = this.getSelectionStart();
                    if (index < 0 || index >= edit.length()) {
                        edit.append(span);
                    } else {
                        edit.insert(index, span);// 光标所在位置插入文字
                    }
                    return true;
                }
                android.text.ClipboardManager clip = (android.text.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                String value = clip.getText().toString();
                if (TextUtils.isEmpty(value)) {
                    return true;
                }
                Editable edit = getEditableText();
                int index = this.getSelectionStart();
                Spannable span = EaseSmileUtils.getSmiledText(getContext(), value);
                if (index < 0 || index >= edit.length()) {
                    edit.append(span);
                } else {
                    edit.insert(index, span);// 光标所在位置插入文字
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onTextContextMenuItem(id);
    }
}