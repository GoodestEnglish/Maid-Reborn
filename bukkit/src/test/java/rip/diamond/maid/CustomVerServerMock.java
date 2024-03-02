package rip.diamond.maid;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.command.MockCommandMap;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandMap;

public class CustomVerServerMock extends ServerMock {

    private CommandMap commandMap = new MockCommandMap(this);

    @Override
    public void broadcast(BaseComponent p0) {
    }

    @Override
    public void broadcast(BaseComponent... p0) {
    }

}

