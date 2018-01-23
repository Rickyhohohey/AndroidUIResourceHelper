package com.nokey.utils.android.codegen;

import com.nokey.utils.android.Constants;

public class GeneratorFactory {
	public static BaseCodeGenerator getGenerator(String type, String name, String suffix) {
		if (Constants.TYPE_CODE_COLOR.equals(type)) {
			return new ColorCodeGen(name, suffix);
		} else if (Constants.TYPE_CODE_DRAWABLE.equals(type)) {
			return new DrawableCodeGen(name, suffix);
		} else {
			return null;
		}
	}
}
