package rip.diamond.maid.event;

import org.bukkit.event.*;
import org.jetbrains.annotations.NotNull;

public class BaseEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
