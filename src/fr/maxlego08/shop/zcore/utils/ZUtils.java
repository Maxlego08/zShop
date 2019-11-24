package fr.maxlego08.shop.zcore.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.ZPlugin;
import net.milkbowl.vault.economy.Economy;

@SuppressWarnings("deprecation")
public abstract class ZUtils {

	private static transient List<String> teleportPlayers = new ArrayList<String>();

	/**
	 * @param location
	 *            as String
	 * @return string as location
	 */
	protected Location changeStringLocationToLocation(String s) {
		String[] a = s.split(",");
		World w = Bukkit.getServer().getWorld(a[0]);
		float x = Float.parseFloat(a[1]);
		float y = Float.parseFloat(a[2]);
		float z = Float.parseFloat(a[3]);
		return new Location(w, x, y, z);
	}

	/**
	 * @param location
	 *            as string
	 * @return string as locaiton
	 */
	protected Location changeStringLocationToLocationEye(String s) {
		String[] a = s.split(",");
		World w = Bukkit.getServer().getWorld(a[0]);
		float x = Float.parseFloat(a[1]);
		float y = Float.parseFloat(a[2]);
		float z = Float.parseFloat(a[3]);
		float yaw = Float.parseFloat(a[3]);
		float pitch = Float.parseFloat(a[3]);
		return new Location(w, x, y, z, yaw, pitch);
	}

	/**
	 * @param location
	 * @return location as string
	 */
	protected String changeLocationToString(Location location) {
		String ret = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + ","
				+ location.getBlockZ();
		return ret;
	}

	/**
	 * @param location
	 * @return location as String
	 */
	protected String changeLocationToStringEye(Location location) {
		String ret = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + ","
				+ location.getBlockZ() + "," + location.getYaw() + "," + location.getPitch();
		return ret;
	}

	/**
	 * @param chunk
	 * @return string as Chunk
	 */
	protected Chunk changeStringChuncToChunk(String chunk) {
		String[] a = chunk.split(",");
		World w = Bukkit.getServer().getWorld(a[0]);
		return w.getChunkAt(Integer.valueOf(a[1]), Integer.valueOf(a[2]));
	}

	/**
	 * @param chunk
	 * @return chunk as string
	 */
	protected String changeChunkToString(Chunk chunk) {
		String c = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
		return c;
	}

	/**
	 * @param {@link
	 * 			Cuboid}
	 * @return cuboid as string
	 */
	protected String changeCuboidToString(Cuboid cuboid) {
		return cuboid.getWorld().getName() + "," + cuboid.getLowerX() + "," + cuboid.getLowerY() + ","
				+ cuboid.getLowerZ() + "," + ";" + cuboid.getWorld().getName() + "," + cuboid.getUpperX() + ","
				+ cuboid.getUpperY() + "," + cuboid.getUpperZ();
	}

	/**
	 * @param str
	 * @return {@link Cuboid}
	 */
	protected Cuboid changeStringToCuboid(String str) {

		String parsedCuboid[] = str.split(";");
		String parsedFirstLoc[] = parsedCuboid[0].split(",");
		String parsedSecondLoc[] = parsedCuboid[1].split(",");

		String firstWorldName = parsedFirstLoc[0];
		double firstX = Double.valueOf(parsedFirstLoc[1]);
		double firstY = Double.valueOf(parsedFirstLoc[2]);
		double firstZ = Double.valueOf(parsedFirstLoc[3]);

		String secondWorldName = parsedSecondLoc[0];
		double secondX = Double.valueOf(parsedSecondLoc[1]);
		double secondY = Double.valueOf(parsedSecondLoc[2]);
		double secondZ = Double.valueOf(parsedSecondLoc[3]);

		Location l1 = new Location(Bukkit.getWorld(firstWorldName), firstX, firstY, firstZ);

		Location l2 = new Location(Bukkit.getWorld(secondWorldName), secondX, secondY, secondZ);

		return new Cuboid(l1, l2);

	}

	/**
	 * @param item
	 * @return the encoded item
	 */
	protected String encode(ItemStack item) {
		return ItemDecoder.serializeItemStack(item);
	}

	/**
	 * @param item
	 * @return the decoded item
	 */
	protected ItemStack decode(String item) {
		return ItemDecoder.deserializeItemStack(item);
	}

	/**
	 * @param material
	 * @return he name of the material with a better format
	 */
	protected String betterMaterial(Material material) {
		return TextUtil.getMaterialLowerAndMajAndSpace(material);
	}

