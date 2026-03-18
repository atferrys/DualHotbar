package com.rebelkeithy.dualhotbar.proxy;

import com.rebelkeithy.dualhotbar.config.DualHotbarConfig;
import com.rebelkeithy.dualhotbar.network.DualHotbarPacketHandler;
import com.rebelkeithy.dualhotbar.network.SyncHotbarsPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.SERVER)
public class ServerProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent e) {

        super.init(e);

        MinecraftForge.EVENT_BUS.register(this);

    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        if(event.player.world.isRemote) {
            return;
        }

        DualHotbarPacketHandler.INSTANCE.sendTo(
                new SyncHotbarsPacket(DualHotbarConfig.enabled, DualHotbarConfig.hotbarsNumber),
                (EntityPlayerMP) event.player
        );

    }

}
