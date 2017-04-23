/*
 * Copyright (C) 2017 LittleRover
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.auroraengine.math;

/**
 * An object representing a colour. Beware when modifying, as this may be used
 * by multiple objects simultaneously.
 *
 * @author LittleRover
 */
public final class Colour {

	private static int toBound(int i) {
		return Math.max(0, Math.min(255, i));
	}

	// The Static Methods
	private static int toInt(float f) {
		// TODO: Make sure this is the right way to handle this.
		return toBound(Math.round(f * 256f - 0.5f));
	}

	/**
	 * Creates a new white colour.
	 */
	public Colour() {
	}

	/**
	 * Creates a new colour using the provided rgb values. Provided values will be
	 * clamped between 0 and 255 inclusive.
	 *
	 * @param r The Red byte
	 * @param g The Green byte
	 * @param b The Blue byte
	 */
	public Colour(int r, int g, int b) {
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
	public Colour(int r, int g, int b, int a) {
		set(r, g, b, a);
	}

	/**
	 * Creates a new colour using the provided rgb values. Provided values will be
	 * clamped between 0 and 1 inclusive.
	 *
	 * @param r The Red
	 * @param g The Green
	 * @param b The Blue
	 */
	public Colour(float r, float g, float b) {
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
	public Colour(float r, float g, float b, float a) {
		set(toInt(r), toInt(g), toInt(b), toInt(a));
	}
	private int A = 255;
	private int ARGB = -1;
	private int B = 255;
	private int BGRA = -1;
	private int G = 255;
	private int R = 255;
	private int RGBA = -1;

	private void calc() {
		BGRA = (B << 24) | (G << 16) | (R << 8) | A;
		RGBA = (R << 24) | (G << 16) | (B << 8) | A;
		ARGB = (A << 24) | (R << 16) | (G << 8) | B;
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
	 * Returns the colour in ARGB format.
	 *
	 * @return The colour in ARGB format.
	 */
	public int getARGB() {
		return ARGB;
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
	 * Returns the colour in BGRA format.
	 *
	 * @return The colour in BGRA format.
	 */
	public int getBGRA() {
		return BGRA;
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
	 * Returns the Red byte.
	 *
	 * @return The Red byte.
	 */
	public int getR() {
		return R;
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
	 * Sets this colour using the provided rgb values. Provided values will be
	 * clamped between 0 and 255 inclusive.
	 *
	 * @param r The Red byte
	 * @param g The Green byte
	 * @param b The Blue byte
	 *
	 * @return This
	 */
	public Colour set(int r, int g, int b) {
		return set(r, g, b, 255);
	}

	/**
	 * Sets this colour using the provided rgb values. Provided values will be
	 * clamped between 0 and 1 inclusive.
	 *
	 * @param r The Red
	 * @param g The Green
	 * @param b The Blue
	 *
	 * @return This
	 */
	public Colour set(float r, float g, float b) {
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
	 *
	 * @return This
	 */
	public Colour set(float r, float g, float b, float a) {
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
	 *
	 * @return This
	 */
	public Colour set(int r, int g, int b, int a) {
		R = r;
		G = g;
		B = b;
		A = a;
		calc();
		return this;
	}

}
