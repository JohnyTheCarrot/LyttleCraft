package com.johnythecarrot.lyttlecraft

import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigCategory
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText





class ConfigScreenUtils {
    companion object {
        @JvmStatic
        fun createMenu(parent: Screen?): Screen {
            val builder: ConfigBuilder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.of("kilikip"))

            val general: ConfigCategory = builder.getOrCreateCategory(Text.of("General"))
            val entryBuilder = builder.entryBuilder()
            general.addEntry(entryBuilder.startStrField(Text.of("Uhhh hi"), "yello")
                .setDefaultValue("This is the default value") // Recommended: Used when user click "Reset"
                .setTooltip(TranslatableText("This option is awesome!")) // Optional: Shown when the user hover over this option
                .setSaveConsumer {

                } // Recommended: Called when user save the config
                .build()) // Builds the option entry for cloth config


            return builder.build()
        }
    }
}