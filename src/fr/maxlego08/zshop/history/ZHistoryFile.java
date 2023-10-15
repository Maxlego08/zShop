package fr.maxlego08.zshop.history;

import fr.maxlego08.zshop.api.history.History;
import fr.maxlego08.zshop.api.history.HistoryFile;
import fr.maxlego08.zshop.save.Config;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ZHistoryFile implements HistoryFile {

    private final UUID uuid;
    private final List<ZHistory> histories = new ArrayList<>();
    private transient long savedAt;

    public ZHistoryFile(UUID uuid) {
        super();
        this.uuid = uuid;
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public OfflinePlayer getPlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    @Override
    public Collection<History> getHistories() {
        return Collections.unmodifiableCollection(this.histories);
    }

    @Override
    public void addHistory(History history) {
        this.histories.add((ZHistory) history);
    }


    @Override
    public long getSavedAt() {
        return this.savedAt;
    }

    @Override
    public void setSavedAt() {
        this.savedAt = System.currentTimeMillis() + 5000L;
    }

    @Override
    public boolean canSave() {
        return System.currentTimeMillis() >= this.savedAt;
    }

    @Override
    public void clear() {
        Iterator<ZHistory> iterator = this.histories.iterator();
        long oldDay = System.currentTimeMillis() - (1000 * 86400 * Config.expireLogDay);
        while (iterator.hasNext()) {
            History history = iterator.next();
            if (history.getDate() < oldDay) {
                iterator.remove();
            }
        }
    }
}
