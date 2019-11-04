package fr.maxlego08.template.zcore.utils.builder;

import fr.maxlego08.template.save.Lang;

public class TimerBuilder {
	
	public static String getFormatLongDays(long temps) {
		long totalSecs = temps / 1000L;
		return String.format(
				"%02d " + Lang.day + " %02d " + Lang.hour + " %02d " + Lang.minute + " %02d " + Lang.second + "",
				new Object[] { Long.valueOf(totalSecs / 86400l), Long.valueOf(totalSecs % 86400l / 3600l),
						Long.valueOf(totalSecs % 3600L / 60L), Long.valueOf(totalSecs % 60L) });
	}

	public static String getFormatLongHours(long temps) {
		long totalSecs = temps / 1000L;
		return String.format("%02d " + Lang.hour + " %02d " + Lang.minute + " %02d " + Lang.second + "",
				new Object[] { Long.valueOf(totalSecs / 3600L), Long.valueOf(totalSecs % 3600L / 60L),
						Long.valueOf(totalSecs % 60L) });
	}

	public static String getFormatLongHoursSimple(long temps) {
		long totalSecs = temps / 1000L;
		return String.format("%02 " + Lang.hour + "s %02 " + Lang.minute + "s %02 " + Lang.second + "s",
				new Object[] { Long.valueOf(totalSecs / 3600L), Long.valueOf(totalSecs % 3600L / 60L),
						Long.valueOf(totalSecs % 60L) });
	}

	public static String getFormatLongMinutes(long temps) {
		long totalSecs = temps / 1000L;
		return String.format("%02d " + Lang.minute + " %02d " + Lang.second + "",
				new Object[] { Long.valueOf(totalSecs % 3600L / 60L), Long.valueOf(totalSecs % 60L) });
	}

	public static String getFormatLongSecondes(long temps) {
		long totalSecs = temps / 1000L;
		return String.format("%02d " + Lang.second + "", new Object[] { Long.valueOf(totalSecs % 60L) });
	}

	public static String getStringTime(long second) {
		if (second < 60)
			return replace(TimerBuilder.getFormatLongSecondes(second * 1000l));
		else if (second >= 60 && second < 3600)
			return replace(TimerBuilder.getFormatLongMinutes(second * 1000l));
		else if (second >= 3600 && second < 86400)
			return replace(TimerBuilder.getFormatLongHours(second * 1000l));
		else
			return replace(TimerBuilder.getFormatLongDays(second * 1000l));
	}

	private static String replace(String m) {
		return m.replace("00" + Lang.day, "").replace("00" + Lang.hour, "").replace("00h" + Lang.minute, "")
				.replace("00" + Lang.second, "");
	}
}
