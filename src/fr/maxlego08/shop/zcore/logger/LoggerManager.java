package fr.maxlego08.shop.zcore.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import fr.maxlego08.shop.save.Config;
import fr.maxlego08.shop.zcore.ZPlugin;
import fr.maxlego08.shop.zcore.logger.Logger.LogType;
import fr.maxlego08.shop.zshop.utils.ShopAction;

public class LoggerManager {

	private static File logFile;
	private static String lastDate = getDateNow();
	private static long lastFileWrite = 0;
	private static Map<UUID, List<ShopAction>> actions = new HashMap<UUID, List<ShopAction>>();

	public LoggerManager(ZPlugin main) {
		if (Config.useLogInFile) {
			File file = new File(main.getDataFolder() + "/log");
			if (!file.exists())
				file.mkdirs();
			logFile = new File(main.getDataFolder() + "/log/" + getDateNow() + ".yml");
			if (!logFile.exists()) {
				try {
					logFile.createNewFile();
					main.getLog().log(logFile.getAbsolutePath() + " created successly !", LogType.SUCCESS);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public final String USER = "%%__USER__%%";

	/**
	 * Log file
	 * 
	 * @param shopAction
	 */
	public static void log(ShopAction shopAction) {

		if (Config.useLogConsole)
			Logger.info(shopAction.toMessage(), LogType.INFO);

		if (Config.useLogInFile) {

			if (lastFileWrite == 0) {

				lastFileWrite = System.currentTimeMillis() + Config.poolFileWriter;
				writeAsync(shopAction.toMessage());
				return;

			}

			List<ShopAction> actionsList = actions.getOrDefault(shopAction.getUniqueId(), new ArrayList<ShopAction>());
			addToList(actionsList, shopAction);
			actions.put(shopAction.getUniqueId(), actionsList);

			// On va write
			if (System.currentTimeMillis() > lastFileWrite) {

				lastFileWrite = System.currentTimeMillis() + Config.poolFileWriter;

				List<ShopAction> clonedActions = new ArrayList<ShopAction>();
				actions.values().forEach(list -> clonedActions.addAll(list));

				actions.clear();

				Bukkit.getScheduler().runTaskAsynchronously(ZPlugin.z(), () -> {

					try {

						FileWriter w = new FileWriter(logFile, true);
						BufferedWriter bf = new BufferedWriter(w);

						for (ShopAction action : clonedActions) {
							bf.newLine();
							bf.write("[" + getHour() + "] > " + action.toMessage());
						}

						bf.close();
						w.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

				});

			}

		}
	}

	/**
	 * 
	 * @param actions
	 * @param action
	 */
	private static void addToList(List<ShopAction> actions, ShopAction action) {

		ShopAction sameAction = actions.stream().filter(currentAction -> {
			return action.equals(currentAction);
		}).findAny().orElse(null);

		if (sameAction == null)
			actions.add(action);
		else {
			sameAction.add(action);
		}

	}

	/**
	 * 
	 * @param message
	 */
	private static void writeAsync(String message) {
		Bukkit.getScheduler().runTaskAsynchronously(ZPlugin.z(), () -> write(message));
	}

	/**
	 * 
	 * @param message
	 */
	private static void write(String message) {
		if (lastDate != null && !lastDate.equals(getDateNow()))
			logFile = null;

		if (logFile == null)
			new LoggerManager(ZPlugin.z());

		try {
			FileWriter w = new FileWriter(logFile, true);
			BufferedWriter bf = new BufferedWriter(w);

			bf.newLine();
			bf.write("[" + getHour() + "] > " + message);

			bf.close();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	private static String getDateNow() {
		SimpleDateFormat datenow = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
		Date now = new Date();
		String strDate = datenow.format(now);
		return strDate;
	}

	/**
	 * 
	 * @return
	 */
	private static String getHour() {
		SimpleDateFormat datenow = new SimpleDateFormat("HH:mm:ss.SSS", Locale.FRANCE);
		Date now = new Date();
		String strDate = datenow.format(now);
		return strDate;
	}

}
