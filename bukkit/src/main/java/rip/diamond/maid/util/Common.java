package rip.diamond.maid.util;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Common {

    public static void log(String... str) {
        for (String string : str) {
            Bukkit.getConsoleSender().sendMessage(Common.text("<red>[LOG] " + string));
        }
    }

    public static void debug(String... str) {
        for (String string : str) {
            Bukkit.getConsoleSender().sendMessage(Common.text("<red>[DEBUG] " + string));
        }
    }

    public static void broadcastMessage(String... str) {
        for (String string : str) {
            Bukkit.broadcast(Common.text(string));
        }
    }

    public static void broadcastMessage(List<String> str) {
        for (String string : str) {
            Bukkit.broadcast(Common.text(string));
        }
    }

    public static void broadcastPermissionMessage(String permission, String... str) {
        for (String message : str) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission(permission)) {
                    sendMessage(player, str);
                }
            }
            Bukkit.getConsoleSender().sendMessage(Common.text(message));
        }
    }

    public static void broadcastPermissionMessage(String permission, List<String> str) {
        for (String message : str) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (permission == null || player.hasPermission(permission)) {
                    sendMessage(player, str);
                }
            }
            Bukkit.getConsoleSender().sendMessage(Common.text(message));
        }
    }

    public static void broadcastSound(Sound sound) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, 10, 1);
        }
    }

    public static void broadcastSound(Sound sound, float v, float v1) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, v, v1);
        }
    }

    public static void sendMessage(Audience audience, String... str) {
        if (audience == null) {
            return;
        }
        for (String s : str) {
            if (s == null) {
                continue;
            }
            audience.sendMessage(Common.text(s));
        }
    }

    public static void sendMessage(Audience audience, Component... components) {
        if (audience == null) {
            return;
        }
        for (Component component : components) {
            if (component == null) {
                continue;
            }
            audience.sendMessage(component);
        }
    }

    public static void sendMessage(Audience audience, List<String> str) {
        if (audience == null) {
            return;
        }
        for (String s : str) {
            if (s == null) {
                continue;
            }
            audience.sendMessage(Common.text(s));
        }
    }

    public static void playSound(Player player, Sound sound) {
        if (player == null) {
            return;
        }
        player.playSound(player.getLocation(), sound, 1f, 1f);
    }

    public static void playSound(Player player, net.kyori.adventure.sound.Sound sound) {
        if (player == null) {
            return;
        }
        if (sound == null) {
            return;
        }
        player.playSound(sound);
    }

    public static void playSound(Player player, Sound sound, float v, float v1) {
        if (player == null) {
            return;
        }
        player.playSound(player.getLocation(), sound, v, v1);
    }

    public static boolean hasPermission(CommandSender sender, List<String> permissions) {
        for (String permission : permissions) {
            if (sender.hasPermission(permission)) return true;
        }
        return false;
    }

    public static Component text(String text) {
        return MiniMessage.miniMessage().deserialize(text);
    }

    public static String translate(String text) {
        return "<lang:" + text + ">";
    }

}
