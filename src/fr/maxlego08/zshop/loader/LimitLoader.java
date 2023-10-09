package fr.maxlego08.zshop.loader;

import fr.maxlego08.zshop.api.limit.Limit;
import fr.maxlego08.zshop.api.limit.LimitType;
import fr.maxlego08.zshop.api.limit.SchedulerType;
import fr.maxlego08.zshop.limit.ZLimit;
import fr.maxlego08.zshop.zcore.logger.Logger;
import fr.maxlego08.zshop.zcore.utils.loader.Loader;
import org.bukkit.configuration.file.YamlConfiguration;

import java.text.DateFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.IntStream;

public class LimitLoader implements Loader<Limit> {


    private static final Map<String, Integer> DAYS = IntStream.range(1, 8).collect(HashMap::new, (map, month) -> map.put(new DateFormatSymbols(Locale.ENGLISH).getWeekdays()[month].toUpperCase(), month), HashMap::putAll);
    private static final Map<String, Integer> MONTHS = IntStream.range(0, 11).collect(HashMap::new, (map, month) -> map.put(new DateFormatSymbols(Locale.ENGLISH).getMonths()[month].toUpperCase(), month), HashMap::putAll);

    private final String material;
    private final LimitType limitType;

    public LimitLoader(String material, LimitType limitType) {
        this.material = material;
        this.limitType = limitType;
    }

    @Override
    public Limit load(YamlConfiguration configuration, String path) {

        int limit = configuration.getInt(path + "limit", 0);

        String typeAsString = configuration.getString(path + "schedulerType", SchedulerType.NEVER.name());
        SchedulerType schedulerType;
        try {
            schedulerType = SchedulerType.valueOf(typeAsString.toUpperCase());
        } catch (Exception exception) {
            Logger.info("Impossible to find the SchedulerType of " + path, Logger.LogType.ERROR);
            return null;
        }

        int dayOfMonth = 0;
        int dayOfWeek = 0;
        int month = 0;
        int hour = configuration.getInt(path + "hour");
        int minute = configuration.getInt(path + "minute");

        switch (schedulerType) {
            case WEEKLY:
                String dayAsString = configuration.getString(path + "day", "").toUpperCase();
                dayOfWeek = DAYS.getOrDefault(dayAsString, -1);
                if (dayOfWeek == -1) {
                    Logger.info("Impossible to find the day of " + path + " with weekly type", Logger.LogType.ERROR);
                    return null;
                }
                break;
            case MONTHLY:
                dayOfMonth = configuration.getInt(path + "day");
                break;
            case YEARLY:
                String monthAsString = configuration.getString(path + "month", "").toUpperCase();
                month = MONTHS.getOrDefault(monthAsString, -1);
                if (month == -1) {
                    Logger.info("Impossible to find the month of " + path + " with yearly type", Logger.LogType.ERROR);
                    return null;
                }
                dayOfMonth = configuration.getInt(path + "day");
                break;
            default:
                break;
        }

        return new ZLimit(this.limitType, this.material, limit, schedulerType, dayOfMonth, dayOfWeek, month, hour, minute);
    }

    @Override
    public void save(Limit object, YamlConfiguration configuration, String path) {

    }
}
