package com.androsa.cml;

import net.minecraft.client.Minecraft;
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

import static com.androsa.cml.CMLConfig.*;

@Mod(modid = CascadingMemeMod.MODID, name = CascadingMemeMod.NAME, version = CascadingMemeMod.VERSION, clientSideOnly = true)
public class CascadingMemeMod {
    public static final String MODID = "cml";
    public static final String NAME = "Cascading Meme Lag";
    public static final String VERSION = "1.2";
    private static final Logger logger = LogManager.getLogger(MODID);
    private static int modCount;
    private static int minimumChance;
    private static int tickTime = 0;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        //Count out the number of mods, minus the necessities (Minecraft, Forge, FML, MCP)
        int modSize = Loader.instance().getIndexedModList().size() - 4;
        logger.debug("We have found " + modSize + " mods to count.");
        if (tooManyModToggle && modSize > maximumModCount) {
            logger.fatal("CAUTION: TOO MANY MODS HAVE BEEN DETECTED. {} HAVE BEEN FOUND, BUT ONLY {} ARE ALLOWED.", modSize, maximumModCount);
            throw new CascadingMemeException("There are too many mods loaded. Cascading Meme Lag has terminated the session. Adjust the number of allowed mods or disable this feature in the configuration file.");
        }
        //Check the config for modids and see if those mods are loaded. Add 5 for each modid found and loaded
        for (String modid : highLatentMeme) {
            if (Loader.isModLoaded(modid)) {
                logger.debug("Detected a high meme-latent mod, {}.", modid);
                modCount += 5;
            }
        }
        //Looking at you, Twilight Forest...
        if (Loader.isModLoaded("twilightforest")) {
            modSize *= 2;
            logger.info("Twilight Forest has been identified in the list of mods available. Meme rates are doubled.");
        }

        modCount = modSize + poolSize;
        logger.debug("The size of the randomizer is {}.", modCount);
        if (tooManyToCount && modCount > maximumMemeThreshold) {
            logger.fatal("CAUTION: OVERABUNDANCE OF MEMES WILL OVERLOAD THE SESSION. {} HAVE BEEN FOUND, BUT THE THRESHOLD IS AT {}.", modCount, maximumMemeThreshold);
            throw new CascadingMemeException("The size of the randomizer exceeds the recommended threshold. Cascading Meme Lag has terminated the session. Adjust the maximum threshold or disable this feature in the configuration file.");
        }
        //If by some chance the randomizer will never yield a result, say that the random number will be below the threshold, make our own. The chance will now be 10 below the total
        //Otherwise, just use the exact value.
        if (threshold >= modCount) {
            minimumChance = modCount - 10;
            logger.error("An error has occurred. Cascading Meme Lag's checker cannot work with the current Meme Threshold. Automatically adjusting the Meme Threshold...");
        } else {
            minimumChance = threshold;
        }
        logger.debug("Meme Threshold has been set to {}.", minimumChance);
    }

    @Mod.EventBusSubscriber(value = Side.CLIENT)
    public static class ClientEvents {
        private static final Random random = new Random();
        private static int randFrame = modCount;
        private static String errorLine = "Minecraft loaded a new chunk [{},{}] in dimension {} ({}) while populating chunk [{},{}], causing cascading meme lag.";

        private static int getChunkCoordinate(int max) {
            return random.nextInt(max * 2) - max;
        }

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent e) {
            if (e.phase != TickEvent.Phase.END) return;
            World world = Minecraft.getMinecraft().world;

            if (world != null) {
                if (tickTime == 0) {
                    if (random.nextInt(randFrame) > minimumChance) {
                        logger.warn(errorLine, getChunkCoordinate(187500), getChunkCoordinate(187500), world.provider.getDimension(), world.provider.getDimensionType().getName(), getChunkCoordinate(187500), getChunkCoordinate(187500));
                        logger.warn(randomMessage());
                    }
                    tickTime = timeToCheck;
                }
                --tickTime;
            }
        }

        static String randomMessage() {
            int randomSize = Loader.isModLoaded("twilightforest") ? 7 : 6;

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