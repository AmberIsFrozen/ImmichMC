package com.lx862.immichmc.client.mixin;

import com.lx862.immichmc.client.AssetManager;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.function.Consumer;

@Mixin(Screenshot.class)
public class ScreenshotMixin {
    @Inject(method = "method_1661", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/NativeImage;writeToFile(Ljava/io/File;)V", shift = At.Shift.AFTER))
    private static void ImmichMC_onScreenshotSave(NativeImage nativeImage, File file, Consumer consumer, CallbackInfo ci) {
        AssetManager.uploadAsset(file, Minecraft.getInstance().player);
    }
}
