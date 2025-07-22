package com.lx862.immichmc.client.gui.screen.generic;

import com.mojang.blaze3d.systems.RenderSystem;
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
        RenderSystem.enableBlend();
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath("immichmc", "icon.png"), -20, 0, 0, 0, 40, 40, 40, 40);
        RenderSystem.disableBlend();

        guiGraphics.pose().translate(0, 50, 0);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(2, 2, 0);
        guiGraphics.drawCenteredString(font, Component.translatable("gui.immichmc.brand"), 0, 0, 0xFFFFFFFF);
        guiGraphics.pose().popPose();
    }

    public int getFooterStartY() {
        return height - 30;
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(previousScreen);
    }
}