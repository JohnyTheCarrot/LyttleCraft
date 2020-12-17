package com.johnythecarrot.lyttlecraft.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
// Mixins HAVE to be written in java due to constraints in the mixin system.
public abstract class PlayerMixin extends LivingEntity {
    private BlockPos lastPos = null;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract Arm getMainArm();

    @Shadow public abstract void sendMessage(Text message, boolean actionBar);

    @Inject(at = @At("HEAD"), method = "isBlockBreakingRestricted", cancellable = true)
    public void isBlockBreakingRestricted(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> cir)
    {
        ItemStack handItem = getMainHandStack();

        if (handItem.getName().asString().toLowerCase().contains("important")) {
            boolean willBreak = handItem.getDamage() + 1 == handItem.getMaxDamage();
            if (willBreak) {
                if (lastPos != pos)
                {
                    sendMessage(Text.of("[LyttleCraft] Item marked as important, using item now would break it."), false);
                    lastPos = pos;
                }
                cir.setReturnValue(true);
            }
        }
    }
}
