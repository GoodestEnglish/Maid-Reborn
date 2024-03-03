package rip.diamond.maid.config;

import lombok.RequiredArgsConstructor;
import rip.diamond.maid.MaidAPIMock;

@RequiredArgsConstructor
public class ServerConfigMock implements ServerConfig {

    private final MaidAPIMock api;

    @Override
    public String getServerID() {
        return api.getPlatform().getServerID();
    }
}
