package fr.maxlego08.shop.zcore.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.maxlego08.shop.listener.ListenerAdapter;
import fr.maxlego08.shop.save.Lang;
import fr.maxlego08.shop.zcore.ZPlugin;
import fr.maxlego08.shop.zcore.logger.Logger;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;

public class VersionChecker extends ListenerAdapter {

	private boolean useLastVersion = false;
	private String lastVersion = "";

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
					Logger.info("You do not use the latest version of the plugin! Thank you for taking the latest version to avoid any risk of problem!", LogType.WARNING);
			}
		}.runTaskAsynchronously(ZPlugin.z());
	}

	@Override
	protected void onConnect(PlayerJoinEvent event, Player player) {
		if (!useLastVersion && event.getPlayer().hasPermission("admin.zshop")){
			new BukkitRunnable() {
				@Override
				public void run() {
					player.sendMessage(Lang.prefix + " §cYou do not use the latest version of the plugin! Thank you for taking the latest version to avoid any risk of problem!");
				}
			}.runTaskLater(ZPlugin.z(), 10);
		}
	}
}
