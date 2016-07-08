/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auroraengine.input;

import com.auroraengine.client.ClientException;

/**
 * A general controller interface.
 * TODO: Improve the mouse control scheme and make better referencing stuff.
 * @author Arthur
 */
public interface Controller {
	/**
	 * Creates the controller.
	 * @throws ClientException
	 */
	public void initialise() throws ClientException;
	/**
	 * Updates the state of this controller to reflect the current state of the
	 * input.
	 * @throws ClientException
	 */
	public void update() throws ClientException;
	/**
	 * Destroys the controller.
	 */
	public void shutdown();

	/**
	 * Returns true if the mouse is held by the window. Required for bounding
	 * the mouse but messes with the touch screen aspect. Should generally be
	 * manually toggleable.
	 * @return
	 */
	public boolean isMouseGrabbed();
	/**
	 * Sets the state of the mouse being grabbed.
	 * @param grab
	 */
	public void setMouseGrabbed(boolean grab);

	/**
	 * Gets the X position of the mouse on the screen. If the mouse is not
	 * grabbed, the position given is the actual mouse position relative to the
	 * window top left corner in negative coordinates. If the mouse is grabbed,
	 * the coordinates are custom based on the mouse control method being
	 * employed.
	 * @return
	 */
	public int getX();
	/**
	 * Gets the Y position of the mouse on the screen. If the mouse is not
	 * grabbed, the position given is the actual mouse position relative to the
	 * window top left corner in negative coordinates. If the mouse is grabbed,
	 * the coordinates are custom based on the mouse control method being
	 * employed.
	 * @return
	 */
	public int getY();
	/**
	 * Returns the actual X position of the mouse on the screen.
	 * @return
	 */
	public int getAbsX();
	/**
	 * Returns the actual Y position of the mouse on the screen.
	 * @return
	 */
	public int getAbsY();
	/**
	 * Returns the change in the X position of the mouse on the screen since the
	 * last update.
	 * @return
	 */
	public int getDX();
	/**
	 * Returns the change in the Y position of the mouse on the screen since the
	 * last update.
	 * @return
	 */
	public int getDY();
	/**
	 * Returns the change in the wheel position since the last update.
	 * @return
	 */
	public int getDW(); // Scroll wheel amount
	/**
	 * Returns the characters pressed by the keyboard in the previous frame in
	 * order. Useful for filling input fields.
	 * @return
	 */
	public char[] getRecentKeyChars();

	/**
	 * Creates a key configuration under a specified key name. Note that entering
	 * a duplicate name will fail, returning false. Whether the name already
	 * exists can be checked with <code>referenceExists()</code> and removed with
	 * <code>deleteReference</code>.
	 * TODO: Introduce a way of returning references, probably by making the
	 * bindings an object instead.
	 * @param ref The name of the key combination
	 * @param mouse The mouse keys pressed.
	 * @param keys The keyboard keys pressed.
	 * @return True if created
	 */
	public boolean createReference(String ref, int[] mouse, int[] keys);
	/**
	 * Returns true if the name corresponds to an existing key configuration.
	 * @param ref
	 * @return
	 */
	public boolean referenceExists(String ref);
	/**
	 * Returns true if the name no longer corresponds to a key configuration,
	 * whether by removing or if it never existed in the first place.
	 * @param ref
	 * @return
	 */
	public boolean deleteReference(String ref);

	/**
	 * Returns true if at least one of the buttons has been clicked. That is,
	 * the state has changed from unpressed in the previous update to pressed
	 * this update.
	 * @param ref
	 * @return
	 */
	public boolean onClick(String ref);
	/**
	 * Returns true if all the buttons are pressed with at least one of them
	 * having been clicked this update. This is equivalent to the first instance
	 * of the key combination being held.
	 * @param ref
	 * @return
	 */
	public default boolean onClickAll(String ref) {
		return onClick(ref) && onPressAll(ref);
	}
	/**
	 * Returns true if at least one of the buttons is pressed down.
	 * @param ref
	 * @return
	 */
	public boolean onPress(String ref);
	/**
	 * Returns true if all the buttons are pressed down.
	 * @param ref
	 * @return
	 */
	public boolean onPressAll(String ref);
	/**
	 * Returns true if one of the buttons have been released. That is, the state
	 * has changed from unpressed in the previous update to pressed this update.
	 * TODO: Define this better.
	 * @param ref
	 * @return
	 */
	public boolean onRelease(String ref);
	/**
	 * Returns true if all of the buttons have been released.
	 * @param ref
	 * @return
	 */
	public boolean onReleaseAll(String ref);
}