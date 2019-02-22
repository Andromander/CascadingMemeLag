package com.androsa.cml;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(modid = CascadingMemeMod.MODID, name = CascadingMemeMod.NAME, version = CascadingMemeMod.VERSION)
public class CascadingMemeMod {
    public static final String MODID = "cml";
    public static final String NAME = "Cascading Meme Lag";
    public static final String VERSION = "1.0";

    private static final Logger logger = LogManager.getLogger(MODID);
    private static int modCount;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        int modSize = Loader.instance().getIndexedModList().size() - 4;

        logger.debug("We have found " + modSize + " mods to count.");

        if(Loader.isModLoaded("twilightforest")) {
            modCount = modSize * 2;
            logger.info("Twilight Forest has been identified in the list of mods available. Meme rates are doubled.");
        }
        modCount = modSize + 99;

        logger.debug("The size of the randomizer is " + modCount + ".");
    }

    @Mod.EventBusSubscriber(value = Side.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent e) {
            int randFrame = modCount;
            Random random = new Random();
            World world = Minecraft.getMinecraft().world;
            String errorLine = "Minecraft loaded a new chunk [{}, {}] in dimension {} ({}) while populating chunk [{}, {}], causing cascading meme lag.";

            if(world != null) {
                if(tickTime == 0) {
                    if(random.nextInt(randFrame) > 90) {
                        logger.warn(errorLine, random.nextInt(375000) - 187500, random.nextInt(375000) - 187500, world.provider.getDimension(), world.provider.getDimensionType().getName(), random.nextInt(375000) - 187500, random.nextInt(375000) - 187500);
                        logger.warn(randomMessage());
                    }
                    tickTime = 100;
                }

                --tickTime;
            }
        }

        static String randomMessage() {
            Random random = new Random();
            int randomSize;

            if (Loader.isModLoaded("twilightforest")) {
                randomSize = 7;
            } else {
                randomSize = 6;
            }

            switch (random.nextInt(randomSize)) {
                case 0:
                    return "Please send help.";
                case 1:
                    return "Press F to pay respects and continue.";
                case 2:
                    return "Are you serious?";
                case 3:
                    return "It's that bloody mate that has the bloody mess of a name, isn't it?";
                case 4:
                    return "Have you tried unplugging the internet?";
                case 6:
                    return "Do I am cake??";
                case 5:
                default:
                    return "There is no way to fix this.";
            }
        }
    }
    private static int tickTime = 0;
}
