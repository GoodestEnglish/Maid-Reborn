package rip.diamond.maid.util.menu.buttons;

import org.bukkit.ChatColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import rip.diamond.maid.Maid;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;

import java.util.function.BiConsumer;

public abstract class ConversationButton extends Button {

    public abstract String getInstruction();

    public abstract BiConsumer<ConversationContext, String> getAction();

    public boolean isAllowClick() {
        return true;
    }

    @Override
    public void clicked(InventoryClickEvent event, Player player, ClickType clickType) {
        if (!isAllowClick()) {
            return;
        }
        player.closeInventory();
        ConversationFactory factory = new ConversationFactory(Maid.INSTANCE).withModality(true).withPrefix(new NullConversationPrefix()).withFirstPrompt(new StringPrompt() {
            @Override
            public @NotNull String getPromptText(@NotNull ConversationContext cc) {
                return ChatColor.YELLOW + getInstruction() + ChatColor.GRAY + " (輸入 " + ChatColor.RED + ChatColor.BOLD + "cancel" + ChatColor.RESET + ChatColor.GRAY + " 去強制終止過程)";
            }

            @Override
            public Prompt acceptInput(@NotNull ConversationContext cc, String s) {
                if (s.equalsIgnoreCase("cancel")) {
                    Common.sendMessage(player, CC.RED + "成功取消程序");
                    return Prompt.END_OF_CONVERSATION;
                }
                getAction().accept(cc, s);
                return Prompt.END_OF_CONVERSATION;
            }
        }).withLocalEcho(false).withTimeout(60).thatExcludesNonPlayersWithMessage("你是如何去到這裏的???");
        player.beginConversation(factory.buildConversation(player));
    }
}
