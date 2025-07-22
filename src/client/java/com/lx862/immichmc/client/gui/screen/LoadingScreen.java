package com.lx862.immichmc.client.gui.screen;

import com.lx862.immichmc.client.ImmichMC;
import com.lx862.immichmc.client.gui.screen.generic.ScreenBase;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingDotsText;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class LoadingScreen<T> extends ScreenBase {
    public LoadingScreen(Component title, CompletableFuture<T> completableFuture, Function<T, Screen> successScreen, Function<Throwable, Screen> failScreen) {
        super(title);

        completableFuture.whenComplete((data, error) -> {
            minecraft.execute(() -> {
                if(error != null) {
                    ImmichMC.LOGGER.error("", error);
                    Minecraft.getInstance().setScreen(failScreen.apply(error));
                }

                else Minecraft.getInstance().setScreen(successScreen.apply(data));
            });
        });
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        super.renderBackground(guiGraphics, mouseX, mouseY, tickDelta);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((width / 2), (height / 2) - 25, 0);
        guiGraphics.pose().scale(2, 2, 0);
        guiGraphics.drawCenteredString(font, title, 0, 0, 0xFFFFFFFF);
        guiGraphics.drawCenteredString(font, LoadingDotsText.get(Util.getMillis()), 0, 15, 0xFF888888);
        guiGraphics.pose().popPose();
    }
}
