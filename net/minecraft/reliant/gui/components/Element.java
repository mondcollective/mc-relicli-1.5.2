package net.minecraft.reliant.gui.components;

import net.minecraft.reliant.Reliant;

/**
 * Element is an abstract class used for
 * Objects in a clickable GUI.
 * 
 * @author Nahr
 */
public abstract class Element extends GuiUtils {
	
	/**
	 * Draws the Element.
	 */
	public abstract void drawElement(int mouseX, int mouseY);
	
	/**
	 * Handles when the Mouse is clicked.
	 * 
	 * @param mouseX
	 * 		The X coordinate of the Mouse.
	 * @param mouseY
	 * 		The Y coordinate of the Mouse.
	 */
	public abstract void mouseClick(int mouseX, int mouseY);
	
	/**
	 * Handles when the Mouse is released.
	 * 
	 * @param mouseX
	 * 		The X coordinate of the Mouse.
	 * @param mouseY
	 * 		The Y coordinate of the Mouse.
	 */
	public abstract void mouseReleased(int mouseX, int mouseY);
	
	/**
	 * Returns true if the Mouse is hovering over the Element.
	 * 
	 * @param mouseX
	 * 		The X coordinate of the Mouse.
	 * @param mouseY
	 * 		The Y coordinate of the Mouse.
	 * @return
	 * 		True if hovering over the Element, false otherwise.
	 */
	public abstract boolean isHovering(int mouseX, int mouseY);
	
	/**
	 * Adds an Element, probably a Button, to another Element,
	 * probably a Window.
	 * 
	 * @param element
	 * 		The Element to add.
	 */
	public abstract void addElement(Element element);
	
	/**
	 * @return
	 * 		The spacing that should be in-between each Element
	 */
	public abstract float getSpacing();
	
	/**
	 * @return
	 * 		NahrFont for Windows
	 */
	protected final NahrFont getWindowFont() {
		return Reliant.getInstance().getWindowFont();
	}
	
	/**
	 * @return
	 * 		NahrFont for Buttons
	 */
	protected final NahrFont getButtonFont() {
		return Reliant.getInstance().getButtonFont();
	}
	
}
