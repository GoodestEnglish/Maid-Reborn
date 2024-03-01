package rip.diamond.maid.grant.menu;

import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.Maid;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.api.user.IRank;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.grant.Grant;
import rip.diamond.maid.player.User;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.PermissionUpdatePacket;
import rip.diamond.maid.util.*;
import rip.diamond.maid.util.menu.Menu;
import rip.diamond.maid.util.menu.MenuType;
import rip.diamond.maid.util.menu.buttons.Button;
import rip.diamond.maid.util.menu.buttons.ChooseButton;
import rip.diamond.maid.util.menu.buttons.ConversationButton;
import rip.diamond.maid.util.recorder.RankChangesRecorder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GrantMenu extends Menu {

    private final User target;
    private IRank rank = Maid.INSTANCE.getRankManager().getDefaultRank();
    private String duration = TimeUtil.TIME_OPTIONS.get(0);
    private String reason = "";

    public GrantMenu(Player player, User target) {
        super(player, 27);
        this.target = target;
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.INVENTORY;
    }

    @Override
    public String getTitle() {
        return "職階升級";
    }

    @Override
    public Map<Integer, Button> getButtons() {
        final Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(10, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PLAYER_HEAD)
                        .name(target.getSimpleDisplayName(false))
                        .texture(target.getTexture())
                        .lore("", CC.GRAY + CC.ITALIC.toString() + "你正在替" + target.getRealName() + "升級職階", "")
                        .build();
            }
        });
        buttons.put(12, new ChooseButton<>(this) {
            @Override
            public String getName() {
                return CC.AQUA + "設置職階";
            }

            @Override
            public Material getIcon() {
                return Material.valueOf(HexColorUtil.getNearestWoolColor(rank.getColor()).name() + "_WOOL");
            }

            @Override
            public Function<GrantMenu, String> read() {
                return (menu) -> {
                    if (menu != GrantMenu.this) {
                        throw new RuntimeException("Menu mismatch while trying to read the value");
                    }
                    return rank.getName();
                };
            }

            @Override
            public BiConsumer<GrantMenu, String> write() {
                return (menu, option) -> {
                    if (menu != GrantMenu.this) {
                        throw new RuntimeException("Menu mismatch while trying to read the value");
                    }
                    rank = Maid.INSTANCE.getRankManager().getRank(option);
                    updateMenu();
                };
            }

            @Override
            public List<String> getOptions() {
                return Maid.INSTANCE.getRankManager().getRanksInOrder().stream().map(IRank::getName).toList();
            }
        });
        buttons.put(13, new ChooseButton<>(this) {
            @Override
            public String getName() {
                return CC.AQUA + "設置持續時間";
            }

            @Override
            public Material getIcon() {
                return Material.CLOCK;
            }

            @Override
            public Function<GrantMenu, String> read() {
                return (menu) -> {
                    if (menu != GrantMenu.this) {
                        throw new RuntimeException("Menu mismatch while trying to read the value");
                    }
                    return duration.equals("-1") ? "永久" : duration;
                };
            }

            @Override
            public BiConsumer<GrantMenu, String> write() {
                return (menu, option) -> {
                    if (menu != GrantMenu.this) {
                        throw new RuntimeException("Menu mismatch while trying to read the value");
                    }
                    duration = option.equals("永久") ? "-1" : option;
                    updateMenu();
                };
            }

            @Override
            public List<String> getOptions() {
                return TimeUtil.TIME_OPTIONS;
            }

            @Override
            public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
                super.clicked(event, player, clickType);
            }
        });
        buttons.put(14, new ConversationButton() {
            @Override
            public String getInstruction() {
                return "請輸入升級原因";
            }

            @Override
            public BiConsumer<ConversationContext, String> getAction() {
                return ((cc, str) -> {
                    reason = str;
                    updateMenu();
                });
            }

            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.OAK_SIGN)
                        .name(CC.AQUA + "設置原因")
                        .lore(
                                "",
                                CC.WHITE + " 現時原因: " + CC.AQUA + reason,
                                "",
                                CC.YELLOW + "點擊設置"
                        )
                        .build();
            }
        });
        buttons.put(16, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.ENCHANTED_BOOK)
                        .name(CC.AQUA + "確認升級")
                        .lore("",
                                CC.WHITE + " 職階: " + CC.AQUA + rank.getName(),
                                CC.WHITE + " 時間: " + CC.AQUA + TimeUtil.formatDuration(TimeUtil.getDuration(duration)),
                                CC.WHITE + " 原因: " + CC.AQUA + reason,
                                "",
                                CC.YELLOW + "點擊確認"
                        )
                        .build();
            }

            @Override
            public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
                player.closeInventory();
                IUser user = Maid.INSTANCE.getUserManager().getUserNow(player.getUniqueId());

                RankChangesRecorder recorder = new RankChangesRecorder(target.getRealRank());

                target.addGrant(new Grant(target, rank, user, reason, System.currentTimeMillis(), TimeUtil.getDuration(duration)));
                Maid.INSTANCE.getUserManager().saveUser(target);
                PacketHandler.send(new PermissionUpdatePacket(Maid.API.getPlatform().getServerID(), target.getUniqueID()));

                Common.sendMessage(player, CC.GREEN + "成功替 " + target.getRealName() + " 升級到 " + rank.getName());

                recorder.recordNewRank(target.getRealRank());
                recorder.outputChanges(target.getUniqueID());
            }
        });
        return buttons;
    }
}
