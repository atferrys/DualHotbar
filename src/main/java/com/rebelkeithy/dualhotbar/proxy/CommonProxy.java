package com.rebelkeithy.dualhotbar.proxy;

import com.rebelkeithy.dualhotbar.network.DualHotbarPacketHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
        DualHotbarPacketHandler.registerMessages();
    }

    public void init(FMLInitializationEvent e) {

    }

}
