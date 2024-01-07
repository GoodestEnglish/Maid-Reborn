package rip.diamond.maid.chat;

import lombok.Getter;
import rip.diamond.maid.config.Config;
import rip.diamond.maid.util.extend.MaidManager;

@Getter
public class ChatManager extends MaidManager {
    private boolean muted;
    private int delay;

    public ChatManager() {
        this.muted = Config.CHAT_MUTED.toBoolean();
        this.delay = Config.CHAT_DELAY.toInteger();
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
        Config.CHAT_MUTED.setValue(muted);
    }

    public void setDelay(int delay) {
        this.delay = delay;
        Config.CHAT_DELAY.setValue(delay);
    }
}
