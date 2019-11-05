package fr.maxlego08.shop.inventory;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.exceptions.InventoryAlreadyExistException;
import fr.maxlego08.shop.exceptions.InventoryOpenException;
import fr.maxlego08.shop.inventory.inventories.InventoryShop;
import fr.maxlego08.shop.inventory.inventories.InventoryShopBuy;
import fr.maxlego08.shop.inventory.inventories.InventoryShopCategory;
import fr.maxlego08.shop.listener.ListenerAdapter;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zshop.utils.EnumCategory;

public class InventoryManager extends ListenerAdapter {

	private final ZShop plugin;
	private Map<Integer, VInventory> inventories = new HashMap<>();
	private Map<Player, VInventory> playerInventories = new HashMap<>();

	public InventoryManager(ZShop plugin) {
		this.plugin = plugin;

		try {
			addInventory(EnumCategory.DEFAULT.getInventoryID(), new InventoryShop());
			addInventory(EnumCategory.SHOP.getInventoryID(), new InventoryShopCategory());
			addInventory(EnumCategory.BUY.getInventoryID(), new InventoryShopBuy());
		} catch (InventoryAlreadyExistException e) {
			e.printStackTrace();
		}
		
		plugin.getLog().log("Loading " + inventories.size() + " inventories", LogType.SUCCESS);
	}

	private void addInventory(int id, VInventory inventory) throws InventoryAlreadyExistException {
		if (!inventories.containsKey(id))
			inventories.put(id, inventory);
		else
			throw new InventoryAlreadyExistException("Inventory with id " + id + " already exist !");
	}

	public void createInventory(int id, Player player, int page, Object... objects) {
		VInventory inventory = getInventory(id);
		if (inventory == null)
			return;
		VInventory clonedInventory = inventory.clone();
		clonedInventory.setPlayer(player);
		clonedInventory.setArgs(objects);
		clonedInventory.setPage(page);
		clonedInventory.setPlugin(plugin);
		try {
			if (clonedInventory.openInventory(plugin, player, page, objects)) {
				player.openInventory(clonedInventory.getInventory());
				playerInventories.put(player, clonedInventory);
			} else
				throw new InventoryOpenException(
						"An internal error occurred while opening the inventory with the id " + id);
		} catch (Exception e) {
			player.sendMessage(
					Lang.prefix + " An internal error occurred while opening the inventory with the id " + id);
			e.printStackTrace();
		}

	}

	@Override
	protected void onInventoryClick(InventoryClickEvent event, Player player) {
		if (event.getClickedInventory() == null)
			return;
		if (event.getWhoClicked() instanceof Player) {
			if (!exist(player))
				return;
			VInventory gui = playerInventories.get(player);
			if (gui.getGuiName() == null || gui.getGuiName().length() == 0) {
				Logger.info("An error has occurred with the menu ! " + gui.getClass().getName());
				return;
			}
			if (event.getView() != null && gui.getPlayer().equals(player)
					&& event.getView().getTitle().equals(gui.getGuiName())) {
				event.setCancelled(true);
				ItemButton button = gui.getItems().getOrDefault(event.getSlot(), null);
				if (button != null)
					button.onClick(event);
			}
		}
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent event, Player player) {
		if (!exist(player))
			return;
		VInventory inventory = playerInventories.get(player);
		remove(player);
		inventory.onClose(event, plugin, player);
	}

	@Override
	protected void onInventoryDrag(InventoryDragEvent event, Player player) {
		if (event.getWhoClicked() instanceof Player) {
			if (!exist(player))
				return;
			playerInventories.get(player).onDrag(event, plugin, player);
		}
	}

	public boolean exist(Player player) {
		return playerInventories.containsKey(player);
	}

	public void remove(Player player) {
		if (playerInventories.containsKey(player))
			playerInventories.remove(player);
	}

	private VInventory getInventory(int id) {
		return inventories.getOrDefault(id, null);
	}

}
