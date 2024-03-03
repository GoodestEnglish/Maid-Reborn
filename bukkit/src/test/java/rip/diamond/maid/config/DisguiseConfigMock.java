package rip.diamond.maid.config;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class DisguiseConfigMock implements DisguiseConfig {
    @Override
    public List<String> getDisguiseSkins() {
        return ImmutableList.of("XiaoNiu_TW", "BedlessNoob", "YuseiFudo", "DULINTW", "Fauzh", "LU__LU", "DragonL");
    }
}
