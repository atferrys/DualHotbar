package com.rebelkeithy.dualhotbar;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

public class DualHotbarGuiConfig extends GuiConfig {

    public DualHotbarGuiConfig(GuiScreen parentScreen) {
        super(parentScreen, new ConfigElement(DualHotbarConfig.config.getCategory("config")).getChildElements(), Tags.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(DualHotbarConfig.config.toString()));
    }

}
