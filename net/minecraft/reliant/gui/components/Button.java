package net.minecraft.reliant.gui.components;

import net.minecraft.reliant.Reliant;

/**
 * Button is an Element used in a 
 * clickable GUI.
 * 
 * @author Nahr
 */
public class Button extends Element {

	private String buttonTitle;					// Variable used to hold the Button's Title to display.
	public Window attachedWindow;				// Window Element to attach the Button to. Used for determining where to draw the Button.
	private int dragX, dragY;					// Variables used when the Window is dragged, and the button needs to be, too.
	private float x, width;						// Variables used to draw the Button and to handle the hovering.
	public int buttonID;						// Variable used when the Button is released.
	private float yPos = -666;					// Variable used to draw the Button at a certain Y coordinate.
	protected boolean state = false;			// Variable used when drawing the Button.
	private final int GREY = 0,
					GREY_HOVER = 1,
					GREY_PRESS = 2,
					BLUE = 3,
					BLUE_HOVER = 4,
					BLUE_PRESS = 5;
	
	public Button(String title, int id) {
		this.buttonTitle = title;
		this.buttonID = id;
		this.width = 90f;
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY) {
		boolean otherPressed = Reliant.getInstance().reliantGui.getSelected() != null
				&& Reliant.getInstance().reliantGui.getSelected() != this;
		boolean isSelected = Reliant.getInstance().reliantGui.getSelected() == this;
		this.x = this.attachedWindow.getXPos() + 4f;
		this.dragX = this.attachedWindow.getDragX();
		this.dragY = this.attachedWindow.getDragY();
		if (this.yPos == -666) {
			return;
		}
		if (this.state) {
			if (this.isHovering(mouseX, mouseY) && !otherPressed && !isSelected) {
				this.drawButton(this.BLUE_HOVER, this.x + this.dragX, this.yPos + this.dragY);
			} else if (this.isHovering(mouseX, mouseY) && isSelected) {
				this.drawButton(this.BLUE_PRESS, this.x + this.dragX, this.yPos + this.dragY);
			} else {
				this.drawButton(this.BLUE, this.x + this.dragX, this.yPos + this.dragY);
			}
		} else {
			if (this.isHovering(mouseX, mouseY) && !otherPressed && !isSelected) {
				this.drawButton(this.GREY_HOVER, this.x + this.dragX, this.yPos + this.dragY);
			} else if (this.isHovering(mouseX, mouseY) && isSelected) {
				this.drawButton(this.GREY_PRESS, this.x + this.dragX, this.yPos + this.dragY);
			} else {
				this.drawButton(this.GREY, this.x + this.dragX, this.yPos + this.dragY);
			}
		}
		this.getButtonFont().drawString(this.buttonTitle, this.x + 26.5f + this.dragX, this.yPos - 0.5f + this.dragY,
				NahrFont.TEXT_EMBOSSED_BOTTOM, this.state ? 0xFFC0C0C0 : 0xFF808080, 0xFF1F1F1F);
	}

	@Override
	public void mouseClick(int mouseX, int mouseY) {		
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
	}

	@Override
	public boolean isHovering(int mouseX, int mouseY) {
		return mouseX >= this.x + this.dragX
				&& mouseY >= this.yPos + this.dragY
				&& mouseX <= this.x + this.width + this.dragX
				&& mouseY <= this.yPos + 12 + this.dragY
				&& this.attachedWindow.isOpen();
	}
	
	/**
	 * Sets the Y position of the Button.
	 * 
	 * @param yPos
	 */
	public void setYPos(float yPos) {
		this.yPos = yPos;
	}
	
	@Override
	public void addElement(Element element) {		
	}
	
	/**
	 * Draws a textured Button at the coordinates.
	 * 
	 * @param x
	 * @param y
	 */
	private void drawButton(int type, float x, float y) {
		float height = 12.5f;
		switch (type) {
			case 0: // Grey Normal
	    		this.drawTexturedRectangle("/GuiRedux.png", x, y, 25, height, 1, 1, 50, 25);
	            break;
	            
	    	case 1: // Grey Hover
	    		this.drawTexturedRectangle("/GuiRedux.png", x, y, 25, height, 51, 1, 50, 25);
	            break;
	            
	    	case 2: // Grey Pressed
	    		this.drawTexturedRectangle("/GuiRedux.png", x, y, 25, height, 101, 1, 50, 25);
	            break;
	            
	    	case 3: // Blue Normal
	    		this.drawTexturedRectangle("/GuiRedux.png", x, y, 25, height, 1, 27, 50, 25);
	    		break;
	    		
	    	case 4: // Blue Hover
	    		this.drawTexturedRectangle("/GuiRedux.png", x, y, 25, height, 51, 27, 50, 25);
	    		break;
	    		
	    	case 5: // Blue Pressed
	    		this.drawTexturedRectangle("/GuiRedux.png", x, y, 25, height, 101, 27, 50, 25);
	    		break;	
	    		
	    	default: // Grey Normal
	    		this.drawTexturedRectangle("/GuiRedux.png", x, y, 25, height, 1, 1, 50, 25);
	    		break;
		}
	}

	@Override
	public float getSpacing() {
		return 15;
	}

}
