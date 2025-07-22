package com.lx862.immichmc.client.gui.screen;

import com.lx862.immichmc.client.ImmichMC;
import com.lx862.immichmc.client.gui.screen.generic.ErrorScreen;
import com.lx862.immichmc.client.gui.screen.generic.ScreenBase;
import com.lx862.immichmc.client.storage.AuthState;
import com.lx862.immichmc.client.storage.Storages;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends ScreenBase {
    public ConfigScreen() {
        super(Component.translatable("gui.immichmc.config.title"));
    }

    public void init() {
        super.init();

        Button albumButton = Button.builder(Component.translatable("gui.immichmc.config.album"), (btn) -> {
            minecraft.setScreen(
                    new LoadingScreen<>(
                            Component.translatable("gui.immichmc.processing.album"),
                            ImmichMC.getClient().getAllAlbums(),
                            (t) -> new AlbumsScreen(Storages.config.assignedAlbum, t.albums()).withPreviousScreen(this),
                            (e) -> new ErrorScreen(e).withPreviousScreen(this)
                    ).withPreviousScreen(this)
            );
        }).tooltip(Tooltip.create(Component.translatable("gui.immichmc.config.album.tooltip"))).width(140).build();
        albumButton.setPosition((width / 2) - albumButton.getWidth() - 5, 120);
        addRenderableWidget(albumButton);

        Button showMessageButton = Button.builder(Component.translatable("gui.immichmc.config.show_chat_message", Storages.config.showUploadMessage), (btn) -> {
            Storages.config.showUploadMessage = !Storages.config.showUploadMessage;
            btn.setMessage(Component.translatable("gui.immichmc.config.show_chat_message", Storages.config.showUploadMessage));
        }).tooltip(Tooltip.create(Component.translatable("gui.immichmc.config.show_chat_message.tooltip"))).width(140).build();
        showMessageButton.setPosition((width / 2) + 5, 120);
        addRenderableWidget(showMessageButton);

        Button logoutButton = Button.builder(Component.translatable("gui.immichmc.config.logout").withStyle(ChatFormatting.RED), (btn) -> {
            ImmichMC.setClient(null);
            Storages.authState = new AuthState(null, null);
            Storages.save();
            minecraft.setScreen(new LandingScreen());
        }).width(140).build();
        logoutButton.setPosition((width / 2) - 5 - logoutButton.getWidth(), 150);
        addRenderableWidget(logoutButton);

        Button doneButton = Button.builder(Component.translatable("gui.immichmc.button.done"), (btn) -> {
            onClose();
        }).width(200).build();
        doneButton.setPosition((width / 2) - (doneButton.getWidth() / 2), getFooterStartY());
        addRenderableWidget(doneButton);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        super.renderBackground(guiGraphics, mouseX, mouseY, tickDelta);

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(width / 2, 15);
        drawHeaderBranding(guiGraphics, font);
        guiGraphics.pose().translate(0, 25);
        guiGraphics.drawCenteredString(font, Component.translatable("gui.immichmc.config.status", Storages.authState.url()).withStyle(ChatFormatting.GREEN), 0, 0, 0xFFFFFFFF);
        guiGraphics.pose().popMatrix();
    }

    @Override
    public void onClose() {
        Storages.save();
        super.onClose();
    }
}
