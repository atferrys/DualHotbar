package com.rebelkeithy.dualhotbar.mixin;

import com.rebelkeithy.dualhotbar.RenderHandler;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameForge.class)
public class GuiIngameForgeMixin {

    @Inject(method = "renderToolHighlight", at = @At("HEAD"), remap = false)
    private void dualhotbar$shiftUp(CallbackInfo ci) {
        RenderHandler.shiftUp();
    }

    @Inject(method = "renderToolHighlight", at = @At("RETURN"), remap = false)
    private void dualhotbar$shiftDown(CallbackInfo ci) {
        RenderHandler.shiftDown();
    }

}
