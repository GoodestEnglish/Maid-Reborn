package rip.diamond.maid.punishment.menu;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.diamond.maid.Maid;
import rip.diamond.maid.api.user.IPunishment;
import rip.diamond.maid.api.user.IUser;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.ItemBuilder;
import rip.diamond.maid.util.TimeUtil;
import rip.diamond.maid.util.menu.Menu;
import rip.diamond.maid.util.menu.MenuType;
import rip.diamond.maid.util.menu.buttons.Button;
import rip.diamond.maid.util.menu.buttons.ConversationButton;
import rip.diamond.maid.util.menu.pagination.PaginatedMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class PunishmentsMenu extends PaginatedMenu {

    private final IUser target;

    public PunishmentsMenu(Player player, IUser target) {
        super(player, 27);
        this.target = target;
    }

    public PunishmentsMenu(Player player, Menu backMenu, IUser target) {
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
        for (IPunishment punishment : target.getPunishments()) {
            buttons.put(buttons.size(), new PunishmentButton(punishment));
        }
        return buttons;
    }

    @Override
    public Map<Integer, Button> getNavigationBar() {
        Map<Integer, Button> buttons = super.getNavigationBar();
        buttons.put(getSize() - 5, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.PLAYER_HEAD)
                        .texture(target.getTexture())
                        .name(CC.AQUA + "懲罰紀錄")
                        .lore(
                                "",
                                CC.WHITE + " 警告次數: " + CC.AQUA + target.getPunishments(ImmutableList.of(IPunishment.PunishmentType.WARN)).size(),
                                CC.WHITE + " 踢除次數: " + CC.AQUA + target.getPunishments(ImmutableList.of(IPunishment.PunishmentType.KICK)).size(),
                                CC.WHITE + " 禁言次數: " + CC.AQUA + target.getPunishments(ImmutableList.of(IPunishment.PunishmentType.MUTE)).size(),
                                CC.WHITE + " 封鎖次數: " + CC.AQUA + target.getPunishments(ImmutableList.of(IPunishment.PunishmentType.BAN)).size(),
                                CC.WHITE + " IP封鎖次數: " + CC.AQUA + target.getPunishments(ImmutableList.of(IPunishment.PunishmentType.IP_BAN)).size(),
                                ""
                        )
                        .build();
            }
        });
        return buttons;
    }

    @Override
    public String getPaginatedTitle() {
        return "懲罰紀錄 - " + target.getRealName();
    }

    @RequiredArgsConstructor
    class PunishmentButton extends ConversationButton {
        private final IPunishment punishment;

        @Override
        public String getInstruction() {
            return "請在聊天室輸入移除的原因";
        }

        @Override
        public BiConsumer<ConversationContext, String> getAction() {
            return (cc, reason) -> {
                switch (punishment.getType()) {
                    case MUTE -> Maid.INSTANCE.getPunishmentManager().unmute(player, punishment, reason);
                    case BAN, IP_BAN -> Maid.INSTANCE.getPunishmentManager().unban(player, punishment, reason);
                    default -> throw new NullPointerException("Cannot find punishment type " + punishment.getType().name());
                }
                updateMenu();
            };
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder builder;
            if (punishment.isActive()) {
                builder = new ItemBuilder(Material.LIME_WOOL)
                        .name(CC.LIME_GREEN + "(有效) " + TimeUtil.formatDate(punishment.getIssuedAt()))
                        .lore(
                                " ",
                                CC.WHITE + " 懲罰類型: " + CC.AQUA + punishment.getType().getName(),
                                CC.WHITE + " 持續時間: " + CC.AQUA + TimeUtil.formatDuration(punishment.getDuration()) + (punishment.getDuration() != TimeUtil.PERMANENT ? CC.GRAY + " (剩餘" + TimeUtil.formatDuration(punishment.getIssuedAt() + punishment.getDuration() - System.currentTimeMillis()) + ")" : ""),
                                "",
                                CC.WHITE + " 執行者: " + CC.AQUA + punishment.getIssuerName(),
                                CC.WHITE + " 執行原因: " + CC.AQUA + punishment.getReason()
                        );
                if (punishment.getType() == IPunishment.PunishmentType.MUTE || punishment.getType() == IPunishment.PunishmentType.BAN || punishment.getType() == IPunishment.PunishmentType.IP_BAN) {
                    builder.lore("", CC.YELLOW + "點擊移除本次懲罰紀錄!");
                }
            } else {
                builder = new ItemBuilder(Material.RED_WOOL)
                        .name(CC.RED + "(無效) " + TimeUtil.formatDate(punishment.getIssuedAt()))
                        .lore(
                                CC.WHITE + " 懲罰類型: " + CC.AQUA + punishment.getType().getName(),
                                CC.WHITE + " 持續時間: " + CC.AQUA + TimeUtil.formatDuration(punishment.getDuration()),
                                "",
                                CC.WHITE + " 執行者: " + CC.AQUA + punishment.getIssuerName(),
                                CC.WHITE + " 執行原因: " + CC.AQUA + punishment.getReason(),
                                "",
                                CC.WHITE + " 移除者: " + CC.AQUA + punishment.getRevokerName(),
                                CC.WHITE + " 移除原因: " + CC.AQUA + punishment.getRevokedReason(),
                                CC.WHITE + " 移除時間: " + CC.AQUA + TimeUtil.formatDate(punishment.getRevokedAt())
                        );
            }
            return builder.build();
        }

        @Override
        public boolean isAllowClick() {
            if (!punishment.isActive()) {
                return false;
            }
            if (punishment.getType() == IPunishment.PunishmentType.KICK || punishment.getType() == IPunishment.PunishmentType.WARN) {
                return false;
            }
            return super.isAllowClick();
        }
    }
}
