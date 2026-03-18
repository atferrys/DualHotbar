package com.rebelkeithy.dualhotbar.config;

import com.rebelkeithy.dualhotbar.Tags;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Tags.MOD_ID)
public class DualHotbarConfig {

    @Config.Name("DualHotbar enabled")
    public static boolean enabled = true;

    @Config.Name("Stacked Hotbar")
    @Config.Comment({
            "Renders the hotbars stacked on top of each other",
            "This is forced to true when the number of hotbars is higher than 2"
    })
    public static boolean stackedHotbar = true;

    @Config.Name("Number of Hotbars")
    @Config.Comment("The number of hotbars (9 slots each)")
    @Config.RangeInt(min = 1, max = 4)
    public static int hotbarsNumber = 2;

    @Config.Name("Double Tap to cycle enabled")
    @Config.Comment("Double tap the hotbar slot key to select the upper hotbar item slot")
    public static boolean enableDoubleTap = true;

    @Config.Name("Double Tap to cycle timing")
    @Config.Comment("Time available for double tapping (in milliseconds)")
    @Config.RangeInt(min = 0, max = 2000)
    public static int doubleTapTime = 900;

    private static void validate() {

        if(hotbarsNumber > 2) {
            stackedHotbar = true;
        }

        if(hotbarsNumber <= 1) {
            enabled = false;
        }

    }

    @Config.Ignore
    private static boolean isServerOverridden = false;

    @Config.Ignore
    private static boolean clientEnabled;

    @Config.Ignore
    private static int clientHotbarNumbers;

    public static void serverOverride(boolean serverEnabled, int serverHotbarsNumber) {

        if(!isServerOverridden) {
            clientEnabled = enabled;
            clientHotbarNumbers = hotbarsNumber;
            isServerOverridden = true;
        }

        enabled = serverEnabled;
        hotbarsNumber = serverHotbarsNumber;
        validate();

    }

    public static void resetServerOverride() {

        if(!isServerOverridden) {
            return;
        }

        isServerOverridden = false;

        enabled = clientEnabled;
        hotbarsNumber = clientHotbarNumbers;
        validate();

    }

    @Mod.EventBusSubscriber(modid = Tags.MOD_ID)
    private static class EventHandler {

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(Tags.MOD_ID)) {
                ConfigManager.sync(Tags.MOD_ID, Config.Type.INSTANCE);
                validate();
                ConfigManager.sync(Tags.MOD_ID, Config.Type.INSTANCE);
            }
        }

    }

}
