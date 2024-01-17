package rip.diamond.maid.util.packetwrapper.wrappers.play.serverbound;

import rip.diamond.maid.util.packetwrapper.wrappers.AbstractPacket;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.Difficulty;

public class WrapperPlayClientDifficultyChange extends AbstractPacket {

    public static final PacketType TYPE = PacketType.Play.Client.DIFFICULTY_CHANGE;

    public WrapperPlayClientDifficultyChange() {
        super(TYPE);
    }

    public WrapperPlayClientDifficultyChange(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieves the value of field 'difficulty'
     *
     * @return 'difficulty'
     */
    public Difficulty getDifficulty() {
        return this.handle.getDifficulties().read(0);
    }

    /**
     * Sets the value of field 'difficulty'
     *
     * @param value New value for field 'difficulty'
     */
    public void setDifficulty(Difficulty value) {
        this.handle.getDifficulties().write(0, value);
    }

}
