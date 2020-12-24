package com.johnythecarrot.lyttlecraft

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import me.sargunvohra.mcmods.autoconfig1u.ConfigData
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import java.io.File

class ConfigScreenUtils {
    companion object {
        private val om = ObjectMapper(YAMLFactory())
        private val file = File(FabricLoader.getInstance().configDir.toString() + "/lyttleconfig.yml")

        const val fieldCount = 5;

        private var configCategories = arrayOf(
            ConfigDataCategory(
                "Servers",
                arrayListOf()
            ),
            ConfigDataCategory(
                "Chat Replies",
                arrayListOf(),
            ),
            ConfigDataCategory(
                "Other",
                arrayListOf(
                    ConfigOption(
                        "measuring_stick_name",
                        "Measuring stick name",
                        "The name to give sticks that will turn them into measuring sticks.",
                        "Measuring Stick",
                        null
                    ),
                    ConfigOption(
                        "important_token",
                        "Important item text token",
                        "The piece of text the mod will look for to mark items as important",
                        "important",
                        null
                    )
                )
            )
        )

        fun init()
        {
            for(i in 0..fieldCount)
            {
                val configOptionToggle = ConfigOption(
                    "send_join_command_$i",
                    "Send join command " + (i + 1),
                    "Whether or not send a command when you join.",
                    false,
                    null
                )
                val configOption = ConfigOption(
                    "send_join_command_command_$i",
                    "Command to send " + (i + 1),
                    "Command to send upon joining.",
                    "",
                    null
                )
                configCategories[0].options.add(configOptionToggle)
                configCategories[0].options.add(configOption)

                val chatReplyEnabledToggle = ConfigOption(
                    "chat_reply_re_enabled_$i",
                    "Chat Regex ${i + 1} Enabled",
                    "Whether to enable this regex or not.",
                    false,
                    null
                )
                val chatReplyRegex = ConfigOption(
                    "chat_reply_re_$i",
                    "Chat Regex ${i + 1}",
                    "Enter your chat regex here.",
                    "",
                    null
                )
                val chatReply = ConfigOption(
                    "chat_reply_$i",
                    "Reply With",
                    "What to reply with",
                    "",
                    null
                )

                configCategories[1].options.add(chatReplyEnabledToggle)
                configCategories[1].options.add(chatReplyRegex)
                configCategories[1].options.add(chatReply)
            }
        }

        var map: MutableMap<String, Any> = mutableMapOf()

        @JvmStatic
        fun createMenu(parent: Screen?): Screen {
            val builder: ConfigBuilder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.of("Stualyttle's Mod Config"))

            createConfigScreen(builder)
            builder.setSavingRunnable {
                saveConfig()
            }
            return builder.build()
        }

        fun loadConfig()
        {
            if (!file.exists())
            {
                file.createNewFile()
                configCategories.forEach { category ->
                    category.options.forEach { option: ConfigOption ->
                        option.value = option.defaultValue
                    }
                }
                saveConfig()
                return
            }
            val values = om.readValue(file, Map::class.java)
            var i = 0
            this.map = values as MutableMap<String, Any>
            configCategories.forEach { category ->
                category.options.forEach { option: ConfigOption ->
                    option.value = values[values.keys.toTypedArray()[i++]]
                }
            }
        }

        private fun saveConfig()
        {
            this.map = mutableMapOf()
            var toWrite = ""
            configCategories.forEach { el ->
                el.options.forEach { op ->
                    toWrite += "${op.key}: ${op.value}\n"
                    map[op.key] = op.value!!
                }
            }
            file.writeText(toWrite)
        }

        private fun createConfigScreen(builder: ConfigBuilder)
        {
            configCategories.forEach { category ->
                val categoryBuilder = builder.getOrCreateCategory(Text.of(category.name))
                val entryBuilder = builder.entryBuilder()

                category.options.forEach { option ->
                    entryBuilder.startAny(categoryBuilder, option)
                }
            }
        }

    }
}

private fun ConfigEntryBuilder.startAny(categoryBuilder: ConfigCategory, option: ConfigOption) {
    val text = Text.of(option.name)
    val value = option.value
    val defaultValue = option.defaultValue
    val tooltip = Text.of(option.description)

    when (option.value)
    {
        is String ->
            categoryBuilder.addEntry(
                this.startStrField(text, value as String)
                    .setDefaultValue(defaultValue as String)
                    .setTooltip(tooltip)
                    .setSaveConsumer {
                        option.value = it
                    }
                    .build()
            )
        is Boolean ->
            categoryBuilder.addEntry(
                this.startBooleanToggle(text, value as Boolean)
                    .setDefaultValue(defaultValue as Boolean)
                    .setTooltip(tooltip)
                    .setSaveConsumer {
                        option.value = it
                    }
                    .build()
            )
        else -> throw NoSuchFieldException("value $value not allowed, please implement")
    }
}
