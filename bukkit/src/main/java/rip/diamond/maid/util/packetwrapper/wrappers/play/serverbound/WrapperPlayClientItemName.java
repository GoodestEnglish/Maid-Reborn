package rip.diamond.maid.util.packetwrapper.wrappers.play.serverbound;

import rip.diamond.maid.util.packetwrapper.wrappers.AbstractPacket;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayClientItemName extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Client.ITEM_NAME;

    public WrapperPlayClientItemName() {
        super(TYPE);
    }

    public WrapperPlayClientItemName(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieves the value of field 'name'
     *
     * @return 'name'
     */
    public String getName() {
        return this.handle.getStrings().read(0);
    }

    /**
     * Sets the value of field 'name'
     *
     * @param value New value for field 'name'
     */
    public void setName(String value) {
        this.handle.getStrings().write(0, value);
    }

}
