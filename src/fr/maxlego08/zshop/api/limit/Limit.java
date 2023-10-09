package fr.maxlego08.zshop.api.limit;

import java.util.Calendar;
import java.util.UUID;

public interface Limit {

    int getId();

    LimitType getType();

    String getMaterial();

    UUID getOwnerUUID();

    int getLimit();

    int getAmount();

    void setAmount(int amount);

    SchedulerType getSchedulerType();

    int getDayOfMonth();

    int getDayOfWeek();

    int getMonth();

    int getHour();

    int getMinute();

    Calendar getCalendar();

    String getFormattedTimeUntilNextTask();

    void update();

}
