package rip.diamond.maid.player.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.player.User;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.ItemBuilder;
import rip.diamond.maid.util.menu.Menu;
import rip.diamond.maid.util.menu.MenuType;
import rip.diamond.maid.util.menu.buttons.Button;
import rip.diamond.maid.util.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AltsMenu extends PaginatedMenu {

    private final User target;

    public AltsMenu(Player player, User target) {
        super(player, 27);
        this.target = target;
    }

    public AltsMenu(Player player, Menu backMenu, User target) {
        super(player, 27, backMenu);
        this.target = target;
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.INVENTORY;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        for (UUID uuid : target.getAlts()) {
            buttons.put(buttons.size(), new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                    String name = offlinePlayer.getName();
                    return new ItemBuilder(Material.PLAYER_HEAD)
                            .name(CC.AQUA + uuid.toString())
                            .skull(name)
                            .lore(
                                    "",
                                    CC.WHITE + " 名字: " + CC.AQUA + (name == null ? "未能找到, 該玩家是否進入過本分流?" : name)
                            )
                            .build();
                }
            });
        }
        return buttons;
    }

    @Override
    public String getPaginatedTitle() {
        return "小號紀錄 - " + target.getRealName();
    }
}
