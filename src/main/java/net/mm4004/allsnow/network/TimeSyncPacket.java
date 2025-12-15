package net.mm4004.allsnow.network;

import net.minecraft.client.Minecraft;
import net.mm4004.allsnow.client.ClientTimeHandler;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.mm4004.allsnow.utils.GameTime;

public class TimeSyncPacket implements IMessage {

    private boolean isTimeFrozen;
    private boolean doSyncBase;
    private long base;

    public TimeSyncPacket() {}

    public TimeSyncPacket(boolean frozenTime, boolean doSyncBase, long base) {
        this.isTimeFrozen = frozenTime;
        this.doSyncBase = doSyncBase;
        this.base = base;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isTimeFrozen);
        buf.writeBoolean(doSyncBase);
        buf.writeLong(base);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isTimeFrozen = buf.readBoolean();
        doSyncBase = buf.readBoolean();
        base = buf.readLong();
    }

    public static class Handler implements IMessageHandler<TimeSyncPacket, IMessage> {

        @Override
        public IMessage onMessage(TimeSyncPacket message, MessageContext ctx) {
            if (!message.isTimeFrozen && ClientTimeHandler.isFrozenDay && message.doSyncBase) {
                Minecraft.getMinecraft().theWorld.setWorldTime(message.base + GameTime.DAY_TWILIGHT);
            }
            ClientTimeHandler.isFrozenDay = message.isTimeFrozen;
            return null;
        }
    }
}
