package fr.maxlego08.zshop;

import fr.maxlego08.zshop.api.PriceModifier;
import fr.maxlego08.zshop.api.PriceType;

import java.util.Map;

public class ZPriceModifier implements PriceModifier {

    private final PriceType priceType;
    private final double modifier;
    private final String permission;

    public ZPriceModifier(PriceType priceType, double modifier, String permission) {
        this.priceType = priceType;
        this.modifier = modifier;
        this.permission = permission;
    }

    public ZPriceModifier(Map<String, Object> map) {
        this(PriceType.valueOf(((String) map.get("type")).toUpperCase()), ((Number) map.get("modifier")).doubleValue(), (String) map.get("permission"));
    }

    @Override
    public PriceType getType() {
        return priceType;
    }

    @Override
    public double getModifier() {
        return modifier;
    }

    @Override
    public String getPermission() {
        return permission;
    }
}
