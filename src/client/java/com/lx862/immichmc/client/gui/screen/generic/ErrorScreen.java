package com.lx862.immichmc.client.gui.screen.generic;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class ErrorScreen extends ScreenBase {
    private final Throwable throwable;

    public ErrorScreen(Throwable e) {
        super(Component.translatable("gui.immichmc.error.title").withStyle(ChatFormatting.RED));
        this.throwable = e;
    }

    public void init() {
        super.init();

        Button backButton = Button.builder(Component.translatable("gui.immichmc.button.back"), (btn) -> onClose()).width(200).build();
        backButton.setPosition((width / 2) - (backButton.getWidth() / 2), getFooterStartY());
        addRenderableWidget(backButton);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        super.renderBackground(guiGraphics, mouseX, mouseY, tickDelta);

        guiGraphics.drawCenteredString(font, title, width / 2, 10, 0xFFFFFFFF);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(width / 2, 25, 0);

        String errorMessage = throwable.toString();
        for(String line : errorMessage.split("\n")) {
            for(FormattedCharSequence lineWrapped : font.split(Component.literal(line), (int)(width * 0.9))) {
                guiGraphics.drawCenteredString(font, lineWrapped, 0, 0, 0xFFFFAAAA);
                guiGraphics.pose().translate(0, 9, 0);
            }
        }
        guiGraphics.pose().popPose();
    }
}
