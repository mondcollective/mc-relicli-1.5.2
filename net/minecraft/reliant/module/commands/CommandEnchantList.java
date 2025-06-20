package net.minecraft.reliant.module.commands;

import java.util.ArrayList;

import net.minecraft.reliant.event.Marker;
import net.minecraft.reliant.event.events.EventCommand;
import net.minecraft.reliant.module.Command;
import net.minecraft.src.Enchantment;

public class CommandEnchantList extends Command {

	private ArrayList<Object[]> enchantList = new ArrayList<Object[]>();
	
	public CommandEnchantList() {
		super(new String[] {
				"enchantlist", "enlist", "enchlist"
		});
	}

	@Override
	@Marker
	public void onCommand(EventCommand eventCommand) {
		String[] args;
		try {
			args = eventCommand.getArgs();
			if (this.equalsCommand(args[0])) {
				if (this.enchantList.size() < 1) {
					for (Enchantment enchantment : Enchantment.enchantmentsList) {
						if (enchantment == null) {
							continue;
						}
						String enchantName = enchantment.getName().replaceFirst("enchantment.", "");
						int enchantID = enchantment.effectId;
						Object enchantInfo[] = new Object[] {
								enchantName, enchantID
						};
						this.enchantList.add(enchantInfo);
					}
				}
				String printString = "Enchantments: \2473";
				for (int index = 0; index < this.enchantList.size(); index++) {
					Object enchantInfo[] = this.enchantList.get(index);
					String info = (String) enchantInfo[0] + " (" + (Integer) enchantInfo[1] + ")";
					if (index == this.enchantList.size() - 1) {
						printString = printString.concat(info + "\247f.");
					} else {
						printString = printString.concat(info + "\247f, \2473");
					}
				}
				this.print(printString);
				eventCommand.setValid(true);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public String returnHelp() {
		return "Usage: .enchantlist";
	}

}
