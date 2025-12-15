package net.mm4004.allsnow.server;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.mm4004.allsnow.AllSnow;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class WorldGenerationHandler {

    private static Block bopFoliage;
    private static Block bopPlants;
    private static Block bopFlowers;

    public void initialize() {
        bopFoliage = GameRegistry.findBlock("BiomesOPlenty", "foliage");
        if (bopFoliage == null) {
            AllSnow.LOG.error("No BoP foliage block was found!");
        } else {
            AllSnow.LOG.info("BoP foliage block was found!");
        }
        bopFlowers = GameRegistry.findBlock("BiomesOPlenty", "flowers");
        if (bopFoliage == null) {
            AllSnow.LOG.error("No BoP flower block was found!");
        } else {
            AllSnow.LOG.info("BoP flower block was found!");
        }
        bopPlants = GameRegistry.findBlock("BiomesOPlenty", "plants");
        if (bopFoliage == null) {
            AllSnow.LOG.error("No BoP plant block was found!");
        } else {
            AllSnow.LOG.info("BoP plant block was found!");
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onBiomeDecorate(DecorateBiomeEvent.Post e) {
        if (bopFoliage == null || bopPlants == null || bopFlowers == null) {
            return;
        }

        World world = e.world;
        int startX = e.chunkX;
        int startZ = e.chunkZ;

        for (int x = startX - 8; x < startX + 32; x++) {
            for (int z = startZ - 8; z < startZ + 32; z++) {
                for (int y = 0; y < world.getHeight(); y++) {
                    if (world.blockExists(x, y, z)) {
                        Block blk = world.getBlock(x, y, z);
                        if (blk == bopFoliage || blk == bopFlowers || blk == bopPlants) {
                            world.setBlock(x, y, z, Blocks.snow_layer, 0, 2);
                        }
                    }
                }
            }
        }
    }
}
