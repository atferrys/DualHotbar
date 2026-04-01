package com.rebelkeithy.dualhotbar.mixin.compat.simpledifficulty;

import com.charles445.simpledifficulty.client.gui.TemperatureGui;
import com.rebelkeithy.dualhotbar.config.DualHotbarConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.rebelkeithy.dualhotbar.client.RenderHandler.yCount;

@Mixin(value = TemperatureGui.class, remap = false)
public class TemperatureGuiMixin {

    @ModifyVariable(
            method = "renderTemperatureIcon",
            at = @At("HEAD"),
            ordinal = 1,
            argsOnly = true
    )
    private int dualhotbar$modifyHeight(int height) {

        if(!DualHotbarConfig.enabled) {
            return height;
        }

        return height - 20 * (yCount() - 1);

    }

    @ModifyVariable(
            method = "renderClassicTemperatureIcon",
            at = @At("HEAD"),
            ordinal = 1,
            argsOnly = true
    )
    private int dualhotbar$modifyClassicHeight(int height) {

        if(!DualHotbarConfig.enabled) {
            return height;
        }

        return height - 20 * (yCount() - 1);

    }

}
