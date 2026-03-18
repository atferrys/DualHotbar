package com.rebelkeithy.dualhotbar.network;

import com.rebelkeithy.dualhotbar.Tags;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class DualHotbarPacketHandler {

    private static int packetId = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    public static int nextID() {
        return packetId++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MOD_ID);
        INSTANCE.registerMessage(SyncHotbarsPacket.Handler.class, SyncHotbarsPacket.class, nextID(), Side.CLIENT);
    }

}
