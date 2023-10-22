package fr.maxlego08.zshop.exceptions;

public class EconomyNotFoundException extends Error {

    public EconomyNotFoundException(String message) {
        super(message);
    }

    public EconomyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EconomyNotFoundException(Throwable cause) {
        super(cause);
    }

    public EconomyNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
