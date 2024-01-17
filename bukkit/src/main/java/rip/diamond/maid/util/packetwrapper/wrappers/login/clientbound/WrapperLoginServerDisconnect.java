package rip.diamond.maid.util.packetwrapper.wrappers.login.clientbound;

import rip.diamond.maid.util.packetwrapper.wrappers.AbstractPacket;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class WrapperLoginServerDisconnect extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Login.Server.DISCONNECT;

    public WrapperLoginServerDisconnect() {
        super(TYPE);
    }

    public WrapperLoginServerDisconnect(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieves the value of field 'reason'
     *
     * @return 'reason'
     */
    public WrappedChatComponent getReason() {
        return this.handle.getChatComponents().read(0);
    }

    /**
     * Sets the value of field 'reason'
     *
     * @param value New value for field 'reason'
     */
    public void setReason(WrappedChatComponent value) {
        this.handle.getChatComponents().write(0, value);
    }

}
