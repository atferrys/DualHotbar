package com.rebelkeithy.dualhotbar.proxy;

import com.rebelkeithy.dualhotbar.client.InventoryChangeHandler;
import com.rebelkeithy.dualhotbar.client.RenderHandler;
import com.rebelkeithy.dualhotbar.Tags;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public static final KeyBinding SELECT_KEYBIND = new KeyBinding("key.select.description", Keyboard.KEY_LCONTROL, Tags.MOD_NAME);
    public static final KeyBinding SWAP_KEYBIND = new KeyBinding("key.swap.description", Keyboard.KEY_LCONTROL, Tags.MOD_NAME);

    @Override
    public void init(FMLInitializationEvent e) {

        super.init(e);

        ClientRegistry.registerKeyBinding(SELECT_KEYBIND);
        ClientRegistry.registerKeyBinding(SWAP_KEYBIND);

        MinecraftForge.EVENT_BUS.register(new RenderHandler());
        MinecraftForge.EVENT_BUS.register(new InventoryChangeHandler());

    }

}
