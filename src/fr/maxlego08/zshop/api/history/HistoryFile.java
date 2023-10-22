package fr.maxlego08.zshop.api.history;

import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.UUID;

public interface HistoryFile {

    /**
     * @return {@link UUID}
     */
    UUID getUniqueId();

    /**
     * @return {@link OfflinePlayer}
     */
    OfflinePlayer getPlayer();

    /**
     * @return
     */
    Collection<History> getHistories();

    /**
     * @param history
     */
    void addHistory(History history);

    long getSavedAt();

    void setSavedAt();

    boolean canSave();

    void clear();
}
