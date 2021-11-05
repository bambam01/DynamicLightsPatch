package com.bambam01.DynamicLightsPatch;

import atomicstryker.dynamiclights.client.DynamicLights;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.commons.lang3.ArrayUtils;


@Mod(modid = "DynamicLightsPatch", name = "Dynamic Lights Patch", version = "0.0.1")
public class DynamicLightsPatch {

    private static int[] blacklistedLightDims;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent evt)
    {
        Configuration config = new Configuration(evt.getSuggestedConfigurationFile());
        config.load();

        Property disabledDimensionIds = config.get(Configuration.CATEGORY_GENERAL, "blacklistedLightDims", new int[]{-100});
        disabledDimensionIds.comment = "list of dimensions ids that are disabled for dynamic lights";
        blacklistedLightDims = disabledDimensionIds.getIntList();

        config.save();

        FMLCommonHandler.instance().bus().register(this);
    }

    public static int getLightValue(IBlockAccess world, Block block, int x, int y, int z)
    {
        if (world instanceof World){
            World realworld = (World) world;
            if(!ArrayUtils.contains(blacklistedLightDims,realworld.provider.dimensionId)){
                return DynamicLights.getLightValue(world, block, x, y, z);
            }
        }
        return block.getLightValue(world, x, y, z);
    }
}
