package rip.diamond.maid.server.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.server.IGlobalUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.ItemBuilder;
import rip.diamond.maid.util.menu.MenuType;
import rip.diamond.maid.util.menu.buttons.Button;
import rip.diamond.maid.util.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;

public class PlayersMenu extends PaginatedMenu {
    public PlayersMenu(Player player) {
        super(player, 27);
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.INVENTORY;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        for (IGlobalUser globalUser : Maid.INSTANCE.getServerManager().getGlobalUsers().values()) {
            buttons.put(buttons.size(), new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.PLAYER_HEAD)
                            .texture(globalUser.getTexture())
                            .name(CC.AQUA + globalUser.getDisplayName())
                            .lore(
                                    "",
                                    CC.WHITE + " 伺服器: " + CC.AQUA + globalUser.getCurrentServer()
                            )
                            .build();
                }
            });
        }
        return buttons;
    }

    @Override
    public String getPaginatedTitle() {
        return "玩家一覽 - 全分流";
    }
}
