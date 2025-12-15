package net.mm4004.allsnow.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.mm4004.allsnow.utils.GameTime;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ClientTimeHandler {

    public static boolean isFrozenDay = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.START) {
            return;
        }

        World world = Minecraft.getMinecraft().theWorld;
        if (world == null) {
            return;
        }

        if (!(world.provider instanceof WorldProviderSurface)) {
            return;
        }

        if (isFrozenDay) {
            long base = world.getWorldTime() - (world.getWorldTime() % 24000);

            long time = world.getWorldTime() % 24000;
            if (time != GameTime.DAY_TWILIGHT) {
                world.setWorldTime(base + GameTime.DAY_TWILIGHT);
            }
        }
    }
}
