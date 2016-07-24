/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.opengl.viewport;

import com.auroraengine.camera.Camera;
import com.auroraengine.opengl.GLException;
import com.auroraengine.opengl.gui.GUI;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author ajh17
 */
public abstract class Viewport {

	public Viewport() {
		gui = new GUI(this);
	}
	private final GUI gui;
	private volatile Camera camera;
	private volatile boolean isactive = false;

	private final Lock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();

	public void setActive(boolean b) {
		this.isactive = true;
	}

	public boolean isActive() {
		return isactive;
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

	public Camera getCamera() {
		return this.camera;
	}

	public void render() throws GLException {
		setViewport();

		gui.renderOpaque();

		if(camera != null) {
			// TODO: Render out from Camera.
			camera.render(this);
		}

		gui.renderTranslucent();

		try {
			lock.lock();
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}

	public abstract int getX();

	public abstract int getY();

	public abstract int getWidth();

	public abstract int getHeight();

	public void setViewport() {
		GL11.glViewport(getX(), getY(), getWidth(), getHeight());
	}
}
