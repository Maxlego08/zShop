package fr.maxlego08.zshop.api;

public interface PriceModifier {

    PriceType getType();

    double getModifier();

    String getPermission();

}