	/**
	 * @param a
	 * @param b
	 * @return number between a and b
	 */
	protected int getNumberBetween(int a, int b) {
		return ThreadLocalRandom.current().nextInt(a, b);
	}

	/**
	 * @param player
	 * @return true if the player's inventory is full
	 */
	protected boolean hasInventoryFull(Player player) {
		Inventory inv = player.getInventory();
		boolean check = false;
		for (ItemStack item : inv.getContents()) {
			if (item == null) {
				check = true;
				break;
			}
		}
		return check;
	}

	/**
	 * Gives an item to the player, if the player's inventory is full then the
	 * item will drop to the ground
	 * 
	 * @param player
	 * @param item
	 */
	protected void give(Player player, ItemStack item) {
		if (hasInventoryFull(player))
			player.getWorld().dropItem(player.getLocation(), item);
		else
			player.getInventory().addItem(item);
	}

	private static transient Material[] byId;

	static {
		byId = new Material[0];
		for (Material material : Material.values()) {
			if (byId.length > material.getId()) {
				byId[material.getId()] = material;
			} else {
				byId = Arrays.copyOfRange(byId, 0, material.getId() + 2);
				byId[material.getId()] = material;
			}
		}
	}

	/**
	 * @param id
	 * @return the material according to his id
	 */
	protected Material getMaterial(int id) {
		return byId.length > id && id >= 0 ? byId[id] : null;
	}

	/**
	 * Check if the item name is the same as the given string
	 * 
	 * @param stack
	 * @param name
	 * @return true if the item name is the same as string
	 */
	protected boolean same(ItemStack stack, String name) {
		return stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()
				&& stack.getItemMeta().getDisplayName().equals(name);
	}

	/**
	 * Check if the item name contains the given string
	 * 
	 * @param stack
	 * @param name
	 * @return true if the item name contains the string
	 */
	protected boolean contains(ItemStack stack, String name) {
		return stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()
				&& stack.getItemMeta().getDisplayName().contains(name);
	}

	/**
	 * Remove the item from the player's hand
	 * 
	 * @param player
	 * @param number
	 *            of items to withdraw
	 */
	protected void removeItemInHand(Player player, int how) {
		if (player.getItemInHand().getAmount() > how)
			player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
		else
			player.setItemInHand(new ItemStack(Material.AIR));
		player.updateInventory();
	}

	/**
	 * Check if two locations are identical
	 * 
	 * @param first
	 *            location
	 * @param second
	 *            location
	 * @return true if both rentals are the same
	 */
	protected boolean same(Location l, Location l2) {
		return (l.getBlockX() == l2.getBlockX()) && (l.getBlockY() == l2.getBlockY())
				&& (l.getBlockZ() == l2.getBlockZ()) && l.getWorld().getName().equals(l2.getWorld().getName());
	}

	/**
	 * Teleport a player to a given location with a given delay
	 * 
	 * @param player
	 *            who will be teleported
	 * @param delay
	 *            before the teleportation of the player
	 * @param location
	 *            where the player will be teleported
	 */
	protected void teleport(Player player, int delay, Location location) {
		teleport(player, delay, location, null);
	}

