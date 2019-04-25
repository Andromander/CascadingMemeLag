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
    @Config.Comment("Adjust the frequency of the checker. The frequency is per tick, so every 20 ticks is 1 second. Lower values increase the frequency the checker will scan for memes.")
    public static int timeToCheck = 100;

    @Config.LangKey(config + "meme_latent_mods")
    @Config.RequiresMcRestart
    @Config.Comment("If you suspect a mod is leaking too many memes, but the checker is unable to pick them up, you can list what mods increase the chances of picking up memes caused by the mod. Mods must be listed by their Mod ID and separated by a comma.")
    public static String[] highLatentMeme = { "examplemod" };

    @Config.LangKey(config + "too_many_mod_toggle")
    @Config.RequiresMcRestart
    @Config.Comment("At a certain mod count threshold, Cascading Meme Lag will terminate the attempted session due to too many mods. This is to prevent an overload of Cascading Memes onset by an overabundance of mods. Turning this off will mean that no amount of mods will terminate the session.")
    public static boolean tooManyModToggle = false;

    @Config.LangKey(config + "maximum_mod_count")
    @Config.RequiresMcRestart
    @Config.Comment("The maximum mod count until Cascading Meme Lag will terminate session due to an overabundance of mods. This option does nothing if Crash On Too Many Mods is disabled.")
    public static int maximumModCount = 350;

    @Config.LangKey(config + "too_high_count_toggle")
    @Config.RequiresMcRestart
    @Config.Comment("If the resulting Meme Threshold is above the set limit, Cascading Meme Lag will terminate the session due to an exceeded threshold. This is to prevent an overload of Cascading Memes onset by an excess abundance of memes. Turning this off will mean that no amount of threshold will terminate the session.")
    public static boolean tooManyToCount = false;

    @Config.LangKey(config + "maximum_meme_count")
    @Config.RequiresMcRestart
    @Config.Comment("The maximum meme threshold until Cascading Meme Lag will terminate session due to an overabundance of memes. This option does nothing if Crash On Too Many Memes is disabled.")
    public static int maximumMemeThreshold = 700;

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(CascadingMemeMod.MODID)) {
            ConfigManager.sync(CascadingMemeMod.MODID, Config.Type.INSTANCE);
        }
    }
}
