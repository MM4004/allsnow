package net.mm4004.allsnow;

import net.minecraft.world.storage.WorldInfo;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

public class WeatherHandler {
    @SubscribeEvent
    public void onWorldTicks(TickEvent.WorldTickEvent e) {
        if (e.side != Side.SERVER) {
            return;
        }
        if (e.phase != TickEvent.Phase.END) {
            return;
        }

        WorldInfo info = e.world.getWorldInfo();
        if (!info.isRaining()) {
            info.setRaining(true);
            info.setRainTime(Integer.MAX_VALUE);
            info.setThundering(false);
            info.setThunderTime(0);
        }
    }
}
