package com.cursee.summons.mixin;

import com.cursee.summons.Constants;
import com.cursee.summons.core.common.registry.ModBlocksNeoForge;
import com.cursee.summons.core.common.registry.ModItemsNeoForge;
import com.cursee.summons.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class NeoForgeTitleScreenMixin {

    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {

        Constants.LOG.info("This line is printed by a mixin loaded in a {} instance!", Services.PLATFORM.getPlatformName());
        Constants.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(GuiGraphics graphics, int mouseX, int mouseY, float questionableTimeFloat, CallbackInfo ci) {
        graphics.renderItem(new ItemStack(ModBlocksNeoForge.SUMMON_TOMBSTONE.get()), mouseX, mouseY);
    }
}