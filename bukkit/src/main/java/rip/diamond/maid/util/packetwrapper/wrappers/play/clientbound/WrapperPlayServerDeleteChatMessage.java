package rip.diamond.maid.util.packetwrapper.wrappers.play.clientbound;

import rip.diamond.maid.util.packetwrapper.wrappers.AbstractPacket;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerDeleteChatMessage extends AbstractPacket {

    /**
     * The packet type that is wrapped by this wrapper.
     */
    public static final PacketType TYPE = PacketType.Play.Server.DELETE_CHAT_MESSAGE;

    /**
     * Constructs a new wrapper and initialize it with a packet handle with default values
     */
    public WrapperPlayServerDeleteChatMessage() {
        super(TYPE);
    }

    public WrapperPlayServerDeleteChatMessage(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieves the value of field 'messageSignature'
     * ProtocolLib currently does not provide a wrapper for this type. Access to this type is only provided by an InternalStructure
     *
     * @return 'messageSignature'
     */
    public InternalStructure getMessageSignatureInternal() {
        return this.handle.getStructures().read(0);
    }

    /**
     * Sets the value of field 'messageSignature'
     * ProtocolLib currently does not provide a wrapper for this type. Access to this type is only provided by an InternalStructure
     *
     * @param value New value for field 'messageSignature'
     */
    public void setMessageSignatureInternal(InternalStructure value) {
        this.handle.getStructures().write(0, value);
    }

}
