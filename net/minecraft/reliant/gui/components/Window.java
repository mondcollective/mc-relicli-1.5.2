package net.minecraft.reliant.gui.components;

import java.util.ArrayList;

import net.minecraft.reliant.Reliant;

import org.lwjgl.input.Mouse;

/**
 * Window is an Element used in a 
 * clickable GUI. 
 * 
 * @author Nahr
 */
public class Window extends Element {

	private String windowTitle;					// Variable used to hold the Window's Title to display.
	private int dragX, dragY;					// Variables used for holding the current X and Y dragged values.
	private int startX, startY;					// Variables used for holding the current X and Y starting click values. Used for dragging.
	private int clickX, clickY;					// Variables used for holding X and Y click coords. Used for determining if the Window should open / close or drag.
	private float x, y;							// Variables used for holding the X and Y coordinates of the Window.
	private boolean dragging = false;			// Variable used to determine if the Window is dragging or not.
	protected boolean moved = false;			// Variable used to determine if the Window should open / close.
	private boolean open = true;				// Variable used to determine if the Window should display its contents or not.
	private long lastClicked = -1;				// Variable used to hold the last time that the header was clicked.
	private float windowWidth;					// Variable used to hold the width of the Window.
	public ArrayList<Element> componentList = new ArrayList<Element>(); // List to hold the Windows components, such as Buttons and Sliders.
	
	/**
	 * Constructs a new Window.
	 * 
	 * @param title
	 * 		Title string of the Window
	 * @param mainColour
	 * 		Main colour of the Window
	 * @param borderColour
	 * 		Border colour of the Window
	 * @param width
	 * 		The width of the Window
	 */
	public Window(String title, float xPos, float yPos, float width) {
		this.windowTitle = title;
		this.x = xPos;
		this.y = yPos;
		this.windowWidth = width;
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY) {
		if (this.dragging && Mouse.isButtonDown(0)) {
			if (this.clickX != mouseX || this.clickY != mouseY) {
				this.moved = true;
			}
		}
		float length = this.open ? (this.componentList.size() * 15f) + 20.5f : 10.5f;
		this.drawOutlinedRectangle(this.x + this.dragX - 0.5f, this.y + this.dragY - 0.5f,
				this.x + this.windowWidth + this.dragX + 0.5f, this.y + this.dragY + length, 
				0, 0x80101010, 2);
		this.drawGradientRect(this.x + this.dragX, this.y + this.dragY,
				this.x + this.windowWidth + this.dragX, this.y + 10 + this.dragY,
				0xFF7C7971, 0xFF5D5C5B);
		this.getWindowFont().drawString(this.windowTitle, this.x + this.dragX + 4, this.y + this.dragY - 1.5f, 
				NahrFont.TEXT_EMBOSSED_BOTTOM, 0xFF191919, 0xFF99A399);
		if (this.open) {
			float line = this.y + 14.5f;
			this.drawRectangle(this.x + this.dragX, this.y + 10f + this.dragY,
				this.x + this.windowWidth + this.dragX, this.y + length - 0.5f + this.dragY, 0x80303030);
			for (Element element : this.componentList) {
				if (element instanceof Button) {
					Button button = (Button) element;
					button.setYPos(line);
				}
				if (element instanceof Slider) {
					Slider slider = (Slider) element;
					slider.setYPos(line);
				}
				element.drawElement(mouseX, mouseY);
				line += element.getSpacing();
			}
		}
	}

