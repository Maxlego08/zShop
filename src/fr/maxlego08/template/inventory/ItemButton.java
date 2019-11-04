package fr.maxlego08.template.inventory;

import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.template.zcore.utils.builder.ItemBuilder;

public class ItemButton {

	private final ItemStack displayItem;
	private Consumer<InventoryClickEvent> onClick;

	public ItemButton(ItemStack displayItem) {
		super();
		this.displayItem = displayItem;
	}

	public ItemButton(Material material, String name, String... lore) {
		this(ItemBuilder.getCreatedItemWithLore(material, 1, name, lore));
	}
	
	public ItemButton(Material material) {
		this(ItemBuilder.getCreatedItemWithLore(material, 1, null));
	}
	
	public ItemButton(Material material, String name) {
		this(ItemBuilder.getCreatedItemWithLore(material, 1, name));
	}

	public ItemButton setClick(Consumer<InventoryClickEvent> onClick) {
		this.onClick = onClick;
		return this;
	}

	public ItemStack getDisplayItem() {
		return displayItem;
	}

	public void onClick(InventoryClickEvent event) {
		if (onClick != null)
			onClick.accept(event);
	}

}
