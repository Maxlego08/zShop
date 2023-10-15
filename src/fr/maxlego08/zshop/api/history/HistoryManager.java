package fr.maxlego08.zshop.api.history;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import java.util.Optional;
import java.util.UUID;

public interface HistoryManager extends Listener {

    /**
     * Get file is present
     *
     * @param uuid
     * @return {@link Optional}
     */
	Optional<HistoryFile> getFile(UUID uuid);

    /**
     * Get or create file
     *
     * @param uuid
     * @return {@link Optional}
     */
	HistoryFile getOrCreate(UUID uuid);

    /**
     * Load or create file
     *
     * @param uuid
     * @return {@link HistoryFile}
     */
	HistoryFile loadOrCreate(UUID uuid);

    /**
     * @param uuid
     * @return true if file is load
     */
	boolean isLoad(UUID uuid);

    /**
     *
     */
	void asyncSave();

    /**
     * @param uuid
     */
	void asyncSave(UUID uuid);

    /**
     * @param uuid
     */
	void save(UUID uuid);

    /**
     * Add async value
     *
     * @param uuid
     * @param history
     */
	void asyncValue(UUID uuid, History history);

    void readLogs(CommandSender sender, OfflinePlayer offlinePlayer, HistoryType type, int page);

}
