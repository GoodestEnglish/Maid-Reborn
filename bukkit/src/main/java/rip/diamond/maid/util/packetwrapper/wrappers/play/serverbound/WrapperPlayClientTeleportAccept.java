package rip.diamond.maid.util.packetwrapper.wrappers.play.serverbound;

import rip.diamond.maid.util.packetwrapper.wrappers.AbstractPacket;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayClientTeleportAccept extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Client.TELEPORT_ACCEPT;

    public WrapperPlayClientTeleportAccept() {
        super(TYPE);
    }

    public WrapperPlayClientTeleportAccept(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieves the value of field 'id'
     *
     * @return 'id'
     */
    public int getId() {
        return this.handle.getIntegers().read(0);
    }

    /**
     * Sets the value of field 'id'
     *
     * @param value New value for field 'id'
     */
    public void setId(int value) {
        this.handle.getIntegers().write(0, value);
    }

}
