/*
 * * Copyright 2019-2020 github.com/ReflxctionDev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing requiredPermissions and
 * limitations under the License.
 */
package io.github.reflxction.snowy.platform

import io.github.reflxction.snowy.SnowyBot
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * An adapter for running as a BukkitPlugin
 */
class BukkitPlatformAdapter : JavaPlugin(), PlatformAdapter {

    companion object {

        /**
         * The plugin instance
         */
        lateinit var PLUGIN: BukkitPlatformAdapter
    }

    /**
     * The JDA instance
     */
    private lateinit var jda: JDA

    /**
     * Invoked when the plugin is enabled
     */
    override fun onEnable() {
        PLUGIN = this
        generateFile("config.yml")
        generateFile("conversations.json")
        generateFile("tickets.json")
        generateFile("default-replies.json")
        runBot(config.getString("Bot.Token"))
        SnowyBot.load(jda)
        SnowyBot.ADAPTER = this
        super.onEnable()
    }

    /**
     * Invoked when the plugin is disabled
     */
    override fun onDisable() {
        jda.shutdown()
        SnowyBot.PACK.save()
        super.onDisable()
    }

    /**
     * Runs the bot
     *
     * @param token The bot token
     */
    override fun runBot(token: String) {
        jda = JDABuilder()
                .setToken(token)
                .setActivity(Activity.of(Activity.ActivityType.valueOf(config.getString("Bot.Presence.Type")), config.getString("Bot.Presence.Text")))
                .build()
    }

    /**
     * Returns the JDA instance
     *
     * @return The JDA instance
     */
    override fun getJDA(): JDA {
        return jda
    }

    /**
     * Returns the data directory of this adapter
     */
    override fun getDataDirectory(): File {
        return dataFolder
    }

    /**
     * Shuts the adapter platform down. Use this to shutdown JDA. No need to
     * add special code since this calls onDisable.
     */
    override fun shutdown() {
        SnowyBot.LOGGER.info("Shutting down...")
        Bukkit.shutdown()
    }

    /**
     * Generates the specified file name
     *
     */
    private fun generateFile(name: String): File {
        val file = File(dataFolder, name)
        if (!file.exists())
            saveResource(name, false)
        return file
    }
}