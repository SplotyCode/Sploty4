package me.david.sploty4.setting.settings;

import me.david.sploty4.setting.CacheState;

public class RamCacheSettings extends CacheSettings {

    public RamCacheSettings() {
        super("Ram");
    }

    @Override
    public CacheState[] getValidCacheStates() {
        return new CacheState[]{CacheState.ALWAYS, CacheState.NEVER};
    }
}
