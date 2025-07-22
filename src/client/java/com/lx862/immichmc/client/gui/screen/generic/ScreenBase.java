package com.lx862.immichmc.client.gui.screen.generic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Generic GUI Screen, use {@link ScreenBase#withPreviousScreen} to reference the previously opened screen that would be opened again after closing.
 */
public abstract class ScreenBase extends Screen {
    protected Screen previousScreen = null;
    
    public ScreenBase(Component title) {
        super(title);
    }
    
    public ScreenBase withPreviousScreen(Screen screen) {
        this.previousScreen = screen;
        return this;
    }

    protected static void drawHeaderBranding(GuiGraphics guiGraphics, Font font) {
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath("immichmc", "icon.png"), -20, 0, 20, 40, 0, 1, 0, 1);

        guiGraphics.pose().translate(0, 50);

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().scale(2, 2);
        guiGraphics.drawCenteredString(font, Component.translatable("gui.immichmc.brand"), 0, 0, 0xFFFFFFFF);
        guiGraphics.pose().popMatrix();
    }

    public int getFooterStartY() {
        return height - 30;
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(previousScreen);
    }
}