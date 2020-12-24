package com.johnythecarrot.lyttlecraft.mixin;

import com.johnythecarrot.lyttlecraft.ConfigScreenUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClientGame;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.apache.commons.lang3.Range;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.johnythecarrot.lyttlecraft.ConfigScreenUtils.fieldCount;

@Mixin(MinecraftClientGame.class)
public class MultiplayerMixin {

    @Inject(at = @At("HEAD"), method = "onStartGameSession")
    private void onStartGameSession(CallbackInfo ci)
    {
        ClientPlayerEntity self = MinecraftClient.getInstance().player;
        assert self != null;
        for(int i = 0; i <= fieldCount; i++)
        {
            if (!(boolean) ConfigScreenUtils.Companion.getMap().get("send_join_command_" + i))
                continue;
            String message = (String) ConfigScreenUtils.Companion.getMap().get("send_join_command_command_" + i);
            self.sendChatMessage(message);
        }
    }
}
