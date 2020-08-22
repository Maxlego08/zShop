package fr.maxlego08.shop.inventory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
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
import fr.maxlego08.shop.zcore.utils.inventory.ZButton;
import fr.maxlego08.shop.zcore.utils.inventory.VInventory;

public class InventoryManager extends ListenerAdapter {

	private final ZShop plugin;
	private final Map<Integer, VInventory> inventories = new HashMap<>();
	private final Map<Player, VInventory> playerInventories = new HashMap<>();

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

	public void createInventory(EnumInventory inv, Player player, int page, Object... objects) {
		createInventory(inv.getId(), player, page, objects);
	}

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
				playerInventories.put(player, clonedInventory);
			} else if (result.equals(InventoryResult.ERROR))
				message(player, Message.INVENTORY_OPEN_ERROR, id);
		} catch (InventoryOpenException e) {
			message(player, Message.INVENTORY_OPEN_ERROR, id);
			e.printStackTrace();
		}
	}

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
			VInventory gui = playerInventories.get(player);
			if (gui.getGuiName() == null || gui.getGuiName().length() == 0) {
				Logger.info("An error has occurred with the menu ! " + gui.getClass().getName());
				return;
			}
			if (event.getView() != null && gui.getPlayer().equals(player)
					&& event.getView().getTitle().equals(gui.getGuiName())) {
				event.setCancelled(true);
				ZButton button = gui.getItems().getOrDefault(event.getSlot(), null);
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
			Bukkit.getScheduler().runTask(plugin, () -> createInventory(inventory, inventory.getPlayer()));
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
				event.getPlayer().sendMessage(Message.PREFIX_END.getMessage() + " §aLe serveur utilise §2"
						+ plugin.getDescription().getFullName() + " §a!");
				String name = "%%__USER__%%";
				event.getPlayer()
						.sendMessage(Message.PREFIX_END.getMessage() + " §aUtilisateur spigot §2" + name + " §a!");
			}

			if (plugin.getDescription().getFullName().toLowerCase().contains("dev")) {
				event.getPlayer().sendMessage(Message.PREFIX_END.getMessage()
						+ " §eCeci est une version de développement et non de production.");
			}

			if (plugin.getDescription().getFullName().toLowerCase().contains("pre")) {
				event.getPlayer().sendMessage(Message.PREFIX_END.getMessage()
						+ " §eCeci n'est pas une version final du plugin mais une pre release !");
				event.getPlayer().sendMessage(Message.PREFIX_END.getMessage()
						+ " §eThis is not a final version of the plugin but a pre release !");
			}

		});
	}

	public void close() {
		Iterator<VInventory> iterator = this.playerInventories.values().iterator();
		while (iterator.hasNext()) {
			VInventory inventory = iterator.next();
			inventory.getPlayer().closeInventory();
		}
	}

}
