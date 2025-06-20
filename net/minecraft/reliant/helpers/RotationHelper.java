package net.minecraft.reliant.helpers;

import java.util.ArrayList;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.event.Listener;
import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.Priority;
import net.minecraft.reliant.event.events.EventPostUpdate;
import net.minecraft.reliant.event.events.EventUpdate;
import net.minecraft.reliant.module.PlayerAngle;
import net.minecraft.src.Entity;

/**
 * RotationHelper is a Helper class for assisting the 
 * Player rotate to meet certain angles. 
 * 
 * @author Nahr
 */
public class RotationHelper implements Listener {

	private static volatile RotationHelper instance;
	private ArrayList<PlayerAngle> angleQueue = new ArrayList<PlayerAngle>();
	private PlayerAngle currentPlayerAngle;
	private float fakeYaw, fakePitch;
	
	private RotationHelper() {
		Reliant.getInstance().getEventManager().addListener(this);
	}
	
	public static RotationHelper getInstance() {
		RotationHelper result = instance;
		if (result == null) {
			synchronized (RotationHelper.class) {
				result = instance;
				if (result == null) {
					result = new RotationHelper();
					instance = result;
				}
			}
		}
		return result;
	}
	
	/**
	 * Iterates through the queue of PlayerAngle's and
	 * selects the first one added with the highest 
	 * priority. Updates the Player's angles based on
	 * that PlayerAngle for one tick, then removes the
	 * PlayerAngle and repeats the process.
	 * 
	 * @param eventUpdate
	 * 		An Event parameter
	 */
	@Marker(eventPriority = Priority.LOW)
	public void onUpdate(EventUpdate eventUpdate) {
		if (this.angleQueue.size() > 0) {
			for (Priority priority : Priority.values()) {
				for (int index = 0; index < this.angleQueue.size(); index++) {
					PlayerAngle playerAngle = this.angleQueue.get(index);
					if (priority.equals(playerAngle.getPriority())) {
						this.currentPlayerAngle = playerAngle;
					}
				}
			}
		}
		if (this.currentPlayerAngle != null) {
			this.fakeYaw = this.updateRotation(this.fakeYaw, this.currentPlayerAngle.getAngles()[0], this.currentPlayerAngle.getDelta());
			this.fakePitch = this.updateRotation(this.fakePitch, this.currentPlayerAngle.getAngles()[1], this.currentPlayerAngle.getDelta());
			this.setYaw(this.fakeYaw);
			this.setPitch(this.fakePitch);
			int index = this.angleQueue.indexOf(this.currentPlayerAngle);
			this.currentPlayerAngle = null;
			this.angleQueue.remove(index);
		} else {
			this.fakeYaw = this.getYaw();
			this.fakePitch = this.getPitch();
		}
	}
	
	/**
	 * Returns true if the Player is very close to aiming
	 * directly at the specified angles.
	 * 
	 * @param yaw
	 * 		Yaw to be aiming at
	 * @param pitch
	 * 		Pitch to be aiming at
	 * @return
	 * 		Yaw and Pitch are about equal to the Player's rotations
	 */
	public boolean isAiming(float yaw, float pitch) {
		yaw = this.wrapAngleTo180(yaw);
		pitch = this.wrapAngleTo180(pitch);
		float curYaw = this.wrapAngleTo180(this.fakeYaw);
		float curPitch = this.wrapAngleTo180(this.fakePitch);
		float yawDiff = Math.abs(yaw - curYaw);
		float pitchDiff = Math.abs(pitch - curPitch);
		return yawDiff <= 20f && pitchDiff <= 20f;
	}
	
	/**
	 * Gets the perfect angles for aiming at an Entity.
	 * 
	 * @param entity
	 * 		The Entity to aim at
	 * @return
	 * 		Float array of the perfect angles. [0] - yaw, [1] - pitch
	 */
	public float[] getPerfectAngles(Entity entity) {
		float xDiff = (float) (entity.posX - Reliant.getInstance().getPlayer().posX);
		float yDiff = (float) (entity.posY + entity.height + entity.getYOffset() - Reliant.getInstance().getPlayer().posY);
		float zDiff = (float) (entity.posZ - Reliant.getInstance().getPlayer().posZ);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180f / Math.PI - 90f); 
        float pitch = (float) -Math.toDegrees(Math.atan(yDiff / Math.sqrt(zDiff * zDiff + xDiff * xDiff)));
        return new float[] {
        		yaw, pitch
        };
	}

	/**
	 * Sets the player yaw.
	 * 
	 * @param yaw
	 */
	public void setYaw(float yaw) {
		Reliant.getInstance().getPlayer().rotationYaw = yaw;
		Reliant.getInstance().getPlayer().rotationYawHead = yaw;
	}
	
	/**
	 * Sets the player pitch.
	 * 
	 * @param pitch
	 */
	public void setPitch(float pitch) {
		Reliant.getInstance().getPlayer().rotationPitch = pitch;
	}
	
	/**
	 * @return
	 * 		Yaw rotation of the player (horizontal)
	 */
	public float getYaw() {
		return Reliant.getInstance().getPlayer().rotationYaw;
	}
	
	/**
	 * @return
	 * 		Pitch rotation of the player (vertical)
	 */
	public float getPitch() {
		return Reliant.getInstance().getPlayer().rotationPitch;
	}
	
	/**
	 * "Smoothly" updates the rotation by a specified increment.
	 * 
	 * @param curRotation
	 * 		Current rotation
	 * @param newRotation
	 * 		Desired rotation
	 * @param delta
	 * 		Amount to increment the angle by
	 * @return
	 * 		Current rotation + amount to increment by
	 */
	public float updateRotation(float curRotation, float newRotation, float delta) {
		float angleDiff = this.wrapAngleTo180(newRotation - curRotation);
		if (angleDiff > delta) {
			angleDiff = delta;
		}
		if (angleDiff < -delta) {
			angleDiff = -delta;
		}
		return curRotation + angleDiff;
	}
	
	/**
	 * Wraps an angle to less than 180 degrees 
	 * and above or equal to -180 degrees.
	 * 
	 * @param angle
	 * 		The angle to wrap
	 * @return
	 * 		An angle less than 180, but >= -180 degrees.
	 */
	private float wrapAngleTo180(float angle) {
		angle %= 360f;
		if (angle >= 180f) {
			angle -= 360f;
		}
		if (angle < -180f) {
			angle += 360f;
		}
		return angle;
	}
	
	/**
	 * Adds a PlayerAngle to the queue.
	 * 
	 * @param playerAngle
	 */
	public boolean addToQueue(PlayerAngle playerAngle) {
		return this.angleQueue.add(playerAngle);
	}
	
	/**
	 * @return
	 * 		Queue of PlayerAngles
	 */
	public ArrayList<PlayerAngle> getAngleQueue() {
		return this.angleQueue;
	}
	
}
