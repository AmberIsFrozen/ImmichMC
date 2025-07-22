package com.lx862.immichmc.client.gui.screen;

import com.lx862.immichmc.client.gui.screen.generic.BasicTitleDescriptionScreen;
import com.lx862.immichmc.client.gui.screen.generic.ScreenBase;
import com.lx862.immichmc.client.immich.ImmichClient;
import com.lx862.immichmc.client.immich.ImmichUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class LandingScreen extends ScreenBase {
    private String protocol = null;
    public LandingScreen() {
        super(Component.translatable("gui.immichmc.landing.title"));
    }

    public void init() {
        super.init();
        protocol = null;
        EditBox textWidget = new EditBox(font, 160, 20, Component.translatable("gui.immichmc.landing.url_field.label"));
        textWidget.setPosition((width / 2) - (textWidget.getWidth() / 2), 120);
        textWidget.setHint(Component.translatable("gui.immichmc.landing.url_field.hint").withStyle(ChatFormatting.GRAY));
        addRenderableWidget(textWidget);

        Button backButton = Button.builder(Component.translatable("gui.immichmc.button.back"), (btn) -> onClose()).width(140).build();
        backButton.setPosition((width / 2) - backButton.getWidth() - 5, getFooterStartY());
        addRenderableWidget(backButton);

        Button nextButton = Button.builder(Component.translatable("gui.immichmc.button.next"), (btn) -> {
            String apiUrl = ImmichUtil.rectifyApiUrl(textWidget.getValue());
            ImmichClient immichClient = new ImmichClient(apiUrl, null);
            ScreenBase newScreen = new LoadingScreen<>(Component.translatable("gui.immichmc.landing.processing.title"), immichClient.getCombinedInstanceInfo(),
                    (info) -> new AuthScreen(apiUrl, info).withPreviousScreen(this),
                    (t) -> new BasicTitleDescriptionScreen(Component.translatable("gui.immichmc.landing.failed.title"), Component.translatable("gui.immichmc.landing.failed.description").getString()).withPreviousScreen(this));
            minecraft.setScreen(newScreen);
        }).width(140).build();
        nextButton.active = false; // Disable button by default
        nextButton.setPosition((width / 2) + 5, getFooterStartY());
        addRenderableWidget(nextButton);

        textWidget.setResponder((str) -> {
            try {
                URL url = new URI(str).toURL();
                protocol = url.getProtocol();
                nextButton.active = protocol.equals("http") || protocol.equals("https");
            } catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
                protocol = null;
                nextButton.active = false;
            }
        });
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        super.renderBackground(guiGraphics, mouseX, mouseY, tickDelta);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(width / 2, 15, 0);
        drawHeaderBranding(guiGraphics, font);
        guiGraphics.pose().translate(0, 25, 0);
        guiGraphics.drawCenteredString(font, Component.translatable("gui.immichmc.landing.instruction"), 0, 0, 0xFFFFFFFF);
        if(Objects.equals(protocol, "http")) {
            guiGraphics.pose().translate(0, 14, 0);
            guiGraphics.drawCenteredString(font, Component.translatable("gui.immichmc.landing.http_warning").withStyle(ChatFormatting.YELLOW), 0, 0, 0xFFFFFFFF);
        }
        guiGraphics.pose().popPose();
    }
}
