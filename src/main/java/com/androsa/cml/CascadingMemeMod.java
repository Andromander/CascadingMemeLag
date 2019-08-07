package com.androsa.cml;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

import static com.androsa.cml.CMLConfig.*;

@Mod(CascadingMemeMod.MODID)
public class CascadingMemeMod {
    public static final String MODID = "cml";
    private static final Logger logger = LogManager.getLogger(MODID);
    public static CMLConfig config;

    private static int modCount;
    private static int minimumChance;
    private static int tickTime = 0;

    public CascadingMemeMod() {
        final Pair<CMLConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CMLConfig::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, specPair.getRight());
        config = specPair.getLeft();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void init(FMLClientSetupEvent event) {
        //Count out the number of mods, minus the necessities
        int modSize = ModList.get().getMods().size() - 2;
        logger.debug("We have found " + modSize + " mods to count.");
        if (tooManyModToggle.get() && modSize > maximumModCount.get()) {
            logger.fatal("CAUTION: TOO MANY MODS HAVE BEEN DETECTED. {} HAVE BEEN FOUND, BUT ONLY {} ARE ALLOWED.", modSize, maximumModCount.get());
            throw new CascadingMemeException("There are too many mods loaded. Cascading Meme Lag has terminated the session. Adjust the number of allowed mods or disable this feature in the configuration file.");
        }
        //Check the config for modids and see if those mods are loaded. Add 5 for each modid found and loaded
        for (String modid : highLatentMeme.get()) {
            if (ModList.get().isLoaded(modid)) {
                logger.debug("Detected a high meme-latent mod, {}.", modid);
                modCount += 5;
            }
        }
        //Looking at you, Twilight Forest...
        if (ModList.get().isLoaded("twilightforest")) {
            modSize *= 2;
            logger.info("Twilight Forest has been identified in the list of mods available. Meme rates are doubled.");
        }

        modCount = modSize + poolSize.get();
        logger.debug("The size of the randomizer is {}.", modCount);
        if (tooManyToCount.get() && modCount > maximumMemeThreshold.get()) {
            logger.fatal("CAUTION: OVERABUNDANCE OF MEMES WILL OVERLOAD THE SESSION. {} HAVE BEEN FOUND, BUT THE THRESHOLD IS AT {}.", modCount, maximumMemeThreshold.get());
            throw new CascadingMemeException("The size of the randomizer exceeds the recommended threshold. Cascading Meme Lag has terminated the session. Adjust the maximum threshold or disable this feature in the configuration file.");
        }
        //If by some chance the randomizer will never yield a result, say that the random number will be below the threshold, make our own. The chance will now be 10 below the total
        //Otherwise, just use the exact value.
        if (threshold.get() >= modCount) {
            minimumChance = modCount - 10;
            logger.error("An error has occurred. Cascading Meme Lag's checker cannot work with the current Meme Threshold. Automatically adjusting the Meme Threshold...");
        } else {
            minimumChance = threshold.get();
        }
        logger.debug("Meme Threshold has been set to {}.", minimumChance);
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT)
    public static class ClientEvents {
        private static final Random random = new Random();
        private static String errorLine = "Minecraft loaded a new chunk [{},{}] in dimension {} ({}) while populating chunk [{},{}], causing cascading meme lag.";

        private static int getChunkCoordinate(int max) {
            return random.nextInt(max * 2) - max;
        }

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent e) {
            if (e.phase != TickEvent.Phase.END) return;
            World world = Minecraft.getInstance().world;

            if (world != null) {
                if (tickTime == 0) {
                    if (random.nextInt(modCount) > minimumChance) {
                        logger.warn(errorLine, getChunkCoordinate(187500), getChunkCoordinate(187500), world.dimension.getType().getId(), DimensionType.getKey(world.dimension.getType()), getChunkCoordinate(187500), getChunkCoordinate(187500));
                        logger.warn(randomMessage());
                    }
                    tickTime = timeToCheck.get();
                }
                --tickTime;
            }
        }

        static String randomMessage() {
            int randomSize = ModList.get().isLoaded("twilightforest") ? 7 : 6;

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