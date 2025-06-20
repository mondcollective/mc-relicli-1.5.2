package net.minecraft.reliant.io;

public abstract class IO {

	public static final String splitString = " : ";
	
	/**
	 * Checks for the IO's file. 
	 */
	public abstract void checkForFile();
	
	/**
	 * Saves the IO's file.
	 */
	public abstract void saveFile();
	
}
