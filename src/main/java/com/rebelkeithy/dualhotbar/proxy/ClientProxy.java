package com.rebelkeithy.dualhotbar.proxy;

import com.rebelkeithy.dualhotbar.InventoryChangeHandler;
import com.rebelkeithy.dualhotbar.RenderHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent e) {

        super.init(e);

        RenderHandler renderHandler = new RenderHandler();
        InventoryChangeHandler inventoryChangeHandler = new InventoryChangeHandler();

        InventoryChangeHandler.selectKey = new KeyBinding("Hold For Second 9", Keyboard.KEY_LCONTROL, "key.categories.inventory");
        ClientRegistry.registerKeyBinding(InventoryChangeHandler.selectKey);

        InventoryChangeHandler.swapkey = new KeyBinding("Hold+Wheel to Swap Bars", Keyboard.KEY_LCONTROL, "key.categories.inventory");
        ClientRegistry.registerKeyBinding(InventoryChangeHandler.swapkey);

        MinecraftForge.EVENT_BUS.register(renderHandler);
        MinecraftForge.EVENT_BUS.register(inventoryChangeHandler);

    }

}
