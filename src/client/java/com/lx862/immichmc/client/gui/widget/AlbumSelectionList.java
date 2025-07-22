package com.lx862.immichmc.client.gui.widget;

import com.lx862.immichmc.client.immich.dtos.Album;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class AlbumSelectionList extends ObjectSelectionList<AlbumSelectionList.Entry> {
    public AlbumSelectionList(Minecraft minecraft, int width, int height, int y, int itemHeight, Album[] albums) {
        super(minecraft, width, height, y, itemHeight);
        addEntry(new Entry(minecraft.font,null));
        for(Album album : albums) {
            addEntry(new Entry(minecraft.font, album));
        }
    }

    @Override
    public int getRowWidth() {
        return 260;
    }

    @Override
    protected void renderHeader(GuiGraphics guiGraphics, int x, int y) {
        // -1 because of the none entry
        Component albumAvailableText = Component.translatable("gui.immichmc.album.list", children().size()-1);
        guiGraphics.drawString(minecraft.font, albumAvailableText, x + (getRowWidth() / 2) - (minecraft.font.width(albumAvailableText)/2), y+2, 0xFFFFFFFF);
    }

    public static class Entry extends ObjectSelectionList.Entry<Entry> {
        private static final int PADDING = 2;
        private final Album immichAlbum;
        private final Font font;

        public Entry(Font font, Album immichAlbum) {
            this.font = font;
            this.immichAlbum = immichAlbum;
        }

        public Album getAlbum() {
            return immichAlbum;
        }

        @Override
        public Component getNarration() {
            return Component.literal("");
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            MutableComponent titleText = immichAlbum == null ? Component.translatable("gui.immichmc.album.none.title") : Component.literal(immichAlbum.albumName());
            MutableComponent descriptionText = immichAlbum == null ? Component.translatable("gui.immichmc.album.none.description") : Component.literal(immichAlbum.description());
            MutableComponent ownerText = immichAlbum == null ? Component.empty() : Component.literal(" ").append(Component.translatable("gui.immichmc.album.owner", immichAlbum.owner().name())).withStyle(ChatFormatting.GRAY);
            if(immichAlbum == null) titleText.withStyle(ChatFormatting.GOLD);

            guiGraphics.drawString(font, titleText.append(ownerText), x + PADDING, y + PADDING, 0xFFFFFFFF);

            if(immichAlbum != null) {
                Component itemText = Component.translatable("gui.immichmc.album.items", immichAlbum.assetCount()).withStyle(ChatFormatting.GRAY);
                guiGraphics.drawString(font, itemText, x + width - font.width(itemText) - 4 - PADDING, y+PADDING, 0xFFFFFFFF);
            }

            List<FormattedCharSequence> descriptionLine = font.split(descriptionText, width);

            int i = 0;
            for(FormattedCharSequence description : descriptionLine) {
                if(i == 2) break;
                guiGraphics.drawString(font, description, x+PADDING, y+9+(i*font.lineHeight)+PADDING, 0xFFFFFFFF);
                i++;
            }
        }
    }
}
