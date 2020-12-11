package fr.maxlego08.shop.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.api.button.Button;
import fr.maxlego08.shop.api.button.buttons.ItemButton;
import fr.maxlego08.shop.api.button.buttons.PlaceholderButton;
import fr.maxlego08.shop.api.enums.InventoryType;
import fr.maxlego08.shop.api.inventory.Inventory;

public class InventoryObject implements Inventory {

	private final String name;
	private final InventoryType type;
	private final int size;
	private final List<Button> buttons;
	private final String fileName;
	private final ItemStack fillItemStack;

	/**
	 * @param name
	 * @param type
	 * @param size
	 * @param buttons
	 */
	public InventoryObject(String name, InventoryType type, int size, List<Button> buttons, String fileName, ItemStack itemStack) {
		super();
		this.name = name;
		this.type = type;
		this.size = size;
		this.buttons = buttons;
		this.fillItemStack = itemStack;
		this.fileName = fileName;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getName(String replace, String newChar) {
		return name.replace(replace, newChar);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Button> List<T> getButtons(Class<T> type) {
		return buttons.stream().filter(e -> type.isAssignableFrom(e.getClass())).map(e -> (T) e)
				.collect(Collectors.toList());
	}

	@Override
	public List<Button> getButtons() {
		return buttons;
	}

	@Override
	public void open(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<PlaceholderButton> sortButtons(int page) {
		List<PlaceholderButton> buttons = new ArrayList<PlaceholderButton>();
		for (Button button : this.buttons) {

			int slot = (button.getSlot() - ((page - 1) * size));

			if ((slot >= 0 && slot < size) || button.isPermament()) {
				button.setTmpSlot(slot);
				buttons.add(button.toButton(PlaceholderButton.class));
			}

		}
		return buttons;
	}

	@Override
	public int getMaxPage() {
		int maxSlot = 0;
		for (Button button : this.buttons)
			maxSlot = Math.max(maxSlot, button.getSlot());
		return (maxSlot / size) + 1;
	}

	@Override
	public InventoryType getType() {
		return type;
	}

	@Override
	public Optional<ItemButton> getItemButton(ItemStack itemStack) {
		return getButtons(ItemButton.class).stream().filter(button -> button.getItemStack().isSimilar(itemStack))
				.findFirst();
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public ItemStack getFillItem() {
		return fillItemStack;
	}

}
