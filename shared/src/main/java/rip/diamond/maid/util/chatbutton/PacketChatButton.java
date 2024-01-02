package rip.diamond.maid.util.chatbutton;

import rip.diamond.maid.redis.messaging.Packet;
import rip.diamond.maid.util.json.GsonProvider;

public class PacketChatButton extends ChatButton {
    public PacketChatButton(Packet packet) {
        super("[" + packet.getClass().getSimpleName() + "]", GsonProvider.GSON.toJson(packet), null, null);
    }
}
