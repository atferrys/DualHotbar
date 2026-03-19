package com.rebelkeithy.dualhotbar.mixin;

import com.rebelkeithy.dualhotbar.config.DualHotbarConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static com.rebelkeithy.dualhotbar.client.RenderHandler.yCount;

@Mixin(GuiNewChat.class)
public class GuiNewChatMixin {

    @Final
    @Shadow
    private Minecraft mc;

    @ModifyVariable(
            method = "getChatComponent",
            at = @At("HEAD"),
            ordinal = 1,
            argsOnly = true
    )
    private int dualhotbar$shiftUp(int mouseY) {

        if(!DualHotbarConfig.enabled) {
            return mouseY;
        }

        int yOffset = -20 * (yCount() - 1);
        yOffset *= new ScaledResolution(mc).getScaleFactor();

        return mouseY + yOffset;

    }

}
