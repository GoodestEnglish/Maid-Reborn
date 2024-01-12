package rip.diamond.maid.server.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.server.IServer;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.ItemBuilder;
import rip.diamond.maid.util.TimeUtil;
import rip.diamond.maid.util.WordUtil;
import rip.diamond.maid.util.menu.MenuType;
import rip.diamond.maid.util.menu.buttons.Button;
import rip.diamond.maid.util.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;

public class ServersMenu extends PaginatedMenu {
    public ServersMenu(Player player) {
        super(player, 27);
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.INVENTORY;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        for (IServer server : Maid.INSTANCE.getServerManager().getServers().values()) {
            buttons.put(buttons.size(), new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(getWoolByTPS(server.getTps()[0]))
                            .name(getColorByTPS(server.getTps()[0]) + server.getID())
                            .lore(
                                    CC.WHITE + " 啟動時間: " + CC.AQUA + TimeUtil.formatDate(server.getStartupTime()) + CC.GRAY + " (" + TimeUtil.formatDuration(server.getUpTime()) + "前)",
                                    CC.WHITE + " TPS: " + getFormattedTPS(server.getTps()[0]) + CC.GRAY + ", " + getFormattedTPS(server.getTps()[1]) + CC.GRAY + ", " + getFormattedTPS(server.getTps()[2]),
                                    "",
                                    CC.WHITE + " 已加載: " + WordUtil.translate(server.isLoaded(), CC.GREEN + "是", CC.RED + "否"),
                                    CC.WHITE + " 白名單: " + WordUtil.translate(server.isWhiteListed(), CC.GREEN + "開啟", CC.RED + "關閉"),
                                    "",
                                    CC.WHITE + " 線上人數: " + CC.AQUA + server.getOnlinePlayers().size(),
                                    CC.WHITE + " 最大人數: " + CC.AQUA + server.getMaxPlayers(),
                                    "",
                                    CC.WHITE + " 聊天室狀態: " + WordUtil.translate(server.isChatMuted(), CC.RED + "關閉", CC.GREEN + "開啟"),
                                    CC.WHITE + " 聊天室延遲: " + CC.AQUA + server.getChatDelay() + "秒",
                                    ""
                            )
                            .build();
                }
            });
        }
        return buttons;
    }

    @Override
    public String getPaginatedTitle() {
        return "伺服器一覽";
    }

    private Material getWoolByTPS(double tps) {
        if (tps > 18.0) {
            return Material.LIME_WOOL;
        } else if (tps > 16.0) {
            return Material.YELLOW_WOOL;
        } else {
            return Material.RED_WOOL;
        }
    }

    private CC getColorByTPS(double tps) {
        if (tps > 18.0) {
            return CC.GREEN;
        } else if (tps > 16.0) {
            return CC.YELLOW;
        } else {
            return CC.RED;
        }
    }

    private String getFormattedTPS(double tps) {
        return getColorByTPS(tps).toString() + Maid.FORMAT.format(tps);
    }
}
