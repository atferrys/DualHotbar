package com.rebelkeithy.dualhotbar.network;

import com.rebelkeithy.dualhotbar.config.DualHotbarConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncHotbarsPacket implements IMessage {

    private boolean enabled;
    private int hotbarsNumber;

    public SyncHotbarsPacket() {

    }

    public SyncHotbarsPacket(boolean enabled, int hotbarsNumber) {
        this.enabled = enabled;
        this.hotbarsNumber = hotbarsNumber;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        enabled = buf.readBoolean();
        hotbarsNumber = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(enabled);
        buf.writeInt(hotbarsNumber);
    }

    public static class Handler implements IMessageHandler<SyncHotbarsPacket, IMessage> {

        @Override
        public IMessage onMessage(SyncHotbarsPacket message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                DualHotbarConfig.serverOverride(message.enabled, message.hotbarsNumber);
            });
            return null;
        }

    }

}
