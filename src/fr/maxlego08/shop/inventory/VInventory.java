package fr.maxlego08.shop.inventory;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.zcore.utils.ZUtils;
import fr.maxlego08.shop.zshop.inventories.InventoryObject;

public abstract class VInventory extends ZUtils implements Cloneable {

	private InventoryManager inventoryManager;
	private ZShop plugin;
	private Map<Integer, ItemButton> items = new HashMap<Integer, ItemButton>();
	private Player player;
	private int page;
	private Object[] args;
	private Inventory inventory;
	private String guiName;
	private boolean disableClick = true;
	private InventoryObject inventoryObject;
	private int id;

	public void setInventoryObject(InventoryObject inventoryObject) {
		this.inventoryObject = inventoryObject;
	}

	public InventoryObject getInventoryObject() {
		return inventoryObject;
	}

	public VInventory setId(int id) {
		this.id = id;
		return this;
	}

	public int getId() {
		return id;
	}

	protected VInventory createInventory(String name) {
		return createInventory(name, 54);
	}

	public ZShop getPlugin() {
		return plugin;
	}

	protected VInventory createInventory(String name, int size) {
		guiName = name;
		this.inventory = Bukkit.createInventory(null, size, name);
		return this;
	}

	public void addItem(int slot, ItemButton item) {
		this.items.put(slot, item);
		this.inventory.setItem(slot, item.getDisplayItem());
	}

	public ItemButton addItem(int slot, ItemStack item) {
		ItemButton button = new ItemButton(item);
		this.items.put(slot, button);
		this.inventory.setItem(slot, item);
		return button;
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

	public boolean isDisableClick() {
		return disableClick;
	}

	public void setDisableClick(boolean disableClick) {
		this.disableClick = disableClick;
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

	public void setInventoryManager(InventoryManager inventoryManager) {
		this.inventoryManager = inventoryManager;
	}

	public void createInventory(int id, Player player, int page, InventoryObject obj, Object... args) {
		inventoryManager.createInventory(id, player, page, obj, args);
	}

	public abstract boolean openInventory(ZShop main, Player player, int page, Object... args) throws Exception;

	protected abstract void onClose(InventoryCloseEvent event, ZShop plugin, Player player);

	protected abstract void onDrag(InventoryDragEvent event, ZShop plugin, Player player);

	public abstract VInventory clone();

}
