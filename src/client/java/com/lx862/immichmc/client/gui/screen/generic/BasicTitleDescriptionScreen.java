package com.lx862.immichmc.client.gui.screen.generic;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public class BasicTitleDescriptionScreen extends ScreenBase {
    private final String description;

    public BasicTitleDescriptionScreen(Component title, String description) {
        super(title);
        this.description = description;
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

        List<FormattedCharSequence> allLines = new ArrayList<>();
        for(String line : description.split("\n")) {
            for(FormattedCharSequence lineWrapped : font.split(Component.literal(line), (int)(width * 0.9))) {
                allLines.add(lineWrapped);
            }
        }

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(width / 2, (height / 2) - ((allLines.size() * font.lineHeight) / 2));
        for(FormattedCharSequence line : allLines) {
            guiGraphics.drawCenteredString(font, line, 0, 0, 0xFFFFFFFF);
            guiGraphics.pose().translate(0, 9);
        }
        guiGraphics.pose().popMatrix();
    }
}
