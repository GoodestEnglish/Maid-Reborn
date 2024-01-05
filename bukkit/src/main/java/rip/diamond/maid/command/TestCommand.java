package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.Maid;
import rip.diamond.maid.disguise.Disguise;
import rip.diamond.maid.player.User;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.Common;
import rip.diamond.maid.util.CraftBukkitImplementation;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;

@Require("maid.command.test")
public class TestCommand {

    @Command(name = "1", desc = "測試")
    public void root(@Sender Player player) {
        User user = (User) Maid.INSTANCE.getUserManager().getUser(player.getUniqueId()).join();

        user.setDisguise(new Disguise(
                "Tom",
                "ewogICJ0aW1lc3RhbXAiIDogMTcwNDQ1MzIzMTgzNiwKICAicHJvZmlsZUlkIiA6ICI1NjBiYTgwMTdiMGY0MjU0OGFjM2RkNzU0Nzc0OWZhYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJGYXV6aCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yNGViM2FkNWZlMWU5YzI2ZTI4M2RmMjhlMzdhM2I1YjM4MDk3ZWUxNDYxNDBkNmUxMjk2OWI5ZjlhNjA3Mzg5IgogICAgfQogIH0KfQ==",
                "\"tVTHOHt1svEp4Wgv7F2F+aYFyM+mwUHB45lEZloSBCQkibMjFQjq7ZR1wH85bv9zuRGlAErAhZXh4Dkx6yRE+nXRtkyjHINZKybTZ1/CV+tyWKZqAIL24pXzp8JdJEgOJHdeijdFtWU7k3oyZiyNzQvMJK/2T3D8xm8STwZ2RmFe4ORyS/PMMaJiLgAH6DzIo7Xj4B6dm9LVHyvrjmqXq98B2kL0OwtDRbz4X3amvnhtUJyP0Uc3cjGF5vD52GlEFsgsgwgJFwDRb7NMB+oO5NdN7MTOWo081jdZgKSEP7GXaam3YE7J8NL2mkfExuQq5qvfWimnU9t7UL9ZIfHFIUiK1MehXfAA8ODI0AiVvsruKEsyh9EQpilvQ5Zb0LF5bzBW6w4Eb84OfhC8KBRo2mew3Mk8oRfwCnVWDMaGfQC2dy2QAM4am0FedIYmJnU4fO0cQRfUAb1fzqBM46fwN9I90a/yupBfjATBSbG0xmyyHAdMjP5cauMQnrP5HIf2j4XP28GyhSV150L0ESOObvUtXN9TfV4wHpQoEXeTOtZKt55uNQJNi73QKEHXjq32EL8gFQedgUz+05dm2EY03NFS2jUyWaxhJLzUsoQomGEUGOUhBEq9rmGhH+azTlbNdqLPm0qL7Xt66+pXjoOFfD3laOu+Fnem41ZjQIkmJOo=",
                Maid.INSTANCE.getRankManager().getDefaultRank().getUniqueID()
        ));
        Maid.INSTANCE.getUserManager().saveUser(user);
        Common.sendMessage(player, "<green>done");
    }

    @Command(name = "2", desc = "測試")
    public void root2(@Sender Player player) {
        User user = (User) Maid.INSTANCE.getUserManager().getUser(player.getUniqueId()).join();
        Common.log("Using version " + CraftBukkitImplementation.SERVER_PACKAGE_VERSION);
        Maid.INSTANCE.getDisguiseManager().disguise(player, user.getDisguise());
    }

    @Command(name = "3", desc = "測試")
    public void root3(@Sender Player player) {
        User user = (User) Maid.INSTANCE.getUserManager().getUser(player.getUniqueId()).join();
        Common.sendMessage(player, CC.PALE_YELLOW + "You " + (user.getDisguise() == null ? "dont have" : "have") + " disguise");
    }
}
