package com.rebelkeithy.dualhotbar.client;

import com.rebelkeithy.dualhotbar.DualHotbar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.inventory.ClickType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static com.rebelkeithy.dualhotbar.config.DualHotbarConfig.*;
import static com.rebelkeithy.dualhotbar.proxy.ClientProxy.SELECT_KEYBIND;
import static com.rebelkeithy.dualhotbar.proxy.ClientProxy.SWAP_KEYBIND;

public class InventoryChangeHandler {

    public int slot = -1;
    public int selectedItem;
    public boolean swapKeyDown;
    public long[] keyTimes = new long[9];
    public int lastKey = -1;
    public int clickCount = 0;

    public boolean[] keyWasDown = new boolean[9];

    @SubscribeEvent
    public void postTickEvent(TickEvent.ClientTickEvent event) {

        if(!enabled || !DualHotbar.installedOnServer) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;

        if(player == null) {
            return;
        }

        if(event.phase == TickEvent.Phase.START) {
            handleStart(mc, player);
        }

        if(event.phase == TickEvent.Phase.END) {
            handleEnd(mc, player);
        }

    }

    private void handleStart(Minecraft mc, EntityPlayerSP player) {

        for(int j = 0; j < 9; ++j) {
            if(Keyboard.isKeyDown(mc.gameSettings.keyBindsHotbar[j].getKeyCode())) {
                selectedItem = player.inventory.currentItem;
            }
        }

        // Do not allow cycling when hotbars are next to each other
        if(!stackedHotbar) {
            return;
        }

        int scrollWheel = Mouse.getDWheel();

        if(!Keyboard.isKeyDown(SWAP_KEYBIND.getKeyCode()) || scrollWheel == 0) {
            swapKeyDown = false;
            return;
        }

        if(swapKeyDown) {
            return;
        }

        swapKeyDown = true;

        int window = player.inventoryContainer.windowId;

        PlayerControllerMP controller = mc.playerController;
        controller.updateController();

        RenderHandler.switchTicks = scrollWheel > 0 ? 12 : -12;

        for(int i = 0; i < 9; i++) {

            controller.windowClick(window, i + 36, 0, ClickType.PICKUP, player);

            if(scrollWheel > 0) {
                for(int j = hotbarsNumber - 1; j >= 1; j--) {
                    controller.windowClick(window, i + 9 * j, 0, ClickType.PICKUP, player);
                }
            } else {
                for(int j = 1; j < hotbarsNumber; j++) {
                    controller.windowClick(window, i + 9 * j, 0, ClickType.PICKUP, player);
                }
            }

            controller.windowClick(window, i + 36, 0, ClickType.PICKUP, player);

        }

        if(hotbarsNumber == 4) {
            for(int i = 9; i < 27; i++) {
                controller.windowClick(window, i, 0, ClickType.PICKUP, player);
                controller.windowClick(window, i + 18, 0, ClickType.PICKUP, player);
                controller.windowClick(window, i, 0, ClickType.PICKUP, player);
            }
        }

        slot = player.inventory.currentItem;

    }

    private void handleEnd(Minecraft mc, EntityPlayerSP player) {

        // If using ctrl-scroll to swap hotbars, put the players selected slot back to what it was before the scroll
        if(slot != -1) {
            player.inventory.currentItem = slot;
            slot = -1;
        }

        long time = System.currentTimeMillis();

        for(int j = 0; j < 9; ++j) {

            if(!Keyboard.isKeyDown(mc.gameSettings.keyBindsHotbar[j].getKeyCode())) {
                keyWasDown[j] = false;
                continue;
            }

            // If using the modifier + inv key combo, we can set the inventory slot without any more checking
            if(Keyboard.isKeyDown(SELECT_KEYBIND.getKeyCode())) {
                player.inventory.currentItem = j + 9;
                continue;
            }

            // Only let this code run when the key is first press, not while it is being held
            if(keyWasDown[j]) {
                continue;
            }

            for(int i = 0; i < hotbarsNumber; i++) {
                if(selectedItem == j + i * 9) {
                    player.inventory.currentItem = (j + 9 * (i + 1)) % (hotbarsNumber * 9);
                }
            }

            // If this key is the same as the last key pressed, and the time difference was less than 900ms, and double tapping is enabled
            // then increment clickCount. Otherwise reset clickCount back to 0
            if(lastKey == j && enableDoubleTap && time - keyTimes[j] < doubleTapTime) {
                clickCount++;
            } else {
                clickCount = 0;
            }

            lastKey = j;
            keyTimes[j] = time;
            keyWasDown[j] = true;

        }

    }

}