	@Override
	public void mouseClick(int mouseX, int mouseY) {
		if (this.isHovering(mouseX, mouseY)) {
			Window window = this;
			Reliant.getInstance().reliantGui.getWindowList().remove(this);
			Reliant.getInstance().reliantGui.getWindowList().add(0, window);
			for (Element element : this.componentList) {
				if (element.isHovering(mouseX, mouseY)) {
					Reliant.getInstance().reliantGui.setSelected(element);
				}
			}
		}
		if (this.isHoveringOverHeader(mouseX, mouseY)) {
			this.clickX = mouseX;
			this.clickY = mouseY;
			this.dragging = true;
			this.toggleDrag(mouseX, mouseY);
		}
		for (Element element : this.componentList) {
			if (element.isHovering(mouseX, mouseY)) {
				Reliant.getInstance().reliantGui.setSelected(element);
				if (element instanceof Slider) {
					((Slider) element).mouseClick(mouseX, mouseY);
				}
			}
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		if (this.isHoveringOverHeader(mouseX, mouseY) 
				&& (Reliant.getInstance().reliantGui.getWindowList().get(0) == this)
				&& !this.moved) {
			this.open = !this.open;
		}
		this.dragging = false;
		this.moved = false;
	}
	
	@Override
	public boolean isHovering(int mouseX, int mouseY) {
		float length = this.open ? (this.componentList.size() * 15) + 19f : 10;
		boolean hoveringOverWindow = mouseX >= this.x + this.dragX
				&& mouseY >= this.y + this.dragY
				&& mouseX <= this.x + this.windowWidth + this.dragX
				&& mouseY <= this.y + length + this.dragY;
		return hoveringOverWindow;
	}
	
	public void mouseDragged(int mouseX, int mouseY) {
		this.dragX = mouseX - this.startX;
		this.dragY = mouseY - this.startY;
	}
	
	public void toggleDrag(int mouseX, int mouseY) {
		this.startX = mouseX - dragX;
		this.startY = mouseY - dragY;
	}
	
	/**
	 * Returns whether or not the Mouse is hovering
	 * over the header portion of the Window.
	 * 
	 * @param mouseX
	 * 		The X coordinate of the Mouse.
	 * @param mouseY
	 * 		The Y coordinate of the Mouse.
	 * @return
	 * 		True if the Mouse is hovering over the header area
	 */
	private boolean isHoveringOverHeader(int mouseX, int mouseY) {
		return mouseX >= this.x + this.dragX
				&& mouseY >= this.y + this.dragY
				&& mouseX <= this.x + this.windowWidth + this.dragX
				&& mouseY <= this.y + this.dragY + 10;
	}
	
	/**
	 * @return
	 * 		Title of the Window
	 */
	public final String getWindowTitle() {
		return this.windowTitle;
	}
	
	/**
	 * @return
	 * 		True if the Window is extended
	 */
	public final boolean isOpen() {
		return this.open;
	}
	
	/**
	 * @return
	 * 		True if the Window is being dragged
	 */
	public final boolean isDragging() {
		return this.dragging;
	}
	
	/**
	 * @return
	 * 		X coordinate of the Window
	 */
	public float getXPos() {
		return this.x;
	}
	
	/**
	 * @return
	 * 		Y coordinate of the Window
	 */
	public float getYPos() {
		return this.y;
	}
	
	/**
	 * @return
	 * 		The amount the Window has been dragged horizontally
	 */
	public int getDragX() {
		return this.dragX;
	}
	
	/**
	 * @return
	 * 		The amount the Window has been dragged vertically
	 */
	public int getDragY() {
		return this.dragY;
	}

	/**
	 * @return
	 * 		Integer array containing the drag and start info for
	 * 		the Window. [0] - dragX, [1] - dragY, [2] - startX,
	 * 		[3] - startY
	 */
	public int[] getDragInfo() {
		return new int[] {
			this.dragX, this.dragY, this.startX, this.startY
		};
	}
	
	/**
	 * Sets the drag and start info for the Window.
	 * 
	 * @param infoArray
	 * 		Integer array containing the info. [0] - dragX,
	 * 		[1] - dragY, [2] - startX, [3] - startY
	 */
	public final void setDragInfo(int[] infoArray) {
		this.dragX = infoArray[0];
		this.dragY = infoArray[1];
		this.startX = infoArray[2];
		this.startY = infoArray[3];
	}
	
	/**
	 * Sets the open / closed state of the Window.
	 * 
	 * @param flag
	 */
	public final void setOpen(boolean flag) {
		this.open = flag;
	}
	
	@Override
	public void addElement(Element element) {
		if (element instanceof Button) {
			((Button) element).attachedWindow = this;
		}
		if (element instanceof Slider) {
			((Slider) element).attachedWindow = this;
		}
		this.componentList.add(element);
	}

	@Override
	public float getSpacing() {
		return 0;
	}

}