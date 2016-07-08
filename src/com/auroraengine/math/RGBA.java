/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.math;

/**
 * An object representing a colour. Beware when modifying, as this may be used
 * by multiple objects simultaneously.
 *
 * @author Arthur
 */
public final class RGBA {

	// The Static Methods
	private static int toInt(float f) {
		// TODO: Make sure this is the right way to handle this.
		return toBound(Math.round(f * 256f - 0.5f));
	}

	private static int toBound(int i) {
		return Math.max(0, Math.min(255, i));
	}
	private int R = 255;
	private int G = 255;
	private int B = 255;
	private int A = 255;
	private int RGBA = -1;
	private int BGRA = -1;

	/**
	 * Creates a new white colour.
	 */
	public RGBA() {
	}

	/**
	 * Creates a new colour using the provided rgb values. Provided values will
	 * be clamped between 0 and 255 inclusive.
	 *
	 * @param r The Red byte
	 * @param g The Green byte
	 * @param b The Blue byte
	 */
	public RGBA(int r, int g, int b) {
		set(r, g, b, 255);
	}

	/**
	 * Creates a new colour using the provided rgba values. Provided values will
	 * be clamped between 0 and 255 inclusive.
	 *
	 * @param r The Red byte
	 * @param g The Green byte
	 * @param b The Blue byte
	 * @param a The Alpha byte
	 */
	public RGBA(int r, int g, int b, int a) {
		set(r, g, b, a);
	}

	/**
	 * Creates a new colour using the provided rgb values. Provided values will
	 * be clamped between 0 and 1 inclusive.
	 *
	 * @param r The Red
	 * @param g The Green
	 * @param b The Blue
	 */
	public RGBA(float r, float g, float b) {
		set(toInt(r), toInt(g), toInt(b), 255);
	}

	/**
	 * Creates a new colour using the provided rgba values. Provided values will
	 * be clamped between 0 and 1 inclusive.
	 *
	 * @param r The Red
	 * @param g The Green
	 * @param b The Blue
	 * @param a The Alpha
	 */
	public RGBA(float r, float g, float b, float a) {
		set(toInt(r), toInt(g), toInt(b), toInt(a));
	}

	/**
	 * Sets this colour using the provided rgb values. Provided values will be
	 * clamped between 0 and 255 inclusive.
	 *
	 * @param r The Red byte
	 * @param g The Green byte
	 * @param b The Blue byte
	 * @return This
	 */
	public RGBA set(int r, int g, int b) {
		return set(r, g, b, 255);
	}

	/**
	 * Sets this colour using the provided rgb values. Provided values will be
	 * clamped between 0 and 1 inclusive.
	 *
	 * @param r The Red
	 * @param g The Green
	 * @param b The Blue
	 * @return This
	 */
	public RGBA set(float r, float g, float b) {
		return set(toInt(r), toInt(g), toInt(b), 255);
	}

	/**
	 * Sets this colour using the provided rgba values. Provided values will be
	 * clamped between 0 and 255 inclusive.
	 *
	 * @param r The Red byte
	 * @param g The Green byte
	 * @param b The Blue byte
	 * @param a The Alpha byte
	 * @return This
	 */
	public RGBA set(float r, float g, float b, float a) {
		return set(toInt(r), toInt(g), toInt(b), toInt(a));
	}

	/**
	 * Sets this colour using the provided rgba values. Provided values will be
	 * clamped between 0 and 1 inclusive.
	 *
	 * @param r The Red
	 * @param g The Green
	 * @param b The Blue
	 * @param a The Alpha
	 * @return This
	 */
	public RGBA set(int r, int g, int b, int a) {
		R = r; G = g; B = b; A = a; calc();
		return this;
	}

	/**
	 * Returns the Red byte.
	 *
	 * @return The Red byte.
	 */
	public int getR() {
		return R;
	}

	/**
	 * Returns the Green byte.
	 *
	 * @return The Green byte.
	 */
	public int getG() {
		return G;
	}

	/**
	 * Returns the Blue byte.
	 *
	 * @return The Blue byte.
	 */
	public int getB() {
		return B;
	}

	/**
	 * Returns the Alpha byte.
	 *
	 * @return The Alpha byte.
	 */
	public int getA() {
		return A;
	}

	/**
	 * Returns the colour in RGBA format.
	 *
	 * @return The colour in RGBA format.
	 */
	public int getRGBA() {
		return RGBA;
	}

	/**
	 * Returns the colour in BGRA format.
	 *
	 * @return The colour in BGRA format.
	 */
	public int getBGRA() {
		return BGRA;
	}

	private void calc() {
		BGRA = (B << 24) | (G << 16) | (R << 8) | A;
		RGBA = (R << 24) | (G << 16) | (B << 8) | A;
	}
}
