package net.mm4004.allsnow;

import net.minecraftforge.common.MinecraftForge;
import net.mm4004.allsnow.network.TimeSyncPacket;
import net.mm4004.allsnow.server.TimeHandler;
import net.mm4004.allsnow.server.WeatherHandler;
import net.mm4004.allsnow.server.WorldGenerationHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        AllSnow.NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("allsnow");
        AllSnow.NETWORK.registerMessage(TimeSyncPacket.Handler.class, TimeSyncPacket.class, 0, Side.CLIENT);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance()
            .bus()
            .register(new WeatherHandler());
        FMLCommonHandler.instance()
            .bus()
            .register(new TimeHandler());
        WorldGenerationHandler wghandler = new WorldGenerationHandler();
        wghandler.initialize();
        MinecraftForge.EVENT_BUS.register(wghandler);
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {}
}
