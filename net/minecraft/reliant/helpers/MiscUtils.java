package net.minecraft.reliant.helpers;

import net.minecraft.src.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

public class MiscUtils {

	private static volatile MiscUtils instance;
	
	public static MiscUtils getInstance() {
		MiscUtils result = instance;
		if (result == null) {
			synchronized (MiscUtils.class) {
				result = instance;
				if (result == null) {
					result = new MiscUtils();
					instance = result;
				}
			}
		}
		return result;
	}
	
	/**
	 * Draws "support beams" around an AxisAlignedBB.
	 * 
	 * @param axisAlignedBB
	 * 		The AxisAlignedBB to be drawn around.
	 */
	public void drawSupportBeams(AxisAlignedBB axisAlignedBB) {
		// Left
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		// Right
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		// Top
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		// Bottom
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
		// Front
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		// Back
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
		GL11.glEnd();
	}
	
	/**
	 * Draws the outline of a box around an AxisAlignedBB.
	 * 
	 * @param axisAlignedBB
	 * 		The AxisAlignedBB to be drawn around
	 */
	public void drawOutlinedBoundingBox(AxisAlignedBB axisAlignedBB) {
		GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
			GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
			GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
			GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
			GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
			GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
			GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINES);
			GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
			GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
			GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
			GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
			GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
			GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
			GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
			GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		GL11.glEnd();
	}
	
	/**
	 * Draws a box around an AxisAlignedBB.
	 * 
	 * @param axisAlignedBB
	 * 		The AxisAlignedBB to be drawn around
	 */
	public void drawBoundingBox(AxisAlignedBB axisAlignedBB) {
		GL11.glBegin(GL11.GL_QUADS);
		// Front 
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		// Back
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
		// Left
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
		// Right
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ); 		 
		// Bottom
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);		
		// Top
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
		GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
		GL11.glEnd(); 
	}
	
	/**
	 * Enables and disables the correct GL constants
	 * for rendering 3-dimensionally. 
	 */
	public void start3DGLConstants() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(2929);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(false);
	}
	
	/**
	 * Enables and disabled the correct GL constants
	 * after finishing a 3D rendering. Does not 
	 * re-enable GL_LIGHTING.
	 */
	public void finish3DGLConstants() {
		GL11.glEnable(2929);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	/**
	 * Returns true if a specified array contains a 
	 * specified object.
	 * 
	 * @param objectArray
	 * 		The Array to check from
	 * @param object
	 * 		The Object in question
	 * @return
	 * 		True if <code>objectArray</code> contains <code>object</code>.
	 * 		False otherwise.
	 */
	public boolean doesArrayContain(Object[] objectArray, Object object) {
		for (Object objectInArray : objectArray) {
			if (objectInArray.equals(object)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns a float with <code>numOfPlaces</code> after
	 * the decimal.
	 * 
	 * @param numOfPlaces
	 * 		Number of places after the decimal
	 * @param floatToFormat
	 * 		The float value to format
	 * @return
	 * 		<code>floatToFormat</code> formatted
	 */
	public float formatFloat(int numOfPlaces, float floatToFormat) {
		float fl = floatToFormat;
		int power = (int) Math.pow(10, numOfPlaces);
		int beforeDivision = Math.round(floatToFormat * power);
		return (float) beforeDivision / power;
	}
	
}
