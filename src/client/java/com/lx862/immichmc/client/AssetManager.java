package com.lx862.immichmc.client;

import com.lx862.immichmc.client.storage.Storages;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class AssetManager {
    private static final MutableComponent chatPrefix = Component.literal("[" + ImmichMC.BRAND + "] ").withStyle(ChatFormatting.YELLOW);

    public static void uploadAsset(File file, Player player) {
        if(file.exists() && ImmichMC.getClient() != null) {
            ImmichMC.getClient().upload(file).whenComplete((assetUploadResult, e) -> {
                boolean hasError = e != null;
                if(hasError) ImmichMC.LOGGER.error("[{}] Failed to upload after taking screenshot!", ImmichMC.BRAND, e);

                if(player != null) {
                    Minecraft.getInstance().execute(() -> {
                        MutableComponent message = Component.literal(hasError ? "Failed to upload image, see console log for detail!" : "Image uploaded!").withStyle(hasError ? ChatFormatting.RED : ChatFormatting.GREEN);
                        if(hasError || Storages.config.showUploadMessage) player.displayClientMessage(chatPrefix.copy().append(message), false);
                    });
                }
            }).thenCompose(uploadResult -> {
                boolean shouldAddToAlbum = Storages.config.assignedAlbum != null;
                return shouldAddToAlbum ? ImmichMC.getClient().addAssetsToAlbum(Storages.config.assignedAlbum, uploadResult.id()) : new CompletableFuture<>();
            }).whenComplete((addAssetToAlbumResult, e) -> {
                boolean hasError = e != null;
                if(hasError || addAssetToAlbumResult != null) {
                    if(hasError) ImmichMC.LOGGER.error("[{}] Failed to add to album after uploading!", ImmichMC.BRAND, e);

                    if(player != null) {
                        Minecraft.getInstance().execute(() -> {
                            MutableComponent message = Component.literal(hasError ? "Failed to add asset to album, see console log for detail!" : "Added to configured album!").withStyle(hasError ? ChatFormatting.RED : ChatFormatting.GREEN);
                            if(hasError || Storages.config.showUploadMessage) player.displayClientMessage(chatPrefix.copy().append(message), false);
                        });
                    }
                }
            });
        }
    }
}
