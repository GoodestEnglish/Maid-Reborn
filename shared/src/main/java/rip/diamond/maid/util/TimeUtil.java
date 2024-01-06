package rip.diamond.maid.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    public static final long PERMANENT = -1;
    public static final List<String> TIME_OPTIONS = List.of("5s", "30m", "1h", "6h", "12h", "1d", "3d", "7d", "14d", "30d", "90d", "1y", "永久");

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

    public static long getDuration(String input) {
        try {
            input = input.toLowerCase();

            if (input.equals(Long.toString(PERMANENT))) {
                return PERMANENT;
            }

            long result = 0L;
            StringBuilder number = new StringBuilder();

            for (int i = 0; i < input.length(); ++i) {
                char c = input.charAt(i);

                if (Character.isDigit(c)) {
                    number.append(c);
                } else if (Character.isLetter(c)) {
                    result += convert(Integer.parseInt(number.toString()), c);
                    number.setLength(0);
                }
            }

            return result;
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    public static long convert(int value, char charType) {
        return switch (charType) {
            case 'y' -> TimeUnit.DAYS.toMillis(365L) * value;
            case 'M' -> TimeUnit.DAYS.toMillis(30L) * value;
            case 'w' -> TimeUnit.DAYS.toMillis(7L) * value;
            case 'd' -> TimeUnit.DAYS.toMillis(1L) * value;
            case 'h' -> TimeUnit.HOURS.toMillis(1L) * value;
            case 'm' -> TimeUnit.MINUTES.toMillis(1L) * value;
            case 's' -> TimeUnit.SECONDS.toMillis(1L) * value;
            default -> -1L;
        };
    }
}
