package com.hjnerp.widget;

import com.hjnerp.util.StringUtil;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

/* 
 * 监听输入内容是否超出最大长度，并设置光标位置  
 * 李庆义
 * 2014-09-21
 * */

public class HJEditTextWatcher implements TextWatcher {

	private int maxLen = 0;
	private EditText editText = null;
	private String format;
	private String valuetype;

	private Handler callHandler;

	public HJEditTextWatcher(EditText editText, int maxLen, String valuetype,
			String format, Handler callHandler) {
		this.maxLen = maxLen;
		this.editText = editText;
		this.valuetype = valuetype;
		this.format = format;
		this.callHandler = callHandler;
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		Message msg = callHandler.obtainMessage(1);
		msg.obj = "1";
		callHandler.sendMessage(msg);
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		Editable editable = editText.getText();
		int len = editable.length();
		if (len > maxLen && maxLen != 0) {
			int selEndIndex = Selection.getSelectionEnd(editable);
			String str = editable.toString();
			// 截取新字符串
			String newStr = str.substring(0, maxLen);
			editText.setText(newStr);
			editable = editText.getText();

			// 新字符串的长度
			int newLen = editable.length();
			// 旧光标位置超过字符串长度
			if (selEndIndex > newLen) {
				selEndIndex = editable.length();
			}
			// 设置新光标所在的位置
			Selection.setSelection(editable, selEndIndex);
			return;
		}

		if ("decimal".equalsIgnoreCase(valuetype)) {
			if (StringUtil.isNullOrEmpty(format)) {
				format = "####.##########";
			}

			if (format.indexOf(".") <= 0) {
				format = format + ".##########";
			}

			String[] str = format.split("\\.");
			String editstrs = editText.getText().toString();

			String[] str2 = editstrs.split("\\.");

			// 截取新字符串

			if (str2.length > 1) {
				int lengthlimit = str[1].length();

				if (lengthlimit > 10) {
					lengthlimit = 10;
				}

				int lengthlimit2 = str2[1].length();

				if (lengthlimit < lengthlimit2) {
					str2[1] = str2[1].substring(0, lengthlimit);
					String strformat = str2[0] + "." + str2[1];
					editText.setText(strformat);
					editable = editText.getText();
					Selection.setSelection(editable, strformat.length());
				}

			}

		}

	}

}