	/**
	 * Teleport a player to a given location with a given delay
	 * 
	 * @param player
	 *            who will be teleported
	 * @param delay
	 *            before the teleportation of the player
	 * @param location
	 *            where the player will be teleported
	 * @param code
	 *            executed when the player is teleported or not
	 */
	protected void teleport(Player player, int delay, Location location, Consumer<Boolean> cmd) {
		if (teleportPlayers.contains(player.getName())) {
			player.sendMessage(Lang.prefix + " §cVous avez déjà une téléportation en cours !");
			return;
		}
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
		Location playerLocation = player.getLocation();
		AtomicInteger verif = new AtomicInteger(delay);
		teleportPlayers.add(player.getName());
		if (!location.getChunk().isLoaded())
			location.getChunk().load();
		ses.scheduleWithFixedDelay(() -> {
			if (!same(playerLocation, player.getLocation())) {
				player.sendMessage(Lang.prefix + "§cVous ne devez pas bouger !");
				ses.shutdown();
				teleportPlayers.remove(player.getName());
				if (cmd != null)
					cmd.accept(false);
				return;
			}
			int currentSecond = verif.getAndDecrement();
			player.sendMessage(Lang.prefix + " " + (currentSecond != 0
					? "§eTéléportatio dans §6" + currentSecond + " §esecondes !" : "§eTéléportation !"));
			if (!player.isOnline()) {
				ses.shutdown();
				teleportPlayers.remove(player.getName());
				return;
			}
			if (currentSecond == 0) {
				ses.shutdown();
				teleportPlayers.remove(player.getName());
				player.teleport(location);
				if (cmd != null)
					cmd.accept(true);
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	/**
	 * Format a double in a String
	 * 
	 * @param decimal
	 * @return formatting current duplicate
	 */
	protected String format(double decimal) {
		return format(decimal, "#.##");
	}

	/**
	 * Format a double in a String
	 * 
	 * @param decimal
	 * @param format
	 * @return formatting current double according to the given format
	 */
	protected String format(double decimal, String format) {
		DecimalFormat decimalFormat = new DecimalFormat(format);
		return decimalFormat.format(decimal);
	}

	private transient Economy economy = ZPlugin.z().getEconomy();

	/**
	 * Player bank
	 * 
	 * @param player
	 * @return player bank
	 */
	protected double getBalance(Player player) {
		return economy.getBalance(player);
	}

	/**
	 * Player bank
	 * 
	 * @param player
	 * @return player bank
	 */
	protected double getBalance(String player) {
		if (economy == null)
			economy = ZPlugin.z().getEconomy();
		return economy.getBalance(player);
	}

	/**
	 * Player has money
	 * 
	 * @param player
	 * @param int
	 *            value
	 * @return player has value in his bank
	 */
	protected boolean hasMoney(Player player, int value) {
		return hasMoney(player, (double) value);
	}

	/**
	 * Player has money
	 * 
	 * @param player
	 * @param float
	 *            value
	 * @return player has value in his bank
	 */
	protected boolean hasMoney(Player player, float value) {
		return hasMoney(player, (double) value);
	}

	/**
	 * Player has money
	 * 
	 * @param player
	 * @param long
	 *            value
	 * @return player has value in his bank
	 */
	protected boolean hasMoney(Player player, long value) {
		return hasMoney(player, (double) value);
	}

	/**
	 * Player has money
	 * 
	 * @param player
	 * @param double
	 *            value
	 * @return player has value in his bank
	 */
	protected boolean hasMoney(Player player, double value) {
		return getBalance(player) >= value;
	}

	/**
	 * Deposit player money
	 * 
	 * @param player
	 * @param double
	 *            value
	 */
	protected void depositMoney(Player player, double value) {
		economy.depositPlayer(player, value);
	}

	/**
	 * Deposit player money
	 * 
	 * @param player
	 * @param long
	 *            value
	 */
	protected void depositMoney(Player player, long value) {
		economy.depositPlayer(player, (double) value);
	}

	/**
	 * Deposit player money
	 * 
	 * @param player
	 * @param int
	 *            value
	 */
	protected void depositMoney(Player player, int value) {
		economy.depositPlayer(player, (double) value);
	}

	/**
	 * Deposit player money
	 * 
	 * @param player
	 * @param float
	 *            value
	 */
	protected void depositMoney(Player player, float value) {
		economy.depositPlayer(player, (double) value);
	}

	/**
	 * With draw player money
	 * 
	 * @param player
	 * @param double
	 *            value
	 */
	protected void withdrawMoney(Player player, double value) {
		economy.withdrawPlayer(player, value);
	}

	/**
	 * With draw player money
	 * 
	 * @param player
	 * @param long
	 *            value
	 */
	protected void withdrawMoney(Player player, long value) {
		economy.withdrawPlayer(player, (double) value);
	}

	/**
	 * With draw player money
	 * 
	 * @param player
	 * @param int
	 *            value
	 */
	protected void withdrawMoney(Player player, int value) {
		economy.withdrawPlayer(player, (double) value);
	}

	/**
	 * With draw player money
	 * 
	 * @param player
	 * @param float
	 *            value
	 */
	protected void withdrawMoney(Player player, float value) {
		economy.withdrawPlayer(player, (double) value);
	}

	/**
	 * 
	 * @return {@link Economy}
	 */
	protected Economy getEconomy() {
		return economy;
	}

	public String color(String message) {
		return message.replace("&", "§");
	}

	public List<String> color(List<String> messages) {
		return messages.stream().map(message -> color(message)).collect(Collectors.toList());
	}

}
