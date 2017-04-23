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
package com.auroraengine.utils;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A special supplier that iterates through the list of objects, allowing for
 *
 * @author LittleRover
 * @param <T>
 */
public class IterativeSupplier<T> implements Supplier<T> {
	public IterativeSupplier(List<T> p_list) {
		this.list = p_list;
		this.iterator = list.iterator();
	}
	private Iterator<T> iterator;
	private final List<T> list;

	@Override
	public T get() {
		if (iterator.hasNext()) {
			return iterator.next();
		} else {
			iterator = list.iterator();
			return null;
		}
	}
}
