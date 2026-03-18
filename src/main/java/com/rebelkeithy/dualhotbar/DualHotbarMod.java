package com.rebelkeithy.dualhotbar;

import com.rebelkeithy.dualhotbar.config.DualHotbarConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class DualHotbarMod {

    @Instance(Tags.MOD_ID)
    public static DualHotbarMod instance;

    @SidedProxy(clientSide = "com.rebelkeithy.dualhotbar.ProxyClient", serverSide = "com.rebelkeithy.dualhotbar.ProxyCommon")
    public static ProxyCommon proxy;

    public static boolean installedOnServer;

    public static int hotbarSize = 9;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    // By this point checkRemote would have been called, so we can set the hotbar size to the correct values depending on if the server has this mod
    @SubscribeEvent
    public void onConnectedToServerEvent(ClientConnectedToServerEvent event) {
        if (!installedOnServer) {
            System.out.println("DualHotbar not installed on server. Disabling selecting slots");
            hotbarSize = 9;
        } else if (DualHotbarConfig.enable) {
            System.out.println("DualHotbar installed on server. Enabling selecting slots");
            hotbarSize = 9 * DualHotbarConfig.hotbarsNumber;
        }
    }

    // Assume the server doesn't have this mod installed, so installedOnServer is reset to false when leaving a server
    @SubscribeEvent
    public void onClientDisconnectionFromServerEvent(ClientDisconnectionFromServerEvent event) {
        installedOnServer = false;
    }

    // This is called before the ClientConnectedToServerEvent event, but this is only called on forge servers
    @NetworkCheckHandler
    public boolean checkRemote(Map<String, String> mods, Side remoteSide) {
        installedOnServer = mods.keySet().stream().anyMatch(Tags.MOD_ID::equals);
        return true;
    }

}
