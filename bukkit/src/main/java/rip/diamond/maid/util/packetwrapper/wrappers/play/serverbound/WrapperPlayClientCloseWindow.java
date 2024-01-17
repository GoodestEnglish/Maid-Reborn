package rip.diamond.maid.util.packetwrapper.wrappers.play.serverbound;

import rip.diamond.maid.util.packetwrapper.wrappers.AbstractPacket;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayClientCloseWindow extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Client.CLOSE_WINDOW;

    public WrapperPlayClientCloseWindow() {
        super(TYPE);
    }

    public WrapperPlayClientCloseWindow(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieves the value of field 'containerId'
     *
     * @return 'containerId'
     */
    public int getContainerId() {
        return this.handle.getIntegers().read(0);
    }

    /**
     * Sets the value of field 'containerId'
     *
     * @param value New value for field 'containerId'
     */
    public void setContainerId(int value) {
        this.handle.getIntegers().write(0, value);
    }

}
