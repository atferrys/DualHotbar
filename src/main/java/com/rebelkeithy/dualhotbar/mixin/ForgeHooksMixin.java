package com.rebelkeithy.dualhotbar.mixin;

import com.rebelkeithy.dualhotbar.DualHotbarMod;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.common.ForgeHooks;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ForgeHooks.class)
public class ForgeHooksMixin {

    @Redirect(
            method = "onPickBlock",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/InventoryPlayer;currentItem:I",
                    opcode = Opcodes.GETFIELD
            )
    )
    private static int dualhotbar$redirectCurrentItem(InventoryPlayer inventory) {
        return DualHotbarMod.inventorySlotOffset(inventory.currentItem);
    }

}
