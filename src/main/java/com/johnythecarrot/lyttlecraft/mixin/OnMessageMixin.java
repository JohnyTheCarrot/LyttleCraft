package com.johnythecarrot.lyttlecraft.mixin;

import com.johnythecarrot.lyttlecraft.ConfigScreenUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.johnythecarrot.lyttlecraft.ConfigScreenUtils.fieldCount;

@Mixin(ClientPlayNetworkHandler.class)
public class OnMessageMixin {
    String lastMessage = "";

    @Inject(at = @At("HEAD"), method = "onGameMessage")
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci)
    {
        Map map = ConfigScreenUtils.Companion.getMap();
        String message = packet.getMessage().getString();

        ClientPlayerEntity self = MinecraftClient.getInstance().player;
        assert self != null;

        for(int i = 0; i <= fieldCount; i++) {
            if (!(boolean) map.get("chat_reply_re_enabled_" + i))
                continue;
            Pattern pattern = Pattern.compile((String) map.get("chat_reply_re_" + i), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(message);
            if (!matcher.matches())
                continue;
            String toReplyWith = (String) map.get("chat_reply_" + i);
            for(int j = 1; j < matcher.groupCount() + 1; j++)
            {
                String group = matcher.group(j);
                toReplyWith = toReplyWith.replace("$" + j, group);
            }
            if (toReplyWith.equals(lastMessage))
                continue;
            self.sendChatMessage(toReplyWith);
            lastMessage = toReplyWith;
        }
    }
}
