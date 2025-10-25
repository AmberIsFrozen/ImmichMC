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

    public static class Entry extends ObjectSelectionList.Entry<Entry> {
        private static final int PADDING = 4;
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
            return immichAlbum == null ? Component.translatable("gui.immichmc.album.none.title") : Component.literal(immichAlbum.albumName());
        }

        @Override
        public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean isHovering, float partialTick) {
            MutableComponent titleText = immichAlbum == null ? Component.translatable("gui.immichmc.album.none.title") : Component.literal(immichAlbum.albumName());
            MutableComponent descriptionText = immichAlbum == null ? Component.translatable("gui.immichmc.album.none.description") : Component.literal(immichAlbum.description());
            MutableComponent ownerText = immichAlbum == null ? Component.empty() : Component.literal(" ").append(Component.translatable("gui.immichmc.album.owner", immichAlbum.owner().name())).withStyle(ChatFormatting.GRAY);
            if(immichAlbum == null) titleText.withStyle(ChatFormatting.GOLD);

            guiGraphics.drawString(font, titleText.append(ownerText), getX() + PADDING, getY() + PADDING, 0xFFFFFFFF);

            if(immichAlbum != null) {
                Component itemText = Component.translatable("gui.immichmc.album.items", immichAlbum.assetCount()).withStyle(ChatFormatting.GRAY);
                guiGraphics.drawString(font, itemText, getX() + getWidth() - font.width(itemText) - PADDING, getY()+PADDING, 0xFFFFFFFF);
            }

            descriptionText.withStyle(ChatFormatting.GRAY);

            List<FormattedText> descriptionLine = font.splitIgnoringLanguage(descriptionText, getWidth());

            int i = 0;
            int lineHeight = (int)(font.lineHeight * 1.2);
            for(FormattedText description : descriptionLine) {
                if(i == 2) break;
                guiGraphics.drawWordWrap(font, description, getX()+PADDING, getY()+lineHeight+(i*lineHeight)+PADDING, getWidth(), 0xFFFFFFFF);
                i++;
            }
        }
    }
}
