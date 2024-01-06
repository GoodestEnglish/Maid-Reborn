package rip.diamond.maid.command;

import org.bukkit.entity.Player;
import rip.diamond.maid.MaidAPI;
import rip.diamond.maid.MaidPermission;
import rip.diamond.maid.redis.messaging.PacketHandler;
import rip.diamond.maid.redis.packets.bukkit.PacketTestPacket;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;
import rip.diamond.maid.util.extend.MaidCommand;

@Require(MaidPermission.PACKET_TEST)
public class PacketTestCommand extends MaidCommand {

    @Command(name = "", desc = "測試封包API是否正常運作")
    public void root(@Sender Player player) {
        PacketHandler.send(new PacketTestPacket(MaidAPI.INSTANCE.getPlatform().getServerID(), player.getUniqueId(), CC.LIME_GREEN + "PacketAPI 正在運作!"));
    }

}
