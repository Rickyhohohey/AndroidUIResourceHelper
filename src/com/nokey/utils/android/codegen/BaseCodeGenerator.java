package com.nokey.utils.android.codegen;

public abstract class BaseCodeGenerator {
	private String mName;
	private String mAttrName;
	private String mSuffix;

	public BaseCodeGenerator(String name, String suffix) {
		mName = name;
		mAttrName = getResourceType() + "_" + mName;
		mSuffix = suffix;
	}

	private String STRING_ATTR = "<attr name=\"%1$s\" format=\"%2$s\"/>\r\n";
	private String STRING_STYLE = "<item name=\"%1$s\">@%2$s/%3$s</item>\r\n";

	abstract protected String getAttrFormat();

	abstract protected String getResourceType();

	public String genAttr() {
		return String.format(STRING_ATTR, getAttrName(), getAttrFormat());
	}

	public String genStyle1() {
		return String.format(STRING_STYLE, getAttrName(), getResourceType(), getResourceName());
	}

	public String genStyle2() {
		return String.format(STRING_STYLE, getAttrName(), getResourceType(), getResourceName() + mSuffix);
	}

	protected String getAttrName() {
		return mAttrName;
	}

	protected String getResourceName() {
		return mName;
	}
}
