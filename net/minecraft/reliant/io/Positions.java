package net.minecraft.reliant.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.lwjgl.input.Keyboard;

import net.minecraft.reliant.Reliant;
import net.minecraft.reliant.gui.components.Window;
import net.minecraft.reliant.module.Module;

public class Positions extends IO {

	private static volatile Positions instance;
	private File positionsFile;
	
	private Positions() {
		this.positionsFile = new File(Reliant.getInstance().getRelFolder().getReliantFolder() + "/positions.txt");
	}
	
	public static Positions getInstance() {
		Positions result = instance;
		if (result == null) {
			synchronized (Positions.class) {
				result = instance;
				if (result == null) {
					result = new Positions();
					instance = result;
				}
			}
		}
		return result;
	}
	
	@Override
	public void checkForFile() {
		try {
			if (!this.positionsFile.exists()) {
				System.out.println("Could not find Positions file. Attempting to create.");
				this.saveFile();
				return;
			}
			
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(this.positionsFile));
				String readLine = "";
				while ((readLine = reader.readLine()) != null) {
					String[] split = readLine.split(this.splitString);
					if (split[0].equalsIgnoreCase("chat")) {
						int[] info = new int[] {
								Integer.parseInt(split[1]), 
								Integer.parseInt(split[2]), 
								Integer.parseInt(split[3]), 
								Integer.parseInt(split[4]), 
						};
						Reliant.getInstance().reliantChat.setDragInfo(info);
						System.out.println("Loaded info for chat.");
					} else {
						String windowTitle = split[0];
						int[] info = new int[] {
								Integer.parseInt(split[1]), 
								Integer.parseInt(split[2]), 
								Integer.parseInt(split[3]), 
								Integer.parseInt(split[4]), 
						};
						boolean isOpen = Boolean.parseBoolean(split[5]);
						for (Window window : Reliant.getInstance().reliantGui.loadedWindowList) {
							if (window.getWindowTitle().equalsIgnoreCase(windowTitle)) {
								window.setDragInfo(info);
								window.setOpen(isOpen);
								System.out.println("Loaded info for Window \"" + window.getWindowTitle() + "\"");
							}
						}
					}
				}
				reader.close();
			} catch (Exception exception) {
				exception.printStackTrace();
				System.out.println("Error in loading a Window / Chat position.");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println("Error in loading Positions.");
		}
	}

	@Override
	public void saveFile() {
		try {
			if (!this.positionsFile.exists()) {
				if (this.positionsFile.createNewFile()) {
					System.out.println("Created Positions file.");
				} else {
					System.out.println("Could not create Positions file.");
					return;
				}
			}

			PrintWriter writer;
			try {
				writer = new PrintWriter(new FileWriter(this.positionsFile));
				for (Window window : Reliant.getInstance().reliantGui.loadedWindowList) {
					int[] info = window.getDragInfo();
					writer.println(window.getWindowTitle() + this.splitString 	// split 0
							+ info[0] + this.splitString 						// split 1
							+ info[1] + this.splitString 						// split 2
							+ info[2] + this.splitString 						// split 3
							+ info[3] + this.splitString 						// split 4
							+ window.isOpen()); 								// split 5
					System.out.println("Saved Window \"" + window.getWindowTitle() + "\" info.");
				}
				int[] chatInfo = Reliant.getInstance().reliantChat.getDragInfo();	
				writer.println("chat" + this.splitString	// split 0
						+ chatInfo[0] + this.splitString	// split 1
						+ chatInfo[1] + this.splitString	// split 2
						+ chatInfo[2] + this.splitString	// split 3
						+ chatInfo[3] + this.splitString);	// split 4
				System.out.println("Saved chat info.");
				writer.close();
			} catch (Exception exception) {
				exception.printStackTrace();
				System.out.println("Error in saving a Window / chat position.");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println("Error in saving Positions.");
		} 
	}

	/**
	 * @return
	 * 		File for loading GUI positions.
	 */
	public File getPositionsFile() {
		return this.positionsFile;
	}
	
}
