package fr.maxlego08.shop.inventory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.maxlego08.shop.ZShop;
import fr.maxlego08.shop.api.exceptions.InventoryAlreadyExistException;
import fr.maxlego08.shop.api.exceptions.InventoryOpenException;
import fr.maxlego08.shop.listener.ListenerAdapter;
import fr.maxlego08.shop.zcore.enums.EnumInventory;
import fr.maxlego08.shop.zcore.enums.Message;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zcore.utils.inventory.InventoryResult;
import fr.maxlego08.shop.zcore.utils.inventory.VInventory;
import fr.maxlego08.shop.zcore.utils.inventory.ZButton;

public class InventoryManager extends ListenerAdapter {

	private final ZShop plugin;
	private Map<Integer, VInventory> inventories = new HashMap<>();
	private Map<UUID, VInventory> playerInventories = new HashMap<>();

	/**
	 * @param plugin
	 */
	public InventoryManager(ZShop plugin) {
		super();
		this.plugin = plugin;
	}

	public void sendLog() {
		plugin.getLog().log("Loading " + inventories.size() + " inventories", LogType.SUCCESS);
	}

	public void addInventory(EnumInventory inv, VInventory inventory) {
		if (!inventories.containsKey(inv.getId()))
			inventories.put(inv.getId(), inventory);
		else
			throw new InventoryAlreadyExistException("Inventory with id " + inv.getId() + " already exist !");
	}

	/**
	 * 
	 * @param inv
	 * @param player
	 * @param page
	 * @param objects
	 */
	public void createInventory(EnumInventory inv, Player player, int page, Object... objects) {
		this.createInventory(inv.getId(), player, page, objects);
	}

	/**
	 * 
	 * @param id
	 * @param player
	 * @param page
	 * @param objects
	 */
	public void createInventory(int id, Player player, int page, Object... objects) {	
		
		VInventory inventory = getInventory(id);
		if (inventory == null) {
			message(player, Message.INVENTORY_CLONE_NULL, id);
			return;
		}
		VInventory clonedInventory = inventory.clone();

		if (clonedInventory == null) {
			message(player, Message.INVENTORY_CLONE_NULL);
			return;
		}

		clonedInventory.setId(id);
		try {
			InventoryResult result = clonedInventory.preOpenInventory(plugin, player, page, objects);
			if (result.equals(InventoryResult.SUCCESS)) {
				
				player.openInventory(clonedInventory.getInventory());
				playerInventories.put(player.getUniqueId(), clonedInventory);
				
			} else if (result.equals(InventoryResult.ERROR)) {
				message(player, Message.INVENTORY_OPEN_ERROR, "%id%", id);
			}
		} catch (InventoryOpenException e) {
			message(player, Message.INVENTORY_OPEN_ERROR, "%id%", id);
			Logger.info("This is a bug, please report it on the discord support: https://discord.gg/4yB2mjB",
					LogType.ERROR);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param parent
	 * @param player
	 */
	public void createInventory(VInventory parent, Player player) {
		createInventory(parent.getId(), player, parent.getPage(), parent.getObjets());
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent event, Player player) {

		if (event.getClickedInventory() == null)
			return;

		if (event.getWhoClicked() instanceof Player) {
			if (!exist(player))
				return;
			VInventory gui = playerInventories.get(player.getUniqueId());
			if (gui.getGuiName() == null || gui.getGuiName().length() == 0) {
				Logger.info("An error has occurred with the menu ! " + gui.getClass().getName());
				return;
			}

			if (event.getClickedInventory().getType().equals(InventoryType.PLAYER) && gui.allowPlayerClick())
				return;

			if (event.getView() != null && gui.getPlayer().equals(player)
					&& event.getView().getTitle().equals(gui.getGuiName())) {

				event.setCancelled(gui.isDisableClick());
				ZButton button = gui.getItems().getOrDefault(event.getSlot(), null);
				if (button != null) {					
					button.onClick(event);
				}
			}
		}
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent event, Player player) {
		if (!exist(player))
			return;
		VInventory inventory = playerInventories.get(player.getUniqueId());
		remove(player);
		inventory.onClose(event, plugin, player);
	}

	@Override
	protected void onInventoryDrag(InventoryDragEvent event, Player player) {
		if (event.getWhoClicked() instanceof Player) {
			if (!exist(player))
				return;
			playerInventories.get(player.getUniqueId()).onDrag(event, plugin, player);
		}
	}

	public boolean exist(Player player) {
		return playerInventories.containsKey(player.getUniqueId());
	}

	public void remove(Player player) {
		if (playerInventories.containsKey(player.getUniqueId()))
			playerInventories.remove(player.getUniqueId());
	}

	private VInventory getInventory(int id) {
		return inventories.getOrDefault(id, null);
	}

	/**
	 * @param id
	 */
	public void updateAllPlayer(int... id) {
		for (int currentId : id)
			updateAllPlayer(currentId);
	}

	/**
	 * @param id
	 */
	public void closeAllPlayer(int... id) {
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
			Bukkit.getScheduler().runTask(this.plugin, () -> createInventory(inventory, inventory.getPlayer()));
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

	@Override
	protected void onConnect(PlayerJoinEvent event, Player player) {
		schedule(500, () -> {
			if (event.getPlayer().getName().startsWith("Maxlego08") || event.getPlayer().getName().startsWith("Sak")) {
				event.getPlayer().sendMessage(Message.PREFIX.getMessage() + "§aLe serveur utilise §2"
						+ plugin.getDescription().getFullName() + "§a!");
				String name = "%%__USER__%%";
				event.getPlayer()
						.sendMessage(Message.PREFIX.getMessage() + "§aUtilisateur spigot §2" + name + " §a!");
			}
		});
	}

	public void close() {
		Iterator<Entry<UUID, VInventory>> iterator = new HashMap<UUID, VInventory>(this.playerInventories).entrySet().iterator();
		while (iterator.hasNext()) {
			try {
				Entry<UUID, VInventory> entry = iterator.next();
				VInventory vInventory = entry.getValue();
				Player player = vInventory.getPlayer();
				player.closeInventory();
			} catch (Exception e) {
			}
		}
	}

}
