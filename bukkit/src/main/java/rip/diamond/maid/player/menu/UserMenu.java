package rip.diamond.maid.player.menu;

import com.google.common.collect.ImmutableList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.api.user.IPunishment;
import rip.diamond.maid.grant.menu.GrantsMenu;
import rip.diamond.maid.player.User;
import rip.diamond.maid.punishment.menu.PunishmentsMenu;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.ItemBuilder;
import rip.diamond.maid.util.TimeUtil;
import rip.diamond.maid.util.menu.Menu;
import rip.diamond.maid.util.menu.MenuType;
import rip.diamond.maid.util.menu.buttons.Button;

import java.util.HashMap;
import java.util.Map;

public class UserMenu extends Menu {

    private final User target;

    public UserMenu(Player player, User target) {
        super(player, 9);
        this.target = target;
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.INVENTORY;
    }

    @Override
    public String getTitle() {
        return "玩家資料 - " + target.getRealName();
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PLAYER_HEAD)
                        .name(CC.WHITE + target.getDisplayName(false))
                        .texture(target.getTexture())
                        .lore(
                                "",
                                CC.WHITE + " 職階: " + CC.AQUA + target.getRealRank().getDisplayName(true),
                                "",
                                CC.WHITE + " 首次進入: " + CC.AQUA + TimeUtil.formatDate(target.getFirstSeen()),
                                CC.WHITE + " 最後進入: " + CC.AQUA + TimeUtil.formatDate(target.getLastSeen()),
                                CC.WHITE + " 最後進入伺服器: " + CC.AQUA + target.getLastServer()
                        )
                        .build();
            }
        });
        buttons.put(2, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                ItemBuilder builder = new ItemBuilder(Material.MAP)
                        .name(CC.AQUA + "IP位址紀錄")
                        .lore(
                                "",
                                CC.WHITE + " 最後紀錄IP: " + CC.AQUA + target.getIP(),
                                CC.WHITE + " 歷史紀錄IP: "
                        );
                builder.lore(target.getIPHistory().stream().map(str -> CC.GRAY + "  • " + CC.AQUA + str).toList());
                return builder.build();
            }
        });
        buttons.put(3, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.CLOCK)
                        .name(CC.AQUA + "權限")
                        .lore(
                                "",
                                CC.WHITE + " 個人權限: " + CC.AQUA + target.getPermissions().size(),
                                CC.WHITE + " 職階權限: " + CC.AQUA + target.getRealRank().getAllPermissions().size(),
                                "",
                                CC.YELLOW + "點擊查看所有權限"
                        )
                        .build();
            }

            @Override
            public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
                new PermissionsMenu(player, UserMenu.this, target).updateMenu();
            }
        });
        buttons.put(4, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.FEATHER)
                        .name(CC.AQUA + "職階升級紀錄")
                        .lore(
                                "",
                                CC.WHITE + " 有效升級紀錄: " + CC.GREEN + target.getActiveGrants().size(),
                                CC.WHITE + " 無效升級紀錄: " + CC.RED + (target.getGrants().size() - target.getActiveGrants().size()),
                                "",
                                CC.YELLOW + "點擊查看所有升級紀錄"
                        )
                        .build();
            }

            @Override
            public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
                new GrantsMenu(player, UserMenu.this, target).updateMenu();
            }
        });
        buttons.put(5, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.BARRIER)
                        .name(CC.AQUA + "懲罰紀錄")
                        .lore(
                                "",
                                CC.WHITE + " 警告次數: " + CC.AQUA + target.getPunishments(ImmutableList.of(IPunishment.PunishmentType.WARN)).size(),
                                CC.WHITE + " 踢除次數: " + CC.AQUA + target.getPunishments(ImmutableList.of(IPunishment.PunishmentType.KICK)).size(),
                                CC.WHITE + " 禁言次數: " + CC.AQUA + target.getPunishments(ImmutableList.of(IPunishment.PunishmentType.MUTE)).size(),
                                CC.WHITE + " 封鎖次數: " + CC.AQUA + target.getPunishments(ImmutableList.of(IPunishment.PunishmentType.BAN)).size(),
                                CC.WHITE + " IP封鎖次數: " + CC.AQUA + target.getPunishments(ImmutableList.of(IPunishment.PunishmentType.IP_BAN)).size(),
                                "",
                                CC.YELLOW + "點擊查看所有懲罰紀錄"
                        )
                        .build();
            }

            @Override
            public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
                new PunishmentsMenu(player, UserMenu.this, target).updateMenu();
            }
        });
        return buttons;
    }
}
