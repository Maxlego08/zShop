package fr.maxlego08.zshop.api.limit;

import java.util.Map;
import java.util.UUID;

public interface PlayerLimit {

    UUID getUniqueId();

    int getSellAmount(String material);

    void setSellAmount(String material, int amount);

    int getBuyAmount(String material);

    void setBuyAmount(String material, int amount);

    void reset(Limit limit);
    void reset(String material);

    Map<String, Integer> getBuyLimits();

    Map<String, Integer> getSellLimits();
}
