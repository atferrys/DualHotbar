package com.rebelkeithy.dualhotbar.mixin.compat.lemonskin;

import com.rebelkeithy.dualhotbar.config.DualHotbarConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import ua.myxazaur.lemonskin.client.HUDOverlayRenderer;

import static com.rebelkeithy.dualhotbar.client.RenderHandler.yCount;

/**
 * <p><a href="https://github.com/squeek502/AppleSkin/blob/1.12/java/squeek/appleskin/client/HUDOverlayHandler.java">Unlike AppleSkin</a>,
 * which uses on Forge's {@link net.minecraftforge.client.event.RenderGameOverlayEvent}s, LemonSkin uses Mixins to render
 * their custom overlays, and that doesn't account for the shifts done in {@link com.rebelkeithy.dualhotbar.client.RenderHandler},
 * as a workaround we're injecting our custom height offsets in their rendering methods.
 */
@Mixin(value = HUDOverlayRenderer.class, remap = false)
public class HUDOverlayRendererMixin {

    @ModifyVariable(
            method = "drawSaturationOverlay(FFLnet/minecraft/client/Minecraft;IIFI)V",
            at = @At("HEAD"),
            index = 4,
            argsOnly = true
    )
    private static int dualhotbar$modifySaturationTop(int top) {

        if(!DualHotbarConfig.enabled) {
            return top;
        }

        return top - 20 * (yCount() - 1);

    }

    @ModifyVariable(
            method = "drawHungerOverlay(IILnet/minecraft/client/Minecraft;IIFZI)V",
            at = @At("HEAD"),
            index = 4,
            argsOnly = true
    )
    private static int dualhotbar$modifyHungerTop(int top) {

        if(!DualHotbarConfig.enabled) {
            return top;
        }

        return top - 20 * (yCount() - 1);

    }

    @ModifyVariable(
            method = "drawHealthOverlay(FFLnet/minecraft/client/Minecraft;IIFI)V",
            at = @At("HEAD"),
            index = 4,
            argsOnly = true
    )
    private static int dualhotbar$modifyHealthTop(int top) {

        if(!DualHotbarConfig.enabled) {
            return top;
        }

        return top - 20 * (yCount() - 1);

    }

}
