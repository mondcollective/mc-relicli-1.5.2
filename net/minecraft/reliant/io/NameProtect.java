package net.minecraft.reliant.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import net.minecraft.reliant.Reliant;

public class NameProtect extends IO {

	private static volatile NameProtect instance;
	private File nameProtectFile;
	private ArrayList<String[]> nameList = new ArrayList<String[]>();
	private String split = "\t=>\t";
	
	private NameProtect() {
		this.nameProtectFile = new File(Reliant.getInstance().getRelFolder().getReliantFolder() + "/nameprotect.txt");
	}
	
	public static NameProtect getInstance() {
		NameProtect result = instance;
		if (result == null) {
			synchronized (NameProtect.class) {
				result = instance;
				if (result == null) {
					result = new NameProtect();
					instance = result;
				}
			}
		}
		return result;
	}
	
	@Override
	public void checkForFile() {
		try {
			if (!this.nameProtectFile.exists()) {
				System.out.println("Could not find Name Protect file. Attempting to create.");
				this.saveFile();
				return;
			}
			
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(this.nameProtectFile));
				String readLine = "";
				while ((readLine = reader.readLine()) != null) {
					String[] splitRead = readLine.split(this.split);
					this.addName(splitRead[0], splitRead[1]);
					System.out.println("Loaded username \"" + splitRead[0] + "\" with alias \"" + splitRead[1] + ".\"");
				}
				reader.close();
			} catch (Exception exception) {
				exception.printStackTrace();
				System.out.println("Error when trying to read Name Protect information.");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println("Error when trying to load Name Protect information.");
		}
	}

	@Override
	public void saveFile() {
		try {
			if (!this.nameProtectFile.exists()) {
				if (this.nameProtectFile.createNewFile()) {
					System.out.println("Created Name Protect file.");
				} else {
					System.out.println("Could not create Name Protect file.");
					return;
				}
			}
			
			PrintWriter writer;
			try {
				writer = new PrintWriter(new FileWriter(this.nameProtectFile));
				for (String nameArray[] : this.nameList) {
					writer.println(nameArray[0] + this.split + nameArray[1]);
					System.out.println("Saved username \"" + nameArray[0] + "\" with alias \"" + nameArray[1] + ".\"");
				}
				writer.close();
			} catch (Exception exception) {
				exception.printStackTrace();
				System.out.println("Error when trying to write Name Protect information.");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println("Error when trying to save Name Protect information.");
		}
	}
	
	/**
	 * Replaces an alias with a new alias.
	 * 
	 * @param name
	 * 		Original name
	 * @param newAlias
	 * 		New alias to go with the name
	 * @return
	 * 		True if the replace worked. False otherwise.
	 */
	public boolean replaceName(String name, String newAlias) {
		if (this.containsName(name)) {
			for (int index = 0; index < this.nameList.size(); index++) {
				String nameArray[] = this.nameList.get(index);
				if (nameArray[0].equalsIgnoreCase(name)) {
					nameArray[1] = newAlias;
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks if the <code>nameList</code> contains a name.
	 * 
	 * @param name
	 * 		The name to check.
	 * @return
	 * 		True if the <code>nameList</code> contains <code>name</code>,
	 * 		false otherwise.
	 */
	public boolean containsName(String name) {
		for (String nameArray[] : this.nameList) {
			if (nameArray[0].equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Attempts to add a name with an alias to the <code>nameList</code>.
	 * 
	 * @param name
	 * 		The name to add
	 * @param alias
	 * 		The alias for the name
	 * @return
	 * 		True if the <code>nameList</code> doesn't already 
	 * 		contain the name. False if it does.
	 */
	public boolean addName(String name, String alias) {
		if (!this.containsName(name)) {
			this.nameList.add(new String[] {
					name, alias
			});
			return true;
		}
		return false;
	}
	
	/**
	 * Attempts to delete a name from the <code>nameList</code>.
	 * 
	 * @param name
	 * 		Name to remove
	 * @return
	 * 		True if the name was removed. False otherwise.
	 */
	public boolean delName(String name) {
		for (int index = 0; index < this.nameList.size(); index++) {
			String nameArray[] = this.nameList.get(index);
			if (nameArray[0].equalsIgnoreCase(name)) {
				this.nameList.remove(index);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return
	 * 		List of names and aliases, in the form of String Arrays
	 * 		in an ArrayList.
	 */
	public ArrayList<String[]> getNameList() {
		return this.nameList;
	}
	
	/**
	 * @return
	 * 		File containing the NameProtect information.
	 */
	public File getNameProtectFile() {
		return this.nameProtectFile;
	}

}
