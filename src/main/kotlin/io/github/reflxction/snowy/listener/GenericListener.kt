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
package io.github.reflxction.snowy.listener

import io.github.reflxction.snowy.SnowyBot
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

/**
 * A class for listening to general events for the bot
 */
class GenericListener(private val welcomeChannel: String) : ListenerAdapter() {

    companion object {

        /**
         * The plugin's welcome message
         */
        private const val WELCOME_MESSAGE = "Welcome to **%s**, **%s**!\n" +
                "\n" +
                "- For support, open a ticket in #bot-commands using **!ticket**, or ask in <#634664594240962570>.\n" +
                "- To submit a bug report, run **!bug <plugin> <issue description>** in <#637658365270687754>.\n" +
                "- To submit a suggestion, run **!suggest <plugin> <suggestion description**** in <#637658365270687754>.\n" +
                "\n" +
                "Enjoy your stay!"
    }

    /**
     * Called when the bot is ready
     */
    override fun onReady(event: ReadyEvent) {
        SnowyBot.LOGGER.info("Assistant on duty!")
    }

    /**
     * Called when a member joins the guild
     */
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        event.guild.getTextChannelById(welcomeChannel)?.sendMessage(WELCOME_MESSAGE.format(event.guild.name, event.member.effectiveName))?.queue()
    }

}