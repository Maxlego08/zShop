package fr.maxlego08.zshop.limit;

import fr.maxlego08.zshop.api.limit.Limit;
import fr.maxlego08.zshop.api.limit.PlayerLimit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ZPlayerLimit implements PlayerLimit {

    private final UUID uuid;
    private Map<String, Integer> sellLimits = new HashMap<>();
    private Map<String, Integer> buyLimits = new HashMap<>();

    public ZPlayerLimit(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public int getSellAmount(String material) {
        return this.sellLimits.getOrDefault(material, 0);
    }

    @Override
    public void setSellAmount(String material, int amount) {
        this.sellLimits.put(material, amount);
    }

    @Override
    public int getBuyAmount(String material) {
        return this.buyLimits.getOrDefault(material, 0);
    }

    @Override
    public void setBuyAmount(String material, int amount) {
        this.buyLimits.put(material, amount);
    }

    public File getFile(Plugin plugin) {
        return new File(plugin.getDataFolder(), "players/" + this.uuid + ".json");
    }

    public boolean isEmpty() {
        if (this.sellLimits == null) this.sellLimits = new HashMap<>();
        if (this.buyLimits == null) this.buyLimits = new HashMap<>();
        return (this.sellLimits.isEmpty() || this.sellLimits.values().stream().allMatch(e -> e == 0)) && (this.buyLimits.isEmpty() || this.buyLimits.values().stream().allMatch(e -> e == 0));
    }

    @Override
    public void reset(Limit limit) {
        this.buyLimits.remove(limit.getMaterial());
        this.sellLimits.remove(limit.getMaterial());
    }

    @Override
    public void reset(String material) {
        this.buyLimits.remove(material);
        this.sellLimits.remove(material);
    }

    @Override
    public Map<String, Integer> getBuyLimits() {
        return buyLimits;
    }
    @Override
    public Map<String, Integer> getSellLimits() {
        return sellLimits;
    }
}
