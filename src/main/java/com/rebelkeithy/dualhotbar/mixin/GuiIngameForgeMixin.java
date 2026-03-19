package com.rebelkeithy.dualhotbar.mixin;

import com.rebelkeithy.dualhotbar.config.DualHotbarConfig;
import net.minecraftforge.client.GuiIngameForge;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.rebelkeithy.dualhotbar.client.RenderHandler.yCount;

@Mixin(GuiIngameForge.class)
public class GuiIngameForgeMixin {

    @Inject(method = "renderToolHighlight", at = @At("HEAD"), remap = false)
    private void dualhotbar$shiftUp(CallbackInfo ci) {

        if(!DualHotbarConfig.enabled) {
            return;
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(0, -20 * (yCount() - 1), 0);

    }

    @Inject(method = "renderToolHighlight", at = @At("RETURN"), remap = false)
    private void dualhotbar$shiftDown(CallbackInfo ci) {

        if(!DualHotbarConfig.enabled) {
            return;
        }

        GL11.glPopMatrix();

    }

}
