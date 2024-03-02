package rip.diamond.maid.command;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.diamond.maid.IMaidAPI;
import rip.diamond.maid.Maid;
import rip.diamond.maid.redis.packets.bukkit.PacketTestPacket;
import rip.diamond.maid.util.CC;
import rip.diamond.maid.util.MaidPermission;
import rip.diamond.maid.util.command.annotation.Command;
import rip.diamond.maid.util.command.annotation.Require;
import rip.diamond.maid.util.command.annotation.Sender;
import rip.diamond.maid.util.extend.MaidCommand;

@RequiredArgsConstructor
@Require(MaidPermission.PACKET_TEST)
public class PacketTestCommand extends MaidCommand {

    private final IMaidAPI api;

    @Command(name = "", desc = "測試封包API是否正常運作")
    public void root(@Sender Player player) {
        api.getPacketHandler().send(new PacketTestPacket(Maid.API.getPlatform().getServerID(), player.getUniqueId(), CC.LIME_GREEN + "PacketAPI 正在運作!"));
    }

}
