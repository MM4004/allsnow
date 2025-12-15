package net.mm4004.allsnow.server;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldProviderSurface;
import net.mm4004.allsnow.AllSnow;
import net.mm4004.allsnow.network.TimeSyncPacket;
import net.mm4004.allsnow.utils.GameTime;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

public class TimeHandler {

    private int dayCounter = 0;

    private enum State {
        FROZEN_DAY,
        RUNNING_NIGHT
    }

    private State state = State.FROZEN_DAY;

    @SubscribeEvent
    public void onWorldTicks(TickEvent.WorldTickEvent e) {
        if (e.side != Side.SERVER) {
            return;
        }
        if (e.phase != TickEvent.Phase.END) {
            return;
        }

        if (!(e.world.provider instanceof WorldProviderSurface)) {
            return;
        }

        long time = e.world.getWorldTime() % 24000;
        long base = e.world.getWorldTime() - time;

        switch (state) {

            case FROZEN_DAY:
                e.world.setWorldTime(base + GameTime.DAY_TWILIGHT);

                dayCounter++;
                if (dayCounter >= GameTime.DAY_WAIT_TICKS) {
                    dayCounter = 0;
                    state = State.RUNNING_NIGHT;
                    syncState(false, base);
                }
                break;

            case RUNNING_NIGHT:
                if (time >= GameTime.MORNING_TWILIGHT && time < GameTime.MORNING_TWILIGHT + 5) {
                    e.world.setWorldTime(base + GameTime.DAY_TWILIGHT);
                    state = State.FROZEN_DAY;
                    syncState(true, base);
                }
                break;
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.player.worldObj.isRemote) {
            return;
        }

        AllSnow.NETWORK.sendTo(new TimeSyncPacket(state == State.FROZEN_DAY, false, 0), (EntityPlayerMP) e.player);
    }

    private void syncState(boolean state, long base) {
        AllSnow.NETWORK.sendToAll(new TimeSyncPacket(state, true, base));
    }
}
