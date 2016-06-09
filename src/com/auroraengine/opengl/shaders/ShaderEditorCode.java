package com.auroraengine.opengl.shaders;


/**
 * Basically exactly the same as ShaderCode but this remembers the text that
 * was used as the code for retrieval. This might be completely unnecessary
 * however.
 */
public class ShaderEditorCode extends ShaderCode {
	public ShaderEditorCode(String code) {
		super(code);
	}
	private String code;
}
