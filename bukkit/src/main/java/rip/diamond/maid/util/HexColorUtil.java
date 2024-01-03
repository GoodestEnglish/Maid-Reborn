package rip.diamond.maid.util;

import org.bukkit.DyeColor;

import java.util.regex.Pattern;

public class HexColorUtil {

    public static boolean isHexColor(String input) {
        // Regular expression pattern for hex color
        String hexColorPattern = "^#?([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$";

        // Compile the pattern
        Pattern pattern = Pattern.compile(hexColorPattern);

        // Check if the input matches the pattern
        return pattern.matcher(input).matches();
    }

    public static DyeColor getNearestWoolColor(String hexColor) {
        // Convert hex color to RGB
        int rgb = Integer.parseInt(hexColor.substring(1), 16);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        // Find the nearest wool color
        return getNearestWoolColor(red, green, blue);
    }

    public static DyeColor getNearestWoolColor(int red, int green, int blue) {
        double minDistance = Double.MAX_VALUE;
        DyeColor nearestColor = null;

        for (DyeColor dyeColor : DyeColor.values()) {
            int woolRed = dyeColor.getColor().getRed();
            int woolGreen = dyeColor.getColor().getGreen();
            int woolBlue = dyeColor.getColor().getBlue();

            double distance = Math.sqrt(Math.pow(woolRed - red, 2) + Math.pow(woolGreen - green, 2) + Math.pow(woolBlue - blue, 2));;

            if (distance < minDistance) {
                minDistance = distance;
                nearestColor = dyeColor;
            }
        }

        return nearestColor;
    }

}
