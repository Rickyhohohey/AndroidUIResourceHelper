package com.nokey.utils.android;

import javax.swing.JOptionPane;

public class ToolsUtils {

	public static void showMsgDialog(String msg) {
		JOptionPane.showMessageDialog(null, msg, "错误", JOptionPane.ERROR_MESSAGE);
	}

	public static String getTrimText(String text) {
		return text == null ? "" : text.trim();
	}
}
