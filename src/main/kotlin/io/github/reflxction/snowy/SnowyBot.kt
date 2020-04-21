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
package io.github.reflxction.snowy

import io.github.reflxction.snowy.command.HelpCommand
import io.github.reflxction.snowy.command.admin.AddReplyCommand
import io.github.reflxction.snowy.command.admin.BulkDeleteCommand
import io.github.reflxction.snowy.command.core.CommandHandler
import io.github.reflxction.snowy.command.misc.ColorCommand
import io.github.reflxction.snowy.command.misc.RoleCommand
import io.github.reflxction.snowy.command.misc.ShutdownCommand
import io.github.reflxction.snowy.command.submission.IssueCommand
import io.github.reflxction.snowy.command.submission.PluginsCommand
import io.github.reflxction.snowy.command.submission.ReleaseCommand
import io.github.reflxction.snowy.command.submission.SuggestCommand
import io.github.reflxction.snowy.command.ticket.AddMemberToTicketCommand
import io.github.reflxction.snowy.command.ticket.CloseCommand
import io.github.reflxction.snowy.command.ticket.ConversationCommand
import io.github.reflxction.snowy.command.ticket.CreateTicketCommand
import io.github.reflxction.snowy.conversation.ConversationProvider
import io.github.reflxction.snowy.listener.DefaultReplyData
import io.github.reflxction.snowy.listener.GenericListener
import io.github.reflxction.snowy.listener.ReplyManager
import io.github.reflxction.snowy.platform.BukkitPlatformAdapter
import io.github.reflxction.snowy.platform.DevEnvAdapter
import io.github.reflxction.snowy.platform.PlatformAdapter
import io.github.reflxction.snowy.ticket.TicketsProvider
import net.dv8tion.jda.api.JDA
import net.moltenjson.configuration.pack.ConfigurationPack
import net.moltenjson.configuration.pack.DeriveFrom
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException

/**
 * Main class for SnowyBot
 */
object SnowyBot {

    /**
     * The bot commands prefix
     */
    const val PREFIX = "!"

    /**
     * The currently running adapter
     *
     * TODO: Use [io.github.reflxction.snowy.platform.BukkitPlatformAdapter] when
     * TODO: done testing
     */
    var ADAPTER: PlatformAdapter = BukkitPlatformAdapter.PLUGIN

    /**
     * The bot logger
     */
    val LOGGER: Logger = LoggerFactory.getLogger(SnowyBot::class.java)

    /**
     * The command handler
     */
    val COMMAND_HANDLER = CommandHandler(ADAPTER.getJDA())
            .registerListeners(LOGGER)

            // misc
            .register(ColorCommand, ShutdownCommand, HelpCommand, RoleCommand)

            // tickets
            .register(CreateTicketCommand, CloseCommand, AddMemberToTicketCommand, ConversationCommand)

            // submission
            .register(IssueCommand, SuggestCommand, ReleaseCommand, PluginsCommand)

            // admin
            .register(BulkDeleteCommand, AddReplyCommand)

    /**
     * The configuration pack
     */
    val PACK = ConfigurationPack(SnowyBot::class.java, ADAPTER.getDataDirectory())

    /**
     * The tickets provider
     */
    @DeriveFrom("tickets.json")
    private var ticketsProvider = TicketsProvider()

    /**
     * The conversations provider
     */
    @DeriveFrom("conversations.json")
    private var conversationProvider = ConversationProvider()

    /**
     * File for the default replies
     */
    @DeriveFrom("default-replies.json")
    private var defaultReplies: DefaultReplyData = DefaultReplyData()

    /**
     * A simple invokation which loads the class
     */
    fun load(jda: JDA) {
        jda.addEventListener(GenericListener("634662311675822092"))
        jda.addEventListener(ReplyManager)
    }

    /**
     * Returns the tickets provider
     */
    fun getTicketsProvider(): TicketsProvider {
        return ticketsProvider
    }

    /**
     * Returns the conversations provider
     */
    fun getConversationsProvider(): ConversationProvider {
        return conversationProvider
    }

    /**
     * Returns the default replies
     */
    fun getDefaultReplies(): DefaultReplyData {
        return defaultReplies
    }

    init {
        try {
            PACK.register()
            LOGGER.info("I have had {} ticket(s) and {} conversation(s) so far.", ticketsProvider.getSize(), conversationProvider.getSize())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}