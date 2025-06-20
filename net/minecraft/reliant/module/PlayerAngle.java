package net.minecraft.reliant.module;

import net.minecraft.reliant.event.Priority;

/**
 * PlayerAngle is an Object used when a Module needs
 * the Player to look or turn a certain angle.
 * 
 * @author Nahr
 */
public class PlayerAngle {

	private boolean smoothLook = false;
	private Priority priority;
	private float yaw;
	private float pitch;
	private float delta;
	
	/**
	 * Instantiates a new PlayerAngle with smooth 
	 * turning.
	 * 
	 * @param yaw
	 * 		Desired Yaw
	 * @param pitch
	 * 		Desired Pitch
	 * @param delta
	 * 		Amount to increment when turning
	 * @param priority
	 * 		Priority of the PlayerAngle
	 */
	public PlayerAngle(float yaw, float pitch, float delta, Priority priority) {
		this.smoothLook = true;
		this.yaw = yaw;
		this.pitch = pitch;
		this.delta = delta;
		this.priority = priority;
	}
	
	/**
	 * Instantiates a new PlayerAngle that will
	 * automatically snap to the desired angles.
	 * 
	 * @param yaw
	 * 		Desired Yaw
	 * @param pitch
	 * 		Desired Pitch
	 * @param priority
	 * 		Priority of the PlayerAngle
	 */
	public PlayerAngle(float yaw, float pitch, Priority priority) {
		this(yaw, pitch, 180, priority);
	}
	
	/**
	 * @return
	 * 		Should the transition be smoothed (and use delta)
	 */
	public boolean shouldSmooth() {
		return this.smoothLook;
	}
	
	/**
	 * @return
	 * 		Array of the angles, [0] - yaw, [1] - pitch
	 */
	public float[] getAngles() {
		return new float[] {
				yaw, pitch
		};
	}
	
	/**
	 * @return
	 * 		Delta - the amount to increment when smoothly transitioning
	 */
	public float getDelta() {
		return delta;
	}
	
	/**
	 * @return
	 * 		The Priority of the PlayerAngle
	 */
	public Priority getPriority() {
		return this.priority;
	}
	
}
