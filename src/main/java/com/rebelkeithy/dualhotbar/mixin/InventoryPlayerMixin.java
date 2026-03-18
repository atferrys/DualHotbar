package com.rebelkeithy.dualhotbar.mixin;

import com.rebelkeithy.dualhotbar.config.DualHotbarConfig;
import net.minecraft.entity.player.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(InventoryPlayer.class)
public class InventoryPlayerMixin {

    @ModifyConstant(
            method = {
                    "isHotbar",
                    "getHotbarSize",
                    "changeCurrentItem",
                    "getBestHotbarSlot"
            },
            constant = @Constant(intValue = 9)
    )
    private static int dualhotbar$replaceHotbarSize(int original) {

        if(!DualHotbarConfig.enabled) {
            return original;
        }

        return DualHotbarConfig.hotbarsNumber * 9;

    }

}
