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
import java.io.File

/**
 * An adapter for development environments
 */
object DevEnvAdapter : PlatformAdapter {

    /**
     * The JDA instance
     */
    private lateinit var jda: JDA

    /**
     * The data folder
     */
    private val dataFolder = File("D:\\Java\\SnowyAssistant\\src\\test\\resources")

    @JvmStatic
    fun main(args: Array<String>) {
        runBot(args[0])
    }

    /**
     * Runs the bot
     *
     * @param token The bot token
     */
    override fun runBot(token: String) {
        jda = JDABuilder().setActivity(Activity.watching("over tickets")).setToken(token).build()
        SnowyBot.load(jda)
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
     * Shuts the adapter platform down. Use this to save and shutdown JDA.
     */
    override fun shutdown() {
        jda.shutdown()
        SnowyBot.PACK.save()
        SnowyBot.LOGGER.info("Shutting down...")
    }
}