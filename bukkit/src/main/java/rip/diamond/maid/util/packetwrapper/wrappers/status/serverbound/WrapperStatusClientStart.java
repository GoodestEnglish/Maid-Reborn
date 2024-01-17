package rip.diamond.maid.util.packetwrapper.wrappers.status.serverbound;

import rip.diamond.maid.util.packetwrapper.wrappers.AbstractPacket;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperStatusClientStart extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Status.Client.START;

    public WrapperStatusClientStart() {
        super(TYPE);
    }

    public WrapperStatusClientStart(PacketContainer packet) {
        super(packet, TYPE);
    }


}
