package rip.diamond.maid.chat;

public class ChatConfigMock implements ChatConfig{

    private int chatDelay = 0;
    private boolean chatMuted = false;

    @Override
    public int getChatDelay() {
        return chatDelay;
    }

    @Override
    public void setChatDelay(int delay) {
        chatDelay = delay;
    }

    @Override
    public boolean isChatMuted() {
        return chatMuted;
    }

    @Override
    public void setChatMuted(boolean muted) {
        chatMuted = muted;
    }
}
