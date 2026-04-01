package com.rebelkeithy.dualhotbar.mixin;

import net.minecraftforge.fml.common.Loader;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LateMixinLoader implements ILateMixinLoader {

    private static final List<String> COMPAT_MIXINS = Arrays.asList(
            "lemonskin"
    );

    @Override
    public List<String> getMixinConfigs() {
        return COMPAT_MIXINS.stream()
                .map(modCompat -> "mixins.dualhotbar.compat." + modCompat + ".json")
                .collect(Collectors.toList());
    }

    @Override
    public boolean shouldMixinConfigQueue(String mixinConfig) {

        String[] configPath = mixinConfig.split("\\.");

        // mixins.dualhotbar.compat.modid.json
        //    0       1        2      3

        if(configPath.length < 4) {
            return true;
        }

        return Loader.isModLoaded(configPath[3]);

    }

}
