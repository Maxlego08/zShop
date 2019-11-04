package fr.maxlego08.template.inventory.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.template.Template;
import fr.maxlego08.template.inventory.ItemButton;
import fr.maxlego08.template.inventory.VInventory;

public class InventoryTest extends VInventory {

	@Override
	public boolean openInventory(Template main, Player player, int page, Object... args) throws Exception {

		createInventory("§eTest ! §a" + page);

		addItem(1, new ItemButton(Material.DIAMOND_AXE, "§aUne §2h§da§2che §a!")
				.setClick(event -> createInventory(1, player, page + 1)));

		return true;
	}

	@Override
	protected void onClose(InventoryCloseEvent event, Template plugin, Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDrag(InventoryDragEvent event, Template plugin, Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public VInventory clone() {
		return new InventoryTest();
	}

}
