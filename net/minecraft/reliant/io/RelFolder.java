package net.minecraft.reliant.io;

import java.io.File;

import net.minecraft.reliant.Reliant;

public class RelFolder extends IO {

	private static volatile RelFolder instance;
	private File reliantFolder;
	
	private RelFolder() {
		this.reliantFolder = new File(Reliant.getInstance().getMinecraft().getAppDir("minecraft") + "/reliant0");
	}
	
	public static RelFolder getInstance() {
		RelFolder result = instance;
		if (result == null) {
			synchronized (RelFolder.class) {
				result = instance;
				if (result == null) {
					result = new RelFolder();
					instance = result;
				}
			}
		}
		return result;
	}
	
	@Override
	public void checkForFile() {
		try {
			if (!this.reliantFolder.exists()) {
				System.out.println("Reliant folder not found. Attempting to create directory.");
				this.saveFile();
			} else {
				System.out.println("Reliant folder found.");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println("Could not check for Reliant folder.");
		}
	}

	@Override
	public void saveFile() {
		if (this.reliantFolder.mkdir()) {
			System.out.println("Created Reliant folder.");
		} else {
			System.out.println("Unable to create Reliant folder.");
		}
	}
	
	/**
	 * @return
	 * 		Folder for Reliant
	 */
	public File getReliantFolder() {
		return this.reliantFolder;
	}
	
}
