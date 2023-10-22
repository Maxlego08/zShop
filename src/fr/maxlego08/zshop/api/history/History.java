package fr.maxlego08.zshop.api.history;

public interface History {

    long getDate();

    HistoryType getType();

    String getMessage();

    String getDateAsString();
}
