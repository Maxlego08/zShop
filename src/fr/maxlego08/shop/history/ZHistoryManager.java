package fr.maxlego08.shop.history;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.plugin.Plugin;

import fr.maxlego08.shop.api.history.HistoryFile;
import fr.maxlego08.shop.api.history.HistoryManager;
import fr.maxlego08.shop.zcore.utils.ZUtils;
import fr.maxlego08.shop.zcore.utils.storage.Persist;

public class ZHistoryManager extends ZUtils implements HistoryManager {

	private final Map<UUID, HistoryFile> histoies = new HashMap<UUID, HistoryFile>();
	private final Plugin plugin;
	private final Persist persist;

	/**
	 * @param plugin
	 * @param persist
	 */
	public ZHistoryManager(Plugin plugin, Persist persist) {
		super();
		this.plugin = plugin;
		this.persist = persist;
		File folder = new File(plugin.getDataFolder(), "players");
		if (!folder.exists())
			folder.mkdir();
	}

	@Override
	public Optional<HistoryFile> getFile(UUID uuid) {
		HistoryFile value = histoies.getOrDefault(uuid, null);
		return value == null ? Optional.empty() : Optional.of(value);
	}

	@Override
	public HistoryFile getOrCreate(UUID uuid) {
		Optional<HistoryFile> optional = getFile(uuid);
		return optional.isPresent() ? optional.get() : loadOrCreate(uuid);
	}

	@Override
	public HistoryFile loadOrCreate(UUID uuid) {
		File file = new File(plugin.getDataFolder(), "players/" + uuid + ".json");
		ZHistoryFile historyFile = new ZHistoryFile(uuid);
		persist.silentLoadOrSaveDefault(historyFile, ZHistoryFile.class, file);
		this.histoies.put(uuid, historyFile);
		return historyFile;
	}

	@Override
	public boolean isLoad(UUID uuid) {
		return getFile(uuid).isPresent();
	}

	@Override
	public void asyncSave() {
		runAsync(plugin, () -> {

			histoies.values().forEach(value -> {

				UUID uuid = value.getUniqueId();
				File file = new File(plugin.getDataFolder(), "players/" + uuid + ".json");
				persist.silentSave((ZHistoryFile) value, file);

			});

		});
	}

	@Override
	public void asyncSave(UUID uuid) {
		runAsync(plugin, () -> {
			Optional<HistoryFile> optional = getFile(uuid);
			if (optional.isPresent()) {
				File file = new File(plugin.getDataFolder(), "players/" + uuid + ".json");
				persist.silentSave((ZHistoryFile) optional.get(), file);
			}
		});
	}

}
