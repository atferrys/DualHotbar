package com.rebelkeithy.dualhotbar.mixin;

import net.minecraft.entity.player.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static com.rebelkeithy.dualhotbar.config.DualHotbarConfig.hotbarsNumber;

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
        return hotbarsNumber * 9;
    }

}
