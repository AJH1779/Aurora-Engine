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
package com.auroraengine.opengl;

import com.auroraengine.client.ClientException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

// TODO extend GLSharedObject and implement.
/**
 *
 * @author LittleRover
 */
public class GLTexture implements GLObject {

	public GLTexture(int w, int h) {
		this.img = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
	}

	public GLTexture(BufferedImage img) {
		this.img = img;
	}

	public GLTexture(File file) {
		this.file = file;
	}
	private File file;
	private BufferedImage img;
	private boolean save_on_unload = false;

	private int min_filter = GL11.GL_LINEAR, mag_filter = GL11.GL_LINEAR;
	private int wrap_s = GL11.GL_REPEAT, wrap_t = GL11.GL_REPEAT;
	private boolean image_modified = true;

	@Override
	public void load()
					throws ClientException {
		if (file != null) {
			try {
				img = ImageIO.read(file);
			} catch (IOException ex) {
				throw new ClientException(ex);
			}
		}
	}

	@Override
	public void unload()
					throws ClientException {
		if (file != null) {
			if (save_on_unload) {
				try {
					ImageIO.write(img, "png", file);
				} catch (IOException ex) {
					throw new ClientException(ex);
				}
			}
			img = null;
		}
	}

	private int tex_ref = 0;

	@Override
	public void create()
					throws GLException {
		if (tex_ref == 0) {
			tex_ref = GL11.glGenTextures();
			update();
		}
	}

	@Override
	public void update()
					throws GLException {
		if (tex_ref != 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex_ref);

			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
													 min_filter);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
													 mag_filter);

			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrap_s);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrap_t);

			if (image_modified) {
				ByteBuffer bb = ByteBuffer.allocateDirect(4 * img.getWidth() * img
																									.getHeight());
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, img.getWidth(),
													img.getHeight(), 0, GL12.GL_BGRA,
													GL11.GL_UNSIGNED_BYTE, bb);
				// TODO: Redirect images to buffers
			}
		}
	}

	@Override
	public void destroy() {
		if (tex_ref != 0) {
			GL11.glDeleteTextures(tex_ref);
			tex_ref = 0;
		}
	}

	@Override
	public boolean isLoaded() {
		return img == null;
	}

	@Override
	public boolean isCreated() {
		return tex_ref > 0;
	}
}
