package fr.maxlego08.zshop.api.utils;

import fr.maxlego08.zshop.api.PriceModifier;

import java.util.Optional;

public class PriceModifierCache {

    private final long expireAt;
    private final Optional<PriceModifier> priceModifier;

    public PriceModifierCache(long expireAt, Optional<PriceModifier> priceModifier) {
        this.expireAt = expireAt;
        this.priceModifier = priceModifier;
    }

    public long getExpireAt() {
        return expireAt;
    }

    public Optional<PriceModifier> getPriceModifier() {
        return priceModifier;
    }

    public boolean isValid() {
        return this.priceModifier.isPresent();
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expireAt;
    }
}