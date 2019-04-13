package com.androsa.cml;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = CascadingMemeMod.MODID)
@Mod.EventBusSubscriber(modid = CascadingMemeMod.MODID)
public class CMLConfig {
    private static final String config = CascadingMemeMod.MODID + ".config.";

    @Config.LangKey(config + "pool_size")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 0)
    @Config.Comment("Adjust the sensitivity of the Cascading Meme Checker. Higher values increase the sensitivity.")
    public static int poolSize = 99;

    @Config.LangKey(config + "threshold")
    @Config.RequiresMcRestart
    @Config.Comment("Adjust the Cascading Meme Threshold. Lower values increase the sensitivity. NOTE: If the Threshold is too high, the Cascading Meme Checker cannot function properly. Fallbacks are in place in the event the Threshold is too high.")
    public static int threshold = 90;

    @Config.LangKey(config + "time_to_check")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 0)
    @Config.Comment("Adjust the frequency of the checker. Lower values increase the frequency the checker will scan for memes.")
    public static int timeToCheck = 100;

    @Config.LangKey(config + "meme_latent_mods")
    @Config.RequiresMcRestart
    @Config.Comment("If you suspect a mod is leaking too many memes, but the checker is unable to pick them up, you can list what mods increase the chances of picking up memes caused by the mod. Mods must be listed by their Mod ID and separated by a comma.")
    public static String[] highLatentMeme = { "examplemod" };

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(CascadingMemeMod.MODID)) {
            ConfigManager.sync(CascadingMemeMod.MODID, Config.Type.INSTANCE);
        }
    }
}
