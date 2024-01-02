package rip.diamond.maid.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    public static final long PERMANENT = -1;

    public static String formatDate(long value) {
        return FORMAT.format(new Date(value));
    }

    public static String formatDuration(long durationInMillis) {
        if (durationInMillis == PERMANENT) {
            return "永久";
        }

        long years = TimeUnit.MILLISECONDS.toDays(durationInMillis) / 365;
        long months = (TimeUnit.MILLISECONDS.toDays(durationInMillis) % 365) / 30;
        long days = TimeUnit.MILLISECONDS.toDays(durationInMillis) % 30;
        long hours = TimeUnit.MILLISECONDS.toHours(durationInMillis) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis) % 60;

        StringBuilder formattedDuration = new StringBuilder();

        if (years > 0) {
            formattedDuration.append(years).append("年 ");
        }

        if (months > 0) {
            formattedDuration.append(months).append("月 ");
        }

        if (days > 0) {
            formattedDuration.append(days).append("日 ");
        }

        if (hours > 0) {
            formattedDuration.append(hours).append("小時 ");
        }

        if (minutes > 0) {
            formattedDuration.append(minutes).append("分鐘 ");
        }

        if (seconds > 0 || durationInMillis == 0) {
            formattedDuration.append(seconds).append("秒");
        }

        return formattedDuration.toString().trim();
    }
}
