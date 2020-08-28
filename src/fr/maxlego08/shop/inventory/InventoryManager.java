package fr.maxlego08.shop.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
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
import fr.maxlego08.shop.inventory.inventories.InventoryShopConfig;
import fr.maxlego08.shop.inventory.inventories.InventoryShopConfirm;
import fr.maxlego08.shop.inventory.inventories.InventoryShopSell;
import fr.maxlego08.shop.listener.ListenerAdapter;
import fr.maxlego08.shop.save.Config;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.ZPlugin;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zshop.inventories.InventoryObject;
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
			addInventory(EnumCategory.SELL.getInventoryID(), new InventoryShopSell());
			addInventory(EnumCategory.CONFIRM.getInventoryID(), new InventoryShopConfirm());
			addInventory(EnumCategory.CONFIG.getInventoryID(), new InventoryShopConfig());
		} catch (InventoryAlreadyExistException e) {
			e.printStackTrace();
		}

		plugin.getLog().log("Loading " + inventories.size() + " inventories", LogType.SUCCESS);
	}

	/**
	 * 
	 * @param id
	 * @param inventory
	 * @throws InventoryAlreadyExistException
	 */
	private void addInventory(int id, VInventory inventory) throws InventoryAlreadyExistException {
		if (!inventories.containsKey(id))
			inventories.put(id, inventory.setId(id));
		else
			throw new InventoryAlreadyExistException("Inventory with id " + id + " already exist !");
	}

	/**
	 * 
	 * @param id
	 * @param player
	 * @param page
	 * @param obj
	 * @param objects
	 */
	public void createInventory(int id, Player player, int page, InventoryObject obj, Object... objects) {

		VInventory inventory = getInventory(id);
		if (inventory == null)
			return;
		VInventory clonedInventory = inventory.clone();

		if (clonedInventory == null) {
			player.sendMessage(Lang.prefix + " §cLe clone de l'inventaire est null !");
			return;
		}

		clonedInventory.setPlayer(player);
		clonedInventory.setArgs(objects);
		clonedInventory.setPage(page);
		clonedInventory.setPlugin(plugin);
		clonedInventory.setId(id);
		clonedInventory.setInventoryManager(this);
		clonedInventory.setInventoryObject(obj);

		try {
			if (clonedInventory.openInventory(plugin, player, page, objects)) {
				player.openInventory(clonedInventory.getInventory());
				playerInventories.put(player, clonedInventory);
			} else
				throw new InventoryOpenException(
						"An internal error occurred while opening the inventory with the id " + id);
		} catch (Exception e) {
			player.sendMessage(
					Lang.prefix + " §cAn internal error occurred while opening the inventory with the id " + id);
			if (Config.enableDebug)
				e.printStackTrace();
		}
	}

	public void createInventory(VInventory parent, Player player) {

		if (parent == null) {
			player.sendMessage(Lang.prefix + " §cLe parent est null !");
			return;
		}

		VInventory clonedInventory = parent.clone();
		if (clonedInventory == null) {
			player.sendMessage(Lang.prefix + " §cLe clone de l'inventaire est null !");
			return;
		}

		clonedInventory.setPlayer(player);
		clonedInventory.setId(parent.getId());
		clonedInventory.setArgs(parent.getArgs());
		clonedInventory.setPage(parent.getPage());
		clonedInventory.setPlugin(plugin);
		clonedInventory.setInventoryObject(parent.getInventoryObject());
		clonedInventory.setInventoryManager(this);

		try {
			if (clonedInventory.openInventory(plugin, player, parent.getPage(), parent.getArgs())) {
				player.openInventory(clonedInventory.getInventory());
				playerInventories.put(player, clonedInventory);
			} else
				throw new InventoryOpenException(
						"An internal error occurred while opening the inventory with the id " + parent.getId());
		} catch (Exception e) {
			player.sendMessage(Lang.prefix + " §cAn internal error occurred while opening the inventory with the id "
					+ parent.getId());
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
					&& gui.getGuiName().replace("§", "&").contains(event.getView().getTitle().replace("§", "&"))) {
				event.setCancelled(gui.isDisableClick());
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

	public void update() {
		List<VInventory> inventories = new ArrayList<>(this.playerInventories.values());
		this.playerInventories.clear();
		inventories.forEach(i -> createInventory(i, i.getPlayer()));
	}

	/**
	 * @param id
	 */
	public void updateAllPlayer(int... id) {
		if (id.length == 0) {
		}
		for (int currentId : id)
			updateAllPlayer(currentId);
	}

	/**
	 * @param id
	 */
	public void closeAllPlayer(int... id) {
		if (id.length == 0)
			this.playerInventories.values().forEach(inventory -> inventory.getPlayer().closeInventory());
		else
			for (int currentId : id)
				closeAllPlayer(currentId);
	}

	/**
	 * @param id
	 */
	private void updateAllPlayer(int id) {
		Iterator<VInventory> iterator = this.playerInventories.values().stream().filter(inv -> inv.getId() == id)
				.collect(Collectors.toList()).iterator();
		while (iterator.hasNext()) {
			VInventory inventory = iterator.next();
			Bukkit.getScheduler().runTask(ZPlugin.z(), () -> createInventory(inventory, inventory.getPlayer()));
		}
	}

	/**
	 * @param id
	 */
	private void closeAllPlayer(int id) {
		Iterator<VInventory> iterator = this.playerInventories.values().stream().filter(inv -> inv.getId() == id)
				.collect(Collectors.toList()).iterator();
		while (iterator.hasNext()) {
			VInventory inventory = iterator.next();
			inventory.getPlayer().closeInventory();
		}
	}

}
