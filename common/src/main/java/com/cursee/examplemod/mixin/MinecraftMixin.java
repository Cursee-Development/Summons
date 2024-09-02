package com.cursee.examplemod.mixin;

import com.cursee.examplemod.Constants;
import com.cursee.examplemod.ExampleMod;
import com.cursee.examplemod.platform.Services;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    
    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo info) {

        if (ExampleMod.debugCommon) {
            Constants.LOG.info("This line is printed by a mixin loaded in a {} instance!", "Common");
            Constants.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());
        }
    }
}
