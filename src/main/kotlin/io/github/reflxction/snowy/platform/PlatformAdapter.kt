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

import net.dv8tion.jda.api.JDA
import java.io.File

/**
 * Represents a platform in which the bot can run in
 */
interface PlatformAdapter {

    /**
     * Runs the bot
     *
     * @param token The bot token
     */
    fun runBot(token: String)

    /**
     * Returns the JDA instance
     */
    fun getJDA(): JDA

    /**
     * Returns the data directory of this adapter
     */
    fun getDataDirectory(): File

    /**
     * Shuts the adapter platform down. Use this to save and shutdown JDA.
     */
    fun shutdown()

}