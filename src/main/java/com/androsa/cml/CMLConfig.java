package com.androsa.cml;

import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static net.minecraftforge.common.ForgeConfigSpec.*;

@Mod.EventBusSubscriber(modid = CascadingMemeMod.MODID)
public class CMLConfig {
    private static final String config = CascadingMemeMod.MODID + ".config.";

    public static IntValue poolSize;
    public static IntValue threshold;
    public static IntValue timeToCheck;
    public static ConfigValue<List<? extends String>> highLatentMeme;
    public static BooleanValue tooManyModToggle;
    public static IntValue maximumModCount;
    public static BooleanValue tooManyToCount;
    public static IntValue maximumMemeThreshold;

    public CMLConfig(Builder builder) {
        poolSize = builder
                        .translation(config + "pool_size")
                        .worldRestart()
                        .comment("Adjust the sensitivity of the Cascading Meme Checker. Higher values increase the sensitivity.")
                        .defineInRange("poolSize", 99, 0, Integer.MAX_VALUE);

        threshold = builder
                        .translation(config + "threshold")
                        .worldRestart()
                        .comment("Adjust the Cascading Meme Threshold. Lower values increase the sensitivity. NOTE: If the Threshold is too high, the Cascading Meme Checker cannot function properly. Fallbacks are in place in the event the Threshold is too high.")
                        .defineInRange("threshold", 90, Integer.MIN_VALUE, Integer.MAX_VALUE);

        timeToCheck = builder
                        .translation(config + "time_to_check")
                        .worldRestart()
                        .comment("Adjust the frequency of the checker. The frequency is per tick, so every 20 ticks is 1 second. Lower values increase the frequency the checker will scan for memes.")
                        .defineInRange("timeToCheck", 100, 0, Integer.MAX_VALUE);

        highLatentMeme = builder
                        .translation(config + "meme_latent_mods")
                        .worldRestart()
                        .comment("If you suspect a mod is leaking too many memes, but the checker is unable to pick them up, you can list what mods increase the chances of picking up memes caused by the mod. Mods must be listed by their Mod ID and separated by a comma.")
                        .defineList("highLatentMeme", Lists.newArrayList("examplemod"), o -> o instanceof String);

        tooManyModToggle = builder
                        .translation(config + "too_many_mod_toggle")
                        .worldRestart()
                        .comment("At a certain mod count threshold, Cascading Meme Lag will terminate the attempted session due to too many mods. This is to prevent an overload of Cascading Memes onset by an overabundance of mods. Turning this off will mean that no amount of mods will terminate the session.")
                        .define("tooManyModToggle", false);

        maximumModCount = builder
                        .translation(config + "maximum_mod_count")
                        .worldRestart()
                        .comment("The maximum mod count until Cascading Meme Lag will terminate session due to an overabundance of mods. This option does nothing if Crash On Too Many Mods is disabled.")
                        .defineInRange("maximumModCount", 350, Integer.MIN_VALUE, Integer.MAX_VALUE);

        tooManyToCount = builder
                        .translation(config + "too_high_count_toggle")
                        .worldRestart()
                        .comment("If the resulting Meme Threshold is above the set limit, Cascading Meme Lag will terminate the session due to an exceeded threshold. This is to prevent an overload of Cascading Memes onset by an excess abundance of memes. Turning this off will mean that no amount of threshold will terminate the session.")
                        .define("tooManyToCount", false);

        maximumMemeThreshold = builder
                        .translation(config + "maximum_meme_count")
                        .worldRestart()
                        .comment("The maximum meme threshold until Cascading Meme Lag will terminate session due to an overabundance of memes. This option does nothing if Crash On Too Many Memes is disabled.")
                        .defineInRange("maximumMemeThreshold", 700, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
}
