package com.rebelkeithy.dualhotbar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.ClickType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InventoryChangeHandler {

    public static KeyBinding swapkey;
    public static KeyBinding selectKey;

    public int mousePrev = -1;
    public int slot = -1;
    public int selectedItem;
    public boolean swapKeyDown;
    public long[] keyTimes = new long[9];
    public int lastKey = -1;
    public int clickCount = 0;

    public boolean[] keyWasDown = new boolean[9];
    public boolean[] changeInv = new boolean[9];

    @SubscribeEvent
    public void postTickEvent(TickEvent.ClientTickEvent event) {

        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if(Minecraft.getMinecraft().player == null) {
            return;
        }

        if(event.phase == TickEvent.Phase.START) {

            for (int j = 0; j < 9; ++j) {
                if(Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindsHotbar[j].getKeyCode())) {
                    selectedItem = player.inventory.currentItem;
                }
            }

            mousePrev = Mouse.getDWheel();

            if(Keyboard.isKeyDown(swapkey.getKeyCode()) && Math.abs(mousePrev - Mouse.getDWheel()) > 0) {

                if(!swapKeyDown) {

                    swapKeyDown = true;

                    int window = player.inventoryContainer.windowId;
                    Minecraft mc = Minecraft.getMinecraft();

                    PlayerControllerMP controller = mc.playerController;
                    controller.updateController();

                    if(mousePrev < 0) {

                        RenderHandler.switchTicks = -12;

                        if(DualHotbarConfig.twoLayerRendering) {
                            for(int i = 0; i < 9; i++) {

                                controller.windowClick(window, i + 36, 0, ClickType.PICKUP, player);

                                if(DualHotbarConfig.numHotbars > 3) {
                                    controller.windowClick(window, i + 27, 0, ClickType.PICKUP, player);
                                }

                                if(DualHotbarConfig.numHotbars > 2) {
                                    controller.windowClick(window, i + 18, 0, ClickType.PICKUP, player);
                                }

                                if(DualHotbarConfig.numHotbars > 1) {
                                    controller.windowClick(window, i + 9, 0, ClickType.PICKUP, player);
                                }

                                controller.windowClick(window, i + 36, 0, ClickType.PICKUP, player);

                            }
                        }

                    } else {

                        RenderHandler.switchTicks = 12;

                        if(DualHotbarConfig.twoLayerRendering) {
                            for(int i = 0; i < 9; i++) {

                                controller.windowClick(window, i + 36, 0, ClickType.PICKUP, player);

                                if(DualHotbarConfig.numHotbars > 1) {
                                    controller.windowClick(window, i + 9, 0, ClickType.PICKUP, player);
                                }

                                if(DualHotbarConfig.numHotbars > 2) {
                                    controller.windowClick(window, i + 18, 0, ClickType.PICKUP, player);
                                }

                                if(DualHotbarConfig.numHotbars > 3) {
                                    controller.windowClick(window, i + 27, 0, ClickType.PICKUP, player);
                                }

                                controller.windowClick(window, i + 36, 0, ClickType.PICKUP, player);

                            }
                        }

                    }

                    if(DualHotbarConfig.numHotbars == 4) {
                        for(int i = 9; i < 27; i++) {
                            controller.windowClick(window, i, 0, ClickType.PICKUP, player);
                            controller.windowClick(window, i + 18, 0, ClickType.PICKUP, player);
                            controller.windowClick(window, i, 0, ClickType.PICKUP, player);
                        }
                    }

                    slot = player.inventory.currentItem;

                }

            } else {
                swapKeyDown = false;
            }
        }

        if(event.phase == TickEvent.Phase.END) {

            // If using ctrl-scroll to swap hotbars, put the players selected slot back to what it was before the scroll
            if (slot != -1) {
                player.inventory.currentItem = slot;
                slot = -1;
            }

            if(!DualHotbarConfig.enable || !DualHotbarMod.installedOnServer) {
                return;
            }

            Minecraft mc = Minecraft.getMinecraft();
            long time = System.currentTimeMillis();

            for(int j = 0; j < 9; ++j) {

                if(Keyboard.isKeyDown(mc.gameSettings.keyBindsHotbar[j].getKeyCode())) {

                    // If using the modifier + inv key combo, we can set the inventory slot without any more checking
                    if(Keyboard.isKeyDown(selectKey.getKeyCode())) {
                        player.inventory.currentItem = j + 9;
                        continue;
                    }

                    // Only let this code run when the key is first press, not while it is being held
                    if(keyWasDown[j]) {
                        continue;
                    }

                    for(int i = 0; i < DualHotbarConfig.numHotbars; i++) {
                        if(selectedItem == j + i * 9) {
                            player.inventory.currentItem = (j + 9 * (i + 1)) % (DualHotbarConfig.numHotbars * 9);
                        }
                    }

                    // If this key is the same as the last key pressed, and the time difference was less than 900ms, and double tapping is enabled
                    // then increment clickCount. Otherwise reset clickCount back to 0
                    if (lastKey == j && DualHotbarConfig.doubleTap && time - keyTimes[j] < 900) {
                        clickCount++;
                    } else {
                        clickCount = 0;
                    }

                    lastKey = j;
                    keyTimes[j] = time;
                    keyWasDown[j] = true;

                } else {
                    keyWasDown[j] = false;
                }
            }
        }
    }
}
