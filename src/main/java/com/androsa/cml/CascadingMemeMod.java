package com.androsa.cml;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
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

@Mod(modid = CascadingMemeMod.MODID, name = CascadingMemeMod.NAME, version = CascadingMemeMod.VERSION, clientSideOnly = true)
public class CascadingMemeMod {
    public static final String MODID = "cml";
    public static final String NAME = "Cascading Meme Lag";
    public static final String VERSION = "1.1";
    private static final Logger logger = LogManager.getLogger(MODID);
    private static int modCount;
    private static int minimumChance;
    private static int tickTime = 0;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        int modSize = Loader.instance().getIndexedModList().size() - 4;
        logger.debug("We have found " + modSize + " mods to count.");
        for (String modid : CMLConfig.highLatentMeme) {
            if (Loader.isModLoaded(modid)) {
                logger.debug("Detected a high meme-latent mod, " + modid + ".");
                modCount += 5;
            }
        }
        if (Loader.isModLoaded("twilightforest")) {
            modCount = modSize * 2;
            logger.info("Twilight Forest has been identified in the list of mods available. Meme rate sare doubled.");
        }

        modCount = modSize + CMLConfig.poolSize;
        logger.debug("The size of the randomizer is " + modCount + ".");

        if (CMLConfig.threshold >= modCount) {
            minimumChance = modCount - 10;
            logger.error("An error has occurred. Cascading Meme Lag's checker cannot work with the current Meme Threshold. Automatically adjusting the Meme Threshold...");
        } else {
            minimumChance = CMLConfig.threshold;
            logger.debug("Meme Threshold has been set to " + minimumChance + ".");
        }
    }

    @Mod.EventBusSubscriber(value = Side.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent e) {
            if (e.phase != TickEvent.Phase.END) return;

            int randFrame = modCount;
            Random random = new Random();
            World world = Minecraft.getMinecraft().world;
            String errorLine = "Minecraft loaded a new chunk [{},{}] in dimension {} ({}) while populating chunk [{},{}], causing cascading meme lag.";

            if (world != null) {
                if (tickTime == 0) {
                    if (random.nextInt(randFrame) > minimumChance) {
                        logger.warn(errorLine, random.nextInt(375000) - 187500, random.nextInt(375000) - 187500, world.provider.getDimension(), world.provider.getDimensionType().getName(), random.nextInt(375000) - 187500, random.nextInt(375000) - 187500);
                        logger.warn(randomMessage());
                    }
                    tickTime = CMLConfig.timeToCheck;
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
}