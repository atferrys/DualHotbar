package com.rebelkeithy.dualhotbar;

import com.rebelkeithy.dualhotbar.config.DualHotbarConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderHandler {

    private static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");

    public static int switchTicks = 0;
    private boolean receivedPost = true;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void renderHotbar(RenderGameOverlayEvent.Pre event) {

        if(!DualHotbarConfig.enable) {
            return;
        }

        if(event.getType() != ElementType.HOTBAR) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();

        if(mc.player.isSpectator()) {
            return;
        }

        float partialTicks = event.getPartialTicks();
        ScaledResolution res = event.getResolution();

        int width = res.getScaledWidth();
        int height = res.getScaledHeight();

        mc.profiler.startSection("actionBar");

        int offset = 20;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(WIDGETS);

        EntityPlayer player = mc.player;

        // Draw the offhand slot
        ItemStack offhandItemstack = player.getHeldItemOffhand();
        EnumHandSide handSide = player.getPrimaryHand().opposite();
        int offhandXOffset = 91 * (DualHotbarConfig.stackedHotbar ? 1 : DualHotbarConfig.hotbarsNumber);

        if(!offhandItemstack.isEmpty()) {
            if(handSide == EnumHandSide.LEFT) {
                mc.ingameGUI.drawTexturedModalRect(width / 2 - offhandXOffset - 29, res.getScaledHeight() - 23, 24, 22, 29, 24);
            } else {
                mc.ingameGUI.drawTexturedModalRect(width / 2 + offhandXOffset, res.getScaledHeight() - 23, 53, 22, 29, 24);
            }
        }

        // Draw the hotbar slots
        InventoryPlayer inv = Minecraft.getMinecraft().player.inventory;

        if(DualHotbarConfig.stackedHotbar) {

            mc.ingameGUI.drawTexturedModalRect(width / 2 - 91, height - 22, 0, 0, 182, 22);

            if(!DualHotbarMod.installedOnServer) {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
            }


            for(int i = 1; i < DualHotbarConfig.hotbarsNumber; i++) {
                mc.ingameGUI.drawTexturedModalRect(width / 2 - 91, height - 22 * i - offset + (i - 1) * 2, 0, 0, 182, 21);
            }

            if(!DualHotbarMod.installedOnServer) {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1F);
            }

            // Draw selection square
            mc.ingameGUI.drawTexturedModalRect(width / 2 - 91 - 1 + (inv.currentItem % 9) * 20, height - 22 - 1 - ((inv.currentItem / 9) * offset), 0, 22, 24, 22);
            mc.ingameGUI.drawTexturedModalRect(width / 2 - 91 - 1 + (inv.currentItem % 9) * 20, height - 1 - ((inv.currentItem / 9) * offset), 0, 22, 24, 1);

        } else {

            mc.ingameGUI.drawTexturedModalRect(width / 2 - 91 - 90, height - 22, 0, 0, 182, 22);
            mc.ingameGUI.drawTexturedModalRect(width / 2 - 91 + 91, height - 22, 1, 0, 181, 22);
            mc.ingameGUI.drawTexturedModalRect(width / 2 - 91 + 91 - 1, height - 22, 20, 0, 3, 22);

            if(DualHotbarConfig.hotbarsNumber == 4) {
                mc.ingameGUI.drawTexturedModalRect(width / 2 - 91 - 90, height - 22 - offset, 0, 0, 182, 21);
                mc.ingameGUI.drawTexturedModalRect(width / 2 - 91 + 91, height - 22 - offset, 1, 0, 181, 21);
                mc.ingameGUI.drawTexturedModalRect(width / 2 - 91 + 91 - 1, height - 22 - offset, 20, 0, 3, 21);
            }

            mc.ingameGUI.drawTexturedModalRect(width / 2 - 91 - 1 + (inv.currentItem % 18) * 20 - 90, height - 22 - 1 - ((inv.currentItem / 18) * offset), 0, 22, 24, 22);
            mc.ingameGUI.drawTexturedModalRect(width / 2 - 91 - 1 + (inv.currentItem % 18) * 20 - 90, height - 1 - ((inv.currentItem / 18) * offset), 0, 22, 24, 1);

        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.enableGUIStandardItemLighting();

        // Draw the offhand item
        if(!offhandItemstack.isEmpty()) {
            if(handSide == EnumHandSide.LEFT) {
                this.renderHotbarItem(width / 2 - offhandXOffset - 26, res.getScaledHeight() - 19, partialTicks, player, offhandItemstack);
            } else {
                this.renderHotbarItem(width / 2 + offhandXOffset + 10, res.getScaledHeight() - 19, partialTicks, player, offhandItemstack);
            }
        }

        // Draw the hotbar items
        for (int i = 0; i < 9 * DualHotbarConfig.hotbarsNumber; ++i) {

            if(DualHotbarConfig.stackedHotbar) {

                int x = width / 2 - 90 + (i % 9) * 20 + 2;
                int z = height - 16 - 3 - ((i / 9) * offset);

                if(!DualHotbarMod.installedOnServer) {
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
                }

                GL11.glPushMatrix();

                if(RenderHandler.switchTicks != 0) {

                    float animationOffset = RenderHandler.switchTicks * 2;

                    if(RenderHandler.switchTicks < 0) {
                        animationOffset += 2;
                    }

                    if(RenderHandler.switchTicks > 0)  {
                        animationOffset -= 2;
                    }

                    if(RenderHandler.switchTicks < -6 && i / 9 == DualHotbarConfig.hotbarsNumber - 1) {
                        animationOffset += 20 * DualHotbarConfig.hotbarsNumber;
                    }

                    if(RenderHandler.switchTicks > 6 && i / 9 == 0) {
                        animationOffset -= 20 * DualHotbarConfig.hotbarsNumber;
                    }

                    GL11.glTranslatef(0, animationOffset, 0);

                }

                renderHotbarItem(x, z, partialTicks, player, player.inventory.getStackInSlot(i));
                GL11.glPopMatrix();

                if(!DualHotbarMod.installedOnServer) {
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1F);
                }

            } else {

                int x = width / 2 - 90 + (i % 18) * 20 + 2 - 90;
                int z = height - 16 - 3 - ((i / 18) * offset);

                GL11.glPushMatrix();

                if(RenderHandler.switchTicks != 0) {

                    float animationOffset = RenderHandler.switchTicks * 2;

                    if(RenderHandler.switchTicks < 0) {
                        animationOffset += 2;
                    }

                    if(RenderHandler.switchTicks > 0) {
                        animationOffset -= 2;
                    }

                    if(RenderHandler.switchTicks < -6 && (i / 9 == 2 || i / 9 == 3)) {
                        animationOffset += 20 * DualHotbarConfig.hotbarsNumber / 2f;
                    }

                    if(RenderHandler.switchTicks > 6 && (i / 9 == 0 || i / 9 == 1)) {
                        animationOffset -= 20 * DualHotbarConfig.hotbarsNumber / 2f;
                    }

                    GL11.glTranslatef(0, animationOffset, 0);

                }

                renderHotbarItem(x, z, partialTicks, player, player.inventory.getStackInSlot(i));
                GL11.glPopMatrix();

            }
        }

        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        mc.profiler.endSection();

        if(RenderHandler.switchTicks > 0) {
            RenderHandler.switchTicks--;
        }

        if(RenderHandler.switchTicks < 0) {
            RenderHandler.switchTicks++;
        }

        // Stop minecraft from drawing the hotbar itself
        event.setCanceled(true);

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void shiftRendererUp(RenderGameOverlayEvent.Pre event) {

        if(!DualHotbarConfig.enable || (!DualHotbarConfig.stackedHotbar && DualHotbarConfig.hotbarsNumber != 4)) {
            return;
        }

        if(event.getType() == ElementType.CHAT || event.getType() == ElementType.ARMOR || event.getType() == ElementType.EXPERIENCE || event.getType() == ElementType.FOOD || event.getType() == ElementType.HEALTH || event.getType() == ElementType.HEALTHMOUNT || event.getType() == ElementType.JUMPBAR || event.getType() == ElementType.AIR/* || event.type == ElementType.TEXT*/) {

            // In some cases the post render event is not received (when the pre event is cancelled by another mod), in the case, go ahead and pop the matrix before continuing
            if(!receivedPost) {
                GL11.glPopMatrix();
            }

            receivedPost = false;
            GL11.glPushMatrix();

            if(DualHotbarConfig.stackedHotbar) {
                GL11.glTranslatef(0, -20 * (DualHotbarConfig.hotbarsNumber - 1), 0);
            } else {
                GL11.glTranslatef(0, -20 * (DualHotbarConfig.hotbarsNumber / 2f - 1), 0);
            }

        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void shiftRendererDown(RenderGameOverlayEvent.Post event) {

        if(!DualHotbarConfig.enable || (!DualHotbarConfig.stackedHotbar && DualHotbarConfig.hotbarsNumber != 4)) {
            return;
        }

        if(event.getType() == ElementType.CHAT || event.getType() == ElementType.ARMOR || event.getType() == ElementType.EXPERIENCE || event.getType() == ElementType.FOOD || event.getType() == ElementType.HEALTH || event.getType() == ElementType.HEALTHMOUNT || event.getType() == ElementType.JUMPBAR || event.getType() == ElementType.AIR/* || event.type == ElementType.TEXT*/) {
            receivedPost = true;
            GL11.glPopMatrix();
        }

    }

    // Copied from GuiIngame.renderHotbarItem
    protected void renderHotbarItem(int x, int y, float partialTicks, EntityPlayer player, ItemStack stack) {

        if(!DualHotbarConfig.enable) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

        if(!stack.isEmpty()) {

            float f = (float) stack.getAnimationsToGo() - partialTicks;
            f = Math.max(f, Math.abs(RenderHandler.switchTicks / 4F));

            if(f > 0.0F) {
                GlStateManager.pushMatrix();
                float f1 = 1.0F + f / 5.0F;
                GlStateManager.translate((float) (x + 8), (float) (y + 12), 0.0F);
                GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
            }

            itemRenderer.renderItemAndEffectIntoGUI(player, stack, x, y);

            if(f > 0.0F) {
                GlStateManager.popMatrix();
            }

            itemRenderer.renderItemOverlays(mc.fontRenderer, stack, x, y);

        }

    }

}
