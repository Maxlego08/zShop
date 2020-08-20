package fr.maxlego08.shop.button.buttons;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.button.buttons.ShowButton;
import fr.maxlego08.shop.api.enums.ButtonType;

public class ZShowButton extends ZPermissibleButton implements ShowButton {

	private final List<String> lore;

	/**
	 * @param type
	 * @param itemStack
	 * @param slot
	 * @param permission
	 * @param message
	 * @param elseButton
	 * @param lore
	 */
	public ZShowButton(ButtonType type, ItemStack itemStack, int slot, List<String> lore) {
		super(type, itemStack, slot);
		this.lore = lore;
	}

	@Override
	public List<String> getLore() {
		return lore;
	}

	@Override
	public List<String> getLore(ItemButton button) {
		return lore.stream().map(line -> {
			line = line.replace("%sellPrice%", String.valueOf(button.getSellPrice()));
			line = line.replace("%buyPrice%", String.valueOf(button.getBuyPrice()));
			line = line.replace("%currency%", button.getEconomy().getCurrenry());
			line = line.replace("&", "§");
			return line;
		}).collect(Collectors.toList());
	}

}
