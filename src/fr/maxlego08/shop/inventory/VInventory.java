package fr.maxlego08.shop.inventory;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zcore.utils.ZUtils;

public abstract class VInventory extends ZUtils {

	private ZShop plugin;
	private Map<Integer, ItemButton> items = new HashMap<Integer, ItemButton>();
	private Player player;
	private int page;
	private Object[] args;
	private Inventory inventory;
	private String guiName;

	protected VInventory createInventory(String name) {
		return createInventory(name, 54);
	}

	protected VInventory createInventory(String name, int size) {
		guiName = name;
		if (name.length() > 32)
			Logger.getLogger().log("The name of the menu is over 32 characters!", LogType.ERROR);
		this.inventory = Bukkit.createInventory(null, size, name);
		return this;
	}

	public void addItem(int slot, ItemButton item) {
		this.items.put(slot, item);
		this.inventory.setItem(slot, item.getDisplayItem());
	}

	public void removeItem(int slot) {
		this.items.remove(slot);
	}

	public void clearItem() {
		this.items.clear();
	}

	/**
	 * @return the items
	 */
	public Map<Integer, ItemButton> getItems() {
		return items;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @return the args
	 */
	public Object[] getArgs() {
		return args;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(Map<Integer, ItemButton> items) {
		this.items = items;
	}

	/**
	 * @param player
	 *            the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @param page
	 *            the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(Object[] args) {
		this.args = args;
	}

	/**
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * @return the guiName
	 */
	public String getGuiName() {
		return guiName;
	}

	public void setPlugin(ZShop plugin) {
		this.plugin = plugin;
	}
	
	public void createInventory(int id, Player player, int page, Object... args){
		plugin.getInventoryManager().createInventory(id, player, page, args);
	}
	
	public abstract boolean openInventory(ZShop main, Player player, int page, Object... args) throws Exception;

	protected abstract void onClose(InventoryCloseEvent event, ZShop plugin, Player player);

	protected abstract void onDrag(InventoryDragEvent event, ZShop plugin, Player player);
	
	public abstract VInventory clone();
}
