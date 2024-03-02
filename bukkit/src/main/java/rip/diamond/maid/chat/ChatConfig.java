package rip.diamond.maid.chat;

public interface ChatConfig {

    int getChatDelay();

    void setChatDelay(int delay);

    boolean isChatMuted();

    void setChatMuted(boolean muted);

}
