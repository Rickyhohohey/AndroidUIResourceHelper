package com.nokey.utils.android.codegen;

public class DrawableCodeGen extends BaseCodeGenerator {
 
	public DrawableCodeGen(String name, String suffix) {
		super(name, suffix);
	}

	@Override
	protected String getAttrFormat() {
		return "reference";
	}

	@Override
	protected String getResourceType() {
		return "drawable";
	}

 

}
