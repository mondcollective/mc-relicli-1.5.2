package net.minecraft.reliant.gui.components;

import org.lwjgl.input.Mouse;

public class WindowValues extends Window {

	public WindowValues(float xPos, float yPos) {
		super("Value config", xPos, yPos, 100f);
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY) {
		if (this.isDragging() && Mouse.isButtonDown(0)) {
			if (this.getDragInfo()[2] != mouseX || this.getDragInfo()[3] != mouseY) {
				this.moved = true;
			}
		}
		float length = this.isOpen() ? (this.componentList.size() * 20f) + 25.5f : 10.5f;
		this.drawOutlinedRectangle(this.getXPos() + this.getDragX() - 0.5f, this.getYPos() + this.getDragY() - 0.5f,
				this.getXPos() + 100f + this.getDragX() + 0.5f, this.getYPos() + this.getDragY() + length, 
				0, 0x80101010, 2);
		this.drawGradientRect(this.getXPos() + this.getDragX(), this.getYPos() + this.getDragY(),
				this.getXPos() + 100f + this.getDragX(), this.getYPos() + 10 + this.getDragY(),
				0xFF7C7971, 0xFF5D5C5B);
		this.getWindowFont().drawString(this.getWindowTitle(), this.getXPos() + this.getDragX() + 4, this.getYPos() + this.getDragY() - 1.5f, 
				NahrFont.TEXT_EMBOSSED_BOTTOM, 0xFF191919, 0xFF99A399);
		if (this.isOpen()) {
			float line = this.getYPos() + 14f;
			this.drawRectangle(this.getXPos() + this.getDragX(), this.getYPos() + 10f + this.getDragY(),
				this.getXPos() + 100f + this.getDragX(), this.getYPos() + length - 0.5f + this.getDragY(), 0x80303030);
			for (Element element : this.componentList) {
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
	public boolean isHovering(int mouseX, int mouseY) {
		float length = this.isOpen() ? (this.componentList.size() * 20) + 28f : 10;
		boolean hoveringOverWindow = mouseX >= this.getXPos() + this.getDragX()
				&& mouseY >= this.getYPos() + this.getDragY()
				&& mouseX <= this.getXPos() + 100f + this.getDragX()
				&& mouseY <= length + this.getDragY();
		return hoveringOverWindow;
	}


}
