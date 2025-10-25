package com.lx862.immichmc.client.gui.screen;

import com.lx862.immichmc.client.gui.screen.generic.ScreenBase;
import com.lx862.immichmc.client.gui.widget.AlbumSelectionList;
import com.lx862.immichmc.client.immich.dtos.Album;
import com.lx862.immichmc.client.storage.Storages;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.network.chat.Component;

public class AlbumsScreen extends ScreenBase {
    private final Album[] albums;
    private final String selectedAlbumId;
    private AlbumSelectionList albumSelectionList;

    public AlbumsScreen(String selectedAlbumId, Album[] albums) {
        super(Component.translatable("gui.immichmc.album.title"));
        this.albums = albums;
        this.selectedAlbumId = selectedAlbumId;
    }

    public void init() {
        super.init();
        HeaderAndFooterLayout headerAndFooterLayout = new HeaderAndFooterLayout(this, 50, 35);
        headerAndFooterLayout.addTitleHeader(title, minecraft.font);

        int preSelectedIndex = -1;
        for(int i = 0; i < albums.length; i++) {
            if(albums[i].id().equals(selectedAlbumId)) {
                preSelectedIndex = i+1;
                break;
            }
        }
        if(preSelectedIndex == -1) preSelectedIndex = 0;

        this.albumSelectionList = new AlbumSelectionList(minecraft, width, height - 70, 0, 34, albums);
        headerAndFooterLayout.addToContents(albumSelectionList);
        albumSelectionList.setPosition(0, 30);
        AlbumSelectionList.Entry entry = albumSelectionList.children().get(preSelectedIndex);
        if(entry != null) {
            albumSelectionList.setSelected(entry);
        }

        Button nextButton = Button.builder(Component.translatable("gui.immichmc.button.done"), (btn) -> {
            onClose();
        }).width(200).build();
        nextButton.setPosition((width / 2) - (nextButton.getWidth() / 2), getFooterStartY());
        headerAndFooterLayout.addToFooter(nextButton);
        headerAndFooterLayout.arrangeElements();
        headerAndFooterLayout.visitWidgets(e -> addRenderableWidget(e));
        //addRenderableWidget(nextButton);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        super.renderBackground(guiGraphics, mouseX, mouseY, tickDelta);
    }

    @Override
    public void onClose() {
        AlbumSelectionList.Entry selectedEntry = albumSelectionList.getSelected();
        if(selectedEntry != null && selectedEntry.getAlbum() != null) {
            Storages.config.assignedAlbum = selectedEntry.getAlbum().id();
        } else {
            Storages.config.assignedAlbum = null;
        }
        super.onClose();
    }
}
