package com.johnythecarrot.lyttlecraft

import io.github.prospector.modmenu.api.ConfigScreenFactory

import io.github.prospector.modmenu.api.ModMenuApi
import net.minecraft.client.gui.screen.Screen


class ConfigScreen : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory retStatement@{ parent: Screen? ->
            return@retStatement ConfigScreenUtils.createMenu(parent)
        }
    }
}