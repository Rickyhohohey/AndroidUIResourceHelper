package com.nokey.utils.android.codegen;

public class ColorCodeGen extends BaseCodeGenerator{
 
	public ColorCodeGen(String name, String suffix) {
		super(name, suffix);
	}

	@Override
	protected String getAttrFormat() {
		return "color";
	}

	@Override
	protected String getResourceType() {
		return "color";
	}
	
}
