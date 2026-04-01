package com.rebelkeithy.dualhotbar.mixin.early;

import com.rebelkeithy.dualhotbar.config.DualHotbarConfig;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ForgeHooks.class)
public class ForgeHooksMixin {

    @Redirect(
            method = "onPickBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;sendSlotPacket(Lnet/minecraft/item/ItemStack;I)V"
            )
    )
    private static void dualHotbar$sendSlotPacket(PlayerControllerMP player, ItemStack stack, int slot) {

        if(!DualHotbarConfig.enabled) {
            player.sendSlotPacket(stack, slot);
            return;
        }

        int rawSlot = slot - 36;
        int newSlot = rawSlot >= 9 ? rawSlot : slot;
        player.sendSlotPacket(stack, newSlot);

    }

}
