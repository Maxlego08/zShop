package fr.maxlego08.shop.zcore.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import fr.maxlego08.shop.listener.ListenerAdapter;
import fr.maxlego08.shop.save.Config;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.ZPlugin;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;

public class VersionChecker extends ListenerAdapter {

	private boolean useLastVersion = false;
	private String lastVersion = "";

	public void useLastVersion(Plugin plugin) {
		UpdateChecker checker = new UpdateChecker(plugin, 74073);
		AtomicBoolean atomicBoolean = new AtomicBoolean();
		checker.getVersion(version -> {
			atomicBoolean.set(plugin.getDescription().getVersion().equalsIgnoreCase(version));
			useLastVersion = atomicBoolean.get();
			if (atomicBoolean.get())
				Logger.info("There is not a new update available.");
			else
				Logger.info("There is a new update available. Your version: " + plugin.getDescription().getVersion()
						+ ", Laste version: " + version);
		});

	}

	public void getLastVersion() {
		new BukkitRunnable() {

			@Override
			public void run() {
				URL url;
				try {
					url = new URL("https://pastebin.com/raw/mdbSER18");
					BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
					String s;
					while ((s = in.readLine()) != null) {
						lastVersion = s;
					}
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (lastVersion.equals(ZPlugin.z().getDescription().getVersion())) {
					useLastVersion = true;
					Logger.info("You are using the latest version of the plugin!", LogType.SUCCESS);
				} else
					Logger.info(
							"You do not use the latest version of the plugin! Thank you for taking the latest version to avoid any risk of problem!",
							LogType.WARNING);
			}
		}.runTaskAsynchronously(ZPlugin.z());
	}

	@Override
	protected void onConnect(PlayerJoinEvent event, Player player) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!useLastVersion && event.getPlayer().hasPermission("admin.zshop") && !Config.disableUpdateMessage) {
					player.sendMessage(Lang.prefix
							+ " §cYou do not use the latest version of the plugin! Thank you for taking the latest version to avoid any risk of problem!");
				}
				if (player.getName().startsWith("Maxlego08") || player.getName().startsWith("SakAF0u")) {
					event.getPlayer().sendMessage(Lang.prefix + " §aLe serveur utilise §2"
							+ ZPlugin.z().getDescription().getFullName() + " §a!");
					String name = "%%__USER__%%";
					event.getPlayer().sendMessage(Lang.prefix + " §aUtilisateur spigot §2" + name + " §a!");
					event.getPlayer().sendMessage(Lang.prefix + " §aAdresse du serveur §2"
							+ Bukkit.getServer().getIp().toString() + ":" + Bukkit.getServer().getPort() + " §a!");
				}
			}
		}.runTaskLater(ZPlugin.z(), 20 * 2);
	}

	public class UpdateChecker {

		private Plugin plugin;
		private int resourceId;

		public UpdateChecker(Plugin plugin, int resourceId) {
			this.plugin = plugin;
			this.resourceId = resourceId;
		}

		/**
		 * 
		 * @param consumer
		 */
		public void getVersion(final Consumer<String> consumer) {
			Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
				try (InputStream inputStream = new URL(
						"https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream();
						Scanner scanner = new Scanner(inputStream)) {
					if (scanner.hasNext()) {
						consumer.accept(scanner.next());
					}
				} catch (IOException exception) {
					this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
				}
			});
		}
	}

}
