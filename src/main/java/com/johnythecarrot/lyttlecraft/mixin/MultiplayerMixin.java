package com.johnythecarrot.lyttlecraft.mixin;

import com.johnythecarrot.lyttlecraft.ConfigScreenUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClientGame;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClientGame.class)
public class MultiplayerMixin {

    @Inject(at = @At("HEAD"), method = "onStartGameSession")
    private void onStartGameSession(CallbackInfo ci)
    {
        System.out.println("blah blah");

        ClientPlayerEntity self = MinecraftClient.getInstance().player;
        assert self != null;
        self.sendChatMessage("hi");
    }

}
