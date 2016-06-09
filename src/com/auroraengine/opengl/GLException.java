/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.opengl;

import com.auroraengine.client.ClientException;
import com.auroraengine.data.Client;

/**
 *
 * @author Arthur
 */
@Client
public class GLException extends ClientException {
	@SuppressWarnings("compatibility:1922922591672453299")
	private static final long serialVersionUID = -2943402453154645236L;

	public GLException() {}
	public GLException(String msg) {
		super(msg);
	}
	public GLException(String msg, Throwable cause) {
		super(msg, cause);
	}
	public GLException(String msg, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(msg, cause, enableSuppression, writableStackTrace);
	}
	public GLException(Throwable cause) {
		super(cause);
	}
}
