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
package com.auroraengine.opengl.viewport;

import com.auroraengine.camera.Camera;
import com.auroraengine.opengl.GLException;
import com.auroraengine.opengl.gui.GUI;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author LittleRover
 */
public abstract class Viewport {

	public Viewport() {
		gui = new GUI(this);
	}
	private volatile Camera camera;
	private final GUI gui;
	private volatile boolean isactive = false;

	private final Lock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();

	public Camera getCamera() {
		return this.camera;
	}

	public abstract int getHeight();

	public abstract int getWidth();

	public abstract int getX();

	public abstract int getY();

	public boolean isActive() {
		return isactive;
	}

	public void render()
					throws GLException {
		setViewport();

		gui.renderOpaque();

		if (camera != null) {
			// TODO: Render out from Camera.
			// camera.render(this);
		}

		gui.renderTranslucent();

		try {
			lock.lock();
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}

	public void setActive(boolean b) {
		this.isactive = true;
	}

	// TODO: Requires proper handling of locks.
	public Camera setCamera(Camera camera) {
		try {
			lock.lock();
			condition.await();
			this.camera = camera;
		} catch (InterruptedException ex) {
		} finally {
			lock.unlock();
		}
		return this.camera;
	}

	public void setViewport() {
		GL11.glViewport(getX(), getY(), getWidth(), getHeight());
	}
}
