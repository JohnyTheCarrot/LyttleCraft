package com.johnythecarrot.lyttlecraft

import io.github.bymartrixx.playerevents.api.event.PlayerJoinCallback
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.math.sqrt


class LyttleCraft : ModInitializer {
    private var pos1: BlockPos? = null
    private var pos2: BlockPos? = null

    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        println("Loading LyttleCraft")
        val useBlockCallback = UseBlockCallback callback@{ player: PlayerEntity, _: World, hand: Hand, hitResult: BlockHitResult ->
            val heldItem: ItemStack = player.getStackInHand(hand)

            if (heldItem.item !== Items.STICK || heldItem.name.string.toLowerCase() != "measuring stick")
                return@callback ActionResult.PASS

            val position: Int
            if (player.isSneaking)
            {
                if (pos2 == hitResult.blockPos)
                    return@callback ActionResult.PASS
                pos2 = hitResult.blockPos
                position = 2
            }
            else
            {
                if (pos1 == hitResult.blockPos)
                    return@callback ActionResult.PASS
                pos1 = hitResult.blockPos
                position = 1
            }

            player.sendMessage("Position $position set.")

            if (pos1 != null && pos2 != null) {
                val distance: Int = pos1!!.distanceTo(pos2!!)
                player.sendMessage("Distance: $distance")
            }

            ActionResult.PASS
        }
        UseBlockCallback.EVENT.register(useBlockCallback)
    }

}

private fun PlayerEntity.sendMessage(s: String) {
    this.sendMessage(Text.of(s), false)
}

private fun BlockPos.distanceTo(pos2: BlockPos): Int {
    val deltaX: Double = (pos2.x - this.x).toDouble()
    val deltaY: Double = (pos2.y - this.y).toDouble()
    val deltaZ: Double = (pos2.z - this.z).toDouble()

    return sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ).toInt()
}

