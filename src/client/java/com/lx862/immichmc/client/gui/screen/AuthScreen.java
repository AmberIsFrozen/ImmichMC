package com.lx862.immichmc.client.gui.screen;

import com.lx862.immichmc.client.ImmichMC;
import com.lx862.immichmc.client.gui.screen.generic.BasicTitleDescriptionScreen;
import com.lx862.immichmc.client.gui.screen.generic.ErrorScreen;
import com.lx862.immichmc.client.gui.screen.generic.ScreenBase;
import com.lx862.immichmc.client.immich.ImmichClient;
import com.lx862.immichmc.client.storage.AuthState;
import com.lx862.immichmc.client.storage.Storages;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

public class AuthScreen extends ScreenBase {
    private final ImmichClient.InstanceInfo instanceInfo;
    private final String apiUrl;

    public AuthScreen(String apiUrl, ImmichClient.InstanceInfo instanceInfo) {
        super(Component.translatable("gui.immichmc.auth.title"));
        this.apiUrl = apiUrl;
        this.instanceInfo = instanceInfo;
    }

    public void init() {
        super.init();

        EditBox apiKeyWidget = new EditBox(font, 160, 20, Component.translatable("gui.immichmc.auth.api_key_field.label"));
        apiKeyWidget.setPosition((width / 2) - (apiKeyWidget.getWidth() / 2), 158);
        apiKeyWidget.setHint(Component.translatable("gui.immichmc.auth.api_key_field.hint").withStyle(ChatFormatting.GRAY));
        apiKeyWidget.setMaxLength(64);
        addRenderableWidget(apiKeyWidget);

        Button backButton = Button.builder(Component.translatable("gui.immichmc.button.back"), (btn) -> onClose()).width(140).build();
        backButton.setPosition((width / 2) - backButton.getWidth() - 5, getFooterStartY());
        addRenderableWidget(backButton);

        Button nextButton = Button.builder(Component.translatable("gui.immichmc.button.next"), (btn) -> {
            AuthState authState = new AuthState(apiUrl, apiKeyWidget.getValue());
            ImmichClient client = new ImmichClient(authState.url(), authState.apiKey());
            CompletableFuture<ImmichClient.ValidateTokenResponse> validateTokenResultCompletableFuture = client.validateAccessToken();
            Screen screen = new LoadingScreen<>(Component.translatable("gui.immichmc.processing.auth"), validateTokenResultCompletableFuture, (response) -> {
                if(response.authStatus()) {
                    ImmichMC.setClient(client);
                    Storages.authState = authState;
                    Storages.save();
                    return new ConfigScreen();
                } else {
                    return new BasicTitleDescriptionScreen(Component.translatable("gui.immichmc.auth.failed.title"), Component.translatable("gui.immichmc.auth.failed.description").getString()).withPreviousScreen(this);
                }
            }, (t) -> new ErrorScreen(t).withPreviousScreen(this));
            minecraft.setScreen(screen);
        }).width(140).build();
        nextButton.setPosition((width / 2) + 5, getFooterStartY());
        addRenderableWidget(nextButton);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        super.renderBackground(guiGraphics, mouseX, mouseY, tickDelta);
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(width / 2, 15);
        drawHeaderBranding(guiGraphics, font);
        guiGraphics.pose().translate(0, 25);
        guiGraphics.drawCenteredString(font, Component.translatable("gui.immichmc.immich.version", instanceInfo.version()), 0, 0, 0xFFFFFFFF);
        guiGraphics.pose().translate(0, 15);

        if(instanceInfo.config().loginPageMessage() != null) {
            int strWidth = font.width(instanceInfo.config().loginPageMessage());
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(2, 2);
            guiGraphics.fill(-strWidth/2 - 10, 0, strWidth/2 + 10, 18, 0xFF484D6A);
            guiGraphics.pose().popMatrix();
            guiGraphics.fill(-strWidth/2 - 10, 0, strWidth/2 + 10, 18, 0xFFD9DBFF);
            guiGraphics.drawString(font, instanceInfo.config().loginPageMessage(), -strWidth/2, 5, 0xFF003781, false);
            guiGraphics.pose().translate(0, 30);
        }

        guiGraphics.drawCenteredString(font, Component.translatable("gui.immichmc.auth.title"), 0, 0, 0xFFFFFFFF);
        guiGraphics.pose().popMatrix();
    }
}
