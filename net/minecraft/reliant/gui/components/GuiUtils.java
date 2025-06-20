package net.minecraft.reliant.gui.components;

import org.lwjgl.opengl.GL11;

import net.minecraft.reliant.Reliant;
import net.minecraft.src.Gui;
import net.minecraft.src.Tessellator;

/**
 * GuiUtils is a class for rendering 2-dimensional
 * shapes onto the screen.
 * 
 * @author Nahr
 */
public class GuiUtils extends Gui {
	
	private static volatile GuiUtils instance; // Variable for holding the instance of GuiUtils
	
	/**
     * A singleton method used to access the single instance of
     * GuiUtils, it is double-locked and thread-safe.
     *
     * @return instance
     *         Instance of GuiUtils
     */
	public static GuiUtils getInstance() {
		GuiUtils result = instance;
		if (result == null) {
			synchronized (GuiUtils.class) {
				result = instance;
				if (result == null) {
					result = new GuiUtils();
					instance = result;
				}
			}
		}
		return result;
	}
	
	/**
	 * Draws a texture at the specified coordinates
	 * with a specified width.
	 * 
	 * @param texture
	 * 		Location of the texture
	 * @param x
	 * 		X to start drawing at
	 * @param y
	 * 		Y to start drawing at
	 * @param width
	 * 		Width of the texture
	 * @param height
	 * 		Height of the texture
	 * @param u
	 * 		X in the texture to start drawing from
	 * @param v
	 * 		Y in the texture to start drawing from
	 * @param uWidth
	 * 		Width in the texture
	 * @param vHeight
	 * 		Height in the texture
	 */
	public void drawTexturedRectangle(String texture, float x, float y, float width, float height, 
			float u, float v, float uWidth, float vHeight) {
		GL11.glEnable(GL11.GL_BLEND);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, Reliant.getInstance().getMinecraft().renderEngine.getTexture(texture));
        GL11.glColor4f(1f, 1f, 1f, 1f);
        float scale = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, 0, u * scale, (v + vHeight) * scale);
        tessellator.addVertexWithUV(x + width, y + height, 0, (u + uWidth) * scale, (v + vHeight) * scale);
        tessellator.addVertexWithUV(x + width, y, 0, (u + uWidth) * scale, v * scale);
        tessellator.addVertexWithUV(x, y, 0, u * scale, v * scale);
        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
	}
	
	/**
	 * Draws a solid color rectangle at the 
	 * specified coordinates.
	 * 
	 * @param x
	 * 		Left X coordinate
	 * @param y
	 * 		Top Y coordinate
	 * @param x1
	 * 		Right X coordinate
	 * @param y1
	 * 		Bottom Y coordinate
	 * @param color
	 * 		The color of the rectangle, in hexadecimal form
	 */
	public void drawRectangle(float x, float y, float x1, float y1, int color) {
		this.setup2DRender();
		this.glQuadDraw(x, y, x1, y1, this.getARGBfromHex(color));
		this.finish2DRender();
	}
	
	/**
	 * Draws an outlined solid color rectangle
	 * at the specified coordinates.
	 * 
	 * @param x
	 * 		Left X coordinate
	 * @param y
	 * 		Top Y coordinate
	 * @param x1
	 * 		Right X coordinate
	 * @param y1
	 * 		Bottom Y coordinate
	 * @param rectColor
	 * 		The color of the rectangle, in hexadecimal form
	 * @param outlineColor
	 * 		The color of the outline, in hexadecimal form
	 * @param outlineWidth
	 * 		The width of the outline
	 */
	public void drawOutlinedRectangle(float x, float y, float x1, float y1, int rectColor, int outlineColor, int outlineWidth) {
		this.setup2DRender();
		this.glQuadDraw(x, y, x1, y1, this.getARGBfromHex(rectColor));
		this.glLineLoopDraw(x, y, x1, y1, outlineWidth, this.getARGBfromHex(outlineColor));
		this.finish2DRender();
	}
	
	/**
	 * Draws a vertical gradient rectangle at the specified
	 * coordinates with 2 given colors.
	 * 
	 * @param x
	 * 		Left X coordinate
	 * @param y
	 * 		Top Y coordinate
	 * @param x1
	 * 		Right x coordinate
	 * @param y1
	 * 		Bottom Y coordinate
	 * @param colorTop
	 * 		The hexadecimal color to start from, at the top of the rectangle
	 * @param colorBottom
	 * 		The hexadecimal color to finish with, at the bottom of the rectangle
	 */
	public void drawGradientRect(float x, float y, float x1, float y1, int colorTop, int colorBottom) {
		this.setupGradient();
		this.glGradientQuadDraw(x, y, x1, y1, this.getARGBfromHex(colorTop), this.getARGBfromHex(colorBottom), false);
		this.finishGradient();
	}
	
	/**
	 * Draws a horizontal gradient rectangle at the specified
	 * coordinates with 2 given colors.
	 * 
	 * @param x
	 * 		Left X coordinate
	 * @param y
	 * 		Top Y coordinate
	 * @param x1
	 * 		Right X coordinate
	 * @param y1
	 * 		Bottom Y coordinate
	 * @param colorLeft
	 * 		The hexadecimal color to start from, on the left side of the rectangle
	 * @param colorRight
	 * 		The hexadecimal color to finish with, on the right side of the rectangle
	 */
	public void drawHorizGradientRect(float x, float y, float x1, float y1, int colorLeft, int colorRight) {
		this.setupGradient();
		this.glGradientQuadDraw(x, y, x1, y1, this.getARGBfromHex(colorLeft), this.getARGBfromHex(colorRight), true);
		this.finishGradient();
	}
	
	/**
	 * Begins and finishes GL_QUADS at the specified 
	 * coordinates and colors with an ARGB float 
	 * array.
	 * 
	 * @param x
	 * 		Left X coordinate
	 * @param y
	 * 		Top Y coordinate
	 * @param x1
	 * 		Right X coordinate
	 * @param y1
	 * 		Bottom Y coordinate
	 * @param colorArray
	 * 		ARGB float array to color from
	 */
	private void glQuadDraw(float x, float y, float x1, float y1, float[] colorArray) {
		GL11.glBegin(GL11.GL_QUADS);
			this.colorFromFloatArray(colorArray);
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x, y1);
			GL11.glVertex2d(x1, y1);
			GL11.glVertex2d(x1, y);
		GL11.glEnd();	
	}
	
	/**
	 * Begins and finishes GL_LINE_LOOP at the specified
	 * coordinates with a specified line width and colored
	 * from an ARGB float array.
	 * 
	 * @param x
	 * 		Left X coordinate
	 * @param y
	 * 		Top Y coordinate
	 * @param x1
	 * 		Right X coordinate
	 * @param y1
	 * 		Bottom Y coordinate
	 * @param lineWidth
	 * 		The width of the lines
	 * @param colorArray
	 * 		ARGB float array to color from
	 */
	private void glLineLoopDraw(float x, float y, float x1, float y1, float lineWidth, float[] colorArray) {
		GL11.glLineWidth(lineWidth);
		GL11.glBegin(GL11.GL_LINE_LOOP);
			this.colorFromFloatArray(colorArray);
			GL11.glVertex2f(x, y);
			GL11.glVertex2f(x, y1);
			GL11.glVertex2f(x1, y1);
			GL11.glVertex2f(x1, y);
			GL11.glVertex2f(x - (lineWidth == 1 ? 0.5f : 0f), y);
		GL11.glEnd();
	}
	
	/**
	 * Begins and finishes GL_QUADS at the specified 
	 * coordinates and colors with two ARGB float 
	 * array, to be used when drawing a Gradient.
	 * 
	 * @param x
	 * 		Left X coordinate
	 * @param y
	 * 		Top Y coordinate
	 * @param x1
	 * 		Right X coordinate
	 * @param y1
	 * 		Bottom Y coordinate
	 * @param firstColor
	 * 		The first color of the gradient, in form of an ARGB float array
	 * @param secondColor
	 * 		The second color of the gradient, in form of an ARGB float array
	 */
	private void glGradientQuadDraw(float x, float y, float x1, float y1, float[] firstColor, float[] secondColor, boolean horizontal) {
		GL11.glBegin(GL11.GL_QUADS);
			this.colorFromFloatArray(firstColor);
			if (horizontal) {
				GL11.glVertex3f(x, y, 0);
	        	GL11.glVertex3f(x, y1, 0);
				this.colorFromFloatArray(secondColor);
				GL11.glVertex3f(x1, y1, 0);
	        	GL11.glVertex3f(x1, y, 0);
			} else {
				GL11.glVertex3f(x1, y, 0);
	        	GL11.glVertex3f(x, y, 0);
				this.colorFromFloatArray(secondColor);
				GL11.glVertex3f(x, y1, 0);
	        	GL11.glVertex3f(x1, y1, 0);
			}
		GL11.glEnd();
	}
	
	/**
	 * Calls GL11.glColor4f() using an ARGB float array.
	 * 
	 * @param array
	 * 		The float array to color from
	 */
	private void colorFromFloatArray(float[] array) {
		GL11.glColor4f(array[1], array[2], array[3], array[0]);
	}
	
	/**
	 * Enables the GL_BLEND constant and disables
	 * the GL_TEXTURE_2D constant.
	 */
	private void setup2DRender() {
		GL11.glEnable(GL11.GL_BLEND /*3042*/);
        GL11.glDisable(GL11.GL_TEXTURE_2D /*3553*/);
	} 
	
	/**
	 * Disables the GL_BLEND constant and enables
	 * the GL_TEXTURE_2D constant. 
	 */
	private void finish2DRender() {
		GL11.glEnable(GL11.GL_TEXTURE_2D /*3553*/);
        GL11.glDisable(GL11.GL_BLEND /*3042*/);     
	}
	
	/**
	 * Enables and disables the correct GL constants
	 * in order to begin drawing a gradient.
	 */
	private void setupGradient() {
		this.setup2DRender();
		GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
	}
	
	/**
	 * Disables and re-enables the GL constants that
	 * were enabled or disabled when setting up.
	 */
	private void finishGradient() {
		this.finish2DRender();
		GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
	}
	
	/**
	 * Returns a float array containing the 
	 * Alpha, Red, Green, and Blue decimal
	 * equivalents from a hexadecimal color.
	 * 
	 * @param hexColor
	 * 		The desired color, in hexadecimal form
	 * @return
	 * 		float[] {
	 * 			alpha, red, green, blue
	 * 		}
	 */
	public float[] getARGBfromHex(int hexColor) {
		float alpha = (float) (hexColor >> 24 & 255) / 255.0F;
        float red = (float) (hexColor >> 16 & 255) / 255.0F;
        float green = (float) (hexColor >> 8 & 255) / 255.0F;
        float blue = (float) (hexColor & 255) / 255.0F;
		return new float[] {
				alpha, red, green, blue
		};
	}
	
}
